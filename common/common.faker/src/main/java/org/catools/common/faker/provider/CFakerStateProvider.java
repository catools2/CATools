package org.catools.common.faker.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.catools.common.faker.model.CRandomCities;
import org.catools.common.faker.model.CRandomCity;
import org.catools.common.faker.model.CRandomState;
import org.catools.common.utils.CIterableUtil;

/**
 * Provider class for generating random city data within a specific state context. This class acts
 * as a facade for accessing random city information associated with a particular state.
 *
 * <p>The provider encapsulates a state and its collection of cities, offering convenient methods to
 * retrieve random city data for testing, data generation, or simulation purposes.
 *
 * <p><strong>Example Usage:</strong>
 *
 * <pre>{@code
 * // Create a state provider with state and cities data
 * CRandomState california = new CRandomState("California", "CA");
 * CRandomCities californiaCities = new CRandomCities(Arrays.asList(
 *     new CRandomCity("Los Angeles", "90210"),
 *     new CRandomCity("San Francisco", "94102"),
 *     new CRandomCity("San Diego", "92101")
 * ));
 *
 * CFakerStateProvider provider = new CFakerStateProvider(california, californiaCities);
 *
 * // Get a random city from the state
 * CRandomCity randomCity = provider.getRandomCity();
 * System.out.println("Random city: " + randomCity.getName());
 *
 * // Use in test scenarios
 * for (int i = 0; i < 5; i++) {
 *     CRandomCity city = provider.getRandomCity();
 *     // Each call returns a randomly selected city from the state's collection
 * }
 * }</pre>
 *
 * @since 1.0
 * @author CATools
 */
@Data
@AllArgsConstructor
public class CFakerStateProvider {
  /**
   * The state information associated with this provider. Contains state-specific data such as name
   * and abbreviation.
   */
  private final CRandomState state;

  /**
   * The collection of cities within the associated state. Contains all available cities that can be
   * randomly selected.
   */
  private final CRandomCities cities;

  /**
   * Retrieves a randomly selected city from the state's collection of cities.
   *
   * <p>This method uses the internal city collection to return a random city instance. Each
   * invocation may return a different city from the available collection, making it suitable for
   * generating varied test data or simulation scenarios.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * // Assuming provider is initialized with Texas cities
   * CFakerStateProvider texasProvider = new CFakerStateProvider(texasState, texasCities);
   *
   * // Get random cities for testing
   * CRandomCity city1 = texasProvider.getRandomCity(); // e.g., "Houston"
   * CRandomCity city2 = texasProvider.getRandomCity(); // e.g., "Dallas"
   * CRandomCity city3 = texasProvider.getRandomCity(); // e.g., "Austin"
   *
   * // Use in address generation
   * String address = String.format("%s, %s %s",
   *     "123 Main St",
   *     texasProvider.getRandomCity().getName(),
   *     texasProvider.getState().getAbbreviation());
   *
   * // Bulk generation for test data
   * List<CRandomCity> testCities = IntStream.range(0, 10)
   *     .mapToObj(i -> texasProvider.getRandomCity())
   *     .collect(Collectors.toList());
   * }</pre>
   *
   * @return a randomly selected {@link CRandomCity} from the state's city collection
   * @throws IllegalArgumentException if the cities collection is empty or null
   * @see CRandomCity
   * @see CIterableUtil#getRandom(Object)
   * @since 1.0
   */
  public CRandomCity getRandomCity() {
    return CIterableUtil.getRandom(cities);
  }
}
