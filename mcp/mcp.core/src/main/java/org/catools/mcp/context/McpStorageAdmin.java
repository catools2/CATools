package org.catools.mcp.context;

import org.catools.mcp.annotation.McpTool;
import org.catools.mcp.annotation.McpToolParam;
import org.catools.mcp.utils.ResourceUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

import static org.catools.mcp.constant.McpConstant.SCHEMAS;

@Slf4j
public class McpStorageAdmin {
    private static final Set<String> SUPPORTED_TYPES = Sets.newHashSet();

    @McpTool(
            groups = "mcp-storage",
            name = "bat_variable_define",
            title = "Get or define variable from Storage",
            description = "Retrieves or define variable by name from the Storage to be used in other tools")
    public static JsonNode defineVariable(
            @McpToolParam(name = "name", description = "The variable name to save in the storage")
            String name,
            @McpToolParam(name = "type", description = "The type of the variable to generate if name does not saved in the storage")
            String type
    ) {
        JsonNode existing = McpDataStorage.read(name);
        if (existing != null) {
            log.debug("Entity '{}' found in Storage", name);
            return existing;
        }

        // Entity doesn't exist, create it from schema and cache it
        log.debug("Entity '{}' not found, creating from schema type '{}'", name, type);
        JsonNode schema = McpStorageConvertor.readSchema(type);
        JsonNode newEntity = McpStorageConvertor.schemaToJson(type);
        McpDataStorage.write(schema, name, newEntity);
        log.debug("Entity '{}' created and cached in Storage", name);
        return newEntity;
    }

    @McpTool(
            groups = "mcp-storage",
            name = "bat_variable_list",
            title = "Get list of all defined variables",
            description = "Retrieves list of the defined variables that is using in the MCP tools")
    public static Set<String> getDefinedVariables() {
        return McpDataStorage.getCopy().keySet();
    }

    @McpTool(
            groups = "mcp-storage",
            name = "bat_variable_update",
            title = "Update Entity Field",
            description = "Updates a specific field in an variable stored in the Storage using JsonPath")
    public static JsonNode updateDefinedVariable(
            @McpToolParam(name = "name", description = "The variable name to save in the storage")
            String name,
            @McpToolParam(name = "type", description = "The type of the variable to generate if name does not saved in the storage")
            String type,
            @McpToolParam(name = "jsonPath", description = "JsonPath to the field to update based on variable schema (e.g., $.user.userName)")
            String jsonPath,
            @McpToolParam(name = "fieldValue", description = "Value to set")
            String fieldValue
    ) {
        try {
            // Get the variable as JsonNode
            JsonNode obj = defineVariable(name, type);
            log.debug("Before update - Entity '{}': {}", name, obj);

            JsonNode schema = McpStorageConvertor.readSchema(type);
            JsonNode updatedNode = McpStorageHelper.setField(obj, jsonPath, fieldValue);
            McpDataStorage.write(schema, name, updatedNode);

            log.debug("After update - Entity '{}' at path '{}': {}", name, jsonPath, updatedNode);
            log.debug("Updated path '{}' to value: {}", jsonPath, fieldValue);
            return updatedNode;
        } catch (Exception e) {
            log.debug("Failed to update variable '{}' at path '{}'", name, jsonPath, e);
            throw new IllegalStateException("Failed to update field: " + e.getMessage(), e);
        }
    }

    @McpTool(
            groups = "mcp-storage",
            name = "bat_variable_delete",
            title = "Delete/Remove variable from Storage",
            description = "Delete a variable by name from the Storage. return true if record wsa existed otherwise false")
    public static boolean deleteDefinedVariable(
            @McpToolParam(name = "name", description = "The defined variable name to delete")
            String name
    ) {
        return McpDataStorage.remove(name);
    }

    @McpTool(
            groups = "mcp-storage",
            name = "bat_variable_types",
            title = "Get list of supporting types",
            description = "Return list of supporting types.")
    public static Set<String> getListOfSupportingTypes() {
        if (SUPPORTED_TYPES.isEmpty()) {
            List<String> types = ResourceUtil.listFiles(SCHEMAS).stream()
                    .map(s -> s.replace(".schema", ""))
                    .toList();
            SUPPORTED_TYPES.addAll(types);
        }
        return SUPPORTED_TYPES;
    }
}
