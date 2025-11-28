package org.catools.mcp.test;

import org.catools.mcp.CMcpServers;
import org.catools.mcp.server.CMcpServerInfo;

import java.time.Duration;
import java.util.Set;

public class TestSimpleMcpStdioServer {

  public static void main(String[] args) {
    CMcpServerInfo info =
        CMcpServerInfo.builder()
            .name("mcp-server")
            .version("1.0.0")
            .instructions("test")
            .groups(Set.of("test"))
            .requestTimeout(Duration.ofSeconds(10))
            .build();
    CMcpServers.run(TestSimpleMcpStdioServer.class).startStdioServer(info);
  }
}
