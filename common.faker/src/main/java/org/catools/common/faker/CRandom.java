package org.catools.common.faker;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.catools.common.exception.CInvalidRangeException;
import org.catools.common.faker.enums.CFakerCountryCode3;
import org.catools.common.faker.etl.CFakerResourceManager;
import org.catools.common.faker.exception.CFakerCountryNotFoundException;
import org.catools.common.faker.model.CRandomAddress;
import org.catools.common.faker.model.CRandomCompany;
import org.catools.common.faker.model.CRandomCountry;
import org.catools.common.faker.model.CRandomName;
import org.catools.common.faker.provider.CFakerCountryProvider;
import org.catools.common.faker.util.CLoremIpsum;
import org.catools.common.utils.CIterableUtil;

/**
 * CRandom is a comprehensive faker and random data generation utility that provides methods for
 * generating various types of fake data including numbers, strings, names, addresses, phone
 * numbers, and company information.
 *
 * <p>This class is organized into nested static classes for different data types:
 *
 * <ul>
 *   <li>{@link Int} - Integer generation
 *   <li>{@link Long} - Long number generation
 *   <li>{@link Double} - Double precision number generation
 *   <li>{@link Float} - Float number generation
 *   <li>{@link BigDecimal} - BigDecimal number generation
 *   <li>{@link String} - Text and Lorem Ipsum generation
 *   <li>{@link PhoneNumber} - Phone number generation
 *   <li>{@link PersonName} - Person name generation
 *   <li>{@link Address} - Address generation
 *   <li>{@link Company} - Company information generation
 *   <li>{@link Country} - Country information generation
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Generate random numbers
 * int randomInt = CRandom.Int.next(1, 100);
 * long randomLong = CRandom.Long.next(1000L, 9999L);
 *
 * // Generate random text
 * String word = CRandom.String.nextWord();
 * String paragraph = CRandom.String.nextParagraph();
 *
 * // Generate fake personal data
 * CRandomName name = CRandom.PersonName.next("USA");
 * CRandomAddress address = CRandom.Address.next("USA");
 * String phoneNumber = CRandom.PhoneNumber.nextNumber("US");
 * }</pre>
 *
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 */
public class CRandom {
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final Map<CFakerCountryCode3, CFakerCountryProvider> COUNTRIES = new HashMap<>();

  /**
   * Random generator functions to generate fake Integer values.
   *
   * <p>This class provides methods for generating random integers within specified ranges or using
   * default bounds.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate random integer between 1 and 100 (exclusive)
   * int dice = CRandom.Int.next(1, 7); // Returns 1-6
   *
   * // Generate any positive integer
   * int anyInt = CRandom.Int.next();
   *
   * // Generate integer in range [10, 20)
   * int rangeInt = CRandom.Int.next(10, 20);
   * }</pre>
   */
  public static class Int {
    /**
     * Returns a fake integer within the specified range.
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive the upper bound (not included)
     * @return the fake integer
     * @throws CInvalidRangeException if {@code startInclusive > endExclusive}
     *     <p>Example usage:
     *     <pre>{@code
     * int diceRoll = CRandom.Int.next(1, 7); // Returns 1, 2, 3, 4, 5, or 6
     * int percentage = CRandom.Int.next(0, 101); // Returns 0-100
     * int negativeRange = CRandom.Int.next(-10, 0); // Returns -10 to -1
     *
     * }</pre>
     */
    public static int next(final int startInclusive, final int endExclusive) {
      if (startInclusive > endExclusive) {
        throw new CInvalidRangeException("Start value must be smaller or equal to end value.");
      }
      if (startInclusive == endExclusive) {
        return startInclusive;
      }
      return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
    }

    /**
     * Returns a fake int within 0 - Integer.MAX_VALUE
     *
     * @return the fake integer
     * @see #next(int, int)
     *     <p>Example usage:
     *     <pre>{@code
     * int randomId = CRandom.Int.next(); // Returns any positive integer
     * int[] randomInts = {CRandom.Int.next(), CRandom.Int.next(), CRandom.Int.next()};
     * }</pre>
     */
    public static int next() {
      return next(0, Integer.MAX_VALUE);
    }
  }

  /**
   * Random generator functions to generate fake Long values.
   *
   * <p>This class provides methods for generating random long integers within specified ranges or
   * using default bounds. Useful for generating IDs, timestamps, or large numeric values.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate timestamp-like long
   * long timestamp = CRandom.Long.next(1600000000000L, 1700000000000L);
   *
   * // Generate large ID
   * long userId = CRandom.Long.next(1000L, 999999999L);
   *
   * // Generate any positive long
   * long anyLong = CRandom.Long.next();
   * }</pre>
   */
  public static class Long {
    /**
     * Returns a fake long within the specified range.
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive the upper bound (not included)
     * @return the fake long
     * @throws CInvalidRangeException if {@code startInclusive > endExclusive}
     *     <p>Example usage:
     *     <pre>{@code
     * long fileSize = CRandom.Long.next(1024L, 1073741824L); // 1KB to 1GB
     * long timestamp = CRandom.Long.next(1609459200000L, 1640995200000L); // 2021 year range
     * long serialNumber = CRandom.Long.next(100000000L, 999999999L);
     *
     * }</pre>
     */
    public static long next(final long startInclusive, final long endExclusive) {
      if (startInclusive > endExclusive) {
        throw new CInvalidRangeException("Start value must be smaller or equal to end value.");
      }
      if (startInclusive == endExclusive) {
        return startInclusive;
      }

      return BigDecimal.next(
              java.math.BigDecimal.valueOf(startInclusive),
              java.math.BigDecimal.valueOf(endExclusive))
          .longValue();
    }

    /**
     * Returns a fake long within 0 - Long.MAX_VALUE
     *
     * @return the fake long
     * @see #next(long, long)
     *     <p>Example usage:
     *     <pre>{@code
     * long randomId = CRandom.Long.next(); // Any positive long value
     * long[] ids = new long[10];
     * for (int i = 0; i < ids.length; i++) {
     *     ids[i] = CRandom.Long.next();
     * }
     * }</pre>
     */
    public static long next() {
      return next(0, java.lang.Long.MAX_VALUE);
    }
  }

