# CATools Project - GitHub Copilot Instructions

**Project:** Core Automation Toolset (CATools)  
**Version:** 2.0.0  
**Java Version:** 21  
**Build Tool:** Maven 3.6.3+  
**Last Updated:** November 21, 2025

---

## Project Overview

CATools is a comprehensive automation testing framework providing tools for:
- Web automation (Selenium, Axe accessibility)
- API testing (REST/SOAP)
- Database testing (Hibernate, SQL)
- Test management integration (Jira, TestNG, ReportPortal)
- Media processing (POI, image analysis)
- Kubernetes/containerization support
- MCP (Model Context Protocol) server integration

---

## Core Principles

### 1. Naming Conventions

**CRITICAL:** All CATools-specific classes MUST start with 'C' prefix:

```java
// ✅ CORRECT
public class CStringUtil { }
public class CJsonUtil { }
public class CMcpServer { }
public record CUserData(...) { }
public enum CServerType { }

// ❌ WRONG - Missing 'C' prefix
public class StringUtil { }
public class JsonUtil { }
public class McpServer { }
```

**Exceptions (External/Protocol Classes):**
- MCP protocol classes keep 'Mcp' prefix: `McpServerInfo`, `@McpTool`
- Third-party integrations keep original names
- Standard Java classes (no prefix needed)

### 2. Package Structure

```
org.catools/
├── common/              # Common utilities and core functionality
│   ├── utils/          # Utility classes (CStringUtil, CJsonUtil, etc.)
│   ├── exception/      # Custom exceptions (C*Exception)
│   ├── configs/        # Configuration classes
│   └── hocon/          # HOCON configuration support
├── mcp/                # Model Context Protocol integration
├── web/                # Web automation (Selenium)
├── ws/                 # Web services (REST/SOAP)
├── sql/                # Database utilities
├── pipeline/           # Test pipeline orchestration
└── [domain]/           # Domain-specific modules
```

---

## Code Style Guidelines

### 1. Use Lombok Appropriately

**Always Use:**
```java
// Logging
@Slf4j
public class CMyService {
    public void doWork() {
        log.info("Work started");
    }
}

// Utility classes
@UtilityClass
public class CMyUtil {
    public static String format(String input) { ... }
}

// Getters only when needed
@Getter
public class CMyClass {
    private final String value;
}
```

**Prefer Java Records over Lombok @Value:**
```java
// ✅ CORRECT - Use Java record
public record CUserConfig(
    String name,
    int age,
    boolean active
) {}

// ❌ AVOID - Don't use Lombok @Value
@Value
public class UserConfig {
    String name;
    int age;
    boolean active;
}
```

**Never Use:**
- `@Data` - Too broad, prefer specific annotations
- `@Builder` on mutable classes
- `@AllArgsConstructor` without `@NoArgsConstructor` for JPA entities

### 2. Exception Handling

All custom exceptions must:
- Extend appropriate base exception
- Use 'C' prefix: `C*Exception`
- Include descriptive messages
- Provide context in error messages

```java
// ✅ CORRECT
public class CInvalidConfigException extends CRuntimeException {
    public CInvalidConfigException(String message) {
        super(message);
    }
    
    public CInvalidConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Usage
throw new CInvalidConfigException("Failed to load config from: " + path, e);
```

### 3. JavaDoc Standards

**Required for:**
- All public classes
- All public methods
- All public fields/constants

**Style:**
```java
/**
 * Utility class providing string manipulation operations for automation testing.
 * Extends Apache Commons StringUtils with test-specific functionality.
 *
 * <p>This class is designed for high-performance test automation scenarios
 * where unchecked exceptions are preferred for readability.
 *
 * @see org.apache.commons.lang3.StringUtils
 */
@UtilityClass
public class CStringUtil extends StringUtils {
    
    /**
     * Returns substring with maximum length. If string is shorter, returns full string.
     *
     * @param input the input string to process
     * @param maxLength the maximum length of the result
     * @return substring of at most maxLength characters
     */
    public static String trySubstring(String input, int maxLength) {
        if (StringUtils.isBlank(input)) return "";
        return input.substring(0, Math.min(input.length(), maxLength));
    }
}
```

### 4. Method Naming

**Use descriptive, action-oriented names:**
```java
// ✅ GOOD
public void validateConfiguration() { }
public CUser findUserById(String id) { }
public boolean hasActiveSession() { }
public void performHealthCheck() { }

// ❌ AVOID
public void doStuff() { }
public CUser get(String id) { }  // Too generic
public boolean check() { }        // Unclear what's being checked
```

### 5. Configuration Management

**Use HOCON for configuration:**
```java
// Reading configuration
String value = CHocon.asString(CPathConfigs.Configs.CATOOLS_OUTPUT_PATH);
boolean enabled = CHocon.asBoolean("catools.feature.enabled");

// Configuration enum pattern
@Getter
@AllArgsConstructor
private enum Configs implements CHoconPath {
    CATOOLS_OUTPUT_PATH("catools.output.path"),
    CATOOLS_RETRY_COUNT("catools.retry.count");
    
    private final String path;
}
```

