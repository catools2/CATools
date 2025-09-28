package org.catools.pipeline.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Utility class for managing pipeline TestNG configuration settings.
 * This class provides access to configuration properties that control the behavior
 * of pipeline listeners in TestNG test execution environments.
 * 
 * <p>Configuration properties are loaded from HOCON configuration files and can be
 * overridden using system properties or environment variables.</p>
 * 
 * <h3>Configuration Properties:</h3>
 * <ul>
 *   <li>{@code catools.pipeline.listener.enabled} - Enables/disables pipeline listener</li>
 *   <li>{@code catools.pipeline.listener.always_create_new_pipeline} - Forces creation of new pipeline</li>
 *   <li>{@code catools.pipeline.listener.create_if_not_exist} - Creates pipeline if it doesn't exist</li>
 * </ul>
 * 
 * <h3>Example Configuration (application.conf):</h3>
 * <pre>{@code
 * catools {
 *   pipeline {
 *     listener {
 *       enabled = true
 *       always_create_new_pipeline = false
 *       create_if_not_exist = true
 *     }
 *   }
 * }
 * }</pre>
 * 
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * // Check if pipeline listener is enabled
 * if (CPipelineTestNGConfigs.isEnabled()) {
 *     // Initialize pipeline listener
 *     setupPipelineListener();
 * }
 * 
 * // Configure pipeline creation behavior
 * if (CPipelineTestNGConfigs.always_create_new_pipeline()) {
 *     // Always create a new pipeline, even if one exists
 *     createNewPipeline();
 * } else if (CPipelineTestNGConfigs.createPipelineIfNotExist()) {
 *     // Create pipeline only if it doesn't exist
 *     createPipelineIfNeeded();
 * }
 * }</pre>
 * 
 * @author CATools Team
 * @since 1.0
 */
@UtilityClass
public class CPipelineTestNGConfigs {
  /**
   * Checks if the pipeline listener is enabled.
   * 
   * <p>When enabled, the pipeline listener will be active during TestNG test execution
   * and will capture test execution data for pipeline tracking and reporting.</p>
   * 
   * <h4>Configuration Property:</h4>
   * {@code catools.pipeline.listener.enabled}
   * 
   * <h4>Default Value:</h4>
   * {@code false} (if not specified in configuration)
   * 
   * <h4>Example:</h4>
   * <pre>{@code
   * // In application.conf
   * catools.pipeline.listener.enabled = true
   * 
   * // In test code
   * if (CPipelineTestNGConfigs.isEnabled()) {
   *     System.out.println("Pipeline listener is active");
   *     // Setup pipeline tracking
   * } else {
   *     System.out.println("Pipeline listener is disabled");
   * }
   * }</pre>
   * 
   * @return {@code true} if the pipeline listener is enabled, {@code false} otherwise
   */
  public static boolean isEnabled() {
    return CHocon.asBoolean(Configs.CATOOLS_PIPELINE_LISTENER_ENABLED);
  }

  /**
   * Determines whether a new pipeline should always be created, regardless of existing pipelines.
   * 
   * <p>When this option is enabled, the system will create a new pipeline for each test execution,
   * even if a pipeline with the same identifier already exists. This is useful for scenarios where
   * you want to maintain separate pipeline instances for different test runs or environments.</p>
   * 
   * <h4>Configuration Property:</h4>
   * {@code catools.pipeline.listener.always_create_new_pipeline}
   * 
   * <h4>Default Value:</h4>
   * {@code false} (if not specified in configuration)
   * 
   * <h4>Example:</h4>
   * <pre>{@code
   * // In application.conf
   * catools.pipeline.listener.always_create_new_pipeline = true
   * 
   * // In test code
   * if (CPipelineTestNGConfigs.always_create_new_pipeline()) {
   *     // Force creation of a new pipeline
   *     String pipelineId = generateUniquePipelineId();
   *     createPipeline(pipelineId);
   *     System.out.println("Created new pipeline: " + pipelineId);
   * } else {
   *     // Use existing pipeline or create if needed
   *     reuseOrCreatePipeline();
   * }
   * }</pre>
   * 
   * @return {@code true} if a new pipeline should always be created, {@code false} otherwise
   */
  public static boolean always_create_new_pipeline() {
    return CHocon.asBoolean(Configs.CATOOLS_PIPELINE_LISTENER_ALWAYS_CREATE_NEW_PIPELINE);
  }