  /**
   * Random generator functions to generate fake Double values.
   *
   * <p>This class provides methods for generating random double-precision floating-point numbers
   * within specified ranges. Useful for generating prices, coordinates, measurements, and other
   * decimal values.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate price between $0.01 and $999.99
   * double price = CRandom.Double.next(0.01, 999.99);
   *
   * // Generate latitude coordinate
   * double latitude = CRandom.Double.next(-90.0, 90.0);
   *
   * // Generate percentage as decimal
   * double percentage = CRandom.Double.next(0.0, 1.0);
   * }</pre>
   */
  public static class Double {
    /**
     * Returns a fake double within the specified range.
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endInclusive the upper bound (included)
     * @return the fake double
     * @throws CInvalidRangeException if {@code startInclusive > endInclusive}
     *     <p>Example usage:
     *     <pre>{@code
     * double temperature = CRandom.Double.next(-40.0, 50.0); // Temperature in Celsius
     * double weight = CRandom.Double.next(0.1, 100.5); // Weight in kg
     * double longitude = CRandom.Double.next(-180.0, 180.0); // Longitude coordinate
     * double rating = CRandom.Double.next(1.0, 5.0); // Star rating
     *
     * }</pre>
     */
    public static double next(final double startInclusive, final double endInclusive) {
      if (startInclusive > endInclusive) {
        throw new CInvalidRangeException("Start value must be smaller or equal to end value.");
      }
      if (startInclusive == endInclusive) {
        return startInclusive;
      }

      return startInclusive
          + java.math.BigDecimal.valueOf(endInclusive)
              .subtract(java.math.BigDecimal.valueOf(startInclusive))
              .multiply(java.math.BigDecimal.valueOf((RANDOM.nextInt(8) + 1) / 10.0d))
              .doubleValue();
    }

    /**
     * Returns a fake double within 0 - Double.MAX_VALUE
     *
     * @return the fake double
     * @see #next(double, double)
     *     <p>Example usage:
     *     <pre>{@code
     * double randomValue = CRandom.Double.next(); // Any positive double
     *
     * // Generate array of random doubles
     * double[] measurements = new double[5];
     * for (int i = 0; i < measurements.length; i++) {
     *     measurements[i] = CRandom.Double.next();
     * }
     * }</pre>
     */
    public static double next() {
      return next(0, java.lang.Double.MAX_VALUE);
    }
  }

  /**
   * Random generator functions to generate fake Float values.
   *
   * <p>This class provides methods for generating random single-precision floating-point numbers
   * within specified ranges. Useful for generating lighter-weight decimal values when double
   * precision is not required.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate small price
   * float price = CRandom.Float.next(0.99f, 99.99f);
   *
   * // Generate height in meters
   * float height = CRandom.Float.next(1.5f, 2.1f);
   *
   * // Generate probability
   * float probability = CRandom.Float.next(0.0f, 1.0f);
   * }</pre>
   */
  public static class Float {
    /**
     * Returns a fake float within the specified range.
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endInclusive the upper bound (included)
     * @return the fake float
     * @throws CInvalidRangeException if {@code startInclusive > endInclusive}
     *     <p>Example usage:
     *     <pre>{@code
     * float bodyTemp = CRandom.Float.next(36.1f, 37.5f); // Normal body temperature
     * float discount = CRandom.Float.next(0.05f, 0.50f); // 5% to 50% discount
     * float score = CRandom.Float.next(0.0f, 100.0f); // Test score
     *
     * }</pre>
     */
    public static float next(final float startInclusive, final float endInclusive) {
      if (startInclusive > endInclusive) {
        throw new CInvalidRangeException("Start value must be smaller or equal to end value.");
      }
      if (startInclusive == endInclusive) {
        return startInclusive;
      }

      return startInclusive
          + java.math.BigDecimal.valueOf(endInclusive)
              .subtract(java.math.BigDecimal.valueOf(startInclusive))
              .multiply(java.math.BigDecimal.valueOf((RANDOM.nextInt(8) + 1) / 10.0f))
              .floatValue();
    }

    /**
     * Returns a fake float within 0 - Float.MAX_VALUE
     *
     * @return the fake float
     * @see #next(float, float)
     *     <p>Example usage:
     *     <pre>{@code
     * float randomFloat = CRandom.Float.next(); // Any positive float
     *
     * // Generate coordinates
     * float x = CRandom.Float.next();
     * float y = CRandom.Float.next();
     * float z = CRandom.Float.next();
     * }</pre>
     */
    public static float next() {
      return next(0, java.lang.Float.MAX_VALUE);
    }
  }

  /**
   * Random generator functions to generate fake BigDecimal values.
   *
   * <p>This class provides methods for generating random BigDecimal numbers within specified
   * ranges. BigDecimal is ideal for financial calculations, monetary values, and situations
   * requiring precise decimal arithmetic.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate precise monetary amount
   * BigDecimal price = CRandom.BigDecimal.next(
   *     new BigDecimal("0.01"),
   *     new BigDecimal("999.99")
   * );
   *
   * // Generate interest rate
   * BigDecimal rate = CRandom.BigDecimal.next(
   *     new BigDecimal("0.001"),
   *     new BigDecimal("0.1")
   * );
   * }</pre>
   */
  public static class BigDecimal {
    /**
     * Returns a fake BigDecimal within the specified range.
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endInclusive the upper bound (included)
     * @return the fake BigDecimal
     * @throws CInvalidRangeException if {@code startInclusive > endInclusive}
     *     <p>Example usage:
     *     <pre>{@code
     * // Generate precise currency amount
     * BigDecimal invoice = CRandom.BigDecimal.next(
     *     new BigDecimal("100.00"),
     *     new BigDecimal("50000.00")
     * );
     *
     * // Generate tax rate
     * BigDecimal taxRate = CRandom.BigDecimal.next(
     *     new BigDecimal("0.05"),
     *     new BigDecimal("0.25")
     * );
     *
     * // Generate scientific measurement
     * BigDecimal measurement = CRandom.BigDecimal.next(
     *     new BigDecimal("0.0001"),
     *     new BigDecimal("99.9999")
     * );
     *
     * }</pre>
     */
    public static java.math.BigDecimal next(
        final java.math.BigDecimal startInclusive, final java.math.BigDecimal endInclusive) {
      if (startInclusive.doubleValue() > endInclusive.doubleValue()) {
        throw new CInvalidRangeException("Start value must be smaller or equal to end value.");
      }

      if (startInclusive.equals(endInclusive)) {
        return startInclusive;
      }

      double rnadomDouble = RANDOM.nextDouble();
      return java.math.BigDecimal.valueOf(
          startInclusive.doubleValue()
              + ((endInclusive.subtract(startInclusive)).doubleValue() * rnadomDouble));
    }

