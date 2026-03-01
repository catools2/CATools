package org.catools.mcp.utils;

import org.catools.mcp.exceptions.FileNotFoundException;
import org.catools.mcp.exceptions.InvalidYamlFileFormatException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.experimental.UtilityClass;

import java.io.File;

@UtilityClass
public class YamlUtil {

    /**
     * Reads a YAML file and converts its content to an object of the specified class.
     *
     * @param file    the YAML file to read
     * @param clazz   the class of the object to convert to
     * @param modules optional Jackson modules to register
     * @param <T>     the type of the object to convert to
     * @return the object converted from the YAML file
     * @throws FileNotFoundException          if the specified file does not exist
     * @throws InvalidYamlFileFormatException if the file content cannot be converted to the specified class
     */
    public static <T> T readFromFile(File file, Class<T> clazz, Module... modules) {
        if (!file.exists()) {
            throw new FileNotFoundException(file, "Property file not found.");
        }
        try {
            T t = getObjectMapper(modules).readValue(file, clazz);
            if (t == null) {
                throw new InvalidYamlFileFormatException(file);
            }
            return t;
        } catch (Throwable t) {
            throw new InvalidYamlFileFormatException(file.getPath(), t);
        }
    }

    private static ObjectMapper getObjectMapper(Module... modules) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModules(modules);
        return mapper;
    }
}