  /**
   * Determines whether a pipeline should be created if it doesn't already exist.
   * 
   * <p>When this option is enabled, the system will check for the existence of a pipeline
   * and create one only if it doesn't exist. This is the recommended approach for most
   * scenarios as it ensures pipeline continuity while avoiding unnecessary duplicates.</p>
   * 
   * <p><strong>Note:</strong> This setting is ignored if {@link #always_create_new_pipeline()}
   * returns {@code true}, as the latter takes precedence.</p>
   * 
   * <h4>Configuration Property:</h4>
   * {@code catools.pipeline.listener.create_if_not_exist}
   * 
   * <h4>Default Value:</h4>
   * {@code true} (if not specified in configuration)
   * 
   * <h4>Example:</h4>
   * <pre>{@code
   * // In application.conf
   * catools.pipeline.listener.create_if_not_exist = true
   * 
   * // In test code
   * String pipelineId = "my-test-pipeline";
   * 
   * if (CPipelineTestNGConfigs.always_create_new_pipeline()) {
   *     // Always create new - highest priority
   *     createNewPipeline(generateUniquePipelineId());
   * } else if (CPipelineTestNGConfigs.createPipelineIfNotExist()) {
   *     // Create only if doesn't exist
   *     if (!pipelineExists(pipelineId)) {
   *         createPipeline(pipelineId);
   *         System.out.println("Created pipeline: " + pipelineId);
   *     } else {
   *         System.out.println("Using existing pipeline: " + pipelineId);
   *     }
   * } else {
   *     // Don't create pipeline automatically
   *     System.out.println("Pipeline auto-creation disabled");
   * }
   * }</pre>
   * 
   * @return {@code true} if a pipeline should be created when it doesn't exist, {@code false} otherwise
   */
  public static boolean createPipelineIfNotExist() {
    return CHocon.asBoolean(Configs.CATOOLS_PIPELINE_LISTENER_CREATE_IF_NOT_EXIST);
  }

  /**
   * Enumeration of configuration paths used by the pipeline TestNG module.
   * 
   * <p>This enum implements {@link CHoconPath} to provide type-safe access to configuration
   * properties. Each enum constant represents a specific configuration path in the HOCON
   * configuration hierarchy.</p>
   * 
   * <h4>Configuration Structure:</h4>
   * <pre>{@code
   * catools {
   *   pipeline {
   *     listener {
   *       enabled = true                      // CATOOLS_PIPELINE_LISTENER_ENABLED
   *       always_create_new_pipeline = false  // CATOOLS_PIPELINE_LISTENER_ALWAYS_CREATE_NEW_PIPELINE  
   *       create_if_not_exist = true          // CATOOLS_PIPELINE_LISTENER_CREATE_IF_NOT_EXIST
   *     }
   *   }
   * }
   * }</pre>
   * 
   * <h4>Environment Variable Override Examples:</h4>
   * <pre>{@code
   * # Enable pipeline listener via environment variable
   * export CATOOLS_PIPELINE_LISTENER_ENABLED=true
   * 
   * # Force new pipeline creation via system property
   * -Dcatools.pipeline.listener.always_create_new_pipeline=true
   * }</pre>
   */
  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    /** Configuration path for enabling/disabling the pipeline listener */
    CATOOLS_PIPELINE_LISTENER_ENABLED("catools.pipeline.listener.enabled"),
    
    /** Configuration path for forcing creation of new pipelines */
    CATOOLS_PIPELINE_LISTENER_ALWAYS_CREATE_NEW_PIPELINE("catools.pipeline.listener.always_create_new_pipeline"),
    
    /** Configuration path for creating pipelines when they don't exist */
    CATOOLS_PIPELINE_LISTENER_CREATE_IF_NOT_EXIST("catools.pipeline.listener.create_if_not_exist");

    /** The HOCON configuration path */
    private final String path;
  }
}
