package org.catools.common.faker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a complete address with all its components for faker data generation.
 * This model provides a comprehensive structure for generating realistic addresses
 * including hierarchical location data (country, state, city) and detailed street information.
 * 
 * <p>The class uses Lombok annotations to automatically generate getters, setters, constructors,
 * equals, hashCode, and toString methods. The {@code @Accessors(chain = true)} annotation enables
 * method chaining for fluent API usage.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create an address using method chaining
 * CRandomAddress address = new CRandomAddress()
 *     .setCountry(new CRandomCountry().setName("United States").setCountryCode2("US"))
 *     .setState(new CRandomState().setName("California").setCode("CA"))
 *     .setCity(new CRandomCity().setName("San Francisco").setZipCode("94102"))
 *     .setStreetNumber("123")
 *     .setStreetName("Market")
 *     .setStreetSuffix("Street")
 *     .setStreetPrefix("North")
 *     .setBuildingNumber("Suite 456");
 * 
 * // Create using the all-args constructor
 * CRandomAddress address2 = new CRandomAddress(
 *     new CRandomCountry("CA", "CAN", "Canada", "CAD", "Canadian Dollar", "+1", "?#? #?#", "[A-Z]\\d[A-Z] \\d[A-Z]\\d"),
 *     new CRandomState("Ontario", "ON"),
 *     new CRandomCity("Toronto", "M5V 3A8"),
 *     "King", "Street", "West", "100", "Unit 200"
 * );
 * 
 * // Access components
 * String fullStreet = address.getStreetNumber() + " " + 
 *                    address.getStreetPrefix() + " " + 
 *                    address.getStreetName() + " " + 
 *                    address.getStreetSuffix(); // "123 North Market Street"
 * 
 * String cityStateZip = address.getCity().getName() + ", " + 
 *                      address.getState().getCode() + " " + 
 *                      address.getCity().getZipCode(); // "San Francisco, CA 94102"
 * }</pre>
 *
 * @author CATools
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CRandomAddress {
  
  /**
   * The country information for this address.
   * Contains comprehensive country data including codes, currency, and postal patterns.
   * 
   * <p>Examples:</p>
   * <ul>
   *   <li>United States with country code "US"</li>
   *   <li>Canada with country code "CA"</li>
   *   <li>United Kingdom with country code "GB"</li>
   *   <li>Germany with country code "DE"</li>
   * </ul>
   * 
   * @see CRandomCountry
   */
  private CRandomCountry country;
  
  /**
   * The state or province information for this address.
   * Contains the state name and abbreviation code.
   * 
   * <p>Examples:</p>
   * <ul>
   *   <li>California (CA)</li>
   *   <li>New York (NY)</li>
   *   <li>Ontario (ON) for Canada</li>
   *   <li>Texas (TX)</li>
   * </ul>
   * 
   * @see CRandomState
   */
  private CRandomState state;
  
  /**
   * The city information for this address.
   * Contains the city name and associated ZIP/postal code.
   * 
   * <p>Examples:</p>
   * <ul>
   *   <li>San Francisco with ZIP code "94102"</li>
   *   <li>New York with ZIP code "10001"</li>
   *   <li>Toronto with postal code "M5V 3A8"</li>
   *   <li>Los Angeles with ZIP code "90210"</li>
   * </ul>
   * 
   * @see CRandomCity
   */
  private CRandomCity city;

  /**
   * The primary name of the street without directional prefixes or suffixes.
   * This is the core identifier of the street.
   * 
   * <p>Examples:</p>
   * <ul>
   *   <li>"Main" (as in "123 North Main Street")</li>
   *   <li>"Oak" (as in "456 Oak Avenue")</li>
   *   <li>"Broadway" (as in "789 Broadway")</li>
   *   <li>"First" (as in "321 East First Street")</li>
   *   <li>"Washington" (as in "654 Washington Boulevard")</li>
   * </ul>
   */
  private String streetName;
  
  /**
   * The street type or suffix that follows the street name.
   * Indicates the type of thoroughfare.
   * 
   * <p>Common examples:</p>
   * <ul>
   *   <li>"Street" or "St"</li>
   *   <li>"Avenue" or "Ave"</li>
   *   <li>"Boulevard" or "Blvd"</li>
   *   <li>"Drive" or "Dr"</li>
   *   <li>"Lane" or "Ln"</li>
   *   <li>"Road" or "Rd"</li>
   *   <li>"Circle" or "Cir"</li>
   *   <li>"Court" or "Ct"</li>
   * </ul>
   */
  private String streetSuffix;
  
  /**
   * The directional prefix that comes before the street name.
   * Indicates the directional orientation of the street.
   * 
   * <p>Examples:</p>
   * <ul>
   *   <li>"North" or "N"</li>
   *   <li>"South" or "S"</li>
   *   <li>"East" or "E"</li>
   *   <li>"West" or "W"</li>
   *   <li>"Northeast" or "NE"</li>
   *   <li>"Southwest" or "SW"</li>
   *   <li>"Northwest" or "NW"</li>
   *   <li>"Southeast" or "SE"</li>
   * </ul>
   */
  private String streetPrefix;

  /**
   * The numeric identifier of the building or property on the street.
   * This is the primary street address number.
   * 
   * <p>Examples:</p>
   * <ul>
   *   <li>"123" (for 123 Main Street)</li>
   *   <li>"4567" (for 4567 Oak Avenue)</li>
   *   <li>"1" (for 1 Broadway)</li>
   *   <li>"999" (for 999 First Street)</li>
   *   <li>"42" (for 42 Washington Boulevard)</li>
   * </ul>
   */
  private String streetNumber;
  
  /**
   * Additional building identifier such as apartment, suite, or unit number.
   * Used for multi-unit buildings or complex addressing.
   * 
   * <p>Examples:</p>
   * <ul>
   *   <li>"Apt 2B" (apartment number)</li>
   *   <li>"Suite 100" (office suite)</li>
   *   <li>"Unit 5" (unit in a complex)</li>
   *   <li>"Floor 3" (floor designation)</li>
   *   <li>"Building A" (building identifier)</li>
   *   <li>"#201" (unit number with hash)</li>
   * </ul>
   */
  private String buildingNumber;
}