---

## Testing Guidelines

### 1. TestNG Best Practices

```java
@Slf4j
public class CMyFeatureTest {
    
    @Test(description = "Verify user creation with valid data")
    public void testCreateUserSuccess() {
        // Arrange
        CUserData userData = CUserData.builder()
            .name("John Doe")
            .email("john@example.com")
            .build();
        
        // Act
        CUser user = userService.create(userData);
        
        // Assert
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
    }
}
```

### 2. Use AssertJ for Assertions

```java
// ✅ CORRECT - Use AssertJ
assertThat(result).isNotNull();
assertThat(users).hasSize(5);
assertThat(user.getName()).isEqualTo("John");

// ❌ AVOID - TestNG assertions
Assert.assertNotNull(result);
Assert.assertEquals(users.size(), 5);
```

---

## Maven Module Guidelines

### 1. Module Naming

```
[domain].[subdomain]/
├── pom.xml
└── src/
    ├── main/java/org/catools/[domain]/
    └── test/java/org/catools/[domain]/
```

### 2. POM File Standards

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <artifactId>[domain].[subdomain]</artifactId>
    <packaging>jar</packaging>
    
    <name>Core Automation Toolset - [Description]</name>
    <description>Detailed description of module purpose</description>
    
    <parent>
        <groupId>org.catools</groupId>
        <artifactId>catools.parent</artifactId>
        <version>${revision}</version>
        <relativePath>../catools.parent/pom.xml</relativePath>
    </parent>
    
    <dependencies>
        <!-- Internal dependencies first -->
        <dependency>
            <groupId>org.catools</groupId>
            <artifactId>common.utils</artifactId>
            <version>${revision}</version>
        </dependency>
        
        <!-- External dependencies -->
        <!-- Group by functionality -->
    </dependencies>
</project>
```

---

## Common Patterns

### 1. Singleton Enum Pattern

```java
@Slf4j
public enum CMyService {
    INSTANCE;
    
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
    
    public void doWork() {
        log.info("Performing work");
    }
}

// Usage
CMyService.INSTANCE.doWork();
```

### 2. Builder Pattern (for complex objects)

```java
@Getter
public class CServerConfig {
    private final String host;
    private final int port;
    private final Duration timeout;
    
    protected CServerConfig(Builder<?> builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.timeout = builder.timeout;
    }
    
    public static Builder<?> builder() {
        return new Builder<>();
    }
    
    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder<T>> {
        protected String host = "localhost";
        protected int port = 8080;
        protected Duration timeout = Duration.ofSeconds(30);
        
        protected T self() {
            return (T) this;
        }
        
        public T host(String host) {
            this.host = host;
            return self();
        }
        
        public T port(int port) {
            this.port = port;
            return self();
        }
        
        public T timeout(Duration timeout) {
            this.timeout = timeout;
            return self();
        }
        
        public CServerConfig build() {
            return new CServerConfig(this);
        }
    }
}
```

### 3. Immutable Wrapper Pattern

```java
// Use CImmutable for thread-safe immutable wrappers
CImmutable<User> user = CImmutable.of(new User());
User actualUser = user.get();
```

---

## Integration Guidelines

### 1. CATools Common Utilities

**Always prefer CATools common utilities over creating new ones:**

```java
// ✅ CORRECT - Use existing utilities
import org.catools.common.utils.CStringUtil;
import org.catools.common.utils.CJsonUtil;
import org.catools.common.utils.CYamlUtil;
import org.catools.common.utils.CFileUtil;

String json = CJsonUtil.toString(object);
MyConfig config = CYamlUtil.readFromFile(file, MyConfig.class);
String cleaned = CStringUtil.normalizeSpace(input);

// ❌ WRONG - Don't create duplicate utilities
public class MyJsonUtil {
    public static String toJson(Object obj) { ... }
}
```

### 2. Dependency Injection (MCP modules)

```java
// Use Guice for DI
@Singleton
public class CMyService {
    
    @Inject
    public CMyService(CDependency dependency) {
        // Constructor injection preferred
    }
}
```

### 3. External Library Integration

When integrating external libraries:
1. Create a wrapper module if complex
2. Use 'C' prefix for wrapper classes
3. Delegate to external library
4. Add deprecation warnings for future migration

```java
@UtilityClass
@Deprecated
public class CExternalLibWrapper {
    /**
     * @deprecated Use {@link org.catools.common.utils.CJsonUtil} instead
     */
    @Deprecated
    public static String toJson(Object obj) {
        return org.catools.common.utils.CJsonUtil.toString(obj);
    }
}
```

---

## MCP Module Specific Guidelines

### 1. MCP Annotations

```java
@CMcpTool(
    name = "search_users",
    description = "Search for users by criteria"
)
public List<User> searchUsers(
    @CMcpToolParam(description = "Search query") String query,
    @CMcpToolParam(description = "Maximum results") int limit
) {
    // Implementation
}
```

### 2. MCP Server Configuration

```java
CMcpServerInfo serverInfo = CMcpServerInfo.builder()
    .name("catools-mcp-server")
    .version("2.0.0")
    .requestTimeout(Duration.ofSeconds(30))
    .build();
