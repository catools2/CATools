package org.catools.mcp;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import org.catools.common.exception.CInvalidYamlFileFormatException;
import org.catools.common.utils.CStringUtil;
import org.catools.mcp.configuration.CMcpServerConfiguration;
import org.catools.mcp.configuration.CYamlConfigurationLoader;
import org.catools.mcp.enums.CJavaTypeToJsonSchemaMapper;
import org.catools.mcp.enums.CServerMode;
import org.catools.mcp.server.CMcpSseServerInfo;
import org.catools.mcp.server.CMcpStreamableServerInfo;
import org.catools.mcp.server.CMcpStructuredContent;
import org.catools.mcp.test.TestMcpToolsStructuredContent;
import org.catools.mcp.test.TestSimpleMcpStdioServer;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.testng.Assert.*;

@Test(singleThreaded = true)
public class McpServersTest {

  private final CMcpServers servers = CMcpServers.run(McpServersTest.class);

  @Test(priority = 1)
  void testStartStdioServer_shouldSucceed() {
    String classpath = System.getProperty("java.class.path");

    ServerParameters serverParameters =
        ServerParameters.builder("java")
            .args("-cp", classpath, TestSimpleMcpStdioServer.class.getName())
            .build();

    StdioClientTransport stdioClientTransport =
        new StdioClientTransport(serverParameters, McpJsonMapper.getDefault());

    try (McpSyncClient client = McpClient.sync(stdioClientTransport).build()) {
      verify(client);
    }
  }

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

  private void verify(McpSyncClient client) {
    verifyServerInfo(client);
    verifyResourcesRegistered(client);
    verifyResourcesCalled(client);
    verifyPromptsRegistered(client);
    verifyToolsRegistered(client);
    verifyPromptsCalled(client);
    verifyToolsCalled(client);
  }

  private void verifyServerInfo(McpSyncClient client) {
    McpSchema.InitializeResult initialized = client.initialize();
    assertEquals(initialized.serverInfo().name(), "mcp-server");
    assertEquals(initialized.serverInfo().version(), "1.0.0");
    assertEquals(initialized.instructions(), "test");
  }

  private void verifyResourcesRegistered(McpSyncClient client) {
    List<McpSchema.Resource> resources = client.listResources().resources();
    assertEquals(resources.size(), 2);

    verifyResourceRegistered(
        resources,
        "test://resource1",
        "resource1_name",
        "resource1_title",
        "resource1_description");
    verifyResourceRegistered(
        resources,
        "test://resource2",
        "resource2_name",
        "resource2_title",
        "resource2_description");
  }

  private void verifyResourceRegistered(
      List<McpSchema.Resource> resources,
      String resourceUri,
      String resourceName,
      String resourceTitle,
      String resourceDescription) {

    McpSchema.Resource resource =
        resources.stream().filter(r -> r.uri().equals(resourceUri)).findAny().orElse(null);
    assertNotNull(resource);
    assertEquals(resource.uri(), resourceUri);
    assertEquals(resource.name(), resourceName);
    assertEquals(resource.title(), resourceTitle);
    assertEquals(resource.description(), resourceDescription);
  }

  private void verifyResourcesCalled(McpSyncClient client) {
    verifyResourceCalled(client, "test://resource1", "text/plain", "resource1_content");
    verifyResourceCalled(client, "test://resource2", "text/plain", "resource2_content");
  }

  private void verifyResourceCalled(
      McpSyncClient client, String resourceUri, String resourceMimeType, String resourceContent) {

    McpSchema.ReadResourceRequest request = new McpSchema.ReadResourceRequest(resourceUri);
    McpSchema.ReadResourceResult result = client.readResource(request);
    McpSchema.TextResourceContents content =
        (McpSchema.TextResourceContents) result.contents().get(0);
    assertNotNull(content);
    assertEquals(content.uri(), resourceUri);
    assertEquals(content.mimeType(), resourceMimeType);
    assertEquals(content.text(), resourceContent);
  }

