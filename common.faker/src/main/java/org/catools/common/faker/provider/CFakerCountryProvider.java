package org.catools.common.faker.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.catools.common.faker.model.CRandomCountry;

/**
 * Provider class for country-specific faker data generation.
 * 
 * <p>This class serves as a central provider for generating fake data specific to a particular country.
 * It aggregates various specialized providers for names, companies, addresses, and states to provide
 * comprehensive country-localized fake data generation capabilities.</p>
 * 
 * <p>The class supports method chaining through the {@code @Accessors(chain = true)} annotation,
 * allowing for fluent-style configuration.</p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Create a country provider for the United States
 * CRandomCountry usCountry = new CRandomCountry("US", "United States");
 * CFakerStateProviders stateProviders = new CFakerStateProviders(usStates);
 * CFakerNameProvider nameProvider = new CFakerNameProvider(usNames);
 * CFakerCompanyProvider companyProvider = new CFakerCompanyProvider(usCompanies);
 * CFakerStreetAddressProvider streetProvider = new CFakerStreetAddressProvider(usStreets);
 * 
 * CFakerCountryProvider countryProvider = new CFakerCountryProvider(
 *     usCountry, stateProviders, nameProvider, companyProvider, streetProvider);
 * 
 * // Access country information
 * CRandomCountry country = countryProvider.getCountry();
 * String countryName = country.getName(); // "United States"
 * 
 * // Access specialized providers
 * CFakerNameProvider names = countryProvider.getNameProvider();
 * CFakerAddressProvider addresses = countryProvider.getAddressProvider();
 * }</pre>
 * 
 * @since 1.0
 * @see CRandomCountry
 * @see CFakerStateProviders
 * @see CFakerNameProvider
 * @see CFakerCompanyProvider
 * @see CFakerAddressProvider
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class CFakerCountryProvider {
  /**
   * The country information associated with this provider.
   * 
   * <p>Contains metadata about the country such as country code and name,
   * which is used to contextualize the fake data generation.</p>
   * 
   * @see CRandomCountry
   */
  private final CRandomCountry country;
  
  /**
   * Provider for state-specific data generation.
   * 
   * <p>Handles generation of fake data related to states, provinces, or other
   * administrative divisions within the country.</p>
   * 
   * @see CFakerStateProviders
   */
  private final CFakerStateProviders stateProviders;
  
  /**
   * Provider for person name generation.
   * 
   * <p>Generates culturally appropriate first names, last names, and full names
   * specific to the country's naming conventions.</p>
   * 
   * @see CFakerNameProvider
   */
  private final CFakerNameProvider nameProvider;
  
  /**
   * Provider for company-related data generation.
   * 
   * <p>Generates fake company names, business types, and other corporate
   * information appropriate for the country's business environment.</p>
   * 
   * @see CFakerCompanyProvider
   */
  private final CFakerCompanyProvider companyProvider;
  
  /**
   * Provider for address generation.
   * 
   * <p>Generates complete addresses including street addresses, using the
   * country-specific formatting and conventions.</p>
   * 
   * @see CFakerAddressProvider
   */
  private final CFakerAddressProvider addressProvider;

  /**
   * Constructs a new CFakerCountryProvider with the specified providers.
   * 
   * <p>This constructor initializes all the necessary providers for generating
   * country-specific fake data. The address provider is automatically created
   * using the current country provider and the provided street address provider.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * // Create country information
   * CRandomCountry canada = new CRandomCountry("CA", "Canada");
   * 
   * // Create specialized providers
   * CFakerStateProviders provinces = new CFakerStateProviders(canadianProvinces);
   * CFakerNameProvider names = new CFakerNameProvider(canadianNames);
   * CFakerCompanyProvider companies = new CFakerCompanyProvider(canadianCompanies);
   * CFakerStreetAddressProvider streets = new CFakerStreetAddressProvider(canadianStreets);
   * 
   * // Create the country provider
   * CFakerCountryProvider canadaProvider = new CFakerCountryProvider(
   *     canada, provinces, names, companies, streets);
   * 
   * // The address provider is automatically created and configured
   * CFakerAddressProvider addresses = canadaProvider.getAddressProvider();
   * }</pre>
   * 
   * @param country the country information containing metadata like country code and name
   * @param stateProviders provider for generating state/province-specific data
   * @param personNameProvider provider for generating person names appropriate for the country
   * @param companyProvider provider for generating company-related data
   * @param streetAddressProvider provider for generating street address components
   * 
   * @throws NullPointerException if any of the parameters are null
   * 
   * @see CRandomCountry
   * @see CFakerStateProviders
   * @see CFakerNameProvider
   * @see CFakerCompanyProvider
   * @see CFakerStreetAddressProvider
   * @see CFakerAddressProvider
   */
  public CFakerCountryProvider(
      CRandomCountry country,
      CFakerStateProviders stateProviders,
      CFakerNameProvider personNameProvider,
      CFakerCompanyProvider companyProvider,
      CFakerStreetAddressProvider streetAddressProvider) {
    this.country = country;
    this.stateProviders = stateProviders;
    this.nameProvider = personNameProvider;
    this.companyProvider = companyProvider;
    this.addressProvider = new CFakerAddressProvider(this, streetAddressProvider);
  }
}
