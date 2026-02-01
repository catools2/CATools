package org.catools.mcp;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import org.catools.common.utils.CRetry;
import org.catools.mcp.test.TestSimpleMcpStdioServer;
import org.testng.annotations.Test;

public class McpServersResourceTest extends BaseMcpServersTest {

  @Test(priority = 1)
  void testStartStdioServer_shouldSucceed() {
    String classpath = System.getProperty("java.class.path");

    ServerParameters serverParameters =
        ServerParameters.builder("java")
            .args("-cp", classpath, TestSimpleMcpStdioServer.class.getName())
            .build();

    StdioClientTransport stdioClientTransport =
        new StdioClientTransport(serverParameters, McpJsonMapper.getDefault());

    CRetry.retry(
        idx -> {
          try (McpSyncClient client = McpClient.sync(stdioClientTransport).build()) {
            verify(client);
          }
          return true;
        },
        3,
        2000);
  }
}
