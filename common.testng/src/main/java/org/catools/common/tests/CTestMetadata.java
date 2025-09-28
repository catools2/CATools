package org.catools.common.tests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;

/**
 * A specialized collection for managing test metadata as key-value pairs.
 * <p>
 * CTestMetadata extends CList to provide convenient methods for handling
 * test metadata entries consisting of name-value pairs. This class is
 * particularly useful for storing and querying test-related information
 * such as test tags, categories, properties, or any other metadata
 * associated with test execution.
 * </p>
 * 
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Creating and adding metadata
 * CTestMetadata metadata = new CTestMetadata();
 * metadata.add("category", "unit");
 * metadata.add("priority", "high");
 * metadata.add("author", "john.doe");
 * 
 * // Checking for specific metadata
 * boolean hasHighPriority = metadata.has("priority", "high"); // true
 * boolean hasLowPriority = metadata.has("priority", "low");   // false
 * 
 * // Adding metadata conditionally
 * metadata.addIfNotExists("category", "unit");        // won't add (already exists)
 * metadata.addIfNotExists("environment", "staging");  // will add (new entry)
 * 
 * // Using inherited CList methods
 * int size = metadata.size();  // number of metadata entries
 * metadata.clear();           // remove all entries
 * }</pre>
 * 
 * @see CList
 * @see MetaData
 */
public class CTestMetadata extends CList<CTestMetadata.MetaData> {

  /**
   * Adds a new metadata entry with the specified name and value.
   * <p>
   * This method creates a new MetaData instance and adds it to the collection.
   * Duplicate entries (same name and value) are allowed.
   * </p>
   * 
   * <p><b>Examples:</b></p>
   * <pre>{@code
   * CTestMetadata metadata = new CTestMetadata();
   * metadata.add("category", "integration");
   * metadata.add("priority", "medium");
   * metadata.add("tags", "smoke");
   * metadata.add("tags", "regression");  // duplicate names allowed
   * }</pre>
   * 
   * @param name  the name/key of the metadata entry (can be null)
   * @param value the value of the metadata entry (can be null)
   */
  public void add(String name, String value) {
    add(new MetaData(name, value));
  }

  /**
   * Checks if the collection contains a metadata entry with the specified name and value.
   * <p>
   * This method uses exact string matching (case-sensitive) to find the metadata entry.
   * Both name and value must match exactly for the method to return true.
   * </p>
   * 
   * <p><b>Examples:</b></p>
   * <pre>{@code
   * CTestMetadata metadata = new CTestMetadata();
   * metadata.add("category", "unit");
   * metadata.add("priority", "high");
   * 
   * boolean exists = metadata.has("category", "unit");    // true
   * boolean exists2 = metadata.has("category", "Unit");   // false (case-sensitive)
   * boolean exists3 = metadata.has("category", "integration"); // false
   * boolean exists4 = metadata.has("nonexistent", "value");    // false
   * }</pre>
   * 
   * @param name  the name/key to search for
   * @param value the value to search for
   * @return true if a metadata entry with the exact name and value exists, false otherwise
   */
  public boolean has(String name, String value) {
    return hasNot(m -> StringUtils.equals(m.name, name) && StringUtils.equals(m.value, value));
  }

  /**
   * Checks if the collection does NOT contain a metadata entry with the specified name and value.
   * <p>
   * This method is the logical inverse of {@link #has(String, String)}. It uses exact string 
   * matching (case-sensitive) to determine if the metadata entry is absent.
   * </p>
   * 
   * <p><b>Examples:</b></p>
   * <pre>{@code
   * CTestMetadata metadata = new CTestMetadata();
   * metadata.add("category", "unit");
   * metadata.add("priority", "high");
   * 
   * boolean notExists = metadata.hasNot("category", "integration"); // true
   * boolean notExists2 = metadata.hasNot("category", "unit");       // false
   * boolean notExists3 = metadata.hasNot("author", "john.doe");     // true
   * }</pre>
   * 
   * @param name  the name/key to search for
   * @param value the value to search for
   * @return true if no metadata entry with the exact name and value exists, false otherwise
   */
  public boolean hasNot(String name, String value) {
    return hasNot(m -> StringUtils.equals(m.name, name) && StringUtils.equals(m.value, value));
  }

  /**
   * Adds a metadata entry with the specified name and value only if it doesn't already exist.
   * <p>
   * This method prevents duplicate metadata entries by checking if the exact name-value
   * pair already exists in the collection before adding it. If the entry already exists,
   * no action is taken.
   * </p>
   * 
   * <p><b>Examples:</b></p>
   * <pre>{@code
   * CTestMetadata metadata = new CTestMetadata();
   * metadata.add("category", "unit");
   * 
   * // This will not add a duplicate entry
   * metadata.addIfNotExists("category", "unit");      // no change, already exists
   * 
   * // This will add a new entry
   * metadata.addIfNotExists("category", "integration"); // adds new entry
   * metadata.addIfNotExists("priority", "high");        // adds new entry
   * 
   * System.out.println(metadata.size()); // prints 3
   * }</pre>
   * 
   * @param name  the name/key of the metadata entry to add
   * @param value the value of the metadata entry to add
   */
  public void addIfNotExists(String name, String value) {
    if (hasNot(name, value)) {
      add(name, value);
    }
  }

  /**
   * Represents a single metadata entry consisting of a name-value pair.
   * <p>
   * This inner class encapsulates a metadata entry with a name (key) and value.
   * Both fields can be null. The class uses Lombok annotations for automatic
   * generation of getters, setters, equals, hashCode, and toString methods.
   * </p>
   * 
   * <p><b>Examples:</b></p>
   * <pre>{@code
   * // Creating metadata entries directly (though typically done via CTestMetadata.add())
   * MetaData entry1 = new MetaData("category", "unit");
   * MetaData entry2 = new MetaData("priority", "high");
   * MetaData entry3 = new MetaData("author", null);  // null values allowed
   * 
   * // Accessing data
   * String name = entry1.getName();   // "category"
   * String value = entry1.getValue(); // "unit"
   * 
   * // Modifying data (via Lombok setters)
   * entry1.setValue("integration");
   * }</pre>
   * 
   * @see lombok.Data
   * @see lombok.AllArgsConstructor
   */
  @Data
  @AllArgsConstructor
  public static class MetaData {
    private String name;
    private String value;
  }
}
