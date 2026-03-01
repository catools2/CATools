package org.catools.common.date;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

/**
 * A utility class for generating random dates within specified ranges. This class provides a fluent
 * API for configuring date ranges and generating random dates within those ranges.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Generate a random date between two years
 * CDate randomDate = CRandomDateGenerator.instance(2020, 2023).getDate();
 *
 * // Generate a formatted random date string
 * String formattedDate = CRandomDateGenerator.getFormattedDate(2020, 2023, "yyyy-MM-dd");
 *
 * // Using fluent API to configure specific date range
 * CDate specificDate = CRandomDateGenerator.instance()
 *     .setFromYear(2022)
 *     .setFromMonth(Calendar.JANUARY)
 *     .setToYear(2023)
 *     .setToMonth(Calendar.DECEMBER)
 *     .getDate();
 * }</pre>
 */
public class CRandomDateGenerator {
  private Calendar from = Calendar.getInstance();
  private Calendar to = Calendar.getInstance();

  private CRandomDateGenerator() {}

  /**
   * Creates a new instance of CRandomDateGenerator with default date range (current time).
   *
   * @return a new CRandomDateGenerator instance
   * @example
   *     <pre>{@code
   * CRandomDateGenerator generator = CRandomDateGenerator.instance();
   * CDate randomDate = generator.setFromYear(2020).setToYear(2023).getDate();
   * }</pre>
   */
  public static CRandomDateGenerator instance() {
    return new CRandomDateGenerator();
  }

  /**
   * Creates a new instance of CRandomDateGenerator with specified Calendar date range.
   *
   * @param from the start date of the range (inclusive)
   * @param to the end date of the range (inclusive)
   * @return a new CRandomDateGenerator instance configured with the specified range
   * @example
   *     <pre>{@code
   * Calendar startCal = Calendar.getInstance();
   * startCal.set(2020, Calendar.JANUARY, 1);
   * Calendar endCal = Calendar.getInstance();
   * endCal.set(2023, Calendar.DECEMBER, 31);
   *
   * CDate randomDate = CRandomDateGenerator.instance(startCal, endCal).getDate();
   * }</pre>
   */
  public static CRandomDateGenerator instance(Calendar from, Calendar to) {
    return new CRandomDateGenerator().setFrom(from).setTo(to);
  }

  /**
   * Creates a new instance of CRandomDateGenerator with specified Date range.
   *
   * @param from the start date of the range (inclusive)
   * @param to the end date of the range (inclusive)
   * @return a new CRandomDateGenerator instance configured with the specified range
   * @example
   *     <pre>{@code
   * Date startDate = new Date(120, 0, 1); // January 1, 2020
   * Date endDate = new Date(123, 11, 31); // December 31, 2023
   *
   * CDate randomDate = CRandomDateGenerator.instance(startDate, endDate).getDate();
   * }</pre>
   */
  public static CRandomDateGenerator instance(Date from, Date to) {
    return new CRandomDateGenerator().setFrom(from).setTo(to);
  }

  /**
   * Creates a new instance of CRandomDateGenerator with specified year range.
   *
   * @param fromYear the start year of the range (inclusive)
   * @param toYear the end year of the range (inclusive)
   * @return a new CRandomDateGenerator instance configured with the specified year range
   * @example
   *     <pre>{@code
   * // Generate random date between 2020 and 2023
   * CDate randomDate = CRandomDateGenerator.instance(2020, 2023).getDate();
   * }</pre>
   */
  public static CRandomDateGenerator instance(int fromYear, int toYear) {
    return new CRandomDateGenerator().setFromYear(fromYear).setToYear(toYear);
  }

