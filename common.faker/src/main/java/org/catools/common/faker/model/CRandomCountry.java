package org.catools.common.faker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a country with its associated metadata for faker data generation. This model contains
 * comprehensive country information including codes, currency details, phone prefixes, and postal
 * code patterns.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * CRandomCountry country = new CRandomCountry()
 *     .setCountryCode2("US")
 *     .setCountryCode3("USA")
 *     .setName("United States")
 *     .setCurrencyCode("USD")
 *     .setCurrencyName("United States Dollar")
 *     .setPhonePrefix("+1")
 *     .setPostalCodeFormat("#####")
 *     .setPostalCodeRegex("\\d{5}");
 *
 * // Or using the all-args constructor
 * CRandomCountry country2 = new CRandomCountry(
 *     "CA", "CAN", "Canada", "CAD", "Canadian Dollar",
 *     "+1", "?#? #?#", "[A-Z]\\d[A-Z] \\d[A-Z]\\d"
 * );
 * }</pre>
 *
 * @author CATools
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CRandomCountry {

  /**
   * The ISO 3166-1 alpha-2 country code (2 characters).
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"US" for United States
   *   <li>"CA" for Canada
   *   <li>"GB" for United Kingdom
   *   <li>"DE" for Germany
   * </ul>
   */
  private String countryCode2;

  /**
   * The ISO 3166-1 alpha-3 country code (3 characters).
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"USA" for United States
   *   <li>"CAN" for Canada
   *   <li>"GBR" for United Kingdom
   *   <li>"DEU" for Germany
   * </ul>
   */
  private String countryCode3;

  /**
   * The full name of the country.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"United States"
   *   <li>"Canada"
   *   <li>"United Kingdom"
   *   <li>"Germany"
   * </ul>
   */
  private String name;

  /**
   * The ISO 4217 currency code for the country's primary currency.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"USD" for US Dollar
   *   <li>"CAD" for Canadian Dollar
   *   <li>"GBP" for British Pound
   *   <li>"EUR" for Euro
   * </ul>
   */
  private String currencyCode;

  /**
   * The full name of the country's primary currency.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"United States Dollar"
   *   <li>"Canadian Dollar"
   *   <li>"British Pound Sterling"
   *   <li>"Euro"
   * </ul>
   */
  private String currencyName;

  /**
   * The international phone prefix for the country, including the plus sign.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"+1" for United States and Canada
   *   <li>"+44" for United Kingdom
   *   <li>"+49" for Germany
   *   <li>"+33" for France
   * </ul>
   */
  private String phonePrefix;

  /**
   * The postal code format pattern for the country, using placeholders. Common placeholders: '#'
   * for digits, '?' for letters, '@' for alphanumeric.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"#####" for US ZIP codes (12345)
   *   <li>"?#? #?#" for Canadian postal codes (K1A 0A6)
   *   <li>"??## #??" for UK postcodes (SW1A 1AA)
   *   <li>"#####" for German postal codes (10115)
   * </ul>
   */
  private String postalCodeFormat;

  /**
   * The regular expression pattern that matches valid postal codes for the country.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>"\\d{5}" for US ZIP codes
   *   <li>"[A-Z]\\d[A-Z] \\d[A-Z]\\d" for Canadian postal codes
   *   <li>"[A-Z]{1,2}\\d[A-Z\\d]? \\d[A-Z]{2}" for UK postcodes
   *   <li>"\\d{5}" for German postal codes
   * </ul>
   */
  private String postalCodeRegex;
}
