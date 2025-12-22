package org.catools.common.faker.model;

import java.util.HashSet;

/**
 * A specialized collection class that extends HashSet to store and manage CRandomCity objects. This
 * class provides a type-safe way to work with collections of cities, inheriting all the benefits of
 * HashSet including fast lookups, automatic duplicate removal, and efficient storage.
 *
 * <p>Since this class extends HashSet, it automatically prevents duplicate cities based on the
 * equals() and hashCode() methods of the CRandomCity class. This makes it ideal for scenarios where
 * you need to ensure unique cities in your collection.
 *
 * <p>All standard HashSet operations are available, including add(), remove(), contains(), size(),
 * isEmpty(), clear(), and iteration capabilities.
 *
 * <h3>Usage Examples:</h3>
 *
 * <h4>Creating and populating a collection of cities:</h4>
 *
 * <pre>{@code
 * CRandomCities cities = new CRandomCities();
 * cities.add(new CRandomCity("New York", "10001"));
 * cities.add(new CRandomCity("Los Angeles", "90210"));
 * cities.add(new CRandomCity("Chicago", "60601"));
 *
 * System.out.println("Number of cities: " + cities.size()); // Output: 3
 * }</pre>
 *
 * <h4>Automatic duplicate prevention:</h4>
 *
 * <pre>{@code
 * CRandomCities cities = new CRandomCities();
 * cities.add(new CRandomCity("Miami", "33101"));
 * cities.add(new CRandomCity("Miami", "33101")); // Duplicate - won't be added
 *
 * System.out.println("Number of cities: " + cities.size()); // Output: 1
 * }</pre>
 *
 * <h4>Checking if a city exists:</h4>
 *
 * <pre>{@code
 * CRandomCities cities = new CRandomCities();
 * CRandomCity seattle = new CRandomCity("Seattle", "98101");
 * cities.add(seattle);
 *
 * if (cities.contains(seattle)) {
 *     System.out.println("Seattle is in the collection");
 * }
 *
 * // Also works with a new instance having the same values
 * boolean hasSeattle = cities.contains(new CRandomCity("Seattle", "98101"));
 * }</pre>
 *
 * <h4>Iterating through cities:</h4>
 *
 * <pre>{@code
 * CRandomCities cities = new CRandomCities();
 * cities.add(new CRandomCity("Denver", "80201"));
 * cities.add(new CRandomCity("Phoenix", "85001"));
 * cities.add(new CRandomCity("Austin", "78701"));
 *
 * // Using enhanced for loop
 * for (CRandomCity city : cities) {
 *     System.out.println(city.getName() + " - " + city.getZipCode());
 * }
 *
 * // Using streams
 * cities.stream()
 *     .filter(city -> city.getZipCode().startsWith("8"))
 *     .forEach(city -> System.out.println("Western city: " + city.getName()));
 * }</pre>
 *
 * <h4>Converting to other collections:</h4>
 *
 * <pre>{@code
 * CRandomCities cities = new CRandomCities();
 * cities.add(new CRandomCity("Boston", "02101"));
 * cities.add(new CRandomCity("Atlanta", "30301"));
 *
 * // Convert to List for ordered processing
 * List<CRandomCity> cityList = new ArrayList<>(cities);
 *
 * // Convert to array
 * CRandomCity[] cityArray = cities.toArray(new CRandomCity[0]);
 *
 * // Get just city names
 * Set<String> cityNames = cities.stream()
 *     .map(CRandomCity::getName)
 *     .collect(Collectors.toSet());
 * }</pre>
 *
 * <h4>Bulk operations:</h4>
 *
 * <pre>{@code
 * CRandomCities eastCoastCities = new CRandomCities();
 * eastCoastCities.add(new CRandomCity("New York", "10001"));
 * eastCoastCities.add(new CRandomCity("Boston", "02101"));
 *
 * CRandomCities westCoastCities = new CRandomCities();
 * westCoastCities.add(new CRandomCity("Los Angeles", "90210"));
 * westCoastCities.add(new CRandomCity("San Francisco", "94101"));
 *
 * CRandomCities allCities = new CRandomCities();
 * allCities.addAll(eastCoastCities);
 * allCities.addAll(westCoastCities);
 *
 * System.out.println("Total cities: " + allCities.size()); // Output: 4
 * }</pre>
 *
 * <h4>Working with data generation and testing:</h4>
 *
 * <pre>{@code
 * // Useful in testing scenarios
 * CRandomCities testCities = new CRandomCities();
 * testCities.add(new CRandomCity("TestCity1", "12345"));
 * testCities.add(new CRandomCity("TestCity2", "54321"));
 *
 * // Validate that test data was properly set up
 * assertEquals(2, testCities.size());
 * assertTrue(testCities.contains(new CRandomCity("TestCity1", "12345")));
 * }</pre>
 *
 * @author CATools
 * @version 1.0
 * @since 1.0
 * @see CRandomCity
 * @see java.util.HashSet
 */
public class CRandomCities extends HashSet<CRandomCity> {

  /**
   * Constructs an empty CRandomCities collection with the default initial capacity (16) and load
   * factor (0.75).
   *
   * <p>This constructor creates a new, empty HashSet that can store CRandomCity objects. The
   * collection will automatically resize as needed when cities are added.
   *
   * <h4>Example:</h4>
   *
   * <pre>{@code
   * CRandomCities cities = new CRandomCities();
   * System.out.println("Initial size: " + cities.size()); // Output: 0
   * System.out.println("Is empty: " + cities.isEmpty());  // Output: true
   *
   * // Now add some cities
   * cities.add(new CRandomCity("Dallas", "75201"));
   * cities.add(new CRandomCity("Houston", "77001"));
   * System.out.println("After adding cities: " + cities.size()); // Output: 2
   * }</pre>
   *
   * @see HashSet#HashSet()
   */
  public CRandomCities() {}
}
