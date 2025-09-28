package org.catools.common.faker.exception;

import org.catools.common.faker.enums.CFakerCountryCode3;

import java.util.Arrays;

/**
 * Exception thrown when an invalid or unsupported country code is provided to the CFaker framework.
 * This exception is typically thrown when attempting to generate fake data for a country that is not
 * recognized by the ISO 3166-1 alpha-3 standard or is not currently supported by the faker library.
 * 
 * <p>The exception message includes the invalid country code that was provided and a list of all
 * currently supported country codes for reference.</p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Example 1: Catching the exception when an invalid country code is used
 * try {
 *     CFaker faker = new CFaker("XYZ"); // Invalid country code
 * } catch (CFakerCountryNotFoundException e) {
 *     System.err.println("Error: " + e.getMessage());
 *     // Output: Country code XYZ is not a valid ISO3 country code or it is not support...
 * }
 * 
 * // Example 2: Validating country codes before using them
 * String countryCode = "ABC";
 * if (!isValidCountryCode(countryCode)) {
 *     throw new CFakerCountryNotFoundException(countryCode);
 * }
 * 
 * // Example 3: Handling the exception in a service method
 * public void generateFakeDataForCountry(String countryCode) {
 *     try {
 *         CFaker faker = new CFaker(countryCode);
 *         // Generate fake data...
 *     } catch (CFakerCountryNotFoundException e) {
 *         log.error("Unsupported country code: {}", countryCode);
 *         throw e; // Re-throw or handle appropriately
 *     }
 * }
 * }</pre>
 * 
 * @see CFakerCountryCode3
 * @see RuntimeException
 * @since 1.0
 * @author CFaker Team
 */
public class CFakerCountryNotFoundException extends RuntimeException {
  
  /**
   * Constructs a new CFakerCountryNotFoundException with a detailed message indicating
   * the invalid country code and listing all supported country codes.
   * 
   * <p>The exception message follows the format:
   * "Country code {countryCode3} is not a valid ISO3 country code or it is not support 
   * at the moment of time. Supporting Countries: [list of supported codes]"</p>
   * 
   * <h3>Usage Examples:</h3>
   * <pre>{@code
   * // Example 1: Throwing the exception for an invalid country code
   * String invalidCode = "XYZ";
   * throw new CFakerCountryNotFoundException(invalidCode);
   * 
   * // Example 2: Using in validation logic
   * if (!Arrays.asList(CFakerCountryCode3.values()).contains(countryCode)) {
   *     throw new CFakerCountryNotFoundException(countryCode);
   * }
   * 
   * // Example 3: Creating the exception with a variable
   * String userProvidedCode = getUserInput();
   * CFakerCountryNotFoundException exception = 
   *     new CFakerCountryNotFoundException(userProvidedCode);
   * }</pre>
   * 
   * @param countryCode3 the invalid ISO 3166-1 alpha-3 country code that was provided.
   *                    This should be a 3-character string representing the country code
   *                    that failed validation. Must not be null.
   * 
   * @throws NullPointerException if countryCode3 is null (inherited from super constructor)
   * 
   * @see CFakerCountryCode3#values()
   */
  public CFakerCountryNotFoundException(String countryCode3) {
    super(
        "Country code "
            + countryCode3
            + " is not a valid ISO3 country code or it is not support at the moment of time. Supporting Countries: "
            + Arrays.toString(CFakerCountryCode3.values()));
  }
}
