package org.catools.common.faker.provider;

import java.util.List;
import lombok.AllArgsConstructor;
import org.catools.common.faker.CRandom;
import org.catools.common.faker.model.CRandomName;
import org.catools.common.utils.CIterableUtil;

/**
 * A provider class for generating random names with gender-specific options. This class provides
 * functionality to generate random names including first names, middle names, last names, prefixes,
 * and suffixes for both male and female genders.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Create lists of names
 * List<String> maleFirstNames = Arrays.asList("John", "Michael", "David");
 * List<String> femaleFirstNames = Arrays.asList("Jane", "Mary", "Sarah");
 * List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams");
 * // ... other name lists
 *
 * // Create the provider
 * CFakerNameProvider nameProvider = new CFakerNameProvider(
 *     maleFirstNames, femaleFirstNames, lastNames, lastNames,
 *     middleNames, middleNames, prefixes, prefixes, suffixes, suffixes
 * );
 *
 * // Generate random names
 * CRandomName anyName = nameProvider.getAny();
 * CRandomName maleName = nameProvider.getAnyMale();
 * CRandomName femaleName = nameProvider.getAnyFemale();
 * }</pre>
 */
@AllArgsConstructor
public class CFakerNameProvider {
  private final List<String> maleFirstNames;
  private final List<String> femaleFirstNames;
  private final List<String> maleLastNames;
  private final List<String> femaleLastNames;
  private final List<String> maleMiddleNames;
  private final List<String> femaleMiddleNames;
  private final List<String> malePrefixes;
  private final List<String> femalePrefixes;
  private final List<String> maleSuffixes;
  private final List<String> femaleSuffixes;

  /**
   * Generates a random name of any gender (male or female). Uses a random selection mechanism where
   * approximately 2/3 of the time it returns a female name and 1/3 of the time it returns a male
   * name.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * CRandomName randomName = nameProvider.getAny();
   * System.out.println("Full name: " + randomName.getFullName());
   * System.out.println("First name: " + randomName.getFirstName());
   * }</pre>
   *
   * @return a {@link CRandomName} object containing randomly selected name components
   */
  public CRandomName getAny() {
    if (CRandom.Int.next() % 3 == 0) {
      return getAnyMale();
    }
    return getAnyFemale();
  }

  /**
   * Generates a random male name using male-specific name components. Selects random values from
   * the male first names, middle names, last names, prefixes, and suffixes collections.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * CRandomName maleName = nameProvider.getAnyMale();
   * System.out.println("Male name: " + maleName.getFullName());
   * // Output example: "Mr. John Michael Smith Jr."
   * }</pre>
   *
   * @return a {@link CRandomName} object with male name components
   */
  public CRandomName getAnyMale() {
    return new CRandomName(
        CIterableUtil.getRandom(maleFirstNames),
        CIterableUtil.getRandom(maleMiddleNames),
        CIterableUtil.getRandom(maleLastNames),
        CIterableUtil.getRandom(malePrefixes),
        CIterableUtil.getRandom(maleSuffixes));
  }

  /**
   * Generates a random female name using female-specific name components. Selects random values
   * from the female first names, middle names, last names, prefixes, and suffixes collections.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * CRandomName femaleName = nameProvider.getAnyFemale();
   * System.out.println("Female name: " + femaleName.getFullName());
   * // Output example: "Ms. Sarah Elizabeth Johnson"
   * }</pre>
   *
   * @return a {@link CRandomName} object with female name components
   */
  public CRandomName getAnyFemale() {
    return new CRandomName(
        CIterableUtil.getRandom(femaleFirstNames),
        CIterableUtil.getRandom(femaleMiddleNames),
        CIterableUtil.getRandom(femaleLastNames),
        CIterableUtil.getRandom(femalePrefixes),
        CIterableUtil.getRandom(femaleSuffixes));
  }
}
