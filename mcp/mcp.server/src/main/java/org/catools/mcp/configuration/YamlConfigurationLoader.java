package org.catools.mcp.configuration;

import org.catools.mcp.exception.McpServerConfigurationException;
import org.catools.mcp.utils.YamlUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.catools.mcp.utils.ResourceUtil.extractConfigFromJar;

/**
 * This record represents a YAML configuration loader for MCP (Model Context Protocol) server
 * configuration.
 *
 * <p>It loads the server configuration from a specified YAML file. If no file name is provided, the
 * default file name "mcp-server.yml" will be used.
 */
@Slf4j
public record YamlConfigurationLoader(String configFileName) {

    /**
     * The default file name for the MCP server configuration file.
     */
    private static final String DEFAULT_CONFIG_FILE_NAME = "mcp-server.yml";

    /**
     * Constructs a YAMLConfigurationLoader with the default configuration file name.
     */
    public YamlConfigurationLoader() {
        this(DEFAULT_CONFIG_FILE_NAME);
    }

    /**
     * Loads the MCP server configuration from the specified YAML file.
     *
     * @return the loaded MCP server configuration
     * @throws McpServerConfigurationException if the configuration file cannot be loaded
     */
    public McpServerConfiguration loadConfig() {
        File file = extractConfigFromJar(configFileName);
        McpServerConfiguration config = YamlUtil.readFromFile(file, McpServerConfiguration.class);
        log.info("Configuration loaded successfully from file: {}", configFileName);
        return config;
    }

    /**
     * Returns the file path of the configuration file.
     *
     * @param fileName the name of the configuration file
     * @return the file path of the configuration file
     * @throws McpServerConfigurationException if the configuration file cannot be found
     */
    private Path getConfigFilePath(String fileName) {
        try {
            ClassLoader classLoader = YamlConfigurationLoader.class.getClassLoader();
            URL configFileUrl = classLoader.getResource(fileName);
            if (configFileUrl == null) {
                throw new McpServerConfigurationException("Configuration file not found: " + fileName);
            }
            return Paths.get(configFileUrl.toURI());
        } catch (URISyntaxException e) {
            // should never happen
            throw new McpServerConfigurationException("Invalid configuration file: " + fileName, e);
        }
    }
}
