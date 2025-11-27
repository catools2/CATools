package org.catools.common.faker.etl;

import com.mifmif.common.regex.Generex;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.faker.exception.CFakerCountryNotFoundException;
import org.catools.common.faker.model.CRandomCities;
import org.catools.common.faker.model.CRandomCity;
import org.catools.common.faker.model.CRandomCountry;
import org.catools.common.faker.model.CRandomState;
import org.catools.common.faker.provider.*;
import org.catools.common.utils.CResourceUtil;
import org.catools.common.utils.CStringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Resource manager for loading and processing data from Ruby Faker resources.
 * This class provides functionality to load country-specific data including names, addresses, 
 * companies, states, and cities from resource files.
 * 
 * <p>The resource files are organized in a hierarchical structure with country-specific directories
 * containing various data files such as names, addresses, states, and cities. The class supports
 * special markers for including other resources and generating data from regular expressions.</p>
 * 
 * <p>Resource file markers supported:</p>
 * <ul>
 *   <li>&lt;&lt; - Include another resource file</li>
 *   <li>@Reg= - Generate data from regular expression</li>
 * </ul>
 * 
 * @see CFakerCountryProvider
 * @see CFakerCountryNotFoundException
 * @author CA Tools Development Team
 * @since 1.0
 */
public class CFakerResourceManager {
  
  /**
   * Retrieves a comprehensive country provider containing all data for the specified country.
   * This method loads country information including basic country data, states/provinces, 
   * cities, name providers, company providers, and address providers.
   * 
   * <p>The method reads from the country_info.txt resource file and matches the country
   * by its 3-letter ISO code. Once found, it constructs a complete CFakerCountryProvider
   * with all associated data providers.</p>
   * 
   * <p>Country data includes:</p>
   * <ul>
   *   <li>ISO codes (2-letter and 3-letter)</li>
   *   <li>Country name</li>
   *   <li>Currency information (code and name)</li>
   *   <li>Phone number format</li>
   *   <li>Postal code format and regex pattern</li>
   *   <li>States/provinces with cities</li>
   *   <li>Name providers (first, middle, last names with prefixes/suffixes)</li>
   *   <li>Company providers (names, prefixes, suffixes)</li>
   *   <li>Address providers (street names, numbers, prefixes, suffixes)</li>
   * </ul>
   * 
   * @param countryCode3 the 3-letter ISO country code (case-insensitive).
   *                     Examples: "USA", "CAN", "GBR", "DEU", "FRA"
   * @return a CFakerCountryProvider containing all data for the specified country
   * @throws CFakerCountryNotFoundException if the country code is not found in the resource files
   * @throws IllegalArgumentException if countryCode3 is null or empty
   * 
   * @example
   * <pre>{@code
   * // Get provider for United States
   * CFakerCountryProvider usaProvider = CFakerResourceManager.getCountry("USA");
   * 
   * // Access country information
   * CRandomCountry country = usaProvider.getCountry();
   * System.out.println(country.getName()); // "United States"
   * System.out.println(country.getCurrencyCode()); // "USD"
   * 
   * // Access state providers
   * CFakerStateProviders stateProviders = usaProvider.getStateProviders();
   * CFakerStateProvider californiaProvider = stateProviders.getByCode("CA");
   * 
   * // Access name provider
   * CFakerNameProvider nameProvider = usaProvider.getNameProvider();
   * String firstName = nameProvider.getRandomMaleFirstName();
   * String lastName = nameProvider.getRandomMaleSurname();
   * 
   * // Access company provider
   * CFakerCompanyProvider companyProvider = usaProvider.getCompanyProvider();
   * String companyName = companyProvider.getRandomCompanyName();
   * 
   * // Access address provider
   * CFakerStreetAddressProvider addressProvider = usaProvider.getAddressProvider();
   * String streetName = addressProvider.getRandomStreetName();
   * }</pre>
   * 
   * @example
   * <pre>{@code
   * // Handle country not found
   * try {
   *     CFakerCountryProvider provider = CFakerResourceManager.getCountry("XYZ");
   * } catch (CFakerCountryNotFoundException e) {
   *     System.err.println("Country not found: " + e.getCountryCode());
   * }
   * 
   * // Get provider for Canada
   * CFakerCountryProvider canadaProvider = CFakerResourceManager.getCountry("can"); // case-insensitive
   * }</pre>
   */
  public static synchronized CFakerCountryProvider getCountry(String countryCode3) {
    List<String> lines = readResource("data/country_info.txt");
    // remove header
    lines.remove(0);

    for (String line : lines) {
      String[] vals = StringUtils.split(line, "\t");
      if (countryCode3.equalsIgnoreCase(vals[1])) {
        return new CFakerCountryProvider(
            new CRandomCountry(
                vals[1], // ISO
                vals[0], // ISO3
                vals[2], // Country
                vals.length > 3 ? vals[3] : CStringUtil.EMPTY, // CurrencyCode
                vals.length > 4 ? vals[4] : CStringUtil.EMPTY, // CurrencyName
                vals.length > 5 ? vals[5] : CStringUtil.EMPTY, // Phone
                vals.length > 6 ? vals[6] : CStringUtil.EMPTY, // Postal Code Format
                vals.length > 7 ? vals[7] : CStringUtil.EMPTY), // Postal Code Regex
            getStateProviders(vals[0]),
            getNameProvider(vals[0]),
            getCompanyProvider(vals[0]),
            getAddressProvider(vals[0]));
      }
    }
    throw new CFakerCountryNotFoundException(countryCode3);
  }

