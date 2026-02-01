package org.catools.mcp;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import org.catools.common.exception.CInvalidYamlFileFormatException;
import org.catools.mcp.configuration.CMcpServerConfiguration;
import org.catools.mcp.configuration.CYamlConfigurationLoader;
import org.catools.mcp.enums.CServerMode;
import org.catools.mcp.server.CMcpSseServerInfo;
import org.catools.mcp.server.CMcpStreamableServerInfo;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Random;
import java.util.Set;

import static org.testng.Assert.*;

@Test(singleThreaded = true)
public class McpServersTest extends BaseMcpServersTest {

  @Test(priority = 2)
  void testStartSseServer_shouldSucceed() {
    final int port = new Random().nextInt(8000, 9000);

    CMcpSseServerInfo serverInfo =
        CMcpSseServerInfo.builder()
            .name("mcp-server")
            .version("1.0.0")
            .instructions("test")
            .groups(Set.of("test"))
            .requestTimeout(Duration.ofSeconds(10))
            .baseUrl("http://localhost:" + port)
            .port(port)
            .sseEndpoint("/sse")
            .messageEndpoint("/mcp/message")
            .build();

    HttpClientSseClientTransport transport =
        HttpClientSseClientTransport.builder("http://localhost:" + port)
            .sseEndpoint("/sse")
            .build();

    servers.startSseServer(serverInfo);

    try (McpSyncClient client = McpClient.sync(transport).build()) {
      verify(client);
    }
  }

  @Test(priority = 2)
  void testStartStreamableServer_shouldSucceed() {
    final int port = new Random().nextInt(8000, 9000);

    CMcpStreamableServerInfo serverInfo =
        CMcpStreamableServerInfo.builder()
            .name("mcp-server")
            .version("1.0.0")
            .instructions("test")
            .groups(Set.of("test"))
            .requestTimeout(Duration.ofSeconds(10))
            .port(port)
            .mcpEndpoint("/mcp/message")
            .build();

    HttpClientStreamableHttpTransport transport =
        HttpClientStreamableHttpTransport.builder("http://localhost:" + port)
            .endpoint("/mcp/message")
            .build();

    servers.startStreamableServer(serverInfo);

    try (McpSyncClient client = McpClient.sync(transport).build()) {
      verify(client);
    }
  }

  @Test(priority = 2)
  void testStartServer_disabledMCP_shouldSucceed() {
    String configFileName = "test-mcp-server-disabled.yml";
    CYamlConfigurationLoader configLoader = new CYamlConfigurationLoader(configFileName);
    CMcpServerConfiguration configuration = configLoader.loadConfig();
    try {
      servers.startServer(configFileName);
    } catch (Exception e) {
      fail("Exception thrown while starting server: " + e.getMessage());
    }
    assertFalse(configuration.enabled());
  }

  @Test(priority = 2)
  void testStartServer_enableStdioMode_shouldSucceed() {
    String configFileName = "test-mcp-server-enable-stdio-mode.yml";
    CYamlConfigurationLoader configLoader = new CYamlConfigurationLoader(configFileName);
    CMcpServerConfiguration configuration = configLoader.loadConfig();
    try {
      servers.startServer(configFileName);
    } catch (Exception e) {
      fail("Exception thrown while starting server: " + e.getMessage());
    }
    assertEquals(configuration.mode(), CServerMode.STDIO);
  }

  @Test(priority = 2)
  void testStartServer_enableHttpSseMode_shouldSucceed() {
    String configFileName = "test-mcp-server-enable-http-sse-mode.yml";
    CYamlConfigurationLoader configLoader = new CYamlConfigurationLoader(configFileName);
    CMcpServerConfiguration configuration = configLoader.loadConfig();
    try {
      servers.startServer(configFileName);
    } catch (Exception e) {
      fail("Exception thrown while starting server: " + e.getMessage());
    }
    assertEquals(configuration.mode(), CServerMode.SSE);
  }

  @Test(priority = 2)
  void testStartServer_enableStreamableHttpMode_shouldSucceed() {
    String configFileName = "test-mcp-server-enable-streamable-http-mode.yml";
    CYamlConfigurationLoader configLoader = new CYamlConfigurationLoader(configFileName);
    CMcpServerConfiguration configuration = configLoader.loadConfig();
    try {
      servers.startServer(configFileName);
    } catch (Exception e) {
      fail("Exception thrown while starting server: " + e.getMessage());
    }
    assertEquals(configuration.mode(), CServerMode.STREAMABLE);
  }

  @Test(priority = 2, expectedExceptions = CInvalidYamlFileFormatException.class)
  void testStartServer_enableUnknownMode_shouldThrowException() {
    String configFileName = "test-mcp-server-enable-unknown-mode.yml";
    servers.startServer(configFileName);
  }

  @Test(priority = 2)
  void testStartServer_useDefaultConfigFileName_shouldSucceed() {
    String configFileName = "mcp-server.yml";
    CYamlConfigurationLoader configLoader = new CYamlConfigurationLoader(configFileName);
    CMcpServerConfiguration configuration = configLoader.loadConfig();
    assertEquals(configuration.mode(), CServerMode.STREAMABLE);
    try {
      servers.startServer();
    } catch (Exception e) {
      fail("Exception thrown while starting server: " + e.getMessage());
    }
  }
}
