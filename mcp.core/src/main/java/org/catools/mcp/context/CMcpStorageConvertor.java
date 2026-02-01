package org.catools.mcp.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.utils.CResourceUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.catools.mcp.constant.CMcpConstant.SCHEMAS;

/**
 * Utility for converting JSON Schema files into example JSON nodes.
 *
 * <p>Responsibilities: - Load schemas from "schemas/entities". - Walk schema structure to build a
 * JSON node representation. - Resolve $ref references using a simple cache (seenRefs) to avoid
 * repeated file I/O.
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
public class CMcpStorageConvertor {
  // Initialize a cache for $ref targets to avoid repeated reads
  private static final ConcurrentHashMap<String, JsonNode> seenRefs = new ConcurrentHashMap<>();

  /**
   * Read Schema JsonNode from resource.
   *
   * @param resourcePath path pointing to a JSON Schema resource
   * @return JsonNode as schema
   * @throws RuntimeException when I/O fails
   */
  public JsonNode readSchema(String resourcePath) {
    log.debug("Reading schema from {}", resourcePath);
    if (!resourcePath.toLowerCase().contains("/")) {
      resourcePath = "entities/".concat(resourcePath);
    }
    if (!resourcePath.toLowerCase().endsWith(".schema")) {
      resourcePath = resourcePath.concat(".schema");
    }
    String resource = "%s/%s".formatted(SCHEMAS, resourcePath.toLowerCase());
    String schemaFile = CResourceUtil.getString(resource, CMcpStorageConvertor.class);
    Objects.requireNonNull(resourcePath, "resource %s content is null".formatted(resource));
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readTree(schemaFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Entry point: read a schema by logical name and convert it to a JSON node.
   *
   * @param schemaName logical entity name (without extension)
   * @return JsonNode representing an example object derived from the schema
   */
  public JsonNode schemaToJson(String schemaName) {
    return readResource("%s.schema".formatted(schemaName));
  }

  /**
   * Read a schema by $ref string (external reference), then convert to JSON. Example:
   * "User.schema".
   *
   * @param ref external schema filename
   * @return JsonNode derived from the referenced schema
   */
  private JsonNode readResource(String ref) {
    return processSchema(ref);
  }

  /**
   * Load a schema from disk and generate a JSON node from it.
   *
   * @param resourcePath path pointing to a JSON Schema resource
   * @return JsonNode as generated example
   * @throws RuntimeException when I/O fails
   */
  private JsonNode processSchema(String resourcePath) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode schema = readSchema(resourcePath);
    return generateFromSchema(schema, mapper);
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
    String schemaId = schema.get("$id").asText();
    log.debug("Generating schema from {}", schemaId);
    if (schema.has("properties")) {
      ObjectNode result = mapper.createObjectNode();
      schema
          .get("properties")
          .properties()
          .forEach(
              entry -> {
                log.trace("processing property {} for {}", entry.getKey(), schemaId);
                String key = entry.getKey();
                result.set(key, readEntry(mapper, entry));
              });
      return resolveInheritance(schema, result);
    }

    return readValue(schema, mapper);
  }

  /**
   * Resolve inheritance by iterating through allOf property and parse each individually and add
   * them to child node
   *
   * @param schema
   * @param child
   * @return
   */
  private JsonNode resolveInheritance(JsonNode schema, JsonNode child) {
    log.debug("Resolving inheritance from {}", child);
    if (schema.has("allOf")) {
      schema
          .get("allOf")
          .elements()
          .forEachRemaining(
              entry -> {
                // Cache the resolved schema to avoid repeated disk reads
                JsonNode parent = getRef(entry.get("$ref").asText());
                if (parent != null) {
                  mergeObjects(child, parent);
                }
              });
    }
    return child;
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
      return getRef(ref);
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
        arrayNode.add(getRef(ref));
      }

      // For non-ref items, current behavior returns empty array placeholder
      return arrayNode;
    }

    // Primitive/unsupported types are returned as NullNode to keep structure predictable
    return NullNode.getInstance();
  }

  /**
   * Read reference node
   *
   * @param ref
   * @return
   */
  private static JsonNode getRef(String ref) {
    log.trace("getting ref {}", ref);
    // Cache the resolved schema to avoid repeated disk reads
    if (!seenRefs.containsKey(ref)) {
      seenRefs.put(ref, CMcpStorageConvertor.processSchema(ref));
    }
    return seenRefs.get(ref);
  }

  private static JsonNode mergeObjects(JsonNode mainNode, JsonNode updateNode) {
    if (mainNode.isObject() && updateNode.isObject()) {
      updateNode
          .properties()
          .forEach(
              entry -> {
                String fieldName = entry.getKey();
                JsonNode valueToUpdate = entry.getValue();
                JsonNode existingValue = mainNode.get(fieldName);
                if (existingValue != null && existingValue.isObject() && valueToUpdate.isObject()) {
                  ((ObjectNode) mainNode)
                      .set(fieldName, mergeObjects(existingValue, valueToUpdate));
                } else {
                  ((ObjectNode) mainNode).set(fieldName, valueToUpdate);
                }
              });
      return mainNode;
    }
    // For non-object nodes, updateNode replaces mainNode
    return mainNode;
  }
}
