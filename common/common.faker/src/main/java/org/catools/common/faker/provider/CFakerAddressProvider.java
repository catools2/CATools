package org.catools.common.faker.provider;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.catools.common.faker.model.CRandomAddress;
import org.catools.common.faker.model.CRandomCity;
import org.catools.common.faker.model.CRandomStreetInfo;
import org.catools.common.utils.CIterableUtil;

/**
 * Provider for generating random address information.
 *
 * <p>This class provides functionality to generate complete random addresses including country,
 * state, city, and street information. It can generate addresses for random locations or specific
 * states and cities.
 *
 * @since 1.0
 */
@AllArgsConstructor
public class CFakerAddressProvider {
  private final CFakerCountryProvider country;
  private final CFakerStreetAddressProvider streetAddressProvider;

  /**
   * Generates a random address from any available state and city.
   *
   * <p>This method selects a random state from the country's available states and then generates a
   * complete address including street information for that location.
   *
   * @return a randomly generated address with all components (country, state, city, street info)
   * @example
   *     <pre>
   * CFakerAddressProvider addressProvider = new CFakerAddressProvider(countryProvider, streetProvider);
   * CRandomAddress address = addressProvider.getAny();
   * // Returns: CRandomAddress with random state like "CA", city like "Los Angeles",
   * // street like "123 Main St"
   * </pre>
   */
  public CRandomAddress getAny() {
    return getAny(CIterableUtil.getRandom(country.getStateProviders()).getState().getCode());
  }

  /**
   * Generates a random address for a specific state.
   *
   * <p>This method generates a complete address for the specified state by randomly selecting a
   * city within that state and generating street information. If the provided state code is not
   * found, returns null.
   *
   * @param stateCode the state code (case-insensitive) for which to generate an address
   * @return a randomly generated address for the specified state, or null if state not found
   * @example
   *     <pre>
   * CFakerAddressProvider addressProvider = new CFakerAddressProvider(countryProvider, streetProvider);
   * CRandomAddress address = addressProvider.getAny("CA");
   * // Returns: CRandomAddress with state "CA", random city like "San Francisco",
   * // street like "456 Oak Ave"
   *
   * CRandomAddress invalidAddress = addressProvider.getAny("XX");
   * // Returns: null (state code "XX" not found)
   * </pre>
   */
  public CRandomAddress getAny(String stateCode) {
    Optional<CFakerStateProvider> state =
        country.getStateProviders().stream()
            .filter(s -> s.getState().getCode().equalsIgnoreCase(stateCode))
            .findFirst();
    return state.isEmpty() ? null : getAny(stateCode, state.get().getRandomCity().getName());
  }

  /**
   * Generates a random address for a specific state and city.
   *
   * <p>This method generates a complete address for the specified state and city combination. It
   * validates that both the state code and city name exist in the country's data before generating
   * the address with random street information.
   *
   * @param stateCode the state code (case-insensitive) for the address
   * @param cityname the city name (case-insensitive) for the address
   * @return a randomly generated address for the specified state and city, or null if either state
   *     or city is not found
   * @example
   *     <pre>
   * CFakerAddressProvider addressProvider = new CFakerAddressProvider(countryProvider, streetProvider);
   * CRandomAddress address = addressProvider.getAny("CA", "Los Angeles");
   * // Returns: CRandomAddress with state "CA", city "Los Angeles",
   * // street like "789 Pine St"
   *
   * CRandomAddress invalidAddress = addressProvider.getAny("CA", "NonExistentCity");
   * // Returns: null (city "NonExistentCity" not found in CA)
   *
   * CRandomAddress anotherInvalidAddress = addressProvider.getAny("XX", "Los Angeles");
   * // Returns: null (state code "XX" not found)
   * </pre>
   */
  public CRandomAddress getAny(String stateCode, String cityname) {
    Optional<CFakerStateProvider> state =
        country.getStateProviders().stream()
            .filter(s -> s.getState().getCode().equalsIgnoreCase(stateCode))
            .findFirst();
    if (state.isEmpty()) return null;

    Optional<CRandomCity> city =
        state.get().getCities().stream()
            .filter(s -> s.getName().equalsIgnoreCase(cityname))
            .findFirst();
    if (city.isEmpty()) return null;

    CRandomStreetInfo streetInfo = streetAddressProvider.getAny();
    return new CRandomAddress(
        country.getCountry(),
        state.get().getState(),
        city.get(),
        streetInfo.getStreetName(),
        streetInfo.getStreetSuffix(),
        streetInfo.getStreetPrefix(),
        streetInfo.getStreetNumber(),
        streetInfo.getBuildingNumber());
  }
}