    /**
     * Returns a fake BigDecimal within 0 - Double.MAX_VALUE
     *
     * @return the fake BigDecimal
     * @see #next(java.math.BigDecimal, java.math.BigDecimal)
     *     <p>Example usage:
     *     <pre>{@code
     * BigDecimal randomAmount = CRandom.BigDecimal.next(); // Any positive BigDecimal
     *
     * // Generate list of random amounts
     * List<BigDecimal> amounts = new ArrayList<>();
     * for (int i = 0; i < 10; i++) {
     *     amounts.add(CRandom.BigDecimal.next());
     * }
     * }</pre>
     */
    public static java.math.BigDecimal next() {
      return next(
          java.math.BigDecimal.ZERO, java.math.BigDecimal.valueOf(java.lang.Double.MAX_VALUE));
    }
  }

  /**
   * Random generator functions to generate Lorem Ipsum style random strings and text content.
   *
   * <p>This class provides comprehensive text generation capabilities including:
   *
   * <ul>
   *   <li>Numeric strings of specified lengths
   *   <li>Lorem Ipsum words, statements, and paragraphs
   *   <li>Customizable word and text generation with length controls
   * </ul>
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate numeric strings
   * String id = CRandom.String.randomNumeric(8); // "12345678"
   * String phone = CRandom.String.randomNumeric(10, 12); // 10-11 digits
   *
   * // Generate Lorem Ipsum text
   * String word = CRandom.String.nextWord(); // "lorem"
   * String sentence = CRandom.String.nextStatement(); // "Lorem ipsum dolor sit amet."
   * String paragraph = CRandom.String.nextParagraph(); // Full paragraph
   *
   * // Generate multiple text elements
   * String multipleWords = CRandom.String.nextWords(5); // "lorem ipsum dolor sit amet"
   * String multipleParagraphs = CRandom.String.nextParagraphs(3); // Three paragraphs
   * }</pre>
   */
  public static class String {
    /**
     * Generate a random numeric string with defined length.
     *
     * @param length length of string, should be positive and greater than 0
     * @return random numeric String with defined length
     * @throws CInvalidRangeException if length is less than 1
     *     <p>Example usage:
     *     <pre>{@code
     * String userId = CRandom.String.randomNumeric(8); // "12345678"
     * String pin = CRandom.String.randomNumeric(4); // "9876"
     * String accountNumber = CRandom.String.randomNumeric(12); // "123456789012"
     * String zipCode = CRandom.String.randomNumeric(5); // "12345"
     *
     * }</pre>
     */
    public static java.lang.String randomNumeric(int length) {
      if (length < 1) {
        throw new CInvalidRangeException(
            "The length value should be greater than 0. length:" + length);
      }
      return RANDOM.nextInt(10) + RandomStringUtils.randomNumeric(length).substring(1);
    }

    /**
     * Generate a random numeric string within the defined length range.
     *
     * @param minLengthInclusive lower bound of string length, should be positive and greater than 0
     * @param maxLengthExclusive upper bound of string length, should be greater or equal to
     *     minLengthInclusive
     * @return random numeric String within defined length range
     * @throws CInvalidRangeException if minLengthInclusive is less than 1 or maxLengthExclusive is
     *     less than minLengthInclusive
     *     <p>Example usage:
     *     <pre>{@code
     * String flexibleId = CRandom.String.randomNumeric(6, 10); // 6-9 digits
     * String phone = CRandom.String.randomNumeric(10, 12); // 10-11 digits
     * String orderNumber = CRandom.String.randomNumeric(8, 15); // 8-14 digits
     * String confirmation = CRandom.String.randomNumeric(4, 8); // 4-7 digits
     *
     * }</pre>
     */
    public static java.lang.String randomNumeric(int minLengthInclusive, int maxLengthExclusive) {
      if (minLengthInclusive < 1) {
        throw new CInvalidRangeException(
            "The minLengthInclusive value should be greater than 0. minLengthInclusive:"
                + minLengthInclusive);
      }

      if (maxLengthExclusive < minLengthInclusive) {
        throw new CInvalidRangeException(
            "The maxLengthExclusive should be equal or greater than minLengthInclusive. maxLengthExclusive:"
                + maxLengthExclusive
                + ", minLengthInclusive:"
                + minLengthInclusive);
      }

      return RANDOM.nextInt(10)
          + RandomStringUtils.randomNumeric(minLengthInclusive, maxLengthExclusive).substring(1);
    }

    /**
     * Generate a Lorem Ipsum paragraph using default parameters.
     *
     * <p>Uses the following default parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 characters
     *   <li>maxWordLength = 10 characters
     *   <li>minStatementLength = 15 characters
     *   <li>maxStatementLength = 30 characters
     *   <li>minParagraphLength = 100 characters
     *   <li>maxParagraphLength = 500 characters
     * </ul>
     *
     * @return random Lorem Ipsum paragraph
     * @see #nextParagraph(int, int)
     * @see #nextParagraph(int, int, int, int, int, int)
     *     <p>Example usage:
     *     <pre>{@code
     * String content = CRandom.String.nextParagraph();
     * // Returns: "Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod..."
     *
     * String articleContent = CRandom.String.nextParagraph();
     * String description = CRandom.String.nextParagraph();
     * }</pre>
     */
    public static java.lang.String nextParagraph() {
      return nextParagraph(3, 10, 15, 30, 100, 500);
    }

    /**
     * Generate sequence of random generated statements using {@link #nextParagraph(int, int, int,
     * int, int, int)} separated by space(" ") using following parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     *   <li>minStatementLength = 15 Char
     *   <li>maxStatementLength = 30 Char
     *   <li>minParagraphLength = {@code minParagraphLength} parameter for minimum length of
     *       paragraph to be generated inclusive
     *   <li>maxParagraphLength = {@code maxParagraphLength} parameter for maximum length of
     *       paragraph to be generated inclusive
     * </ul>
     *
     * @param minParagraphLength minimum length of paragraph to be generated inclusive (cannot be
     *     negative)
     * @param maxParagraphLength maximum length of paragraph to be generated inclusive
     * @return random String with length in defined range
     * @see #nextParagraph()
     * @see #nextParagraph(int, int, int, int, int, int)
     *     <p>Example usage:
     *     <pre>{@code
     * String shortParagraph = CRandom.String.nextParagraph(50, 150);
     * String mediumParagraph = CRandom.String.nextParagraph(200, 400);
     * String longParagraph = CRandom.String.nextParagraph(500, 1000);
     * }</pre>
     */
    public static java.lang.String nextParagraph(int minParagraphLength, int maxParagraphLength) {
      return nextParagraph(3, 10, 15, 30, minParagraphLength, maxParagraphLength);
    }

