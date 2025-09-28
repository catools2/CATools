package org.catools.common.faker.provider;

import lombok.AllArgsConstructor;
import org.catools.common.faker.model.CRandomCompany;
import org.catools.common.utils.CIterableUtil;

import java.util.List;

/**
 * Provider class for generating random company data for testing and mock purposes.
 * This class uses predefined lists of company names, prefixes, and suffixes to create
 * realistic-looking company information.
 * 
 * <p>The provider randomly selects elements from the configured lists to generate
 * {@link CRandomCompany} objects with varied company information.</p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Create lists of company data
 * List<String> names = Arrays.asList("Tech Corp", "Data Systems", "Innovation Ltd");
 * List<String> prefixes = Arrays.asList("Global", "Advanced", "Premier");
 * List<String> suffixes = Arrays.asList("Inc", "LLC", "Corporation");
 * 
 * // Initialize the provider
 * CFakerCompanyProvider provider = new CFakerCompanyProvider(names, prefixes, suffixes);
 * 
 * // Generate random company data
 * CRandomCompany company = provider.getAny();
 * System.out.println(company.getName());    // e.g., "Tech Corp"
 * System.out.println(company.getPrefix());  // e.g., "Global"
 * System.out.println(company.getSuffix());  // e.g., "Inc"
 * }</pre>
 * 
 * @author CATools
 * @since 1.0
 * @see CRandomCompany
 * @see CIterableUtil
 */
@AllArgsConstructor
public class CFakerCompanyProvider {
  private final List<String> companyNames;
  private final List<String> companyPrefixes;
  private final List<String> companySuffixes;

  /**
   * Generates a random company object by randomly selecting elements from the configured
   * company names, prefixes, and suffixes lists.
   * 
   * <p>This method creates a new {@link CRandomCompany} instance with randomly selected
   * values from each of the three configured lists. Each call to this method may return
   * different values depending on the random selection process.</p>
   * 
   * <h3>Example Usage:</h3>
   * <pre>{@code
   * CFakerCompanyProvider provider = new CFakerCompanyProvider(
   *     Arrays.asList("Microsoft", "Google", "Amazon"),
   *     Arrays.asList("Global", "International", "Worldwide"),
   *     Arrays.asList("Inc", "Corp", "LLC")
   * );
   * 
   * CRandomCompany company = provider.getAny();
   * // Possible results:
   * // company.getName() = "Google"
   * // company.getPrefix() = "Worldwide" 
   * // company.getSuffix() = "Inc"
   * 
   * // Generate multiple companies
   * for (int i = 0; i < 3; i++) {
   *     CRandomCompany randomCompany = provider.getAny();
   *     System.out.println(randomCompany.getPrefix() + " " + 
   *                       randomCompany.getName() + " " + 
   *                       randomCompany.getSuffix());
   * }
   * // Output might be:
   * // Global Microsoft Corp
   * // International Amazon LLC
   * // Worldwide Google Inc
   * }</pre>
   * 
   * @return a new {@link CRandomCompany} instance with randomly selected name, prefix, and suffix
   * @throws RuntimeException if any of the configured lists are empty or null (thrown by {@link CIterableUtil#getRandom})
   * 
   * @see CRandomCompany
   * @see CIterableUtil#getRandom(List)
   */
  public CRandomCompany getAny() {
    return new CRandomCompany(
        CIterableUtil.getRandom(companyNames),
        CIterableUtil.getRandom(companyPrefixes),
        CIterableUtil.getRandom(companySuffixes));
  }
}
