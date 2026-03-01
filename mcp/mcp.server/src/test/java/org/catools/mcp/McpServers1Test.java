package org.catools.mcp;

import org.catools.mcp.test.TestSimpleMcpStdioServer;
import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

@Test(singleThreaded = true)
public class McpServers1Test extends BaseMcpServersTest {

    @Test(priority = 1)
    void testStartStdioServer_shouldSucceed() {
        String classpath = System.getProperty("java.class.path");

        ServerParameters serverParameters =
                ServerParameters.builder("java")
                        .args("-cp", classpath, TestSimpleMcpStdioServer.class.getName())
                        .build();

        StdioClientTransport stdioClientTransport =
                new StdioClientTransport(serverParameters, McpJsonMapper.getDefault());

        RetryPolicy<Object> retryPolicy = RetryPolicy.builder()
                .handle(List.of(Exception.class, AssertionError.class))
                .withDelay(Duration.ofSeconds(3))
                .withMaxRetries(5)
                .build();

        try (McpSyncClient client = McpClient.sync(stdioClientTransport).build()) {
            Failsafe.with(retryPolicy).get(() -> {
                verify(client);
                return null;
            });
        }

    }

}