    /**
     * Generate sequence of random generated statements with the word length in range between
     * minWordLength and maxWordLength, the statement length in range between minStatementLength and
     * maxStatementLength. The total paragraph length will be in range between minParagraphLength
     * and maxParagraphLength
     *
     * @param minWordLength minimum length of string to be generated inclusive (cannot be negative)
     * @param maxWordLength maximum length of string to be generated inclusive
     * @param minStatementLength minimum length of statement to be generated inclusive (cannot be
     *     negative)
     * @param maxStatementLength maximum length of statement to be generated inclusive
     * @param minParagraphLength minimum length of paragraph to be generated inclusive (cannot be
     *     negative)
     * @param maxParagraphLength maximum length of paragraph to be generated inclusive
     * @return random String with length in defined range
     * @see #nextParagraph()
     * @see #nextParagraph(int, int)
     *     <p>Example usage:
     *     <pre>{@code
     * // Short words, short statements, short paragraph
     * String compact = CRandom.String.nextParagraph(2, 5, 10, 20, 50, 100);
     *
     * // Long words, long statements, long paragraph
     * String verbose = CRandom.String.nextParagraph(8, 15, 40, 80, 300, 800);
     *
     * // Custom technical documentation
     * String techDoc = CRandom.String.nextParagraph(5, 12, 25, 50, 200, 600);
     * }</pre>
     */
    public static java.lang.String nextParagraph(
        int minWordLength,
        int maxWordLength,
        int minStatementLength,
        int maxStatementLength,
        int minParagraphLength,
        int maxParagraphLength) {
      return CLoremIpsum.getParagraph(
          minWordLength,
          maxWordLength,
          minStatementLength,
          maxStatementLength,
          minParagraphLength,
          maxParagraphLength);
    }

    /**
     * For {@code count} times, repeat generating a sequence of random statements using {@link
     * #nextParagraph(int, int, int, int, int, int)} separated by space(" ") using following
     * parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     *   <li>minStatementLength = 15 Char
     *   <li>maxStatementLength = 30 Char
     *   <li>minParagraphLength = 100 Char
     *   <li>maxParagraphLength = 500 Char
     * </ul>
     *
     * @param count number of paragraphs which should be generated
     * @return concatenated generated paragraphs
     * @see #nextParagraph()
     * @see #nextParagraph(int, int)
     * @see #nextParagraph(int, int, int, int, int, int)
     *     <p>Example usage:
     *     <pre>{@code
     * String multiParagraph = CRandom.String.nextParagraphs(3);
     * // Returns three paragraphs separated by spaces
     *
     * String article = CRandom.String.nextParagraphs(5);
     * String essay = CRandom.String.nextParagraphs(8);
     * }</pre>
     */
    public static java.lang.String nextParagraphs(int count) {
      return nextParagraphs(3, 10, 15, 30, 100, 500, count);
    }

    /**
     * For {@code count} times, repeat generating a sequence of random statements using {@link
     * #nextParagraph(int, int, int, int, int, int)} separated by space(" ") using following
     * parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     *   <li>minStatementLength = 15 Char
     *   <li>maxStatementLength = 30 Char
     *   <li>minParagraphLength = {@code minParagraphLength} parameter for minimum length of
     *       paragraph to be generated inclusive
     *   <li>maxParagraphLength = {@code maxParagraphLength} parameter for maximum length of
     *       paragraph to be generated inclusive
     * </ul>
     *
     * @param minParagraphLength minimum length of paragraph to be generated inclusive (cannot be
     *     negative)
     * @param maxParagraphLength maximum length of paragraph to be generated inclusive
     * @param count number of paragraphs which should be generated
     * @return concatenated generated paragraphs
     * @see #nextParagraph()
     * @see #nextParagraph(int, int)
     * @see #nextParagraph(int, int, int, int, int, int)
     *     <p>Example usage:
     *     <pre>{@code
     * String customMultiParagraph = CRandom.String.nextParagraphs(150, 300, 3);
     * String shortArticle = CRandom.String.nextParagraphs(100, 200, 5);
     * String longDocument = CRandom.String.nextParagraphs(400, 800, 10);
     * }</pre>
     */
    public static java.lang.String nextParagraphs(
        int minParagraphLength, int maxParagraphLength, int count) {
      return nextParagraphs(3, 10, 15, 30, minParagraphLength, maxParagraphLength, count);
    }

    /**
     * For {@code count} times, repeat generating a sequence of random statements with the word
     * length in range between minWordLength and maxWordLength, the statement length in range
     * between minStatementLength and maxStatementLength. The total paragraph length will be in
     * range between minParagraphLength and maxParagraphLength
     *
     * @param minWordLength minimum length of string to be generated inclusive (cannot be negative)
     * @param maxWordLength maximum length of string to be generated inclusive
     * @param minStatementLength minimum length of statement to be generated inclusive (cannot be
     *     negative)
     * @param maxStatementLength maximum length of statement to be generated inclusive
     * @param minParagraphLength minimum length of paragraph to be generated inclusive (cannot be
     *     negative)
     * @param maxParagraphLength maximum length of paragraph to be generated inclusive
     * @param count number of words which should be generated
     * @return list of generated words
     * @see #nextParagraph()
     * @see #nextParagraph(int, int)
     * @see #nextParagraph(int, int, int, int, int, int)
     */
    public static java.lang.String nextParagraphs(
        int minWordLength,
        int maxWordLength,
        int minStatementLength,
        int maxStatementLength,
        int minParagraphLength,
        int maxParagraphLength,
        int count) {
      return CLoremIpsum.getParagraphs(
          minWordLength,
          maxWordLength,
          minStatementLength,
          maxStatementLength,
          minParagraphLength,
          maxParagraphLength,
          count);
    }

