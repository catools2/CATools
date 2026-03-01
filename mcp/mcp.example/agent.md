---
description: Expert mcp to add 2 integers.
name: Calc-MCP-Agent
tools: ["calc/*"]
---

# Calc MCP Agent

A minimal Model Context Protocol (MCP) agent that exposes a simple arithmetic tool via an HTTP Streamable server.

## Overview

- Name: `calc`
- Group: `core`
- Version: `1.0.0`
- Transport: HTTP (streamable)
- Endpoint: `http://localhost:9000/mcp/calc`
- Capabilities: tools, resources, prompts

## Tools

The agent registers tools discovered via annotations. Currently available:

- `add_2_integers`
  - Title: Add Two Integers
  - Description: Adds two integers and returns the result
  - Parameters:
    - `num1` (int): First Number
    - `num2` (int): Second Number
  - Returns: `int`

Implementation: see `mcp.example/src/main/java/com/bat/mcp/web/Calc.java`.

## Dependency Injection for Tool Parameters

The MCP server supports **injector-first parameter resolution**, allowing tool parameters to be resolved via Guice
dependency injection before falling back to request arguments.

### How It Works

When a tool method is invoked:

1. **If no argument is provided** in the request → the injector attempts to resolve the parameter by type or `@Named`
   qualifier
2. **If an argument is provided** in the request → the argument value takes precedence and is converted to the target
   type
3. **Fallback** → if injection fails, TypeConverter or Jackson handles primitive/complex type conversion

### Example: Injectable User Parameter

```java
@McpTool(groups = "core")
public String greet(@McpToolParam(name = "user") User user) {
    return "Hello, " + user.name();
}

public record User(String name) {}
```

#### Bind the User in Your Guice Module

```java
public class MyModule extends AbstractModule {
    @Override
    protected void configure() {
        // Bind a singleton User instance
        bind(User.class).toInstance(new User("Alice"));
    }
}
```

Or use a `@Provides` method:

```java
@Provides
@Singleton
User provideUser() {
    return new User("Bob");
}
```

#### Invocation Scenarios

1. **No argument provided** → injector supplies `User("Alice")` or `User("Bob")`

   ```json
   { "name": "greet", "arguments": {} }
   ```

   Result: `"Hello, Alice"` or `"Hello, Bob"`

2. **Argument provided** → overrides injection
   ```json
   { "name": "greet", "arguments": { "user": { "name": "Charlie" } } }
   ```
   Result: `"Hello, Charlie"`

### Named Bindings

You can use `@Named` qualifiers to provide multiple bindings for the same type:

```java
public class MyModule extends AbstractModule {
    @Provides
    @Named("admin")
    User provideAdmin() {
        return new User("AdminUser");
    }

    @Provides
    @Named("guest")
    User provideGuest() {
        return new User("GuestUser");
    }
}
```

The parameter name in `@McpToolParam(name = "admin")` is used to match `@Named("admin")` bindings.

### Benefits

- **Default values**: Provide sensible defaults without cluttering tool signatures
- **Dependency management**: Inject services, configurations, or storage objects
- **Testing**: Easily swap implementations via bindings
- **Flexibility**: Explicit arguments always override injected values

## Server Configuration

The MCP server is configured via `mcp.example/src/main/resources/mcp-server.yml`:

```
enabled: true
mode: STREAMABLE
name: calc
groups: [ "core" ]
version: 1.0.0
type: SYNC
request-timeout: 20000
capabilities:
  resource: true
  prompt: true
  tool: true
change-notification:
  resource: true
  prompt: true
  tool: true
streamable:
  mcp-endpoint: /mcp/calc
  disallow-delete: true
  keep-alive-interval: 30000
  port: 9000
```

## How to Run

1. Build the project (from repo root):

```sh
mvn -DskipTests package
```

2. Start the example Calc MCP server:

```sh
java -cp mcp.example/target/classes:$(mvn -q -Dexec.cleanupDaemonThreads=false -Dexec.classpathScope=compile -Dexec.printClasspath -Dexec.javaExecutable=echo --non-recursive org.codehaus.mojo:exec-maven-plugin:3.5.0:classpath | tail -n 1) org.catools.mcp.core.McpServer
```

Alternatively, run via your IDE using the `main` in `CWebMcpServer`.

Once running, the Calc agent will be available at `http://localhost:9000/mcp/calc`.

## Client Integration

Point your MCP client to the endpoint:

- Base URL: `http://localhost:9000`
- MCP path: `/mcp/calc`

On connect, the client should:

- Fetch server info and capabilities
- Discover tools (`add_2_integers`)
- Invoke tools with JSON arguments, e.g.:

```json
{
  "name": "add_2_integers",
  "arguments": { "num1": 5, "num2": 7 }
}
```

## Notes

- Tools are discovered via `@McpTool` annotations and are grouped under `core`.
- Dependency injection binds an instance of `Calc` via `CWebMcpInjectorModule`.
- Tool parameters support injector-first resolution - see "Dependency Injection for Tool Parameters" above.
- Update `mcp-server.yml` if you change port or endpoint.