  /**
   * Generates a formatted random date string within the specified Calendar range.
   *
   * @param from the start date of the range (inclusive)
   * @param to the end date of the range (inclusive)
   * @param format the date format string (e.g., "yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss")
   * @return a formatted random date string
   * @example
   *     <pre>{@code
   * Calendar startCal = Calendar.getInstance();
   * startCal.set(2020, Calendar.JANUARY, 1);
   * Calendar endCal = Calendar.getInstance();
   * endCal.set(2023, Calendar.DECEMBER, 31);
   *
   * String formattedDate = CRandomDateGenerator.getFormattedDate(startCal, endCal, "yyyy-MM-dd");
   * // Output: "2022-07-15" (example)
   * }</pre>
   */
  public static String getFormattedDate(Calendar from, Calendar to, String format) {
    return new CRandomDateGenerator().setFrom(from).setTo(to).getDate(format);
  }

  /**
   * Generates a formatted random date string within the specified Date range.
   *
   * @param from the start date of the range (inclusive)
   * @param to the end date of the range (inclusive)
   * @param format the date format string (e.g., "yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss")
   * @return a formatted random date string
   * @example
   *     <pre>{@code
   * Date startDate = new Date(120, 0, 1); // January 1, 2020
   * Date endDate = new Date(123, 11, 31); // December 31, 2023
   *
   * String formattedDate = CRandomDateGenerator.getFormattedDate(startDate, endDate, "MM/dd/yyyy");
   * // Output: "07/15/2022" (example)
   * }</pre>
   */
  public static String getFormattedDate(Date from, Date to, String format) {
    return new CRandomDateGenerator().setFrom(from).setTo(to).getDate(format);
  }

  /**
   * Generates a formatted random date string within the specified year range.
   *
   * @param fromYear the start year of the range (inclusive)
   * @param toYear the end year of the range (inclusive)
   * @param format the date format string (e.g., "yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss")
   * @return a formatted random date string
   * @example
   *     <pre>{@code
   * String formattedDate = CRandomDateGenerator.getFormattedDate(2020, 2023, "yyyy-MM-dd");
   * // Output: "2022-07-15" (example)
   *
   * // Different format
   * String customFormat = CRandomDateGenerator.getFormattedDate(2020, 2023, "dd/MM/yyyy HH:mm:ss");
   * // Output: "15/07/2022 14:32:18" (example)
   * }</pre>
   */
  public static String getFormattedDate(int fromYear, int toYear, String format) {
    return new CRandomDateGenerator().setFromYear(fromYear).setToYear(toYear).getDate(format);
  }

