package org.catools.mcp.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility for converting JSON Schema files into example JSON nodes.
 *
 * <p>Responsibilities: - Load schemas from ".github/skills/schemas/entities". - Walk schema
 * structure to build a JSON node representation. - Resolve $ref references using a simple cache
 * (seenRefs) to avoid repeated file I/O.
 *
 * <p>Current behavior: - Object schemas: iterate over "properties" and generate values for each. -
 * Array schemas: look at "items"; if "$ref" exists, load and insert referenced schema. - Primitive
 * or unsupported types: return NullNode to maintain structural placeholders.
 *
 * <p>Notes: - External $ref is supported via filename (e.g., "Some.schema"); local "#/..." refs are
 * not handled in this version. - seenRefs cache prevents re-reading the same $ref multiple times
 * but does not detect cycles.
 */
@Slf4j
@UtilityClass
public class CMcpContextDataConvertor {
  // Initialize a cache for $ref targets to avoid repeated reads
  private final ConcurrentHashMap<String, JsonNode> seenRefs = new ConcurrentHashMap<>();

  /**
   * Entry point: read a schema by logical name and convert it to a JSON node.
   *
   * @param schemaName logical entity name (without extension)
   * @return JsonNode representing an example object derived from the schema
   */
  public JsonNode schemaToJson(String schemaName) {
    File schemaFile = new File(".github/skills/schemas/entities/%s.schema".formatted(schemaName));
    return readSchema(schemaFile);
  }

  /**
   * Read a schema by $ref string (external reference), then convert to JSON. Example:
   * "User.schema".
   *
   * @param ref external schema filename
   * @return JsonNode derived from the referenced schema
   */
  private JsonNode readSchema(String ref) {
    File schemaFile = new File(".github/skills/schemas/entities/" + ref);

    return readSchema(schemaFile);
  }

  /**
   * Load a schema from disk and generate a JSON node from it.
   *
   * @param schemaFile file pointing to a JSON Schema
   * @return JsonNode as generated example
   * @throws RuntimeException when I/O fails
   */
  private JsonNode readSchema(File schemaFile) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      JsonNode schema = mapper.readTree(schemaFile);
      return generateFromSchema(schema, mapper);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Generate a JsonNode from a schema. - If "properties" exists, builds an object and processes
   * each property. - Otherwise delegates to readValue for arrays or primitive placeholders.
   *
   * @param schema current schema node
   * @param mapper object mapper for node creation
   * @return generated JsonNode
   */
  private JsonNode generateFromSchema(JsonNode schema, ObjectMapper mapper) {
    if (schema.has("properties")) {
      ObjectNode result = mapper.createObjectNode();
      schema
          .get("properties")
          .fields()
          .forEachRemaining(
              entry -> {
                String key = entry.getKey();
                result.set(key, readEntry(mapper, entry));
              });
      return result;
    }

    return readValue(schema, mapper);
  }

  /**
   * Read a single property entry: - If it contains "$ref", resolve via cache and return referenced
   * node. - Otherwise, interpret the property directly (arrays/primitive placeholders).
   *
   * @param mapper object mapper
   * @param entry property key/value pair
   * @return value node for the property
   */
  private JsonNode readEntry(ObjectMapper mapper, Map.Entry<String, JsonNode> entry) {
    JsonNode prop = entry.getValue();

    // Resolve external $ref references by filename
    if (prop.has("$ref")) {
      String ref = prop.get("$ref").asText();
      // Cache the resolved schema to avoid repeated disk reads
      seenRefs.computeIfAbsent(ref, CMcpContextDataConvertor::readSchema);
      return seenRefs.get(ref);
    }

    return readValue(prop, mapper);
  }

  /**
   * Read a schema node that is not an object with "properties": - Arrays: process "items" and
   * handle "$ref" on the item. - Otherwise: return NullNode as placeholder for
   * unsupported/primitive definitions.
   *
   * @param prop schema node to interpret
   * @param mapper object mapper
   * @return generated node (ArrayNode or NullNode)
   */
  private JsonNode readValue(JsonNode prop, ObjectMapper mapper) {
    // Array handling: generate a sample array with a single item (if items are defined)
    if (prop.has("items")) {
      ArrayNode arrayNode = mapper.createArrayNode();
      JsonNode item = prop.get("items");

      if (item.has("$ref")) {
        String ref = item.get("$ref").asText();
        // External $ref resolution for array items
        seenRefs.computeIfAbsent(ref, CMcpContextDataConvertor::readSchema);
        arrayNode.add(seenRefs.get(ref));
      }

      // For non-ref items, current behavior returns empty array placeholder
      return arrayNode;
    }

    // Primitive/unsupported types are returned as NullNode to keep structure predictable
    return NullNode.getInstance();
  }
}