  private void verifyPromptsRegistered(McpSyncClient client) {
    List<McpSchema.Prompt> prompts = client.listPrompts().prompts();
    assertEquals(prompts.size(), 10);

    verifyPromptRegistered(prompts, "promptWithDefaultName", "title", "description", 0);
    verifyPromptRegistered(prompts, "promptWithDefaultTitle", CStringUtil.EMPTY, "description", 0);
    verifyPromptRegistered(prompts, "promptWithDefaultDescription", "title", CStringUtil.EMPTY, 0);
    verifyPromptRegistered(prompts, "promptWithAllDefault", CStringUtil.EMPTY, CStringUtil.EMPTY, 0);
    verifyPromptRegistered(prompts, "promptWithOptionalParam", CStringUtil.EMPTY, CStringUtil.EMPTY, 1);
    verifyPromptRegistered(prompts, "promptWithRequiredParam", CStringUtil.EMPTY, CStringUtil.EMPTY, 1);
    verifyPromptRegistered(prompts, "promptWithMultiParams", CStringUtil.EMPTY, CStringUtil.EMPTY, 2);
    verifyPromptRegistered(prompts, "promptWithMixedParams", CStringUtil.EMPTY, CStringUtil.EMPTY, 1);
    verifyPromptRegistered(prompts, "promptWithVoidReturn", CStringUtil.EMPTY, CStringUtil.EMPTY, 0);
    verifyPromptRegistered(prompts, "promptWithReturnNull", CStringUtil.EMPTY, CStringUtil.EMPTY, 0);
  }

  private void verifyPromptRegistered(
      List<McpSchema.Prompt> prompts,
      String promptName,
      String promptTitle,
      String promptDescription,
      int promptArgumentsSize) {

    McpSchema.Prompt prompt =
        prompts.stream().filter(p -> p.name().equals(promptName)).findAny().orElse(null);
    assertNotNull(prompt);
    assertEquals(prompt.name(), promptName);
    assertEquals(prompt.title(), promptTitle);
    assertEquals(prompt.description(), promptDescription);
    assertEquals(prompt.arguments().size(), promptArgumentsSize);
  }

  private void verifyPromptsCalled(McpSyncClient client) {
    verifyPromptCalled(
        client, "promptWithDefaultName", Map.of(), "promptWithDefaultName is called");
    verifyPromptCalled(
        client, "promptWithDefaultTitle", Map.of(), "promptWithDefaultTitle is called");
    verifyPromptCalled(
        client, "promptWithDefaultDescription", Map.of(), "promptWithDefaultDescription is called");
    verifyPromptCalled(client, "promptWithAllDefault", Map.of(), "promptWithAllDefault is called");
    verifyPromptCalled(
        client,
        "promptWithOptionalParam",
        Map.of("param", "value"),
        "promptWithOptionalParam is called with param: value");
    verifyPromptCalled(
        client,
        "promptWithRequiredParam",
        Map.of("param", "value"),
        "promptWithRequiredParam is called with param: value");
    verifyPromptCalled(
        client,
        "promptWithMultiParams",
        Map.of("param1", "value1", "param2", "value2"),
        "promptWithMultiParams is called with params: value1, value2");
    verifyPromptCalled(
        client,
        "promptWithMixedParams",
        Map.of("mcpParam", "value"),
        "promptWithMixedParams is called with params: value, " + CStringUtil.EMPTY);
    verifyPromptCalled(
        client,
        "promptWithVoidReturn",
        Map.of(),
        "Method executed successfully with void return type");
    verifyPromptCalled(
        client,
        "promptWithReturnNull",
        Map.of(),
        "The method call succeeded but the return value is null");
  }

  private void verifyPromptCalled(
      McpSyncClient client, String promptName, Map<String, Object> params, String expectedResult) {

    McpSchema.GetPromptRequest request = new McpSchema.GetPromptRequest(promptName, params);
    McpSchema.GetPromptResult result = client.getPrompt(request);
    McpSchema.TextContent content = (McpSchema.TextContent) result.messages().get(0).content();
    assertEquals(content.text(), expectedResult);
  }