```

---

## Code Review Checklist

Before committing, verify:

- [ ] All classes use 'C' prefix (except MCP protocol classes)
- [ ] Lombok used appropriately (@Slf4j, @UtilityClass, etc.)
- [ ] Java records used instead of Lombok @Value
- [ ] All public APIs have JavaDoc
- [ ] Exceptions include descriptive messages
- [ ] No duplicate utility code (reuse common.utils)
- [ ] Tests use AssertJ assertions
- [ ] Configuration uses HOCON
- [ ] No anti-patterns (@Data, excessive Lombok)
- [ ] Package structure follows conventions

---

## Anti-Patterns to Avoid

### ❌ Don't Do This

```java
// 1. Missing 'C' prefix
public class StringUtil { }

// 2. Using @Data
@Data
public class User { }

// 3. Using Lombok @Value instead of record
@Value
public class Config { }

// 4. Generic exception messages
throw new Exception("Error");

// 5. Duplicate utilities
public class MyJsonUtil {
    public static String toJson(Object o) { ... }
}

// 6. No JavaDoc on public API
public void importantMethod() { }

// 7. TestNG assertions
Assert.assertEquals(actual, expected);
```

---

## Resources

### Common Utilities Available

- **String:** `CStringUtil` - String manipulation
- **JSON:** `CJsonUtil` - JSON serialization
- **YAML:** `CYamlUtil` - YAML processing
- **File:** `CFileUtil` - File operations
- **Date:** `CDateUtil` - Date/time utilities
- **Config:** `CConfigUtil` - Configuration management
- **Console:** `CConsoleUtil` - Console I/O
- **Regex:** `CRegExUtil` - Regular expressions

### Exception Hierarchy

- `CRuntimeException` - Base runtime exception
- `CFileNotFoundException` - File not found
- `CInvalidJsonFormatException` - JSON parsing errors
- `CInvalidYamlFormatException` - YAML parsing errors
- `CMcpServerException` - MCP server errors
  - `CMcpServerConfigurationException` - Configuration errors
  - `CMcpServerJsonProcessingException` - JSON processing errors

---

## Examples

### Creating a New Utility Class

```java
package org.catools.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for XML operations in test automation.
 * Provides methods for parsing, validating, and transforming XML documents.
 */
@Slf4j
@UtilityClass
public class CXmlUtil {
    
    /**
     * Parses XML string into a Document object.
     *
     * @param xmlContent the XML content to parse
     * @return parsed Document object
     * @throws CInvalidXmlFormatException if XML is malformed
     */
    public static Document parse(String xmlContent) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlContent)));
        } catch (Exception e) {
            log.error("Failed to parse XML content", e);
            throw new CInvalidXmlFormatException("Invalid XML format", e);
        }
    }
}
```

### Creating a Configuration Class

```java
package org.catools.web.driver.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Configuration management for WebDriver settings.
 * Uses HOCON for flexible, type-safe configuration.
 */
@UtilityClass
public class CWebDriverConfigs {
    
    public static String getBrowserType() {
        return CHocon.asString(Configs.CATOOLS_WEBDRIVER_BROWSER);
    }
    
    public static boolean isHeadless() {
        return CHocon.asBoolean(Configs.CATOOLS_WEBDRIVER_HEADLESS);
    }
    
    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
        CATOOLS_WEBDRIVER_BROWSER("catools.webdriver.browser"),
        CATOOLS_WEBDRIVER_HEADLESS("catools.webdriver.headless");
        
        private final String path;
    }
}
```

---

## Version Information

- **Framework Version:** 2.0.0
- **Java Version:** 21
- **Maven Version:** 3.6.3+
- **Lombok Version:** 1.18.42
- **Spring Version:** 6.2.11
- **Selenium Version:** 4.38.0
- **TestNG Version:** 7.11.0

---

## Quick Reference

### Most Common Imports

```java
// Lombok
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.UtilityClass;

// CATools Common
import org.catools.common.utils.*;
import org.catools.common.exception.*;
import org.catools.common.configs.*;

// Testing
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;
```

### Command Line

```bash
# Build entire project
mvn clean install

# Build specific module
mvn clean install -pl mcp -am

# Run tests
mvn test

# Skip tests
mvn clean install -DskipTests

# Update dependencies
mvn versions:display-dependency-updates
```

---

**Remember:** When in doubt, look at existing CATools modules for examples. Follow the established patterns for consistency across the codebase.

