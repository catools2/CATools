package org.catools.mcp.utils;

import io.modelcontextprotocol.util.Assert;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.stream.Streams;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@UtilityClass
public class ResourceUtil {
    /**
     * Extracts a configuration file from the classpath to a temporary file.
     * This is needed when running from a JAR, as YamlConfigurationLoader requires file system access.
     *
     * @param configFileName the name of the configuration file on the classpath
     * @return the path to the extracted file, or the original filename if extraction fails
     */
    public static File extractConfigFromJar(String configFileName) {
        Assert.notNull(configFileName, "configFileName must not be null");

        try {
            // Try to load as a resource from classpath
            InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);

            if (resourceStream == null) {
                log.warn("Could not find {} in classpath, assuming it's an external file", configFileName);
                return new File(configFileName);
            }

            // Create a temporary file
            Path tempFile = Files.createTempFile("mcp-", "-" + configFileName);
            tempFile.toFile().deleteOnExit(); // Clean up on exit

            // Copy resource to temporary file
            Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            resourceStream.close();

            File absolutePath = tempFile.toFile();
            log.info("Extracted {} from JAR to temporary file: {}", configFileName, absolutePath);
            return absolutePath;
        } catch (IOException e) {
            log.error("Failed to extract config file from JAR: {}", e.getMessage());
            // Fall back to original filename - might work if file is external
            return new File(configFileName);
        }
    }


    public static String readResourceAsString(String resourcePath) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                return sb.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + resourcePath, e);
        }
    }

    public static Set<String> listFiles(String resourcePath) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(resourcePath);

        if (url == null) {
            throw
                    new RuntimeException("Resource %s not found".formatted(resourcePath));
        }

        try {
            File dir = new File(url.toURI());
            return Streams.of(dir.listFiles()).filter(File::isFile).map(file -> file.getName()).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list files in resource path " + resourcePath, e);
        }
    }
}
