package org.catools.common.date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.catools.common.extensions.types.interfaces.CDynamicDateExtension;
import org.catools.common.utils.CDateUtil;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.IsoEra;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.catools.common.configs.CDateConfigs.getDefaultTimeZone;

/**
 * A Wrapper to make our life easier with date related operations which is usually does through
 * DateUtil.
 */
public class CDate extends Date implements CDynamicDateExtension {
  private static final String DATE_ONLY_FORMAT_STRING = "MM/dd/yyyy";
  private static final String TIME_FORMAT = "HH:mm:ss";

  private static final String LONG_DATE_FORMAT_STRING_MILI_SECONDS = "yyyy-MMM-dd HH:mm:ss:SSS";
  private static final String FILENAME_TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss_SSS";

  /**
   * Creates a new CDate object representing the current date and time.
   * 
   * @example
   * <pre>
   * CDate date = new CDate();
   * System.out.println(date); // Current date and time
   * </pre>
   */
  public CDate() {
    super();
  }

  /**
   * Creates a new CDate object from the specified time in milliseconds since January 1, 1970, 00:00:00 GMT.
   * 
   * @param date the time in milliseconds since the epoch
   * 
   * @example
   * <pre>
   * long timestamp = System.currentTimeMillis();
   * CDate date = new CDate(timestamp);
   * System.out.println(date); // Date corresponding to the timestamp
   * </pre>
   */
  public CDate(long date) {
    super(date);
  }

  /**
   * Creates a new CDate object by parsing the given date string using the specified format.
   * 
   * @param date the date string to parse
   * @param format the format pattern to use for parsing
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-25", "yyyy-MM-dd");
   * System.out.println(date); // December 25, 2023
   * 
   * CDate dateTime = new CDate("25/12/2023 14:30:00", "dd/MM/yyyy HH:mm:ss");
   * System.out.println(dateTime); // December 25, 2023 2:30 PM
   * </pre>
   */
  public CDate(String date, String format) {
    if (StringUtils.isNotBlank(date)) {
      valueOf(date, format);
    }
  }

  /**
   * Creates a new CDate object from an existing Date object.
   * 
   * @param date the Date object to copy
   * 
   * @example
   * <pre>
   * Date javaDate = new Date();
   * CDate cDate = new CDate(javaDate);
   * System.out.println(cDate); // Same date and time as javaDate
   * </pre>
   */
  public CDate(Date date) {
    super(date.getTime());
  }

  /**
   * Returns a CDate representing the current date and time in the default time zone.
   * 
   * @return a new CDate instance representing the current moment
   * 
   * @example
   * <pre>
   * CDate now = CDate.now();
   * System.out.println("Current time: " + now);
   * </pre>
   */
  public static CDate now() {
    return now(getDefaultTimeZone());
  }

  /**
   * Returns a CDate representing the current date and time in the specified time zone.
   * 
   * @param timeZone the time zone to use
   * @return a new CDate instance representing the current moment in the specified time zone
   * 
   * @example
   * <pre>
   * TimeZone utc = TimeZone.getTimeZone("UTC");
   * CDate utcNow = CDate.now(utc);
   * System.out.println("Current UTC time: " + utcNow);
   * 
   * TimeZone tokyo = TimeZone.getTimeZone("Asia/Tokyo");
   * CDate tokyoNow = CDate.now(tokyo);
   * System.out.println("Current Tokyo time: " + tokyoNow);
   * </pre>
   */
  public static CDate now(TimeZone timeZone) {
    return new CDate(Calendar.getInstance(timeZone).getTime());
  }

  /**
   * Parses a string representing a date by trying a variety of different parsers, using the default
   * date format symbols for the given locale..
   *
   * <p>The parse will try each parse pattern in turn. A parse is only deemed successful if it
   * parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * The parser parses strictly - it does not allow for dates such as "February 942, 1996".
   *
   * @param date the date, not null
   * @return the parsed date
   */
  public static CDate of(final Date date) {
    return new CDate(date);
  }

  /**
   * Parses a string representing a date by trying a variety of different parsers, using the default
   * date format symbols for the given locale..
   *
   * <p>The parse will try each parse pattern in turn. A parse is only deemed successful if it
   * parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * The parser parses strictly - it does not allow for dates such as "February 942, 1996".
   *
   * @param date the date, not null
   * @return the parsed date
   */
  public static CDate valueOf(final Date date) {
    return of(date);
  }

  /**
   * Parses a string representing a date by trying a variety of different parsers, using the default
   * date format symbols for the given locale..
   *
   * <p>The parse will try each parse pattern in turn. A parse is only deemed successful if it
   * parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * The parser parses strictly - it does not allow for dates such as "February 942, 1996".
   *
   * @param str           the date to parse, not null
   * @param parsePatterns the date format patterns to use, see SimpleDateFormat, not null
   * @return the parsed date
   */
  public static CDate of(final String str, final String... parsePatterns) {
    return new CDate(CDateUtil.valueOf(str, parsePatterns));
  }

  /**
   * Parses a string representing a date by trying a variety of different parsers, using the default
   * date format symbols for the given locale..
   *
   * <p>The parse will try each parse pattern in turn. A parse is only deemed successful if it
   * parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * The parser parses strictly - it does not allow for dates such as "February 942, 1996".
   *
   * @param str           the date to parse, not null
   * @param parsePatterns the date format patterns to use, see SimpleDateFormat, not null
   * @return the parsed date
   */
  public static CDate valueOf(final String str, final String... parsePatterns) {
    return of(str, parsePatterns);
  }

  /**
   * Parses a string representing a date by trying a variety of different parsers, using the default
   * date format symbols for the given locale..
   *
   * <p>The parse will try each parse pattern in turn. A parse is only deemed successful if it
   * parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * The parser parses strictly - it does not allow for dates such as "February 942, 1996".
   *
   * @param str           the date to parse, not null
   * @param locale        the locale whose date format symbols should be used. If <code>null</code>, the
   *                      system locale is used (as per {@see #valueOf(String, String...)}).
   * @param parsePatterns the date format patterns to use, see SimpleDateFormat, not null
   * @return the parsed date
   */
  public static CDate of(final String str, final Locale locale, final String... parsePatterns) {
    return new CDate(CDateUtil.valueOf(str, locale, parsePatterns));
  }

