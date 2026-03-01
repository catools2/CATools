package org.catools.mcp.utils;

import org.catools.mcp.exceptions.InvalidJsonFormatException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Objects;

@UtilityClass
public class JsonUtil {

    /**
     * Converts an object to its JSON string representation.
     *
     * @param object  the object to convert
     * @param modules optional Jackson modules to register
     * @return the JSON string representation of the object
     */
    public static String toString(Object object, Module... modules) {
        try {
            Objects.requireNonNull(object);
            return getObjectWriter(modules).withDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Throwable t) {
            throw new RuntimeException("Could not convert object to JSON string", t);
        }
    }

    /**
     * Convert json string to provided class type
     *
     * @param input
     * @param clazz
     * @param modules
     * @param <T>
     * @return
     */
    public static <T> T read(String input, Class<T> clazz, Module... modules) {
        try {
            ObjectMapper mapper = getObjectMapper(modules);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(input, clazz);
        } catch (IOException e) {
            throw new InvalidJsonFormatException("Could not read json from " + input, e);
        }
    }


    private static synchronized ObjectWriter getObjectWriter(Module... modules) {
        ObjectMapper mapper = getObjectMapper(modules);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        return mapper.writerWithDefaultPrettyPrinter();
    }

    private static synchronized ObjectMapper getObjectMapper(Module... modules) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(modules);
        return mapper;
    }
}
