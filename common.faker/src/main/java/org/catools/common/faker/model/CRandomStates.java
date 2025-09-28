package org.catools.common.faker.model;

import java.util.HashSet;

/**
 * A specialized collection class for managing {@link CRandomState} objects.
 * This class extends {@link HashSet} to provide a type-safe container for states,
 * ensuring unique state entries based on the default {@code equals} and {@code hashCode}
 * implementations of {@link CRandomState}.
 * 
 * <p>This class is particularly useful in faker libraries or data generation scenarios
 * where you need to manage a collection of states (e.g., US states, provinces, etc.)
 * and ensure no duplicates are present.</p>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Basic Usage:</h4>
 * <pre>{@code
 * CRandomStates states = new CRandomStates();
 * 
 * // Add states to the collection
 * states.add(new CRandomState("California", "CA"));
 * states.add(new CRandomState("Texas", "TX"));
 * states.add(new CRandomState("New York", "NY"));
 * 
 * // Check if a state exists
 * boolean hasCA = states.contains(new CRandomState("California", "CA"));
 * 
 * // Get the number of states
 * int stateCount = states.size();
 * }</pre>
 * 
 * <h4>Bulk Operations:</h4>
 * <pre>{@code
 * CRandomStates usStates = new CRandomStates();
 * 
 * // Add multiple states at once
 * List<CRandomState> stateList = Arrays.asList(
 *     new CRandomState("Florida", "FL"),
 *     new CRandomState("Illinois", "IL"),
 *     new CRandomState("Ohio", "OH")
 * );
 * usStates.addAll(stateList);
 * 
 * // Create another collection for comparison
 * CRandomStates southernStates = new CRandomStates();
 * southernStates.add(new CRandomState("Florida", "FL"));
 * southernStates.add(new CRandomState("Georgia", "GA"));
 * 
 * // Find common states
 * CRandomStates commonStates = new CRandomStates();
 * commonStates.addAll(usStates);
 * commonStates.retainAll(southernStates);
 * }</pre>
 * 
 * <h4>Iteration and Processing:</h4>
 * <pre>{@code
 * CRandomStates states = new CRandomStates();
 * states.add(new CRandomState("Washington", "WA"));
 * states.add(new CRandomState("Oregon", "OR"));
 * 
 * // Iterate through all states
 * for (CRandomState state : states) {
 *     System.out.println(state.getName() + " (" + state.getCode() + ")");
 * }
 * 
 * // Using streams for filtering
 * List<String> stateCodes = states.stream()
 *     .map(CRandomState::getCode)
 *     .sorted()
 *     .collect(Collectors.toList());
 * }</pre>
 * 
 * <h4>Integration with Faker Libraries:</h4>
 * <pre>{@code
 * // Example usage in a data generation context
 * CRandomStates availableStates = new CRandomStates();
 * availableStates.add(new CRandomState("Alaska", "AK"));
 * availableStates.add(new CRandomState("Hawaii", "HI"));
 * 
 * // Convert to array for random selection
 * CRandomState[] stateArray = availableStates.toArray(new CRandomState[0]);
 * Random random = new Random();
 * CRandomState randomState = stateArray[random.nextInt(stateArray.length)];
 * }</pre>
 * 
 * @author catools
 * @version 1.0
 * @since 1.0
 * @see CRandomState
 * @see HashSet
 */
public class CRandomStates extends HashSet<CRandomState> {
  
  /**
   * Constructs a new, empty {@code CRandomStates} collection.
   * 
   * <p>The backing {@link HashSet} is initialized with default initial capacity (16)
   * and load factor (0.75), providing efficient performance for most use cases.</p>
   * 
   * <h4>Example:</h4>
   * <pre>{@code
   * // Create an empty collection of states
   * CRandomStates states = new CRandomStates();
   * 
   * // Initially empty
   * assert states.isEmpty();
   * assert states.size() == 0;
   * 
   * // Ready to accept state objects
   * states.add(new CRandomState("Nevada", "NV"));
   * assert states.size() == 1;
   * }</pre>
   * 
   * @see HashSet#HashSet()
   */
  public CRandomStates() {
  }
}
