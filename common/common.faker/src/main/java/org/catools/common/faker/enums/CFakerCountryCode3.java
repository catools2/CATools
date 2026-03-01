package org.catools.common.faker.enums;

/**
 * An enumeration of 3-letter ISO 3166-1 alpha-3 country codes used by the faker library.
 *
 * <p>This enum provides standardized 3-letter country codes that can be used for generating fake
 * data related to countries in testing and data generation scenarios.
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>
 * // Get the country code as a string
 * String countryCode = CFakerCountryCode3.USA.name();
 * System.out.println(countryCode); // Outputs: "USA"
 *
 * // Use in switch statements
 * switch (CFakerCountryCode3.USA) {
 *     case USA:
 *         System.out.println("United States of America");
 *         break;
 * }
 *
 * // Iterate through all available country codes
 * for (CFakerCountryCode3 code : CFakerCountryCode3.values()) {
 *     System.out.println("Country code: " + code.name());
 * }
 *
 * // Parse from string
 * CFakerCountryCode3 country = CFakerCountryCode3.valueOf("USA");
 * </pre>
 *
 * @see <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3">ISO 3166-1 alpha-3</a>
 * @since 1.0
 */
public enum CFakerCountryCode3 {

  /**
   * United States of America - ISO 3166-1 alpha-3 country code.
   *
   * <p>This represents the United States of America using the standard 3-letter country code
   * format.
   *
   * <h4>Example Usage:</h4>
   *
   * <pre>
   * CFakerCountryCode3 usa = CFakerCountryCode3.USA;
   * String code = usa.name(); // Returns "USA"
   * </pre>
   */
  USA
}