    /**
     * Generate sequence of random generated string using {@see #nextStatement(int, int, int, int)}}
     * using following parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     *   <li>minStatementLength = 15 Char
     *   <li>maxStatementLength = 30 Char
     * </ul>
     *
     * @return random String with length in default range
     * @see #nextStatement(int, int)
     * @see #nextStatement(int, int, int, int)
     */
    public static java.lang.String nextStatement() {
      return nextStatement(3, 10, 15, 30);
    }

    /**
     * Generate sequence of random generated string using {@see #nextStatement(int, int, int, int)}}
     * using following parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     *   <li>minStatementLength = {@code minStatementLength} parameter for minimum length of
     *       statement to be generated inclusive
     *   <li>maxStatementLength = {@code maxStatementLength} parameter for maximum length of
     *       statement to be generated inclusive
     * </ul>
     *
     * @param minStatementLength minimum length of statement to be generated inclusive (cannot be
     *     negative)
     * @param maxStatementLength maximum length of statement to be generated inclusive
     * @return random String with length in range between minStatementLength and maxStatementLength
     * @see #nextStatement()
     * @see #nextStatement(int, int, int, int)
     */
    public static java.lang.String nextStatement(int minStatementLength, int maxStatementLength) {
      return nextStatement(3, 10, minStatementLength, maxStatementLength);
    }

    /**
     * Generate sequence of random generated string using {@see #nextWord(int, int)} using parameter
     * minWordLength and maxWordLength separated by space (" "). The length of final statement will
     * be in range between minStatementLength and maxStatementLength.
     *
     * @param minWordLength minimum length of string to be generated inclusive (cannot be negative)
     * @param maxWordLength maximum length of string to be generated inclusive
     * @param minStatementLength minimum length of statement to be generated inclusive (cannot be
     *     negative)
     * @param maxStatementLength maximum length of statement to be generated inclusive
     * @return random String with length in defined range
     * @see #nextStatement()
     * @see #nextStatement(int, int)
     */
    public static java.lang.String nextStatement(
        int minWordLength, int maxWordLength, int minStatementLength, int maxStatementLength) {
      return CLoremIpsum.getStatement(
          minWordLength, maxWordLength, minStatementLength, maxStatementLength);
    }

    /**
     * For {@code count} times, repeat generating a sequence of randomstring using {@link
     * #nextStatement(int, int, int, int)}} using following parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     *   <li>minStatementLength = 15 Char
     *   <li>maxStatementLength = 30 Char
     * </ul>
     *
     * @param count number of words which should be generated
     * @return list of generated words
     * @see #nextStatement(int, int)
     * @see #nextStatement(int, int, int, int)
     */
    public static java.lang.String nextStatements(int count) {
      return nextStatements(3, 10, 15, 30, count);
    }

    /**
     * For {@code count} times, repeat generating a sequence of randomstring using {@link
     * #nextStatement(int, int, int, int)}} using following parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     *   <li>minStatementLength = {@code minStatementLength} parameter for minimum length of
     *       statement to be generated inclusive
     *   <li>maxStatementLength = {@code maxStatementLength} parameter for maximum length of
     *       statement to be generated inclusive
     * </ul>
     *
     * @param minStatementLength minimum length of statement to be generated inclusive (cannot be
     *     negative)
     * @param maxStatementLength maximum length of statement to be generated inclusive
     * @param count number of words which should be generated
     * @return list of generated words
     * @see #nextStatement()
     * @see #nextStatement(int, int, int, int)
     */
    public static java.lang.String nextStatements(
        int minStatementLength, int maxStatementLength, int count) {
      return nextStatements(3, 10, minStatementLength, maxStatementLength, count);
    }

    /**
     * For {@code count} times, repeat generating a sequence of randomstring using {@link
     * #nextWord(int, int)} using parameter minWordLength and maxWordLength separated by space ("
     * "). The length of final statement will be in range between minStatementLength and
     * maxStatementLength.
     *
     * @param minWordLength minimum length of string to be generated inclusive (cannot be negative)
     * @param maxWordLength maximum length of string to be generated inclusive
     * @param minStatementLength minimum length of statement to be generated inclusive (cannot be
     *     negative)
     * @param maxStatementLength maximum length of statement to be generated inclusive
     * @param count number of words which should be generated
     * @return list of generated words
     * @see #nextStatement()
     * @see #nextStatement(int, int)
     */
    public static java.lang.String nextStatements(
        int minWordLength,
        int maxWordLength,
        int minStatementLength,
        int maxStatementLength,
        int count) {
      return CLoremIpsum.getStatements(
          minWordLength, maxWordLength, minStatementLength, maxStatementLength, count);
    }

    /**
     * Generate sequence of random generated string using {@see #nextWord(int, int)} using following
     * parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     * </ul>
     *
     * @return random String with length in default range
     * @see #nextWord(int, int)
     */
    public static java.lang.String nextWord() {
      return nextWord(3, 10);
    }

    /**
     * Generate next random generated string with the length in range between minWordLength and
     * maxWordLength
     *
     * @param minWordLength minimum length of string to be generated inclusive (cannot be negative)
     * @param maxWordLength maximum length of string to be generated inclusive
     * @return random String with length in range between minWordLength and maxWordLength
     * @see #nextWord()
     */
    public static java.lang.String nextWord(int minWordLength, int maxWordLength) {
      return CLoremIpsum.getWord(minWordLength, maxWordLength);
    }

    /**
     * For {@code count} times, repeat generating a sequence of randomstring using {@link
     * #nextWord(int, int)} using following parameters:
     *
     * <ul>
     *   <li>minWordLength = 3 Char
     *   <li>maxWordLength = 10 Char
     * </ul>
     *
     * @param count number of words which should be generated
     * @return list of generated words
     * @see #nextWord(int, int)
     * @see #nextWords(int, int, int)
     */
    public static java.lang.String nextWords(int count) {
      return nextWords(3, 10, count);
    }

    /**
     * For {@code count} times, repeat generating a sequence of randomstring with the length in
     * range between minWordLength and maxWordLength
     *
     * @param minWordLength minimum length of string to be generated inclusive (cannot be negative)
     * @param maxWordLength maximum length of string to be generated inclusive
     * @param count number of words which should be generated
     * @return list of generated words
     * @see #nextWord(int, int)
     * @see #nextWords(int)
     */
    public static java.lang.String nextWords(int minWordLength, int maxWordLength, int count) {
      return CLoremIpsum.getWords(minWordLength, maxWordLength, count);
    }
  }

