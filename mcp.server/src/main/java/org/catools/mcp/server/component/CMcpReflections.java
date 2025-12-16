package org.catools.mcp.server.component;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.annotation.CMcpServerApplication;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.reflections.scanners.Scanners.FieldsAnnotated;
import static org.reflections.scanners.Scanners.MethodsAnnotated;

/**
 * Registers MCP server components (resources, prompts, and tools) with the specified server.
 * Uses Lombok to reduce boilerplate code for field access.
 */
@Slf4j
@UtilityClass
public final class CMcpReflections {
  /**
   * The reflections instance for MCP server components.
   */
  @Getter
  private final Set<Reflections> reflections = new HashSet<>();

  public void addReflections(Set<Reflections> reflections) {
    CMcpReflections.reflections.addAll(reflections);
  }


  /**
   * Provides a {@link Reflections} instance for the main class.
   *
   * @return a {@link Reflections} instance for the main class
   */
  public void registerReflections(Class<?> mainClass) {
    CMcpServerApplication application = mainClass.getAnnotation(CMcpServerApplication.class);
    final Set<String> basePackage = determineBasePackage(mainClass, application);
    basePackage.stream()
        .map(p -> new Reflections(p, MethodsAnnotated, FieldsAnnotated))
        .forEach(reflections::add);
  }

  /**
   * Determines the base package for the {@link Reflections} instance to scan.
   *
   * @param application the {@link CMcpServerApplication} annotation
   * @return the base package for the {@link Reflections} instance to scan
   */
  private Set<String> determineBasePackage(Class<?> mainClass, CMcpServerApplication application) {
    if (application != null) {
      if (application.basePackages() != null && application.basePackages().length > 0) {
        Set<String> basePackages = Arrays.stream(application.basePackages())
            .map(String::trim)
            .filter(pkg -> !pkg.isBlank())
            .collect(toSet());
        ;
        if (!basePackages.isEmpty()) {
          return basePackages;
        }
      }
      if (application.basePackageClass() != Object.class) {
        return Set.of(application.basePackageClass().getPackageName());
      }
    }
    return Set.of(mainClass.getPackageName());
  }
}