  /**
   * Parses a string representing a date by trying a variety of different parsers, using the default
   * date format symbols for the given locale..
   *
   * <p>The parse will try each parse pattern in turn. A parse is only deemed successful if it
   * parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * The parser parses strictly - it does not allow for dates such as "February 942, 1996".
   *
   * @param str           the date to parse, not null
   * @param locale        the locale whose date format symbols should be used. If <code>null</code>, the
   *                      system locale is used (as per {@see #valueOf(String, String...)}).
   * @param parsePatterns the date format patterns to use, see SimpleDateFormat, not null
   * @return the parsed date
   */
  public static CDate valueOf(final String str, final Locale locale, final String... parsePatterns) {
    return of(CDateUtil.valueOf(str, locale, parsePatterns));
  }

  /**
   * Parses a string representing a date by trying a variety of different parsers, using the default
   * date format symbols for the given locale..
   *
   * <p>The parse will try each parse pattern in turn. A parse is only deemed successful if it
   * parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * The parser parses strictly - it does not allow for dates such as "February 942, 1996".
   *
   * @param str           the date to parse, not null
   * @param parsePatterns the date format patterns to use, see SimpleDateFormat, not null
   * @return the parsed date or null if could not parse
   */
  public static CDate valueOfOrNull(final String str, final String... parsePatterns) {
    Date date = CDateUtil.valueOfOrNull(str, parsePatterns);
    return date == null ? null : of(date);
  }

  /**
   * Parses a string representing a date by trying a variety of different parsers, using the default
   * date format symbols for the given locale..
   *
   * <p>The parse will try each parse pattern in turn. A parse is only deemed successful if it
   * parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * The parser parses strictly - it does not allow for dates such as "February 942, 1996".
   *
   * @param str           the date to parse, not null
   * @param locale        the locale whose date format symbols should be used. If <code>null</code>, the
   *                      system locale is used (as per {@see #valueOf(String, String...)}).
   * @param parsePatterns the date format patterns to use, see SimpleDateFormat, not null
   * @return the parsed date or null if could not parse
   */
  public static CDate valueOfOrNull(final String str, final Locale locale, final String... parsePatterns) {
    Date date = CDateUtil.valueOfOrNull(str, locale, parsePatterns);
    return date == null ? null : of(date);
  }