  /**
   * Random generator functions to generate fake phone numbers.
   *
   * <p>This class provides methods for generating realistic phone numbers for different countries.
   * The generated numbers follow the general format patterns for each country but are not
   * guaranteed to be valid, working phone numbers.
   *
   * <p>Supported country codes include:
   *
   * <ul>
   *   <li>US - United States
   *   <li>CA - Canada
   *   <li>GB - Great Britain
   *   <li>And others via phone number utility
   * </ul>
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * String usPhone = CRandom.PhoneNumber.nextNumber("US"); // "6031234567"
   * String caPhone = CRandom.PhoneNumber.nextNumber("CA"); // "2041234567"
   * String gbPhone = CRandom.PhoneNumber.nextNumber("GB"); // "3451234567"
   * String dePhone = CRandom.PhoneNumber.nextNumber("DE"); // German format
   * }</pre>
   */
  public static class PhoneNumber {
    /**
     * Generate a fake phone number for the specified country.
     *
     * <p>The method generates phone numbers that follow the general format for each country. For
     * US, CA, and GB, it uses predefined prefixes. For other countries, it uses the Google phone
     * number utility to determine the appropriate format.
     *
     * @param countryCode ISO country code (e.g., "US", "CA", "GB", "DE", "FR")
     * @return fake phone number string following the country's format
     *     <p>Example usage:
     *     <pre>{@code
     * // North American numbers
     * String usNumber = CRandom.PhoneNumber.nextNumber("US"); // "6032345678"
     * String caNumber = CRandom.PhoneNumber.nextNumber("CA"); // "2042345678"
     *
     * // International numbers
     * String ukNumber = CRandom.PhoneNumber.nextNumber("GB"); // "3452345678"
     * String deNumber = CRandom.PhoneNumber.nextNumber("DE"); // German format
     * String frNumber = CRandom.PhoneNumber.nextNumber("FR"); // French format
     *
     * // Generate for testing
     * List<String> testPhones = Arrays.asList("US", "CA", "GB", "DE")
     *     .stream()
     *     .map(CRandom.PhoneNumber::nextNumber)
     *     .collect(Collectors.toList());
     * }</pre>
     */
    public static java.lang.String nextNumber(java.lang.String countryCode) {
      java.lang.String prefix;
      int numberLength = 7; // Default length for US/CA/GB
      switch (countryCode) {
        case "US" -> prefix = "603";
        case "CA" -> prefix = "204";
        case "GB" -> prefix = "345";
        default -> {
          java.lang.String numb =
              java.lang.String.valueOf(
                  PhoneNumberUtil.getInstance().getExampleNumber(countryCode).getNationalNumber());
          prefix = numb.substring(0, 4);
          numberLength = numb.length() - 4;
        }
      }
      return prefix + RandomStringUtils.random(numberLength, "2345678");
    }
  }

  /**
   * Random generator functions to generate fake person names.
   *
   * <p>This class provides methods for generating realistic person names from various countries. It
   * supports generating names by gender (male/female) and by country-specific naming conventions.
   *
   * <p>Features:
   *
   * <ul>
   *   <li>Generate names from any supported country
   *   <li>Gender-specific name generation
   *   <li>Random country selection when country is not specified
   *   <li>Returns {@link CRandomName} objects with first name, last name, and gender information
   * </ul>
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate random names
   * CRandomName anyName = CRandom.PersonName.next(); // Random country
   * CRandomName usName = CRandom.PersonName.next("USA"); // US name
   *
   * // Generate gender-specific names
   * CRandomName maleName = CRandom.PersonName.nextMale("USA");
   * CRandomName femaleName = CRandom.PersonName.nextFemale("USA");
   *
   * // Access name components
   * String firstName = anyName.getFirstName();
   * String lastName = anyName.getLastName();
   * String fullName = anyName.getFullName();
   * }</pre>
   */
  public static class PersonName {
    /**
     * Generate a random person name from any supported country.
     *
     * <p>This method randomly selects a country and generates a name appropriate for that country's
     * naming conventions. The gender is also randomly selected.
     *
     * @return {@link CRandomName} object containing first name, last name, and gender information
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomName person = CRandom.PersonName.next();
     * System.out.println("Name: " + person.getFullName());
     * System.out.println("First: " + person.getFirstName());
     * System.out.println("Last: " + person.getLastName());
     *
     * // Generate multiple names for testing
     * List<CRandomName> testUsers = new ArrayList<>();
     * for (int i = 0; i < 10; i++) {
     *     testUsers.add(CRandom.PersonName.next());
     * }
     * }</pre>
     */
    public static CRandomName next() {
      return next(CIterableUtil.getRandom(CFakerCountryCode3.values()).name());
    }

    /**
     * Generate a random person name for the specified country.
     *
     * <p>This method generates a name using the naming conventions of the specified country. The
     * gender is randomly selected (male or female).
     *
     * @param countryCode3 three-letter country code (e.g., "USA", "GBR", "CAN", "DEU")
     * @return {@link CRandomName} object containing first name, last name, and gender information
     * @throws CFakerCountryNotFoundException if the country code is not supported
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomName americanName = CRandom.PersonName.next("USA");
     * CRandomName britishName = CRandom.PersonName.next("GBR");
     * CRandomName germanName = CRandom.PersonName.next("DEU");
     *
     * // Generate names for different countries
     * String[] countries = {"USA", "GBR", "CAN", "AUS", "DEU"};
     * for (String country : countries) {
     *     CRandomName name = CRandom.PersonName.next(country);
     *     System.out.println(country + ": " + name.getFullName());
     * }
     *
     * }</pre>
     */
    public static CRandomName next(java.lang.String countryCode3) {
      return nextCountryProvider(countryCode3).getNameProvider().getAny();
    }

    /**
     * Generate a random male person name from any supported country.
     *
     * <p>This method randomly selects a country and generates a male name appropriate for that
     * country's naming conventions.
     *
     * @return {@link CRandomName} object containing male first name, last name, and gender
     *     information
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomName malePerson = CRandom.PersonName.nextMale();
     * System.out.println("Male name: " + malePerson.getFullName());
     *
     * // Generate multiple male names
     * List<String> maleNames = new ArrayList<>();
     * for (int i = 0; i < 5; i++) {
     *     maleNames.add(CRandom.PersonName.nextMale().getFullName());
     * }
     * }</pre>
     */
    public static CRandomName nextMale() {
      return nextMale(CIterableUtil.getRandom(CFakerCountryCode3.values()).name());
    }