  /**
   * Sets the start Calendar for the date range.
   *
   * @param from the Calendar representing the start date of the range (inclusive)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * Calendar startCal = Calendar.getInstance();
   * startCal.set(2020, Calendar.JANUARY, 1);
   *
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFrom(startCal)
   *     .setToYear(2023)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFrom(Calendar from) {
    this.from = from;
    return this;
  }

  /**
   * Sets the start Date for the date range.
   *
   * @param from the Date representing the start date of the range (inclusive)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * Date startDate = new Date(120, 0, 1); // January 1, 2020
   *
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFrom(startDate)
   *     .setToYear(2023)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFrom(Date from) {
    this.from.setTime(from);
    return this;
  }

  /**
   * Sets the start year for the date range.
   *
   * @param amount the year to set as the start of the range
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setToYear(2023)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFromYear(int amount) {
    this.from.set(Calendar.YEAR, amount);
    return this;
  }

  /**
   * Sets the start month for the date range.
   *
   * @param amount the month to set as the start of the range (Calendar.JANUARY, Calendar.FEBRUARY,
   *     etc.)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setToYear(2023)
   *     .setToMonth(Calendar.NOVEMBER)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFromMonth(int amount) {
    this.from.set(Calendar.MONTH, amount);
    return this;
  }

  /**
   * Sets the start day of year for the date range.
   *
   * @param amount the day of year to set as the start of the range (1-366)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromDayOfYear(100) // April 9th in a leap year
   *     .setToYear(2020)
   *     .setToDayOfYear(200)   // July 18th in a leap year
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFromDayOfYear(int amount) {
    this.from.set(Calendar.DAY_OF_YEAR, amount);
    return this;
  }

  /**
   * Sets the start day of month for the date range.
   *
   * @param amount the day of month to set as the start of the range (1-31)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setToYear(2020)
   *     .setToMonth(Calendar.MARCH)
   *     .setToDayOfMonth(31)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFromDayOfMonth(int amount) {
    this.from.set(Calendar.DAY_OF_MONTH, amount);
    return this;
  }

  /**
   * Sets the start hour for the date range.
   *
   * @param amount the hour to set as the start of the range (0-23)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)  // 9:00 AM
   *     .setToHour(17)   // 5:00 PM
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFromHour(int amount) {
    this.from.set(Calendar.HOUR, amount);
    return this;
  }

  /**
   * Sets the start minute for the date range.
   *
   * @param amount the minute to set as the start of the range (0-59)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)
   *     .setFromMinute(30)  // 9:30 AM
   *     .setToMinute(45)    // 9:45 AM (same day/hour)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFromMinute(int amount) {
    this.from.set(Calendar.MINUTE, amount);
    return this;
  }

  /**
   * Sets the start second for the date range.
   *
   * @param amount the second to set as the start of the range (0-59)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)
   *     .setFromMinute(30)
   *     .setFromSecond(15)  // 9:30:15 AM
   *     .setToSecond(45)    // 9:30:45 AM (same day/hour/minute)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFromSecond(int amount) {
    this.from.set(Calendar.SECOND, amount);
    return this;
  }

  /**
   * Sets the start millisecond for the date range.
   *
   * @param amount the millisecond to set as the start of the range (0-999)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)
   *     .setFromMinute(30)
   *     .setFromSecond(15)
   *     .setFromMilliSecond(100)  // 9:30:15.100 AM
   *     .setToMilliSecond(500)    // 9:30:15.500 AM (same day/hour/minute/second)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setFromMilliSecond(int amount) {
    this.from.set(Calendar.MILLISECOND, amount);
    return this;
  }

  /**
   * Sets the end Calendar for the date range.
   *
   * @param to the Calendar representing the end date of the range (inclusive)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * Calendar endCal = Calendar.getInstance();
   * endCal.set(2023, Calendar.DECEMBER, 31);
   *
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setTo(endCal)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setTo(Calendar to) {
    this.to = to;
    return this;
  }

  /**
   * Sets the end Date for the date range.
   *
   * @param to the Date representing the end date of the range (inclusive)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * Date endDate = new Date(123, 11, 31); // December 31, 2023
   *
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setTo(endDate)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setTo(Date to) {
    this.to.setTime(to);
    return this;
  }

  /**
   * Sets the end year for the date range.
   *
   * @param amount the year to set as the end of the range
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setToYear(2023)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setToYear(int amount) {
    this.to.set(Calendar.YEAR, amount);
    return this;
  }

  /**
   * Sets the end month for the date range.
   *
   * @param amount the month to set as the end of the range (Calendar.JANUARY, Calendar.FEBRUARY,
   *     etc.)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setToYear(2023)
   *     .setToMonth(Calendar.NOVEMBER)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setToMonth(int amount) {
    this.to.set(Calendar.MONTH, amount);
    return this;
  }

  /**
   * Sets the end day of year for the date range.
   *
   * @param amount the day of year to set as the end of the range (1-366)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromDayOfYear(100) // April 9th in a leap year
   *     .setToYear(2020)
   *     .setToDayOfYear(200)   // July 18th in a leap year
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setToDayOfYear(int amount) {
    this.to.set(Calendar.DAY_OF_YEAR, amount);
    return this;
  }

  /**
   * Sets the end day of month for the date range.
   *
   * @param amount the day of month to set as the end of the range (1-31)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setToYear(2020)
   *     .setToMonth(Calendar.MARCH)
   *     .setToDayOfMonth(31)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setToDayOfMonth(int amount) {
    this.to.set(Calendar.DAY_OF_MONTH, amount);
    return this;
  }

  /**
   * Sets the end hour for the date range.
   *
   * @param amount the hour to set as the end of the range (0-23)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)  // 9:00 AM
   *     .setToHour(17)   // 5:00 PM
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setToHour(int amount) {
    this.to.set(Calendar.HOUR, amount);
    return this;
  }

  /**
   * Sets the end minute for the date range.
   *
   * @param amount the minute to set as the end of the range (0-59)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)
   *     .setFromMinute(30)  // 9:30 AM
   *     .setToMinute(45)    // 9:45 AM (same day/hour)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setToMinute(int amount) {
    this.to.set(Calendar.MINUTE, amount);
    return this;
  }

  /**
   * Sets the end second for the date range.
   *
   * @param amount the second to set as the end of the range (0-59)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)
   *     .setFromMinute(30)
   *     .setFromSecond(15)  // 9:30:15 AM
   *     .setToSecond(45)    // 9:30:45 AM (same day/hour/minute)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setToSecond(int amount) {
    this.to.set(Calendar.SECOND, amount);
    return this;
  }

  /**
   * Sets the end millisecond for the date range.
   *
   * @param amount the millisecond to set as the end of the range (0-999)
   * @return this CRandomDateGenerator instance for method chaining
   * @example
   *     <pre>{@code
   * CDate randomDate = CRandomDateGenerator.instance()
   *     .setFromYear(2020)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)
   *     .setFromMinute(30)
   *     .setFromSecond(15)
   *     .setFromMilliSecond(100)  // 9:30:15.100 AM
   *     .setToMilliSecond(500)    // 9:30:15.500 AM (same day/hour/minute/second)
   *     .getDate();
   * }</pre>
   */
  public CRandomDateGenerator setToMilliSecond(int amount) {
    this.to.set(Calendar.MILLISECOND, amount);
    return this;
  }

