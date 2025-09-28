package org.catools.common.faker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a random city entity with its basic information including name and zip code.
 * This class is designed to be used in data generation and testing scenarios where
 * city information is needed.
 * 
 * <p>This class uses Lombok annotations to automatically generate boilerplate code:
 * <ul>
 *   <li>Getters and setters for all fields</li>
 *   <li>Default no-argument constructor</li>
 *   <li>All-arguments constructor</li>
 *   <li>equals(), hashCode(), and toString() methods</li>
 *   <li>Fluent setter methods that return 'this' for method chaining</li>
 * </ul>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Creating a city using constructor:</h4>
 * <pre>{@code
 * CRandomCity city = new CRandomCity("New York", "10001");
 * System.out.println(city.getName()); // Output: New York
 * System.out.println(city.getZipCode()); // Output: 10001
 * }</pre>
 * 
 * <h4>Creating a city using default constructor and setters:</h4>
 * <pre>{@code
 * CRandomCity city = new CRandomCity();
 * city.setName("Los Angeles");
 * city.setZipCode("90210");
 * }</pre>
 * 
 * <h4>Creating a city using fluent interface (method chaining):</h4>
 * <pre>{@code
 * CRandomCity city = new CRandomCity()
 *     .setName("Chicago")
 *     .setZipCode("60601");
 * }</pre>
 * 
 * <h4>Using in collections:</h4>
 * <pre>{@code
 * List<CRandomCity> cities = Arrays.asList(
 *     new CRandomCity("Miami", "33101"),
 *     new CRandomCity("Seattle", "98101"),
 *     new CRandomCity("Denver", "80201")
 * );
 * 
 * // Filter cities by zip code pattern
 * List<CRandomCity> filteredCities = cities.stream()
 *     .filter(city -> city.getZipCode().startsWith("9"))
 *     .collect(Collectors.toList());
 * }</pre>
 * 
 * @author CATools
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CRandomCity {
  
  /**
   * The name of the city.
   * 
   * <p>This field stores the city name as a string. It can contain any valid
   * city name including those with spaces, hyphens, or apostrophes.
   * 
   * <h4>Examples of valid city names:</h4>
   * <ul>
   *   <li>"New York"</li>
   *   <li>"Los Angeles"</li>
   *   <li>"Saint-Petersburg"</li>
   *   <li>"O'Fallon"</li>
   * </ul>
   * 
   * @see #getName()
   * @see #setName(String)
   */
  private String name;
  
  /**
   * The ZIP code of the city.
   * 
   * <p>This field stores the postal code associated with the city. The format
   * may vary depending on the country, but typically represents a US ZIP code format.
   * 
   * <h4>Examples of valid ZIP codes:</h4>
   * <ul>
   *   <li>"10001" - Standard 5-digit US ZIP code</li>
   *   <li>"90210" - Another 5-digit US ZIP code</li>
   *   <li>"12345-6789" - ZIP+4 format (though this implementation uses String, so any format is supported)</li>
   * </ul>
   * 
   * <p><strong>Note:</strong> While this field is typed as String to allow flexibility,
   * it's typically expected to contain numeric ZIP codes for US cities.
   * 
   * @see #getZipCode()
   * @see #setZipCode(String)
   */
  private String zipCode;
}
