package org.catools.common.faker.provider;

import com.mifmif.common.regex.Generex;
import lombok.AllArgsConstructor;
import org.catools.common.faker.model.CRandomStreetInfo;
import org.catools.common.utils.CIterableUtil;

import java.util.List;

/**
 * Provider for generating random street address information.
 * 
 * <p>This class generates realistic street addresses by combining random elements
 * from predefined lists of street names, suffixes, prefixes, and number patterns.
 * It uses regex patterns to generate varied street and building numbers.</p>
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * List<String> streetNames = Arrays.asList("Main", "Oak", "Elm", "First");
 * List<String> streetSuffixes = Arrays.asList("St", "Ave", "Blvd", "Dr");
 * List<String> streetPrefixes = Arrays.asList("N", "S", "E", "W");
 * List<String> streetNumberPatterns = Arrays.asList("\\d{1,4}", "\\d{2,5}");
 * List<String> buildingNumberPatterns = Arrays.asList("\\d{1,3}[A-Z]?", "\\d{2,4}");
 * 
 * CFakerStreetAddressProvider provider = new CFakerStreetAddressProvider(
 *     streetNames, streetSuffixes, streetPrefixes, 
 *     streetNumberPatterns, buildingNumberPatterns);
 * 
 * CRandomStreetInfo streetInfo = provider.getAny();
 * // Returns something like: "123 N Main St, Apt 4B"
 * }</pre>
 * 
 * @see CRandomStreetInfo
 * @see CIterableUtil
 * @since 1.0
 */
@AllArgsConstructor
public class CFakerStreetAddressProvider {
  private final List<String> streetNames;
  private final List<String> streetSuffixes;
  private final List<String> streetPrefixes;
  private final List<String> streetNumberPatterns;
  private final List<String> buildingNumberPatterns;

  /**
   * Generates a random street address information object.
   * 
   * <p>This method creates a complete street address by randomly selecting elements
   * from the configured lists and generating numbers based on the provided patterns.
   * Each call returns a new, randomly generated address.</p>
   * 
   * <p><strong>Generated components:</strong></p>
   * <ul>
   *   <li><strong>Street Name:</strong> Randomly selected from the streetNames list</li>
   *   <li><strong>Street Suffix:</strong> Randomly selected from the streetSuffixes list (e.g., "St", "Ave", "Blvd")</li>
   *   <li><strong>Street Prefix:</strong> Randomly selected from the streetPrefixes list (e.g., "N", "S", "E", "W")</li>
   *   <li><strong>Street Number:</strong> Generated using a random pattern from streetNumberPatterns</li>
   *   <li><strong>Building Number:</strong> Generated using a random pattern from buildingNumberPatterns</li>
   * </ul>
   * 
   * <p><strong>Example outputs:</strong></p>
   * <pre>{@code
   * // Example 1:
   * CRandomStreetInfo info1 = provider.getAny();
   * // streetName: "Main", streetSuffix: "St", streetPrefix: "N"
   * // streetNumber: "123", buildingNumber: "4A"
   * 
   * // Example 2:
   * CRandomStreetInfo info2 = provider.getAny();
   * // streetName: "Oak", streetSuffix: "Ave", streetPrefix: "S"
   * // streetNumber: "456", buildingNumber: "12"
   * }</pre>
   * 
   * @return a {@link CRandomStreetInfo} object containing randomly generated street address components
   * @throws IllegalStateException if any of the configured lists are empty
   * @see CRandomStreetInfo
   */
  public CRandomStreetInfo getAny() {
    return new CRandomStreetInfo(
        CIterableUtil.getRandom(streetNames),
        CIterableUtil.getRandom(streetSuffixes),
        CIterableUtil.getRandom(streetPrefixes),
        new Generex(CIterableUtil.getRandom(streetNumberPatterns)).random(),
        new Generex(CIterableUtil.getRandom(buildingNumberPatterns)).random());
  }
}
