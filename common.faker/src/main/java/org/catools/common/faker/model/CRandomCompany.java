package org.catools.common.faker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a randomly generated company with various naming components.
 * This class is used for generating fake company data for testing purposes.
 * 
 * <p>The class uses Lombok annotations to automatically generate boilerplate code:
 * <ul>
 *   <li>{@code @Data} - generates getters, setters, toString, equals, and hashCode methods</li>
 *   <li>{@code @NoArgsConstructor} - generates a no-argument constructor</li>
 *   <li>{@code @AllArgsConstructor} - generates a constructor with all fields as parameters</li>
 *   <li>{@code @Accessors(chain = true)} - enables method chaining for setters</li>
 * </ul>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Creating a company using the no-args constructor and setters
 * CRandomCompany company1 = new CRandomCompany()
 *     .setName("TechCorp")
 *     .setSuffix("Inc")
 *     .setPrefix("Global");
 * 
 * // Creating a company using the all-args constructor
 * CRandomCompany company2 = new CRandomCompany("DataSystems", "LLC", "Advanced");
 * 
 * // Method chaining example
 * CRandomCompany company3 = new CRandomCompany()
 *     .setPrefix("Mega")
 *     .setName("Solutions")
 *     .setSuffix("Corp");
 * 
 * // Accessing company information
 * String fullCompanyName = company1.getPrefix() + " " + company1.getName() + " " + company1.getSuffix();
 * // Results in: "Global TechCorp Inc"
 * }</pre>
 * 
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CRandomCompany {
  
  /**
   * The main name of the company.
   * This is typically the core business identifier.
   * 
   * <p>Examples: "TechCorp", "DataSystems", "Solutions"
   * 
   * @return the company name
   */
  private String name;
  
  /**
   * The suffix that appears after the company name.
   * This usually indicates the legal structure or business type.
   * 
   * <p>Common examples: "Inc", "LLC", "Corp", "Ltd", "Co"
   * 
   * @return the company suffix
   */
  private String suffix;
  
  /**
   * The prefix that appears before the company name.
   * This often describes the scope, specialty, or distinguishing characteristic.
   * 
   * <p>Examples: "Global", "Advanced", "Mega", "Premier", "Elite"
   * 
   * @return the company prefix
   */
  private String prefix;
}
