package org.catools.common.faker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a random name model with various name components. This class is used to store and
 * manipulate different parts of a person's name including first name, middle name, last name,
 * prefix, and suffix.
 *
 * <p>The class uses Lombok annotations for automatic generation of getters, setters, constructors,
 * and other utility methods. The @Accessors(chain = true) annotation enables method chaining for a
 * fluent API.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Create using no-args constructor and method chaining
 * CRandomName name = new CRandomName()
 *     .setFirstName("John")
 *     .setMiddleName("Michael")
 *     .setLastName("Smith")
 *     .setPrefix("Mr.")
 *     .setSuffix("Jr.");
 *
 * // Create using all-args constructor
 * CRandomName fullName = new CRandomName("Jane", "Elizabeth", "Doe", "Dr.", "PhD");
 *
 * // Access individual components
 * String firstName = name.getFirstName(); // "John"
 * String fullNameStr = name.getPrefix() + " " + name.getFirstName() + " " +
 *                     name.getMiddleName() + " " + name.getLastName() + " " +
 *                     name.getSuffix(); // "Mr. John Michael Smith Jr."
 * }</pre>
 *
 * @author CA Tools
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CRandomName {

  /**
   * The first name (given name) of the person.
   *
   * <p>Example values: "John", "Jane", "Michael", "Sarah"
   */
  private String firstName;

  /**
   * The middle name of the person. This field is optional and can be null.
   *
   * <p>Example values: "Michael", "Elizabeth", "James", "Anne"
   */
  private String middleName;

  /**
   * The last name (family name/surname) of the person.
   *
   * <p>Example values: "Smith", "Johnson", "Williams", "Brown"
   */
  private String lastName;

  /**
   * The prefix or title that comes before the name. This field is optional and can be null.
   *
   * <p>Example values: "Mr.", "Mrs.", "Ms.", "Dr.", "Prof."
   */
  private String prefix;

  /**
   * The suffix that comes after the name. This field is optional and can be null.
   *
   * <p>Example values: "Jr.", "Sr.", "III", "PhD", "MD"
   */
  private String suffix;
}
