package org.catools.mcp.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CMcpStorageHelper {

  /**
   * Updates a field at the specified path by directly accessing fields (no getters/setters),
   * creating intermediate objects when they are null.
   */
  public static JsonNode setField(JsonNode obj, String jsonPath, Object fieldValue) {
    try {
      // Convert JsonNode to JSON string for JsonPath manipulation
      ObjectMapper mapper = new ObjectMapper();
      String jsonString = mapper.writeValueAsString(obj);

      // Use JsonPath to update the field
      DocumentContext ctx = JsonPath.parse(jsonString);
      ctx.set(jsonPath, fieldValue);
      String updatedJsonString = ctx.jsonString();

      return mapper.readTree(updatedJsonString);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to update field: " + e.getMessage(), e);
    }
  }
}
