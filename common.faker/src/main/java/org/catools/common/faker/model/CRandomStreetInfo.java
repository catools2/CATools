package org.catools.common.faker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A data model class representing random street address information for faker/test data generation.
 * This class provides a structured way to store various components of a street address.
 * 
 * <p>The class uses Lombok annotations to automatically generate:
 * <ul>
 *   <li>Getters and setters for all fields</li>
 *   <li>equals() and hashCode() methods</li>
 *   <li>toString() method</li>
 *   <li>No-args and all-args constructors</li>
 *   <li>Fluent setter methods (method chaining support)</li>
 * </ul>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Create using no-args constructor and fluent setters
 * CRandomStreetInfo streetInfo = new CRandomStreetInfo()
 *     .setStreetName("Main Street")
 *     .setStreetNumber("123")
 *     .setStreetSuffix("St")
 *     .setStreetPrefix("N")
 *     .setBuildingNumber("45A");
 * 
 * // Create using all-args constructor
 * CRandomStreetInfo streetInfo2 = new CRandomStreetInfo(
 *     "Oak Avenue", "456", "Ave", "S", "12B"
 * );
 * 
 * // Access field values
 * String fullAddress = streetInfo.getStreetPrefix() + " " + 
 *                     streetInfo.getStreetNumber() + " " +
 *                     streetInfo.getStreetName() + " " +
 *                     streetInfo.getStreetSuffix();
 * // Result: "N 123 Main Street St"
 * }</pre>
 * 
 * @since 1.0
 * @author CATools Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CRandomStreetInfo {
  
  /**
   * The name of the street (e.g., "Main Street", "Oak Avenue", "First Street").
   * This is typically the primary identifier of the street without directional prefixes or suffixes.
   * 
   * <p>Examples:
   * <ul>
   *   <li>"Main Street"</li>
   *   <li>"Oak Avenue"</li>
   *   <li>"Broadway"</li>
   *   <li>"First Street"</li>
   * </ul>
   */
  private String streetName;
  
  /**
   * The street number or house number portion of the address.
   * This represents the numeric identifier for a specific location on the street.
   * 
   * <p>Examples:
   * <ul>
   *   <li>"123"</li>
   *   <li>"4567"</li>
   *   <li>"1"</li>
   *   <li>"999"</li>
   * </ul>
   */
  private String streetNumber;
  
  /**
   * The street suffix that indicates the type of street.
   * Common suffixes include abbreviations for street types.
   * 
   * <p>Examples:
   * <ul>
   *   <li>"St" (Street)</li>
   *   <li>"Ave" (Avenue)</li>
   *   <li>"Blvd" (Boulevard)</li>
   *   <li>"Rd" (Road)</li>
   *   <li>"Dr" (Drive)</li>
   *   <li>"Ln" (Lane)</li>
   * </ul>
   */
  private String streetSuffix;
  
  /**
   * The directional prefix that appears before the street name.
   * This indicates the directional orientation or quadrant of the address.
   * 
   * <p>Examples:
   * <ul>
   *   <li>"N" (North)</li>
   *   <li>"S" (South)</li>
   *   <li>"E" (East)</li>
   *   <li>"W" (West)</li>
   *   <li>"NE" (Northeast)</li>
   *   <li>"SW" (Southwest)</li>
   * </ul>
   */
  private String streetPrefix;
  
  /**
   * The building number or unit identifier within a larger structure.
   * This is used for apartments, suites, or other sub-divisions of a main address.
   * 
   * <p>Examples:
   * <ul>
   *   <li>"Apt 2B"</li>
   *   <li>"Suite 100"</li>
   *   <li>"Unit 5"</li>
   *   <li>"12A"</li>
   *   <li>"Floor 3"</li>
   * </ul>
   */
  private String buildingNumber;
}
