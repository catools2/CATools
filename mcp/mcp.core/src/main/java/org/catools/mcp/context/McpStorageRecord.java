package org.catools.mcp.context;

import org.catools.mcp.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;


public record McpStorageRecord(String clazz, String key, Object value) {

    public static McpStorageRecord of(JsonNode schema, String key, Object value) {
        String baseClass = schema != null && schema.has("$class") ? schema.get("$class").asText() : null;
        return new McpStorageRecord(baseClass, key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPojo() {
        // If not value then return null
        if (value == null) {
            return null;
        }
        // If not class defined then return value as is
        if (StringUtils.isBlank(clazz)) {
            return (T) value;
        }
        try {
            Class<?> entityType = Class.forName(clazz);
            return (T) JsonUtil.read(value.toString(), entityType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
