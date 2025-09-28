package org.catools.common.faker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a random state data model with name and code attributes.
 * This class is commonly used for generating fake state information in testing scenarios.
 * <p>
 * The class uses Lombok annotations to automatically generate getters, setters, equals,
 * hashCode, toString methods, and constructors. The {@code @Accessors(chain = true)}
 * annotation enables method chaining for setter methods.
 * </p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Creating a new state using no-args constructor
 * CRandomState state = new CRandomState();
 * state.setName("California");
 * state.setCode("CA");
 * 
 * // Creating a state using all-args constructor
 * CRandomState state = new CRandomState("Texas", "TX");
 * 
 * // Using method chaining
 * CRandomState state = new CRandomState()
 *     .setName("New York")
 *     .setCode("NY");
 * 
 * // Accessing state information
 * System.out.println("State: " + state.getName() + " (" + state.getCode() + ")");
 * }</pre>
 * 
 * @author CA Tools
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CRandomState {
  
  /**
   * The full name of the state.
   * <p>
   * This field stores the complete name of a state, such as "California", "Texas", or "New York".
   * The name should be the official state name as commonly recognized.
   * </p>
   * 
   * <h4>Examples:</h4>
   * <ul>
   *   <li>"California"</li>
   *   <li>"Texas"</li>
   *   <li>"New York"</li>
   *   <li>"Florida"</li>
   * </ul>
   * 
   * @return the state name
   * @see #getCode()
   */
  private String name;
  
  /**
   * The abbreviated code of the state.
   * <p>
   * This field stores the standard two-letter abbreviation for the state,
   * as defined by the United States Postal Service (USPS).
   * The code should always be in uppercase format.
   * </p>
   * 
   * <h4>Examples:</h4>
   * <ul>
   *   <li>"CA" for California</li>
   *   <li>"TX" for Texas</li>
   *   <li>"NY" for New York</li>
   *   <li>"FL" for Florida</li>
   * </ul>
   * 
   * @return the state code
   * @see #getName()
   */
  private String code;
}