    /**
     * Generate a random male person name for the specified country.
     *
     * <p>This method generates a male name using the naming conventions of the specified country.
     *
     * @param countryCode3 three-letter country code (e.g., "USA", "GBR", "CAN", "DEU")
     * @return {@link CRandomName} object containing male first name, last name, and gender
     *     information
     * @throws CFakerCountryNotFoundException if the country code is not supported
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomName americanMale = CRandom.PersonName.nextMale("USA");
     * CRandomName britishMale = CRandom.PersonName.nextMale("GBR");
     * CRandomName germanMale = CRandom.PersonName.nextMale("DEU");
     *
     * // Generate male names for a user registration system
     * CRandomName testUser = CRandom.PersonName.nextMale("USA");
     * String username = testUser.getFirstName().toLowerCase() + testUser.getLastName().toLowerCase();
     *
     * }</pre>
     */
    public static CRandomName nextMale(java.lang.String countryCode3) {
      return nextCountryProvider(countryCode3).getNameProvider().getAnyMale();
    }

    /**
     * Generate a random female person name from any supported country.
     *
     * <p>This method randomly selects a country and generates a female name appropriate for that
     * country's naming conventions.
     *
     * @return {@link CRandomName} object containing female first name, last name, and gender
     *     information
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomName femalePerson = CRandom.PersonName.nextFemale();
     * System.out.println("Female name: " + femalePerson.getFullName());
     *
     * // Generate multiple female names
     * List<String> femaleNames = new ArrayList<>();
     * for (int i = 0; i < 5; i++) {
     *     femaleNames.add(CRandom.PersonName.nextFemale().getFullName());
     * }
     * }</pre>
     */
    public static CRandomName nextFemale() {
      return nextFemale(CIterableUtil.getRandom(CFakerCountryCode3.values()).name());
    }

    /**
     * Generate a random female person name for the specified country.
     *
     * <p>This method generates a female name using the naming conventions of the specified country.
     *
     * @param countryCode3 three-letter country code (e.g., "USA", "GBR", "CAN", "DEU")
     * @return {@link CRandomName} object containing female first name, last name, and gender
     *     information
     * @throws CFakerCountryNotFoundException if the country code is not supported
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomName americanFemale = CRandom.PersonName.nextFemale("USA");
     * CRandomName britishFemale = CRandom.PersonName.nextFemale("GBR");
     * CRandomName germanFemale = CRandom.PersonName.nextFemale("DEU");
     *
     * // Generate female names for testing
     * CRandomName testUser = CRandom.PersonName.nextFemale("USA");
     * String email = testUser.getFirstName().toLowerCase() + "." +
     *                testUser.getLastName().toLowerCase() + "@example.com";
     *
     * }</pre>
     */
    public static CRandomName nextFemale(java.lang.String countryCode3) {
      return nextCountryProvider(countryCode3).getNameProvider().getAnyFemale();
    }
  }

  /**
   * Random generator functions to generate fake addresses.
   *
   * <p>This class provides methods for generating realistic addresses from various countries. The
   * addresses include street names, cities, states/provinces, postal codes, and country information
   * based on the specified country's addressing conventions.
   *
   * <p>Features:
   *
   * <ul>
   *   <li>Generate addresses from any supported country
   *   <li>Country-specific address formats and components
   *   <li>Random country selection when country is not specified
   *   <li>Returns {@link CRandomAddress} objects with all address components
   * </ul>
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate random addresses
   * CRandomAddress anyAddress = CRandom.Address.next(); // Random country
   * CRandomAddress usAddress = CRandom.Address.next("USA"); // US address
   *
   * // Access address components
   * String street = anyAddress.getStreet();
   * String city = anyAddress.getCity();
   * String postalCode = anyAddress.getPostalCode();
   * String fullAddress = anyAddress.getFullAddress();
   * }</pre>
   */
  public static class Address {
    /**
     * Generate a random address from any supported country.
     *
     * <p>This method randomly selects a country and generates an address appropriate for that
     * country's addressing conventions and format.
     *
     * @return {@link CRandomAddress} object containing all address components
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomAddress address = CRandom.Address.next();
     * System.out.println("Address: " + address.getFullAddress());
     * System.out.println("Street: " + address.getStreet());
     * System.out.println("City: " + address.getCity());
     * System.out.println("Postal: " + address.getPostalCode());
     *
     * // Generate multiple addresses for testing
     * List<CRandomAddress> testAddresses = new ArrayList<>();
     * for (int i = 0; i < 10; i++) {
     *     testAddresses.add(CRandom.Address.next());
     * }
     * }</pre>
     */
    public static CRandomAddress next() {
      return next(CIterableUtil.getRandom(CFakerCountryCode3.values()).name());
    }

    /**
     * Generate a random address for the specified country.
     *
     * <p>This method generates an address using the addressing conventions and format of the
     * specified country. The address will include appropriate components like street names, city
     * names, postal codes, and regional divisions based on the country's standards.
     *
     * @param countryCode3 three-letter country code (e.g., "USA", "GBR", "CAN", "DEU")
     * @return {@link CRandomAddress} object containing all address components
     * @throws CFakerCountryNotFoundException if the country code is not supported
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomAddress americanAddress = CRandom.Address.next("USA");
     * CRandomAddress britishAddress = CRandom.Address.next("GBR");
     * CRandomAddress germanAddress = CRandom.Address.next("DEU");
     *
     * // Generate addresses for shipping simulation
     * String[] countries = {"USA", "GBR", "CAN", "AUS"};
     * for (String country : countries) {
     *     CRandomAddress address = CRandom.Address.next(country);
     *     System.out.println(country + " address: " + address.getFullAddress());
     * }
     *
     * // Generate address for form testing
     * CRandomAddress testAddress = CRandom.Address.next("USA");
     * fillAddressForm(testAddress.getStreet(), testAddress.getCity(),
     *                 testAddress.getState(), testAddress.getPostalCode());
     *
     * }</pre>
     */
    public static CRandomAddress next(java.lang.String countryCode3) {
      return nextCountryProvider(countryCode3).getAddressProvider().getAny();
    }
  }

