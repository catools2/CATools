package org.catools.mcp.context;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.datastate.CDataState;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;

import java.util.Set;

@Slf4j
public class CMcpContextAdminTools {

  @CMcpTool(
      groups = "mcp-admin",
      name = "get_data_state_entity",
      title = "Get entity from data state",
      description = "Retrieves an entity by name from the data state to be used in MCP tools")
  public static JsonNode getDataStateEntity(
      @CMcpToolParam(name = "name", description = "The entity name to save in the context")
          String name,
      @CMcpToolParam(
              name = "type",
              description =
                  "The type of the entity to generate if name does not saved in the context")
          String type) {
    JsonNode existing = CDataState.read(name);
    if (existing != null) {
      log.debug("Entity '{}' found in data state", name);
      return existing;
    }

    // Entity doesn't exist, create it from schema and cache it
    log.debug("Entity '{}' not found, creating from schema type '{}'", name, type);
    JsonNode newEntity = CMcpContextDataConvertor.schemaToJson(type);
    CDataState.write(name, newEntity);
    log.debug("Entity '{}' created and cached in data state", name);
    return newEntity;
  }

  @CMcpTool(
      groups = "mcp-admin",
      name = "get_data_state_names",
      title = "Get name of available data states",
      description = "Retrieves list of the data state names to be used in MCP tools")
  public static Set<String> getDataStateNames() {
    return CDataState.getCopy().keySet();
  }

  @CMcpTool(
      groups = "mcp-admin",
      name = "update_data_state_entity",
      title = "Update Entity Field",
      description = "Updates a specific field in an entity stored in the data state using JsonPath")
  public static JsonNode updateDataStateEntity(
      @CMcpToolParam(name = "name", description = "The entity name to save in the context")
          String name,
      @CMcpToolParam(
              name = "type",
              description =
                  "The type of the entity to generate if name does not saved in the context")
          String type,
      @CMcpToolParam(
              name = "jsonPath",
              description =
                  "JsonPath to the field to update based on entity schema (e.g., $.user.userName)")
          String jsonPath,
      @CMcpToolParam(name = "fieldValue", description = "Value to set") String fieldValue) {
    try {
      // Get the entity as JsonNode
      JsonNode obj = getDataStateEntity(name, type);
      log.debug("Before update - Entity '{}': {}", name, obj);

      JsonNode updatedNode = CMcpContextHelper.setField(obj, jsonPath, fieldValue);
      CDataState.write(name, updatedNode);

      log.debug("After update - Entity '{}' at path '{}': {}", name, jsonPath, updatedNode);
      log.debug("Updated path '{}' to value: {}", jsonPath, fieldValue);

      return updatedNode;
    } catch (Exception e) {
      log.debug("Failed to update entity '{}' at path '{}'", name, jsonPath, e);
      throw new IllegalStateException("Failed to update field: " + e.getMessage(), e);
    }
  }

  @CMcpTool(
      groups = "mcp-admin",
      name = "delete_data_state_entity",
      title = "Delete/Remove entity from data state",
      description =
          "Delete an entity by name from the data state. return true if record wsa existed otherwise false")
  public static boolean deleteDataStateEntity(
      @CMcpToolParam(name = "name", description = "The entity name to save in the context")
          String name) {
    return CDataState.remove(name);
  }
}
