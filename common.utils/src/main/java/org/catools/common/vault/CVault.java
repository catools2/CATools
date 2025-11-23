package org.catools.common.vault;

import com.bettercloud.vault.SslConfig;
import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.configs.CVaultConfigs;
import org.catools.common.exception.CVaultAuthenticationException;
import org.catools.common.exception.CVaultOperationException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@UtilityClass
public class CVault {
  private static final Map<String, CVaultClient> clients = new HashMap<>();

  /**
   * Get value from vault by key
   *
   * @param key
   * @return value
   */
  public static String getValue(String key) {
    return getValue(key, null);
  }

  /**
   * Get value from vault by key or return default value if not found
   *
   * @param key       lookup key
   * @param defaultTo default value if not found
   * @return value
   */
  public static String getValue(String key, String defaultTo) {
    if (clients.isEmpty()) {
      init();
    }

    for (CVaultClient value : clients.values()) {
      String v = value.getValue(key, null);
      if (v != null) {
        return v;
      }
    }
    return defaultTo;
  }

  private static void init() {
    if (!CVaultConfigs.isEnabled()) {
      return;
    }
    List<String> paths = CVaultConfigs.getPaths();
    if (paths == null || paths.isEmpty()) {
      throw new CVaultOperationException("Failed to initialize vault clients. No vault paths configured.");
    }

    VaultConfig token = getVaultBaseConfig().token(getAuthClientToken());
    Vault vault = new Vault(token);
    for (String path : paths) {
      clients.put(path, build(vault, path));
    }
  }

  private static CVaultClient build(Vault vault, String path) {
    return new CVaultClient(vault, path);
  }

  private static String getAuthClientToken() {
    CVaultAuthType authType = CVaultConfigs.getAuthType();
    Objects.requireNonNull(authType);

    try {
      return switch (authType) {
        case TOKEN -> CVaultConfigs.getAuthToken();

        case LDAP -> new Vault(getVaultBaseConfig()).auth()
            .loginByLDAP(CVaultConfigs.getAuthLdapUsername(), CVaultConfigs.getAuthLdapPassword())
            .getAuthClientToken();

        case APP_ROLE -> new Vault(getVaultBaseConfig()).auth()
            .loginByAppRole(CVaultConfigs.getAuthAppRoleRoleId(), CVaultConfigs.getAuthAppRoleSecretId())
            .getAuthClientToken();

        default -> throw new CVaultAuthenticationException(authType.name());
      };
    } catch (VaultException e) {
      throw new CVaultAuthenticationException(authType.name(), e);
    }
  }

  private static VaultConfig getVaultBaseConfig() {
    try {
      return new VaultConfig().address(CVaultConfigs.getUrl())
          .openTimeout(CVaultConfigs.getOpenTimeout())
          .readTimeout(CVaultConfigs.getReadTimeout())
          .sslConfig(getSslConfig())
          .build();
    } catch (VaultException e) {
      throw new CVaultOperationException("Build vault config.", e);
    }
  }

  private static SslConfig getSslConfig() {
    SslConfig sslConfig = new SslConfig();
    try {
      if (!StringUtils.isBlank(CVaultConfigs.getTlsCertFile())) {
        sslConfig.pemFile(new File(CVaultConfigs.getTlsCertFile()));
      } else if (!StringUtils.isBlank(CVaultConfigs.getTlsCertString())) {
        sslConfig.pemUTF8(CVaultConfigs.getTlsCertString());
      }
      return sslConfig.verify(true).build();
    } catch (VaultException e) {
      throw new CVaultOperationException("Build ssl config.", e);
    }
  }


}