  private void verifyToolsRegistered(McpSyncClient client) {
    List<McpSchema.Tool> tools = client.listTools().tools();
    assertEquals(tools.size(), 22);

    verifyToolRegistered(tools, "toolWithDefaultName", "title", "description", Map.of());
    verifyToolRegistered(tools, "toolWithDefaultTitle", CStringUtil.EMPTY, "description", Map.of());
    verifyToolRegistered(tools, "toolWithDefaultDescription", "title", CStringUtil.EMPTY, Map.of());
    verifyToolRegistered(tools, "toolWithAllDefault", CStringUtil.EMPTY, CStringUtil.EMPTY, Map.of());
    verifyToolRegistered(
        tools,
        "toolWithOptionalParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", String.class));
    verifyToolRegistered(
        tools,
        "toolWithRequiredParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", String.class));
    verifyToolRegistered(
        tools,
        "toolWithMultiParams",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param1", String.class, "param2", String.class));
    verifyToolRegistered(
        tools,
        "toolWithMixedParams",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("mcpParam", String.class));
    verifyToolRegistered(tools, "toolWithVoidReturn", CStringUtil.EMPTY, CStringUtil.EMPTY, Map.of());
    verifyToolRegistered(tools, "toolWithReturnNull", CStringUtil.EMPTY, CStringUtil.EMPTY, Map.of());
    verifyToolRegistered(
        tools, "toolWithIntParam", CStringUtil.EMPTY, CStringUtil.EMPTY, Map.of("param", int.class));
    verifyToolRegistered(
        tools,
        "toolWithIntegerParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", Integer.class));
    verifyToolRegistered(
        tools, "toolWithLongParam", CStringUtil.EMPTY, CStringUtil.EMPTY, Map.of("param", long.class));
    verifyToolRegistered(
        tools,
        "toolWithLongClassParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", Long.class));
    verifyToolRegistered(
        tools,
        "toolWithFloatParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", float.class));
    verifyToolRegistered(
        tools,
        "toolWithFloatClassParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", Float.class));
    verifyToolRegistered(
        tools,
        "toolWithDoubleParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", double.class));
    verifyToolRegistered(
        tools,
        "toolWithDoubleClassParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", Double.class));
    verifyToolRegistered(
        tools,
        "toolWithNumberParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", Number.class));
    verifyToolRegistered(
        tools,
        "toolWithBooleanParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", boolean.class));
    verifyToolRegistered(
        tools,
        "toolWithBooleanClassParam",
        CStringUtil.EMPTY,
        CStringUtil.EMPTY,
        Map.of("param", Boolean.class));
    verifyToolRegistered(
        tools, "toolWithReturnStructuredContent", CStringUtil.EMPTY, CStringUtil.EMPTY, Map.of());
  }

  @SuppressWarnings("unchecked")
  private void verifyToolRegistered(
      List<McpSchema.Tool> tools,
      String toolName,
      String toolTitle,
      String toolDescription,
      Map<String, Class<?>> inputSchemaPropertiesTypes) {

    McpSchema.Tool tool =
        tools.stream().filter(t -> t.name().equals(toolName)).findAny().orElse(null);
    assertNotNull(tool);
    assertEquals(tool.name(), toolName);
    assertEquals(tool.title(), toolTitle);
    assertEquals(tool.description(), toolDescription);
    assertEquals(tool.inputSchema().properties().size(), inputSchemaPropertiesTypes.size());

    // verify input schema properties types
    tool.inputSchema()
        .properties()
        .forEach(
            (name, property) -> {
              Map<String, String> props = (Map<String, String>) property;
              Class<?> javaClass = inputSchemaPropertiesTypes.get(name);
              final String jsonSchemaType = CJavaTypeToJsonSchemaMapper.getJsonSchemaType(javaClass);
              assertEquals(props.get("type"), jsonSchemaType);
            });
  }

  private void verifyToolsCalled(McpSyncClient client) {
    verifyToolCalled(client, "toolWithDefaultName", Map.of(), "toolWithDefaultName is called");
    verifyToolCalled(client, "toolWithDefaultTitle", Map.of(), "toolWithDefaultTitle is called");
    verifyToolCalled(
        client, "toolWithDefaultDescription", Map.of(), "toolWithDefaultDescription is called");
    verifyToolCalled(client, "toolWithAllDefault", Map.of(), "toolWithAllDefault is called");
    verifyToolCalled(
        client,
        "toolWithOptionalParam",
        Map.of("param", "value"),
        "toolWithOptionalParam is called with optional param: value");
    verifyToolCalled(
        client,
        "toolWithRequiredParam",
        Map.of("param", "value"),
        "toolWithRequiredParam is called with required param: value");
    verifyToolCalled(
        client,
        "toolWithMultiParams",
        Map.of("param1", "value1", "param2", "value2"),
        "toolWithMultiParams is called with params: value1, value2");
    verifyToolCalled(
        client,
        "toolWithMixedParams",
        Map.of("mcpParam", "value"),
        "toolWithMixedParams is called with params: value, " + CStringUtil.EMPTY);
    verifyToolCalled(
        client,
        "toolWithVoidReturn",
        Map.of(),
        "Method executed successfully with void return type");
    verifyToolCalled(
        client,
        "toolWithReturnNull",
        Map.of(),
        "The method call succeeded but the return value is null");
    verifyToolCalled(
        client,
        "toolWithIntParam",
        Map.of("param", 123),
        "toolWithIntParam is called with param: 123");
    verifyToolCalled(
        client,
        "toolWithIntegerParam",
        Map.of("param", 123),
        "toolWithIntegerParam is called with param: 123");
    verifyToolCalled(
        client,
        "toolWithLongParam",
        Map.of("param", 123L),
        "toolWithLongParam is called with param: 123");
    verifyToolCalled(
        client,
        "toolWithLongClassParam",
        Map.of("param", 123L),
        "toolWithLongClassParam is called with param: 123");
    verifyToolCalled(
        client,
        "toolWithFloatParam",
        Map.of("param", 123.0F),
        "toolWithFloatParam is called with param: 123.0");
    verifyToolCalled(
        client,
        "toolWithFloatClassParam",
        Map.of("param", 123.0F),
        "toolWithFloatClassParam is called with param: 123.0");
    verifyToolCalled(
        client,
        "toolWithDoubleParam",
        Map.of("param", 123.0),
        "toolWithDoubleParam is called with param: 123.0");
    verifyToolCalled(
        client,
        "toolWithDoubleClassParam",
        Map.of("param", 123.0),
        "toolWithDoubleClassParam is called with param: 123.0");
    verifyToolCalled(
        client,
        "toolWithNumberParam",
        Map.of("param", 123),
        "toolWithNumberParam is called with param: 123");
    verifyToolCalled(
        client,
        "toolWithBooleanParam",
        Map.of("param", true),
        "toolWithBooleanParam is called with param: true");
    verifyToolCalled(
        client,
        "toolWithBooleanClassParam",
        Map.of("param", true),
        "toolWithBooleanClassParam is called with param: true");
    verifyToolCalled(
        client,
        "toolWithReturnStructuredContent",
        Map.of(),
        new TestMcpToolsStructuredContent.TestStructuredContentC(1, 2, 3L, 4L, 5.0F, 6.0F, 7.0, 8.0)
            .asTextContent());
  }

  private void verifyToolCalled(
      McpSyncClient client, String toolName, Map<String, Object> args, String expectedResult) {

    McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(toolName, args);
    McpSchema.CallToolResult result = client.callTool(request);
    McpSchema.TextContent content = (McpSchema.TextContent) result.content().get(0);
    assertFalse(result.isError());
    assertEquals(content.text(), expectedResult);

    if (result.structuredContent() instanceof CMcpStructuredContent structuredContent) {
      assertEquals(structuredContent.asTextContent(), expectedResult);
    }
  }
}
