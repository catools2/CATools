package org.catools.common.faker.provider;

import org.catools.common.faker.model.CRandomCities;
import org.catools.common.faker.model.CRandomState;

import java.util.HashSet;

/**
 * A collection of state providers for a specific country, extending HashSet to provide
 * convenient management of CFakerStateProvider instances. This class serves as a container
 * for all states/provinces within a country, allowing easy access and manipulation of
 * state-specific faker data.
 * 
 * <p>Each CFakerStateProvider contains information about a state including its details
 * and associated cities. This collection provides methods to add, remove, and iterate
 * over state providers while maintaining the performance benefits of a HashSet.</p>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Creating and populating a CFakerStateProviders collection:</h4>
 * <pre>{@code
 * // Create an empty collection
 * CFakerStateProviders stateProviders = new CFakerStateProviders();
 * 
 * // Create state data
 * CRandomState california = new CRandomState("California", "CA", "US");
 * CRandomCities californiaCities = new CRandomCities();
 * californiaCities.add(new CRandomCity("Los Angeles", "LA"));
 * californiaCities.add(new CRandomCity("San Francisco", "SF"));
 * 
 * // Create and add state provider
 * CFakerStateProvider caProvider = new CFakerStateProvider(california, californiaCities);
 * stateProviders.add(caProvider);
 * 
 * // Add more states
 * CRandomState texas = new CRandomState("Texas", "TX", "US");
 * CRandomCities texasCities = new CRandomCities();
 * texasCities.add(new CRandomCity("Houston", "HOU"));
 * texasCities.add(new CRandomCity("Dallas", "DAL"));
 * 
 * CFakerStateProvider txProvider = new CFakerStateProvider(texas, texasCities);
 * stateProviders.add(txProvider);
 * }</pre>
 * 
 * <h4>Creating from an existing collection:</h4>
 * <pre>{@code
 * // Prepare a list of state providers
 * List<CFakerStateProvider> providerList = Arrays.asList(
 *     new CFakerStateProvider(californiaState, californiaCities),
 *     new CFakerStateProvider(texasState, texasCities),
 *     new CFakerStateProvider(nyState, nyCities)
 * );
 * 
 * // Create CFakerStateProviders from existing collection
 * CFakerStateProviders stateProviders = new CFakerStateProviders(providerList);
 * 
 * System.out.println("Number of states: " + stateProviders.size()); // 3
 * }</pre>
 * 
 * <h4>Iterating through state providers:</h4>
 * <pre>{@code
 * CFakerStateProviders stateProviders = getCountryStateProviders("US");
 * 
 * // Iterate through all state providers
 * for (CFakerStateProvider provider : stateProviders) {
 *     CRandomState state = provider.getState();
 *     System.out.println("State: " + state.getName() + " (" + state.getCode() + ")");
 *     
 *     // Get a random city from this state
 *     CRandomCity randomCity = provider.getRandomCity();
 *     System.out.println("Random city: " + randomCity.getName());
 * }
 * }</pre>
 * 
 * <h4>Finding specific state providers:</h4>
 * <pre>{@code
 * CFakerStateProviders stateProviders = countryProvider.getStateProviders();
 * 
 * // Find state by code using stream operations
 * Optional<CFakerStateProvider> californiaProvider = stateProviders.stream()
 *     .filter(provider -> "CA".equals(provider.getState().getCode()))
 *     .findFirst();
 * 
 * if (californiaProvider.isPresent()) {
 *     CRandomCity randomCaliforniaCity = californiaProvider.get().getRandomCity();
 *     System.out.println("Random California city: " + randomCaliforniaCity.getName());
 * }
 * 
 * // Check if a specific state exists
 * boolean hasTexas = stateProviders.stream()
 *     .anyMatch(provider -> "TX".equals(provider.getState().getCode()));
 * }</pre>
 * 
 * <h4>Working with HashSet operations:</h4>
 * <pre>{@code
 * CFakerStateProviders stateProviders = new CFakerStateProviders();
 * 
 * // Add providers (HashSet ensures uniqueness)
 * stateProviders.add(californiaProvider);
 * stateProviders.add(texasProvider);
 * 
 * // Check size and emptiness
 * System.out.println("Is empty: " + stateProviders.isEmpty());
 * System.out.println("Size: " + stateProviders.size());
 * 
 * // Remove a provider
 * stateProviders.remove(californiaProvider);
 * 
 * // Clear all providers
 * stateProviders.clear();
 * }</pre>
 * 
 * @see CFakerStateProvider
 * @see CRandomState
 * @see CRandomCities
 * @see CFakerCountryProvider
 * @author CA Tools Development Team
 * @since 1.0
 */
public class CFakerStateProviders extends HashSet<CFakerStateProvider> {
  
  /**
   * Creates an empty CFakerStateProviders collection.
   * 
   * <p>This constructor initializes an empty HashSet that can be populated
   * with CFakerStateProvider instances using the add() method or other
   * collection operations.</p>
   * 
   * <h4>Example:</h4>
   * <pre>{@code
   * CFakerStateProviders stateProviders = new CFakerStateProviders();
   * stateProviders.add(new CFakerStateProvider(state, cities));
   * }</pre>
   */
  public CFakerStateProviders() {
  }

  /**
   * Creates a CFakerStateProviders collection populated with state providers
   * from the given iterable.
   * 
   * <p>This constructor is useful when you have an existing collection of
   * CFakerStateProvider instances that you want to convert to a CFakerStateProviders
   * collection. The constructor safely handles null input by checking for null
   * before iterating.</p>
   * 
   * <h4>Example with List:</h4>
   * <pre>{@code
   * List<CFakerStateProvider> providerList = Arrays.asList(
   *     new CFakerStateProvider(californiaState, californiaCities),
   *     new CFakerStateProvider(texasState, texasCities)
   * );
   * 
   * CFakerStateProviders stateProviders = new CFakerStateProviders(providerList);
   * System.out.println("Created with " + stateProviders.size() + " states");
   * }</pre>
   * 
   * <h4>Example with Stream:</h4>
   * <pre>{@code
   * // Create from a stream of data
   * Stream<StateData> stateDataStream = getStateDataStream();
   * List<CFakerStateProvider> providers = stateDataStream
   *     .map(data -> new CFakerStateProvider(data.getState(), data.getCities()))
   *     .collect(Collectors.toList());
   * 
   * CFakerStateProviders stateProviders = new CFakerStateProviders(providers);
   * }</pre>
   * 
   * <h4>Example with null safety:</h4>
   * <pre>{@code
   * // Safe to pass null - will create empty collection
   * CFakerStateProviders stateProviders = new CFakerStateProviders(null);
   * System.out.println("Size: " + stateProviders.size()); // 0
   * }</pre>
   * 
   * @param iterable the collection of CFakerStateProvider instances to add to this collection.
   *                 Can be null, in which case an empty collection is created.
   */
  public CFakerStateProviders(Iterable<CFakerStateProvider> iterable) {
    super();
    if (iterable != null) {
      iterable.forEach(i -> add(i));
    }
  }
}
