package org.catools.mcp.test;

import org.catools.mcp.McpServers;
import org.catools.mcp.di.GuiceInjectorModule;
import org.catools.mcp.server.McpServerInfo;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.time.Duration;
import java.util.Set;

public class TestSimpleMcpStdioServer {

    public static void main(String[] args) {
        McpServerInfo info =
                McpServerInfo.builder()
                        .name("mcp-server")
                        .version("1.0.0")
                        .instructions("test")
                        .groups(Set.of("test"))
                        .requestTimeout(Duration.ofSeconds(10))
                        .build();
        McpServers.run(
                new GuiceInjectorModule(TestSimpleMcpStdioServer.class, Set.of("test")),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        // no-op
                    }

                    @Provides
                    @Singleton
                    TestMcpToolInjection.User testUser() {
                        return new TestMcpToolInjection.User("guice-user");
                    }
                }).startStdioServer(info);
    }
}
