package org.catools.common.faker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a complete address with all its components for faker data generation. This model
 * provides a comprehensive structure for generating realistic addresses including hierarchical
 * location data (country, state, city) and detailed street information.
 *
 * <p>The class uses Lombok annotations to automatically generate getters, setters, constructors,
 * equals, hashCode, and toString methods. The {@code @Accessors(chain = true)} annotation enables
 * method chaining for fluent API usage.
 *
 * <p>Example usage:
 *
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
   * The country information for this address. Contains comprehensive country data including codes,
   * currency, and postal patterns.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>United States with country code "US"
   *   <li>Canada with country code "CA"
   *   <li>United Kingdom with country code "GB"
   *   <li>Germany with country code "DE"
   * </ul>
   *
   * @see CRandomCountry
   */
  private CRandomCountry country;

  /**
   * The state or province information for this address. Contains the state name and abbreviation
   * code.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>California (CA)
   *   <li>New York (NY)
   *   <li>Ontario (ON) for Canada
   *   <li>Texas (TX)
   * </ul>
   *
   * @see CRandomState
   */
  private CRandomState state;

  /**
   * The city information for this address. Contains the city name and associated ZIP/postal code.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>San Francisco with ZIP code "94102"
   *   <li>New York with ZIP code "10001"
   *   <li>Toronto with postal code "M5V 3A8"
   *   <li>Los Angeles with ZIP code "90210"
   * </ul>
   *
   * @see CRandomCity
   */
  private CRandomCity city;

  /**
   * The primary name of the street without directional prefixes or suffixes. This is the core
   * identifier of the street.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"Main" (as in "123 North Main Street")
   *   <li>"Oak" (as in "456 Oak Avenue")
   *   <li>"Broadway" (as in "789 Broadway")
   *   <li>"First" (as in "321 East First Street")
   *   <li>"Washington" (as in "654 Washington Boulevard")
   * </ul>
   */
  private String streetName;

  /**
   * The street type or suffix that follows the street name. Indicates the type of thoroughfare.
   *
   * <p>Common examples:
   *
   * <ul>
   *   <li>"Street" or "St"
   *   <li>"Avenue" or "Ave"
   *   <li>"Boulevard" or "Blvd"
   *   <li>"Drive" or "Dr"
   *   <li>"Lane" or "Ln"
   *   <li>"Road" or "Rd"
   *   <li>"Circle" or "Cir"
   *   <li>"Court" or "Ct"
   * </ul>
   */
  private String streetSuffix;

  /**
   * The directional prefix that comes before the street name. Indicates the directional orientation
   * of the street.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"North" or "N"
   *   <li>"South" or "S"
   *   <li>"East" or "E"
   *   <li>"West" or "W"
   *   <li>"Northeast" or "NE"
   *   <li>"Southwest" or "SW"
   *   <li>"Northwest" or "NW"
   *   <li>"Southeast" or "SE"
   * </ul>
   */
  private String streetPrefix;

  /**
   * The numeric identifier of the building or property on the street. This is the primary street
   * address number.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"123" (for 123 Main Street)
   *   <li>"4567" (for 4567 Oak Avenue)
   *   <li>"1" (for 1 Broadway)
   *   <li>"999" (for 999 First Street)
   *   <li>"42" (for 42 Washington Boulevard)
   * </ul>
   */
  private String streetNumber;

  /**
   * Additional building identifier such as apartment, suite, or unit number. Used for multi-unit
   * buildings or complex addressing.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"Apt 2B" (apartment number)
   *   <li>"Suite 100" (office suite)
   *   <li>"Unit 5" (unit in a complex)
   *   <li>"Floor 3" (floor designation)
   *   <li>"Building A" (building identifier)
   *   <li>"#201" (unit number with hash)
   * </ul>
   */
  private String buildingNumber;
}
