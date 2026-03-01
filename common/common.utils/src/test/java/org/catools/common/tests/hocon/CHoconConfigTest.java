package org.catools.common.tests.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import java.lang.reflect.Field;
import java.util.Map;
import org.catools.common.hocon.model.CHoconConfig;
import org.catools.common.utils.CStringUtil;
import org.catools.common.vault.CVault;
import org.catools.common.vault.CVaultClient;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

@Test(singleThreaded = true)
public class CHoconConfigTest {

  @AfterMethod
  public void cleanupVaultClients() throws Exception {
    Field clientsField = CVault.class.getDeclaredField("clients");
    clientsField.setAccessible(true);
    Map<?, ?> clients = (Map<?, ?>) clientsField.get(null);
    clients.clear();
  }

  @Test
  public void testReadFromSystemProperty() {
    String path = "my.test.path";
    String envKey = path.toUpperCase().replaceAll("\\.", "_");

    String previous = System.getProperty(envKey);
    try {
      System.clearProperty(envKey);
      String expected = "hello-from-system";
      System.setProperty(envKey, expected);

      Config config =
          ConfigFactory.empty().withValue(path, ConfigValueFactory.fromAnyRef(expected));
      CHoconConfig cfg = new CHoconConfig(config, path);
      String value = cfg.asString();
      Assert.assertEquals(value, expected);
    } finally {
      if (previous == null) {
        System.clearProperty(envKey);
      } else {
        System.setProperty(envKey, previous);
      }
    }
  }

  @Test
  public void testVaultFallback() throws Exception {
    String path = "secret.some.value";

    // ensure no system property interferes
    String envKey = path.toUpperCase().replaceAll("\\.", "_");
    System.clearProperty(envKey);

    // Inject a fake CVaultClient into CVault.clients via reflection
    Field clientsField = CVault.class.getDeclaredField("clients");
    clientsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, CVaultClient> clients = (Map<String, CVaultClient>) clientsField.get(null);
    clients.clear();

    CVaultClient fakeClient =
        new CVaultClient(null, "fake") {
          @Override
          public String getValue(String key, String defaultTo) {
            if (key == null) return defaultTo;
            if (key.equals(path)) return "vault-secret-value";
            // also support env-style key
            if (key.equals(path.toUpperCase().replaceAll("\\.", "_"))) return "vault-secret-value";
            return defaultTo;
          }

          @Override
          public String getValue(String key) {
            return getValue(key, null);
          }
        };

    clients.put("fake", fakeClient);

    CHoconConfig cfg = new CHoconConfig(ConfigFactory.empty(), path);
    String value = cfg.asString();
    Assert.assertEquals(value, "vault-secret-value");
  }

  @Test
  public void testVaultPathNotPresent() throws Exception {
    String path = "secret.not.exists";

    // ensure no system property interferes
    String envKey = path.toUpperCase().replaceAll("\\.", "_");
    System.clearProperty(envKey);

    // Inject a fake CVaultClient into CVault.clients via reflection
    Field clientsField = CVault.class.getDeclaredField("clients");
    clientsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, CVaultClient> clients = (Map<String, CVaultClient>) clientsField.get(null);
    clients.clear();

    CVaultClient fakeClient =
        new CVaultClient(null, "fake") {
          @Override
          public String getValue(String key, String defaultTo) {
            // Always return default (simulate key not present)
            return defaultTo;
          }

          @Override
          public String getValue(String key) {
            // Simulate key not present -> return null
            return null;
          }
        };

    clients.put("fake", fakeClient);

    CHoconConfig cfg = new CHoconConfig(ConfigFactory.empty(), path);
    String value = cfg.asString();
    Assert.assertEquals(
        value, CStringUtil.EMPTY, "Expected e,pty string when vault does not contain the path");
  }

  @Test
  public void testVaultPathExistsButValueEmpty() throws Exception {
    String path = "secret.some.empty";

    // ensure no system property interferes
    String envKey = path.toUpperCase().replaceAll("\\.", "_");
    System.clearProperty(envKey);

    // Inject a fake CVaultClient into CVault.clients via reflection
    Field clientsField = CVault.class.getDeclaredField("clients");
    clientsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, CVaultClient> clients = (Map<String, CVaultClient>) clientsField.get(null);
    clients.clear();

    CVaultClient fakeClient =
        new CVaultClient(null, "fake") {
          @Override
          public String getValue(String key, String defaultTo) {
            if (key == null) return defaultTo;
            if (key.equals(path)) return CStringUtil.EMPTY; // path exists but value is empty
            return defaultTo;
          }

          @Override
          public String getValue(String key) {
            if (key == null) return null;
            if (key.equals(path)) return CStringUtil.EMPTY; // path exists but value is empty
            return null;
          }
        };

    clients.put("fake", fakeClient);

    CHoconConfig cfg = new CHoconConfig(ConfigFactory.empty(), path);
    String value = cfg.asString();
    Assert.assertEquals(
        value,
        CStringUtil.EMPTY,
        "Expected empty string when vault path exists but value is empty");
  }
}