  /**
   * Random generator functions to generate fake company information.
   *
   * <p>This class provides methods for generating realistic company information from various
   * countries. The generated company data includes company names, business types, and other
   * relevant company information based on the specified country's business conventions.
   *
   * <p>Features:
   *
   * <ul>
   *   <li>Generate company information from any supported country
   *   <li>Country-specific company names and business types
   *   <li>Random country selection when country is not specified
   *   <li>Returns {@link CRandomCompany} objects with company details
   * </ul>
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate random company information
   * CRandomCompany anyCompany = CRandom.Company.next(); // Random country
   * CRandomCompany usCompany = CRandom.Company.next("USA"); // US company
   *
   * // Access company information
   * String companyName = anyCompany.getName();
   * String industry = anyCompany.getIndustry();
   * String businessType = anyCompany.getBusinessType();
   * }</pre>
   */
  public static class Company {
    /**
     * Generate random company information from any supported country.
     *
     * <p>This method randomly selects a country and generates company information appropriate for
     * that country's business conventions and naming patterns.
     *
     * @return {@link CRandomCompany} object containing company information
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomCompany company = CRandom.Company.next();
     * System.out.println("Company: " + company.getName());
     * System.out.println("Industry: " + company.getIndustry());
     *
     * // Generate multiple companies for testing
     * List<CRandomCompany> testCompanies = new ArrayList<>();
     * for (int i = 0; i < 10; i++) {
     *     testCompanies.add(CRandom.Company.next());
     * }
     * }</pre>
     */
    public static CRandomCompany next() {
      return next(CIterableUtil.getRandom(CFakerCountryCode3.values()).name());
    }

    /**
     * Generate random company information for the specified country.
     *
     * <p>This method generates company information using the business conventions and naming
     * patterns of the specified country. This includes country-specific company name formats,
     * common business types, and industry classifications.
     *
     * @param countryCode3 three-letter country code (e.g., "USA", "GBR", "CAN", "DEU")
     * @return {@link CRandomCompany} object containing company information
     * @throws CFakerCountryNotFoundException if the country code is not supported
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomCompany americanCompany = CRandom.Company.next("USA");
     * CRandomCompany britishCompany = CRandom.Company.next("GBR");
     * CRandomCompany germanCompany = CRandom.Company.next("DEU");
     *
     * // Generate companies for business simulation
     * String[] countries = {"USA", "GBR", "CAN", "AUS", "DEU"};
     * for (String country : countries) {
     *     CRandomCompany company = CRandom.Company.next(country);
     *     System.out.println(country + " company: " + company.getName());
     * }
     *
     * // Generate company for testing
     * CRandomCompany testCompany = CRandom.Company.next("USA");
     * createBusinessProfile(testCompany.getName(), testCompany.getIndustry());
     *
     * }</pre>
     */
    public static CRandomCompany next(java.lang.String countryCode3) {
      return nextCountryProvider(countryCode3).getCompanyProvider().getAny();
    }
  }

  /**
   * Random generator functions to generate fake country information.
   *
   * <p>This class provides methods for generating country information including country names,
   * country codes, and other country-related data. This is useful for generating test data for
   * applications that need country information.
   *
   * <p>Features:
   *
   * <ul>
   *   <li>Generate random country information
   *   <li>Get specific country information by country code
   *   <li>Returns {@link CRandomCountry} objects with country details
   * </ul>
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * // Generate random country information
   * CRandomCountry anyCountry = CRandom.Country.next(); // Random country
   * CRandomCountry specificCountry = CRandom.Country.next("USA"); // US information
   *
   * // Access country information
   * String countryName = anyCountry.getName();
   * String countryCode = anyCountry.getCode();
   * String continent = anyCountry.getContinent();
   * }</pre>
   */
  public static class Country {
    /**
     * Generate random country information.
     *
     * <p>This method randomly selects a country and returns its information including name, codes,
     * and other country-related data.
     *
     * @return {@link CRandomCountry} object containing country information
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomCountry country = CRandom.Country.next();
     * System.out.println("Country: " + country.getName());
     * System.out.println("Code: " + country.getCode());
     *
     * // Generate multiple countries for testing
     * List<CRandomCountry> testCountries = new ArrayList<>();
     * for (int i = 0; i < 10; i++) {
     *     testCountries.add(CRandom.Country.next());
     * }
     * }</pre>
     */
    public static CRandomCountry next() {
      return next(CIterableUtil.getRandom(CFakerCountryCode3.values()).name());
    }

    /**
     * Generate country information for the specified country code.
     *
     * <p>This method returns information for the specified country including its name, codes,
     * continent, and other relevant country data.
     *
     * @param countryCode3 three-letter country code (e.g., "USA", "GBR", "CAN", "DEU")
     * @return {@link CRandomCountry} object containing country information
     * @throws CFakerCountryNotFoundException if the country code is not supported
     *     <p>Example usage:
     *     <pre>{@code
     * CRandomCountry usa = CRandom.Country.next("USA");
     * CRandomCountry britain = CRandom.Country.next("GBR");
     * CRandomCountry germany = CRandom.Country.next("DEU");
     *
     * // Get information about multiple countries
     * String[] countryCodes = {"USA", "GBR", "CAN", "AUS", "DEU", "FRA"};
     * for (String code : countryCodes) {
     *     CRandomCountry country = CRandom.Country.next(code);
     *     System.out.println(code + ": " + country.getName());
     * }
     *
     * // Use country information for localization testing
     * CRandomCountry testCountry = CRandom.Country.next("USA");
     * configureLocalization(testCountry.getCode(), testCountry.getName());
     *
     * }</pre>
     */
    public static synchronized CRandomCountry next(java.lang.String countryCode3) {
      return nextCountryProvider(countryCode3).getCountry();
    }
  }

  private static synchronized CFakerCountryProvider nextCountryProvider(
      java.lang.String countryCode3) {
    boolean countryExists =
        Arrays.stream(CFakerCountryCode3.values())
            .anyMatch(e -> e.name().equalsIgnoreCase(countryCode3));
    if (!countryExists) {
      throw new CFakerCountryNotFoundException(countryCode3);
    }
    CFakerCountryCode3 countryCode = CFakerCountryCode3.valueOf(countryCode3.toUpperCase());
    if (COUNTRIES.isEmpty()) {
      COUNTRIES.put(countryCode, CFakerResourceManager.getCountry(countryCode3));
    }
    return COUNTRIES.get(countryCode);
  }
}