  private static CFakerStateProviders getStateProviders(String countryCode) {
    Map<String, CRandomCities> stateCitiesMap = getStateCitiesMap(countryCode);
    List<String> lines = readResource("states.txt", countryCode);

    // remove header
    lines.remove(0);

    return new CFakerStateProviders(
        lines.stream()
            .map(
                s -> {
                  String[] vals = s.split("\t");
                  return new CFakerStateProvider(
                      new CRandomState(vals[1], vals[0]), stateCitiesMap.get(vals[0]));
                })
            .collect(Collectors.toSet()));
  }

  private static Map<String, CRandomCities> getStateCitiesMap(String countryCode) {
    List<String[]> lines =
        readResource("cities.txt", countryCode).stream()
            .map(l -> l.split("\t"))
            .filter(v -> v.length == 3)
            .collect(Collectors.toList());

    // remove header
    lines.remove(0);

    Map<String, CRandomCities> output = new HashMap<>();

    lines.forEach(
        vals -> {
          String stateCode = vals[2];
          output.putIfAbsent(stateCode, new CRandomCities());
          output.get(stateCode).add(new CRandomCity(vals[1], vals[0]));
        });

    return output;
  }

  private static CFakerNameProvider getNameProvider(String countryCode) {
    return new CFakerNameProvider(
        readResource("male_name.txt", countryCode),
        readResource("female_name.txt", countryCode),
        readResource("male_middle_name.txt", countryCode),
        readResource("female_middle_name.txt", countryCode),
        readResource("male_surname.txt", countryCode),
        readResource("female_surname.txt", countryCode),
        readResource("male_prefix.txt", countryCode),
        readResource("female_prefix.txt", countryCode),
        readResource("male_suffix.txt", countryCode),
        readResource("female_suffix.txt", countryCode));
  }

  private static CFakerStreetAddressProvider getAddressProvider(String countryCode) {
    return new CFakerStreetAddressProvider(
        readResource("street_name.txt", countryCode),
        readResource("street_suffix.txt", countryCode),
        readResource("street_prefix.txt", countryCode),
        readResource("street_number_regex.txt", countryCode),
        readResource("building_number_regex.txt", countryCode));
  }

  private static CFakerCompanyProvider getCompanyProvider(String countryCode) {
    return new CFakerCompanyProvider(
        readResource("company_name.txt", countryCode),
        readResource("company_prefix.txt", countryCode),
        readResource("company_suffix.txt", countryCode));
  }

  private static List<String> readResource(String resourceName, String countryCode) {
    return readResource(String.format("data/%s/" + resourceName, countryCode.toLowerCase()));
  }

  private static List<String> readResource(String resourceFullName) {
    String RESOURCE_MARKER = "<<";
    String REGEX_MARKER = "@Reg=";
    List<String> output = new ArrayList<>();
    for (String line :
        CResourceUtil.readLines(resourceFullName.trim(), CFakerResourceManager.class).stream()
            .filter(CStringUtil::isNotBlank)
            .toList()) {
      if (line.startsWith(RESOURCE_MARKER)) {
        output.addAll(readResource(CStringUtil.substringAfter(line, RESOURCE_MARKER)));
      } else if (line.startsWith(REGEX_MARKER)) {
        new Generex(CStringUtil.substringAfter(line, REGEX_MARKER).trim())
            .getAllMatchedStrings()
            .forEach(s -> output.add(s.trim()));
      } else {
        output.add(line.trim());
      }
    }
    return output;
  }
}