  @JsonIgnore
  private static String getFormattedDuration(Duration duration) {
    String time = String.format("%02d:%02d:%02d:%03d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart(), duration.toMillisPart()).replaceAll("\\s+", "0");

    if (duration.toDaysPart() > 0) {
      return String.format("%dd %s", duration.toDaysPart(), time);
    }
    return time;
  }

  /**
   * Adds a number of days to a date returning a new object.
   *
   * @param amount the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate addDays(final int amount) {
    return add(Calendar.DAY_OF_YEAR, amount);
  }

  /**
   * Adds a number of hours to a date returning a new object.
   *
   * @param amount the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate addHours(final int amount) {
    return add(Calendar.HOUR_OF_DAY, amount);
  }

  /**
   * Adds a number of milliseconds to a date returning a new object.
   *
   * @param amount the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate addMilliseconds(final int amount) {
    return add(Calendar.MILLISECOND, amount);
  }

  /**
   * Adds a number of minutes to a date returning a new object.
   *
   * @param amount the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate addMinutes(final int amount) {
    return add(Calendar.MINUTE, amount);
  }

  /**
   * Adds a number of months to a date returning a new object.
   *
   * @param amount the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate addMonths(final int amount) {
    return add(Calendar.MONTH, amount);
  }

  /**
   * Adds a number of seconds to a date returning a new object.
   *
   * @param amount the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate addSeconds(final int amount) {
    return add(Calendar.SECOND, amount);
  }

  /**
   * Adds a number of weeks to a date returning a new object.
   *
   * @param amount the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate addWeeks(final int amount) {
    return add(Calendar.WEEK_OF_YEAR, amount);
  }

  /**
   * Adds a number of years to a date returning a new object.
   *
   * @param amount the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate addYears(final int amount) {
    return add(Calendar.YEAR, amount);
  }

  /**
   * Converts a LocalDate to a CDate instance.
   * 
   * @return a LocalDate representation of this CDate
   * 
   * @example
   * <pre>
   * CDate date = CDate.now();
   * LocalDate localDate = date.asLocalDate();
   * System.out.println("Local date: " + localDate); // 2023-12-25
   * </pre>
   */
  public LocalDate asLocalDate() {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  /**
   * Gets a date ceiling, leaving the field specified as the most significant field.
   *
   * <p>For example, if you had the date-time of 28 Mar 2002 13:45:01.231, if you passed with HOUR,
   * it would return 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it would return 1 Apr
   * 2002 0:00:00.000.
   *
   * @param field the field from {@code Calendar} or <code>SEMI_MONTH</code>
   * @return the different ceil date, not null
   * @throws IllegalArgumentException if the date is <code>null</code>
   * @throws ArithmeticException      if the year is over 280 million
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-03-28 13:45:01.231", "yyyy-MM-dd HH:mm:ss.SSS");
   * 
   * CDate ceilHour = date.clone().ceiling(Calendar.HOUR_OF_DAY);
   * System.out.println(ceilHour); // 2023-03-28 14:00:00.000
   * 
   * CDate ceilMonth = date.clone().ceiling(Calendar.MONTH);
   * System.out.println(ceilMonth); // 2023-04-01 00:00:00.000
   * </pre>
   */
  public CDate ceiling(final int field) {
    setTime(DateUtils.ceiling(this, field).getTime());
    return this;
  }

  /**
   * Create a deep copy of CDate object
   *
   * @return CDate with the time set to value of getTime()
   * 
   * @example
   * <pre>
   * CDate original = CDate.now();
   * CDate copy = original.clone();
   * 
   * // Modify the copy without affecting the original
   * copy.addDays(5);
   * System.out.println("Original: " + original);
   * System.out.println("Copy: " + copy);
   * </pre>
   */
  public CDate clone() {
    return new CDate(getTime());
  }

  /**
   * Compare value of given date against current object using the particular date format. To do so,
   * we convert date values to formatted string using provided format and then compare then using
   * string compression.
   *
   * @param date   date to compare current value against
   * @param format the format which should be used during compression.
   * @return the value {@code 0} if the argument Date is equal to this Date; a value less than
   * {@code 0} if this Date is before the Date argument; and a value greater than
   * 
   * @example
   * <pre>
   * CDate date1 = new CDate("2023-12-25 10:30:45", "yyyy-MM-dd HH:mm:ss");
   * CDate date2 = new CDate("2023-12-25 15:45:30", "yyyy-MM-dd HH:mm:ss");
   * 
   * // Compare only the date part (ignoring time)
   * int result = date1.compareDateByFormat(date2, "yyyy-MM-dd");
   * System.out.println(result); // 0 (same date)
   * 
   * // Compare with time included
   * int resultWithTime = date1.compareDateByFormat(date2, "yyyy-MM-dd HH:mm:ss");
   * System.out.println(resultWithTime); // negative value (date1 is before date2)
   * </pre>
   */
  public int compareDateByFormat(Date date, String format) {
    return CDateUtil.compareDateByFormat(this, date, format);
  }

  /**
   * Compare value of given date against current object using MM/dd/yyyy date format. To do so, we
   * convert date values to formatted string using MM/dd/yyyy format and then compare then using
   * string compression.
   *
   * @param date date to compare current value against
   * @return the value {@code 0} if the values are equal; the value less than {@code 0} if the
   * string value is less and greater than {@code 0} if it is bigger
   * @see #compareDateByFormat(Date, String)
   * @see #compareTimePortion(Date)
   * 
   * @example
   * <pre>
   * CDate date1 = new CDate("2023-12-25 10:30:45", "yyyy-MM-dd HH:mm:ss");
   * CDate date2 = new CDate("2023-12-25 23:59:59", "yyyy-MM-dd HH:mm:ss");
   * CDate date3 = new CDate("2023-12-26 01:00:00", "yyyy-MM-dd HH:mm:ss");
   * 
   * int result1 = date1.compareDatePortion(date2);
   * System.out.println(result1); // 0 (same date, different times)
   * 
   * int result2 = date1.compareDatePortion(date3);
   * System.out.println(result2); // negative value (date1 is before date3)
   * </pre>
   */
  public int compareDatePortion(Date date) {
    return compareDateByFormat(date, DATE_ONLY_FORMAT_STRING);
  }

  /**
   * Compare value of given date against current object using HH:mm:ss date format. To do so, we
   * convert date values to formatted string using HH:mm:ss format and then compare then using
   * string compression.
   *
   * @param date date to compare current value against
   * @return the value {@code 0} if the values are equal; the value less than {@code 0} if the
   * string value is less and greater than {@code 0} if it is bigger
   * @see #compareDateByFormat(Date, String)
   * @see #compareDatePortion(Date)
   * 
   * @example
   * <pre>
   * CDate date1 = new CDate("2023-12-25 10:30:45", "yyyy-MM-dd HH:mm:ss");
   * CDate date2 = new CDate("2023-12-26 10:30:45", "yyyy-MM-dd HH:mm:ss");
   * CDate date3 = new CDate("2023-12-25 15:45:30", "yyyy-MM-dd HH:mm:ss");
   * 
   * int result1 = date1.compareTimePortion(date2);
   * System.out.println(result1); // 0 (same time, different dates)
   * 
   * int result2 = date1.compareTimePortion(date3);
   * System.out.println(result2); // negative value (10:30:45 is before 15:45:30)
   * </pre>
   */
  public int compareTimePortion(Date date) {
    return compareDateByFormat(date, TIME_FORMAT);
  }

  /**
   * Returns the value of the given calendar field. In lenient mode, all calendar fields are
   * normalized. In non-lenient mode, all calendar fields are validated and this method throws an
   * exception if any calendar fields have out-of-range values.
   *
   * @param field the given calendar field.
   * @return the value for the given calendar field.
   * @throws ArrayIndexOutOfBoundsException if the specified field is out of range (<code>
   *                                        field &lt; 0 || field &gt;= FIELD_COUNT</code>).
   * @see #set(int, int)
   */
  @JsonIgnore
  public int get(int field) {
    return toCalendar().get(field);
  }

  /**
   * Gets the day-of-month field.
   *
   * <p>This method returns the primitive {@code int} value for the day-of-month.
   *
   * @return the day-of-month, from 1 to 31
   */
  @JsonIgnore
  public int getDayOfMonth() {
    return get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Gets the day-of-week field.
   *
   * <p>This method returns the primitive {@code int} value for the day-of-week.
   *
   * @return the day-of-week, from 0 to 7
   */
  @JsonIgnore
  public int getDayOfWeek() {
    return get(Calendar.DAY_OF_WEEK);
  }

  /**
   * Gets the day-of-year field.
   *
   * <p>This method returns the primitive {@code int} value for the day-of-year.
   *
   * @return the day-of-year, from 1 to 365, or 366 in a leap year
   */
  @JsonIgnore
  public int getDayOfYear() {
    return get(Calendar.DAY_OF_YEAR);
  }

  /**
   * Gets the duration from the specified date to this date.
   * 
   * @param date the starting date
   * @return the duration from the specified date to this date
   * 
   * @example
   * <pre>
   * CDate startDate = new CDate("2023-12-25 10:00:00", "yyyy-MM-dd HH:mm:ss");
   * CDate endDate = new CDate("2023-12-25 15:30:45", "yyyy-MM-dd HH:mm:ss");
   * 
   * Duration duration = endDate.getDurationFrom(startDate);
   * System.out.println("Duration: " + duration.toHours() + " hours, " + 
   *                    duration.toMinutesPart() + " minutes");
   * // Output: Duration: 5 hours, 30 minutes
   * </pre>
   */
  @JsonIgnore
  public Duration getDurationFrom(Date date) {
    return CDateUtil.getDuration(date, this);
  }

  /**
   * Gets the duration from now to this date.
   * 
   * @return the duration from the current time to this date
   * 
   * @example
   * <pre>
   * CDate futureDate = CDate.now().addHours(3);
   * Duration duration = futureDate.getDurationFromNow();
   * System.out.println("Time until future date: " + duration.toHours() + " hours");
   * // Output: Time until future date: 3 hours
   * </pre>
   */
  @JsonIgnore
  public Duration getDurationFromNow() {
    return CDateUtil.getDuration(now(), this);
  }

  /**
   * Gets the duration from this date to the specified date.
   * 
   * @param date the ending date
   * @return the duration from this date to the specified date
   * 
   * @example
   * <pre>
   * CDate startDate = new CDate("2023-12-25 10:00:00", "yyyy-MM-dd HH:mm:ss");
   * CDate endDate = new CDate("2023-12-27 14:30:00", "yyyy-MM-dd HH:mm:ss");
   * 
   * Duration duration = startDate.getDurationTo(endDate);
   * System.out.println("Duration: " + duration.toDays() + " days, " + 
   *                    duration.toHoursPart() + " hours");
   * // Output: Duration: 2 days, 4 hours
   * </pre>
   */
  @JsonIgnore
  public Duration getDurationTo(Date date) {
    return CDateUtil.getDuration(this, date);
  }

  /**
   * Gets the duration from this date to the current time.
   * 
   * @return the duration from this date to now
   * 
   * @example
   * <pre>
   * CDate pastDate = CDate.now().addHours(-2);
   * Duration duration = pastDate.getDurationToNow();
   * System.out.println("Time since past date: " + duration.toHours() + " hours");
   * // Output: Time since past date: 2 hours
   * </pre>
   */
  @JsonIgnore
  public Duration getDurationToNow() {
    return CDateUtil.getDuration(this, now());
  }

  /**
   * Gets the era applicable at this date.
   *
   * <p>The official ISO-8601 standard does not define eras, however {@code IsoChronology} does. It
   * defines two eras, 'CE' from year one onwards and 'BCE' from year zero backwards. Since dates
   * before the Julian-Gregorian cutover are not in line with history, the cutover betweenExclusive
   * 'BCE' and 'CE' is also not aligned with the commonly used eras, often referred to using 'BC'
   * and 'AD'.
   *
   * <p>Users of this class should typically ignore this method as it exists primarily to fulfill
   * the {@link ChronoLocalDate} contract where it is necessary to support the Japanese calendar
   * system.
   *
   * @return the IsoEra applicable at this date, not null
   */
  @JsonIgnore
  public IsoEra getEra() {
    return toLocalTime().getEra();
  }

  // -----------------------------------------------------------------------

  /**
   * Returns the number of days within the fragment. All datefields greater than the fragment will
   * be ignored.
   *
   * <p>Asking the days of any date will only return the number of days of the current month
   * (resulting in a number betweenExclusive 1 and 31). This method will retrieve the number of days
   * for any fragment. For example, if you want to calculate the number of days past this year, your
   * fragment is Calendar.YEAR. The result will be all days of the past month(s).
   *
   * <p>Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and
   * Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND
   * A fragment less than or equal to a DAY field will return 0.
   *
   * <ul>
   *   <li>January 28, 2008 with Calendar.MONTH as fragment will return 28 (equivalent to deprecated
   *       date.getDay())
   *   <li>February 28, 2008 with Calendar.MONTH as fragment will return 28 (equivalent to
   *       deprecated date.getDay())
   *   <li>January 28, 2008 with Calendar.YEAR as fragment will return 28
   *   <li>February 28, 2008 with Calendar.YEAR as fragment will return 59
   *   <li>January 28, 2008 with Calendar.MILLISECOND as fragment will return 0 (a millisecond
   *       cannot be split in days)
   * </ul>
   *
   * @param fragment the {@code Calendar} field part of date to calculate
   * @return number of days within the fragment of date
   * @throws IllegalArgumentException if the date is <code>null</code> or fragment is not supported
   */
  @JsonIgnore
  public long getFragmentInDays(final int fragment) {
    return DateUtils.getFragmentInDays(this, fragment);
  }

  /**
   * Returns the number of hours within the fragment. All datefields greater than the fragment will
   * be ignored.
   *
   * <p>Asking the hours of any date will only return the number of hours of the current day
   * (resulting in a number betweenExclusive 0 and 23). This method will retrieve the number of
   * hours for any fragment. For example, if you want to calculate the number of hours past this
   * month, your fragment is Calendar.MONTH. The result will be all hours of the past day(s).
   *
   * <p>Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and
   * Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND
   * A fragment less than or equal to a HOUR field will return 0.
   *
   * <ul>
   *   <li>January 1, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7
   *       (equivalent to deprecated date.getHours())
   *   <li>January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7
   *       (equivalent to deprecated date.getHours())
   *   <li>January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 7
   *   <li>January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 127 (5*24 + 7)
   *   <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a
   *       millisecond cannot be split in hours)
   * </ul>
   *
   * @param fragment the {@code Calendar} field part of date to calculate
   * @return number of hours within the fragment of date
   * @throws IllegalArgumentException if the date is <code>null</code> or fragment is not supported
   */
  @JsonIgnore
  public long getFragmentInHours(final int fragment) {
    return DateUtils.getFragmentInHours(this, fragment);
  }

  /**
   * Returns the number of milliseconds within the fragment. All datefields greater than the
   * fragment will be ignored.
   *
   * <p>Asking the milliseconds of any date will only return the number of milliseconds of the
   * current second (resulting in a number betweenExclusive 0 and 999). This method will retrieve
   * the number of milliseconds for any fragment. For example, if you want to calculate the number
   * of milliseconds past today, your fragment is Calendar.DATE or Calendar.DAY_OF_YEAR. The result
   * will be all milliseconds of the past hour(s), minutes(s) and second(s).
   *
   * <p>Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and
   * Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND
   * A fragment less than or equal to a SECOND field will return 0.
   *
   * <ul>
   *   <li>January 1, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538
   *   <li>January 6, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538
   *   <li>January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10538 (10*1000 +
   *       538)
   *   <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a
   *       millisecond cannot be split in milliseconds)
   * </ul>
   *
   * @param fragment the {@code Calendar} field part of date to calculate
   * @return number of milliseconds within the fragment of date
   * @throws IllegalArgumentException if the date is <code>null</code> or fragment is not supported
   */
  @JsonIgnore
  public long getFragmentInMilliseconds(final int fragment) {
    return DateUtils.getFragmentInMilliseconds(this, fragment);
  }

  /**
   * Returns the number of minutes within the fragment. All datefields greater than the fragment
   * will be ignored.
   *
   * <p>Asking the minutes of any date will only return the number of minutes of the current hour
   * (resulting in a number betweenExclusive 0 and 59). This method will retrieve the number of
   * minutes for any fragment. For example, if you want to calculate the number of minutes past this
   * month, your fragment is Calendar.MONTH. The result will be all minutes of the past day(s) and
   * hour(s).
   *
   * <p>Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and
   * Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND
   * A fragment less than or equal to a MINUTE field will return 0.
   *
   * <ul>
   *   <li>January 1, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15
   *       (equivalent to deprecated date.getMinutes())
   *   <li>January 6, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15
   *       (equivalent to deprecated date.getMinutes())
   *   <li>January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 15
   *   <li>January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 435 (7*60 + 15)
   *   <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a
   *       millisecond cannot be split in minutes)
   * </ul>
   *
   * @param fragment the {@code Calendar} field part of date to calculate
   * @return number of minutes within the fragment of date
   * @throws IllegalArgumentException if the date is <code>null</code> or fragment is not supported
   */
  @JsonIgnore
  public long getFragmentInMinutes(final int fragment) {
    return DateUtils.getFragmentInMinutes(this, fragment);
  }

  /**
   * Returns the number of seconds within the fragment. All datefields greater than the fragment
   * will be ignored.
   *
   * <p>Asking the seconds of any date will only return the number of seconds of the current minute
   * (resulting in a number betweenExclusive 0 and 59). This method will retrieve the number of
   * seconds for any fragment. For example, if you want to calculate the number of seconds past
   * today, your fragment is Calendar.DATE or Calendar.DAY_OF_YEAR. The result will be all seconds
   * of the past hour(s) and minutes(s).
   *
   * <p>Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and
   * Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND
   * A fragment less than or equal to a SECOND field will return 0.
   *
   * <ul>
   *   <li>January 1, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10 (equivalent
   *       to deprecated date.getSeconds())
   *   <li>January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10 (equivalent
   *       to deprecated date.getSeconds())
   *   <li>January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 26110
   *       (7*3600 + 15*60 + 10)
   *   <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a
   *       millisecond cannot be split in seconds)
   * </ul>
   *
   * @param fragment the {@code Calendar} field part of date to calculate
   * @return number of seconds within the fragment of date
   * @throws IllegalArgumentException if the date is <code>null</code> or fragment is not supported
   */
  @JsonIgnore
  public long getFragmentInSeconds(final int fragment) {
    return DateUtils.getFragmentInSeconds(this, fragment);
  }

  /**
   * Gets the month-of-year field using the {@code Month} enum.
   *
   * <p>This method returns the enum {@link Month} for the month. This avoids confusion as to what
   * {@code int} values mean. If you need access to the primitive {@code int} value then the enum
   * provides the {@link Month#getValue() int value}.
   *
   * @return the month-of-year, not null
   */
  @JsonIgnore
  public Month getMonthName() {
    return Month.of(get(Calendar.MONTH) + 1);
  }

  /**
   * Gets the timestamp as a SQL Timestamp object.
   * 
   * @return the timestamp representation of this date
   * 
   * @example
   * <pre>
   * CDate date = CDate.now();
   * Timestamp timestamp = date.getTimeStamp();
   * System.out.println("SQL Timestamp: " + timestamp);
   * // Output: SQL Timestamp: 2023-12-25 10:30:45.123
   * </pre>
   */
  @JsonIgnore
  public Timestamp getTimeStamp() {
    return new Timestamp(getTime());
  }

  /**
   * Checks if the year is a leap year, according to the ISO proleptic calendar system rules.
   *
   * <p>This method applies the current rules for leap years across the whole time-line. In general,
   * a year is a leap year if it is divisible by four without remainder. However, years divisible by
   * 100, are not leap years, with the exception of years divisible by 400 which are.
   *
   * <p>For example, 1904 is a leap year it is divisible by 4. 1900 was not a leap year as it is
   * divisible by 100, however 2000 was a leap year as it is divisible by 400.
   *
   * <p>The calculation is proleptic - applying the same rules into the far future and far past.
   * This is historically inaccurate, but is correct for the ISO-8601 standard.
   *
   * @return true if the year is leap, false otherwise
   */
  @JsonIgnore
  public boolean isLeapYear() {
    return toLocalTime().isLeapYear();
  }

  /**
   * Checks if two date objects are on the same day ignoring time.
   *
   * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002 13:45 and 12 Mar 2002
   * 13:45 would return false.
   *
   * @param date the date, not altered, not null
   * @return true if they represent the same day
   * @throws IllegalArgumentException if date is <code>null</code>
   */
  @JsonIgnore
  public boolean isSameDay(final Date date) {
    return DateUtils.isSameDay(this, date);
  }

  /**
   * Checks if two date objects represent the same instant in time.
   *
   * <p>This method compares the long millisecond time of the two objects.
   *
   * @param date the date, not altered, not null
   * @return true if they represent the same millisecond instant
   * @throws IllegalArgumentException if date is <code>null</code>
   */
  @JsonIgnore
  public boolean isSameInstant(final Date date) {
    return DateUtils.isSameInstant(this, date);
  }

  /**
   * Checks if two calendar objects represent the same local time.
   *
   * <p>This method compares the values of the fields of the two objects. In addition, both
   * calendars must be the same of the same type.
   *
   * @param date the calendar, not altered, not null
   * @return true if they represent the same millisecond instant
   * @throws IllegalArgumentException if date is <code>null</code>
   */
  @JsonIgnore
  public boolean isSameLocalTime(final Date date) {
    Calendar instance2 = Calendar.getInstance();
    instance2.setTime(date);
    return DateUtils.isSameLocalTime(toCalendar(), instance2);
  }

  /**
   * Checks if this date represents today's date (ignoring time).
   * 
   * @return true if this date is today, false otherwise
   * 
   * @example
   * <pre>
   * CDate today = CDate.now();
   * CDate yesterday = CDate.now().addDays(-1);
   * CDate todayDifferentTime = new CDate(today.getTime() + 3600000); // +1 hour
   * 
   * System.out.println(today.isTodayDate()); // true
   * System.out.println(yesterday.isTodayDate()); // false
   * System.out.println(todayDifferentTime.isTodayDate()); // true (same date, different time)
   * </pre>
   */
  @JsonIgnore
  public boolean isTodayDate() {
    return compareDatePortion(new CDate()) == 0;
  }

  /**
   * Checks if this date falls on a Monday.
   * 
   * @return true if this date is a Monday, false otherwise
   * 
   * @example
   * <pre>
   * CDate monday = new CDate("2023-12-25", "yyyy-MM-dd"); // Assuming this is a Monday
   * CDate tuesday = monday.clone().addDays(1);
   * 
   * System.out.println(monday.isMonday()); // true
   * System.out.println(tuesday.isMonday()); // false
   * </pre>
   */
  @JsonIgnore
  public boolean isMonday() {
    return getDayOfWeek() == Calendar.MONDAY;
  }

  /**
   * Checks if this date falls on a Saturday.
   * 
   * @return true if this date is a Saturday, false otherwise
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-23", "yyyy-MM-dd"); // Assuming this is a Saturday
   * System.out.println(date.isSaturday()); // true
   * System.out.println(date.isSunday()); // false
   * </pre>
   */
  @JsonIgnore
  public boolean isSaturday() {
    return getDayOfWeek() == Calendar.SATURDAY;
  }

  /**
   * Checks if this date falls on a Sunday.
   * 
   * @return true if this date is a Sunday, false otherwise
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-24", "yyyy-MM-dd"); // Assuming this is a Sunday
   * System.out.println(date.isSunday()); // true
   * System.out.println(date.isMonday()); // false
   * </pre>
   */
  @JsonIgnore
  public boolean isSunday() {
    return getDayOfWeek() == Calendar.SUNDAY;
  }

  /**
   * Checks if this date falls on a Thursday.
   * 
   * @return true if this date is a Thursday, false otherwise
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-28", "yyyy-MM-dd"); // Assuming this is a Thursday
   * System.out.println(date.isThursday()); // true
   * System.out.println("Is weekend: " + (date.isSaturday() || date.isSunday())); // false  
   * </pre>
   */
  @JsonIgnore
  public boolean isThursday() {
    return getDayOfWeek() == Calendar.THURSDAY;
  }

  /**
   * Checks if this date falls on a Tuesday.
   * 
   * @return true if this date is a Tuesday, false otherwise
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-26", "yyyy-MM-dd"); // Assuming this is a Tuesday
   * System.out.println(date.isTuesday()); // true
   * System.out.println(date.isWednesday()); // false
   * </pre>
   */
  @JsonIgnore
  public boolean isTuesday() {
    return getDayOfWeek() == Calendar.TUESDAY;
  }

  /**
   * Checks if this date falls on a Wednesday.
   * 
   * @return true if this date is a Wednesday, false otherwise
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-27", "yyyy-MM-dd"); // Assuming this is a Wednesday
   * System.out.println(date.isWednesday()); // true
   * System.out.println("Mid-week: " + date.isWednesday()); // true
   * </pre>
   */
  @JsonIgnore
  public boolean isWednesday() {
    return getDayOfWeek() == Calendar.WEDNESDAY;
  }

  /**
   * Checks if this date falls on a Friday.
   * 
   * @return true if this date is a Friday, false otherwise
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-29", "yyyy-MM-dd"); // Assuming this is a Friday
   * System.out.println(date.isFriday()); // true
   * System.out.println("TGIF: " + date.isFriday()); // true
   * </pre>
   */
  @JsonIgnore
  public boolean isFriday() {
    return getDayOfWeek() == Calendar.FRIDAY;
  }

  /**
   * Returns the length of the month represented by this date.
   *
   * <p>This returns the length of the month in days. For example, a date in January would return
   * 31.
   *
   * @return the length of the month in days
   * 
   * @example
   * <pre>
   * CDate january = new CDate("2023-01-15", "yyyy-MM-dd");
   * CDate february = new CDate("2023-02-15", "yyyy-MM-dd");
   * CDate februaryLeap = new CDate("2024-02-15", "yyyy-MM-dd");
   * 
   * System.out.println("January days: " + january.lengthOfMonth()); // 31
   * System.out.println("February 2023 days: " + february.lengthOfMonth()); // 28
   * System.out.println("February 2024 days: " + februaryLeap.lengthOfMonth()); // 29 (leap year)
   * </pre>
   */
  public int lengthOfMonth() {
    return toLocalTime().lengthOfMonth();
  }

  /**
   * Returns the length of the year represented by this date.
   *
   * <p>This returns the length of the year in days, either 365 or 366.
   *
   * @return 366 if the year is leap, 365 otherwise
   * 
   * @example
   * <pre>
   * CDate regularYear = new CDate("2023-06-15", "yyyy-MM-dd");
   * CDate leapYear = new CDate("2024-06-15", "yyyy-MM-dd");
   * 
   * System.out.println("2023 length: " + regularYear.lengthOfYear()); // 365
   * System.out.println("2024 length: " + leapYear.lengthOfYear()); // 366
   * System.out.println("2024 is leap: " + leapYear.isLeapYear()); // true
   * </pre>
   */
  public int lengthOfYear() {
    return toLocalTime().lengthOfYear();
  }

  /**
   * Rounds a date, leaving the field specified as the most significant field.
   *
   * <p>For example, if you had the date-time of 28 Mar 2002 13:45:01.231, if this was passed with
   * HOUR, it would return 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it would return
   * 1 April 2002 0:00:00.000.
   *
   * <p>For a date in a timezone that handles the change to daylight saving time, rounding to
   * Calendar.HOUR_OF_DAY will behave as follows. Suppose daylight saving time begins at 02:00 on
   * March 30. Rounding a date that crosses this time would produce the following values:
   *
   * <ul>
   *   <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00
   *   <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00
   *   <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00
   *   <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00
   * </ul>
   *
   * @param field the field from {@code Calendar} or {@code SEMI_MONTH}
   * @return the different rounded date, not null
   * @throws ArithmeticException if the year is over 280 million
   */
  public CDate round(final int field) {
    setTime(DateUtils.round(this, field).getTime());
    return this;
  }

  /**
   * Sets the day of month field to a date returning a new object.
   *
   * @param amount the amount to set
   * @return {@code CDate} set with the specified value
   * @throws IllegalArgumentException if the date is null
   */
  public CDate setDays(final int amount) {
    return set(Calendar.DAY_OF_MONTH, amount);
  }

  /**
   * Sets the milliseconds field to a date returning a new object.
   *
   * @param amount the amount to set
   * @return {@code CDate} set with the specified value
   * @throws IllegalArgumentException if the date is null
   */
  public CDate setMilliseconds(final int amount) {
    return set(Calendar.MILLISECOND, amount);
  }

  /**
   * Sets the months field to a date returning a new object.
   *
   * @param amount the amount to set
   * @return {@code CDate} set with the specified value
   * @throws IllegalArgumentException if the date is null
   */
  public CDate setMonths(final int amount) {
    return set(Calendar.MONTH, amount);
  }

  /**
   * Sets the years field to a date returning a new object.
   *
   * @param amount the amount to set
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  public CDate setYears(final int amount) {
    return set(Calendar.YEAR, amount);
  }

  /**
   * Converts a {@code Date} into a {@code Calendar}.
   *
   * @return the created Calendar
   * @throws NullPointerException if null is passed in
   * 
   * @example
   * <pre>
   * CDate date = CDate.now();
   * Calendar calendar = date.toCalendar();
   * 
   * System.out.println("Year: " + calendar.get(Calendar.YEAR));
   * System.out.println("Month: " + calendar.get(Calendar.MONTH));
   * System.out.println("Day: " + calendar.get(Calendar.DAY_OF_MONTH));
   * </pre>
   */
  public Calendar toCalendar() {
    return DateUtils.toCalendar(this);
  }

  /**
   * Converts a {@code Date} of a given {@code TimeZone} into a {@code Calendar}
   *
   * @param tz the time zone of the {@code date}
   * @return the created Calendar
   * @throws NullPointerException if {@code date} or {@code tz} is null
   * 
   * @example
   * <pre>
   * CDate date = CDate.now();
   * TimeZone utc = TimeZone.getTimeZone("UTC");
   * Calendar utcCalendar = date.toCalendar(utc);
   * 
   * TimeZone tokyo = TimeZone.getTimeZone("Asia/Tokyo");
   * Calendar tokyoCalendar = date.toCalendar(tokyo);
   * 
   * System.out.println("UTC hour: " + utcCalendar.get(Calendar.HOUR_OF_DAY));
   * System.out.println("Tokyo hour: " + tokyoCalendar.get(Calendar.HOUR_OF_DAY));
   * </pre>
   */
  public Calendar toCalendar(final TimeZone tz) {
    return DateUtils.toCalendar(this, tz);
  }

  /**
   * Formats this date as a date-only string using MM/dd/yyyy format.
   * 
   * @return the formatted date string
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-25 14:30:45", "yyyy-MM-dd HH:mm:ss");
   * String dateOnly = date.toDateOnlyString();
   * System.out.println(dateOnly); // "12/25/2023"
   * </pre>
   */
  public String toDateOnlyString() {
    return toFormat(DATE_ONLY_FORMAT_STRING);
  }

  /**
   * Converts this date to a Duration representing the time elapsed since the epoch.
   * 
   * @return the Duration representation of this date's time value
   * 
   * @example
   * <pre>
   * CDate date = CDate.now();
   * Duration duration = date.toDuration();
   * System.out.println("Milliseconds since epoch: " + duration.toMillis());
   * System.out.println("Days since epoch: " + duration.toDays());
   * </pre>
   */
  public Duration toDuration() {
    return Duration.ofMillis(getTime());
  }

  /**
   * Formats this date using the specified format pattern.
   * 
   * @param format the format pattern to use
   * @return the formatted date string
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-25 14:30:45", "yyyy-MM-dd HH:mm:ss");
   * 
   * String formatted1 = date.toFormat("yyyy-MM-dd");
   * System.out.println(formatted1); // "2023-12-25"
   * 
   * String formatted2 = date.toFormat("dd/MM/yyyy HH:mm");
   * System.out.println(formatted2); // "25/12/2023 14:30"
   * 
   * String formatted3 = date.toFormat("EEEE, MMMM dd, yyyy");
   * System.out.println(formatted3); // "Monday, December 25, 2023"
   * </pre>
   */
  public String toFormat(String format) {
    return CDateUtil.toFormat(this, format);
  }

  /**
   * Formats this date using the specified format pattern and time zone.
   * 
   * @param format the format pattern to use
   * @param timeZone the time zone to use for formatting
   * @return the formatted date string in the specified time zone
   * 
   * @example
   * <pre>
   * CDate date = CDate.now();
   * TimeZone utc = TimeZone.getTimeZone("UTC");
   * TimeZone tokyo = TimeZone.getTimeZone("Asia/Tokyo");
   * 
   * String utcTime = date.toFormat("yyyy-MM-dd HH:mm:ss", utc);
   * String tokyoTime = date.toFormat("yyyy-MM-dd HH:mm:ss", tokyo);
   * 
   * System.out.println("UTC: " + utcTime);
   * System.out.println("Tokyo: " + tokyoTime);
   * </pre>
   */
  public String toFormat(String format, TimeZone timeZone) {
    return CDateUtil.toFormat(this, format, timeZone);
  }

  /**
   * Returns a formatted duration string representation of this date's time value.
   * 
   * @return formatted duration string (e.g., "1d 05:30:15:123" or "05:30:15:123")
   * 
   * @example
   * <pre>
   * CDate date = new CDate(90061123L); // 1 day, 1 hour, 1 minute, 1 second, 123 ms
   * String formatted = date.toFormattedDuration();
   * System.out.println(formatted); // "1d 01:01:01:123"
   * 
   * CDate shortDuration = new CDate(3661123L); // 1 hour, 1 minute, 1 second, 123 ms
   * String shortFormatted = shortDuration.toFormattedDuration();
   * System.out.println(shortFormatted); // "01:01:01:123"
   * </pre>
   */
  public String toFormattedDuration() {
    return getFormattedDuration(toDuration());
  }

  /**
   * Returns a formatted duration string from the specified date to this date.
   * 
   * @param date the starting date
   * @return formatted duration string
   * 
   * @example
   * <pre>
   * CDate startDate = new CDate("2023-12-25 10:00:00", "yyyy-MM-dd HH:mm:ss");
   * CDate endDate = new CDate("2023-12-26 15:30:45", "yyyy-MM-dd HH:mm:ss");
   * 
   * String duration = endDate.toFormattedDurationFrom(startDate);
   * System.out.println("Duration: " + duration); // "1d 05:30:45:000"
   * </pre>
   */
  public String toFormattedDurationFrom(Date date) {
    return getFormattedDuration(getDurationFrom(date));
  }

  /**
   * Returns a formatted duration string from now to this date.
   * 
   * @return formatted duration string
   * 
   * @example
   * <pre>
   * CDate futureDate = CDate.now().addHours(3).addMinutes(15);
   * String duration = futureDate.toFormattedDurationFromNow();
   * System.out.println("Time until: " + duration); // "03:15:00:000"
   * </pre>
   */
  public String toFormattedDurationFromNow() {
    return getFormattedDuration(getDurationFrom(now()));
  }

  /**
   * Returns a formatted duration string from this date to the specified date.
   * 
   * @param date the ending date
   * @return formatted duration string
   * 
   * @example
   * <pre>
   * CDate startDate = new CDate("2023-12-25 10:00:00", "yyyy-MM-dd HH:mm:ss");
   * CDate endDate = new CDate("2023-12-25 13:45:30", "yyyy-MM-dd HH:mm:ss");
   * 
   * String duration = startDate.toFormattedDurationTo(endDate);
   * System.out.println("Duration: " + duration); // "03:45:30:000"
   * </pre>
   */
  public String toFormattedDurationTo(Date date) {
    return getFormattedDuration(getDurationTo(date));
  }

  /**
   * Returns a formatted duration string from this date to now.
   * 
   * @return formatted duration string
   * 
   * @example
   * <pre>
   * CDate pastDate = CDate.now().addHours(-2).addMinutes(-30);
   * String duration = pastDate.toFormattedDurationToNow();
   * System.out.println("Time since: " + duration); // "02:30:00:000"
   * </pre>
   */
  public String toFormattedDurationToNow() {
    return getFormattedDuration(getDurationTo(now()));
  }

  /**
   * Converts this date to a LocalDate representation.
   * 
   * @return the LocalDate representation of this date
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-25 14:30:45", "yyyy-MM-dd HH:mm:ss");
   * LocalDate localDate = date.toLocalTime();
   * System.out.println(localDate); // "2023-12-25"
   * System.out.println("Year: " + localDate.getYear()); // 2023
   * System.out.println("Month: " + localDate.getMonthValue()); // 12
   * </pre>
   */
  public LocalDate toLocalTime() {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  /**
   * Formats this date using the long date format with milliseconds (yyyy-MMM-dd HH:mm:ss:SSS).
   * 
   * @return the formatted long date string
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-25 14:30:45.123", "yyyy-MM-dd HH:mm:ss.SSS");
   * String longDate = date.toLongDate();
   * System.out.println(longDate); // "2023-Dec-25 14:30:45:123"
   * </pre>
   */
  public String toLongDate() {
    return toFormat(LONG_DATE_FORMAT_STRING_MILI_SECONDS);
  }

  /**
   * Formats this date using the specified format pattern.
   * This is an alias for toFormat(String).
   * 
   * @param format the format pattern to use
   * @return the formatted date string
   * 
   * @example
   * <pre>
   * CDate date = CDate.now();
   * String formatted = date.toString("yyyy-MM-dd HH:mm:ss");
   * System.out.println(formatted); // "2023-12-25 14:30:45"
   * </pre>
   */
  public String toString(final String format) {
    return toFormat(format);
  }

  /**
   * Returns the string representation of this date using the long date format.
   * 
   * @return the string representation in yyyy-MMM-dd HH:mm:ss:SSS format
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-25 14:30:45.123", "yyyy-MM-dd HH:mm:ss.SSS");
   * System.out.println(date.toString()); // "2023-Dec-25 14:30:45:123"
   * </pre>
   */
  @Override
  public String toString() {
    return toFormat(LONG_DATE_FORMAT_STRING_MILI_SECONDS);
  }

  /**
   * Formats this date for use in file names using yyyyMMdd_HHmmss_SSS format.
   * 
   * @return timestamp string suitable for file names
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-25 14:30:45.123", "yyyy-MM-dd HH:mm:ss.SSS");
   * String filename = "log_" + date.toTimeStampForFileName() + ".txt";
   * System.out.println(filename); // "log_20231225_143045_123.txt"
   * </pre>
   */
  public String toTimeStampForFileName() {
    return toFormat(FILENAME_TIMESTAMP_FORMAT);
  }

  /**
   * Removes the time portion from this date, keeping only the date part (sets time to 00:00:00.000).
   * This method modifies the current instance.
   * 
   * @return this CDate instance with time set to midnight
   * 
   * @example
   * <pre>
   * CDate date = new CDate("2023-12-25 14:30:45.123", "yyyy-MM-dd HH:mm:ss.SSS");
   * System.out.println("Before: " + date); // "2023-Dec-25 14:30:45:123"
   * 
   * date.trimTime();
   * System.out.println("After: " + date); // "2023-Dec-25 00:00:00:000"
   * </pre>
   */
  public CDate trimTime() {
    setTime(of(toFormat(DATE_ONLY_FORMAT_STRING), DATE_ONLY_FORMAT_STRING).getTime());
    return this;
  }

  /**
   * Truncates a date, leaving the field specified as the most significant field.
   *
   * <p>For example, if you had the date-time of 28 Mar 2002 13:45:01.231, if you passed with HOUR,
   * it would return 28 Mar 2002 13:00:00.000. If this was passed with MONTH, it would return 1 Mar
   * 2002 0:00:00.000.
   *
   * @param field the field from {@code Calendar} or <code>SEMI_MONTH</code>
   * @return the different truncated date, not null
   * @throws IllegalArgumentException if the date is <code>null</code>
   * @throws ArithmeticException      if the year is over 280 million
   */
  public CDate truncate(final int field) {
    setTime(DateUtils.truncate(this, field).getTime());
    return this;
  }

  /**
   * Determines how two dates compare up to no more than the specified most significant field.
   *
   * @param date  the date, not <code>null</code>
   * @param field the field from <code>Calendar</code>
   * @return a negative integer, zero, or a positive integer as the first date is less than, equal
   * to, or greater than the second.
   * @throws IllegalArgumentException if any argument is <code>null</code>
   */
  public int truncatedCompareTo(final Date date, final int field) {
    return DateUtils.truncatedCompareTo(this, date, field);
  }

  /**
   * Determines if two dates are equal up to no more than the specified most significant field.
   *
   * @param date  the date, not <code>null</code>
   * @param field the field from {@code Calendar}
   * @return <code>true</code> if equal; otherwise <code>false</code>
   * @throws IllegalArgumentException if any argument is <code>null</code>
   */
  public boolean truncatedEquals(final Date date, final int field) {
    return DateUtils.truncatedEquals(this, date, field);
  }

  /**
   * Adds to a date returning a new object. if
   *
   * @param calendarField the calendar field to add to
   * @param amount        the amount to add, may be negative
   * @return current {@code CDate}
   * @throws IllegalArgumentException if the date is null
   */
  private CDate add(final int calendarField, final int amount) {
    setTime(CDateUtil.add(this, calendarField, amount).getTime());
    return this;
  }

  /**
   * Sets the specified field to a date returning a new object. This does not use a lenient
   * calendar.
   *
   * @param calendarField the {@code Calendar} field to set the amount to
   * @param amount        the amount to set
   * @return {@code CDate} set with the specified value
   * @throws IllegalArgumentException if the date is null
   */
  private CDate set(final int calendarField, final int amount) {
    final Calendar c = toCalendar();
    c.setLenient(false);
    c.set(calendarField, amount);
    setTime(c.getTime().getTime());
    return this;
  }

  @Override
  @JsonIgnore
  public CDate _get() {
    return this;
  }
}