  /**
   * Generates a random CDate within the configured date range.
   *
   * @return a CDate object representing a random date within the specified range
   * @example
   *     <pre>{@code
   * // Simple usage with year range
   * CDate randomDate = CRandomDateGenerator.instance(2020, 2023).getDate();
   *
   * // Complex usage with precise range
   * CDate preciseDate = CRandomDateGenerator.instance()
   *     .setFromYear(2022)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)
   *     .setToYear(2022)
   *     .setToMonth(Calendar.MARCH)
   *     .setToDayOfMonth(20)
   *     .setToHour(17)
   *     .getDate();
   *
   * System.out.println("Random date: " + randomDate);
   * }</pre>
   */
  public CDate getDate() {
    long fTime = from.getTime().getTime();
    long tTime = to.getTime().getTime();
    return new CDate(fTime + ((tTime - fTime) * (new SecureRandom().nextInt(8) + 1) / 10));
  }

  /**
   * Generates a formatted random date string within the configured date range.
   *
   * @param format the date format string (e.g., "yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss")
   * @return a formatted string representation of a random date within the specified range
   * @example
   *     <pre>{@code
   * // Generate formatted date with specific format
   * String isoDate = CRandomDateGenerator.instance(2020, 2023).getDate("yyyy-MM-dd");
   * // Output: "2022-07-15" (example)
   *
   * String customFormat = CRandomDateGenerator.instance(2020, 2023).getDate("EEEE, MMMM dd, yyyy 'at' HH:mm:ss");
   * // Output: "Friday, July 15, 2022 at 14:32:18" (example)
   *
   * // Using with precise range
   * String timeRange = CRandomDateGenerator.instance()
   *     .setFromYear(2022)
   *     .setFromMonth(Calendar.MARCH)
   *     .setFromDayOfMonth(15)
   *     .setFromHour(9)
   *     .setToHour(17)
   *     .getDate("HH:mm:ss");
   * // Output: "14:32:18" (example - between 9 AM and 5 PM)
   * }</pre>
   */
  public String getDate(String format) {
    return getDate().toFormat(format);
  }
}
