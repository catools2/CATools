package org.catools.common.extensions.states.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.catools.common.collections.CList;
import org.catools.common.utils.CRegExUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.RegExUtils.removePattern;

/**
 * CStringState is an interface for String state related methods.
 *
 * <p>We need this interface to have possibility of adding state to any exists objects with the
 * minimum change in the code.l
 */
public interface CStringState extends CObjectState<String> {

  /**
   * Verify if result of {@link StringUtils#center(String, int, String)} is equals to expected
   * value.
   *
   * @param size     the int size of new String, negative treated as zero
   * @param padStr   the String to pad the new String with, must not be null or empty
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean centerPadEquals(int size, String padStr, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.center(_get(), size, padStr), expected);
  }

  /**
   * Verify if result of {@link StringUtils#center(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param size     the int size of new String, negative treated as zero
   * @param padStr   the String to pad the new String with, must not be null or empty
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean centerPadNotEquals(int size, String padStr, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.center(_get(), size, padStr), expected);
  }

  /**
   * Verify if result of {@link StringUtils#compare(String, String)} equals to the expected value.
   *
   * @param stringToCompare the string value to compare against
   * @param expected        the expected result.
   * @return execution boolean result
   */
  default boolean compare(String stringToCompare, int expected) {
    return StringUtils.compare(_get(), stringToCompare) == expected;
  }

  /**
   * Verify if result of {@link StringUtils#compareIgnoreCase(String, String)} equals to the
   * expected value.
   *
   * @param stringToCompare the string value to compare against
   * @param expected        the expected result.
   * @return execution boolean result
   */
  default boolean compareIgnoreCase(String stringToCompare, int expected) {
    return StringUtils.compareIgnoreCase(_get(), stringToCompare) == expected;
  }

  /**
   * Verify if result of {@link StringUtils#contains(CharSequence, CharSequence)} is true.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean contains(String expected) {
    return _get() != null && expected != null && StringUtils.contains(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#containsIgnoreCase(CharSequence, CharSequence)} is true,
   * ignoring case.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean containsIgnoreCase(String expected) {
    return _get() != null && expected != null && StringUtils.containsIgnoreCase(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#containsAnyIgnoreCase(CharSequence, CharSequence...)}
   * (CharSequence, CharSequence)} is true, ignoring case.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean containsAny(Iterable<String> expected) {
    return _get() != null
        && expected != null
        && StringUtils.containsAny(_get(), toStringArray(expected));
  }

  /**
   * Verify if result of {@link StringUtils#containsAnyIgnoreCase(CharSequence, CharSequence...)}
   * (CharSequence, CharSequence)} is true, ignoring case.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean containsAnyIgnoreCase(Iterable<String> expected) {
    return _get() != null
        && expected != null
        && StringUtils.containsAnyIgnoreCase(_get(), toStringArray(expected));
  }

  /**
   * Verify if result of {@link StringUtils#endsWith(CharSequence, CharSequence)} is true
   *
   * @param suffix the suffix to find, may be {@code null}
   * @return execution boolean result
   */
  default boolean endsWith(String suffix) {
    return _get() != null && suffix != null && StringUtils.endsWith(_get(), suffix);
  }

  /**
   * Verify if result of {@link StringUtils#endsWithAny(CharSequence, CharSequence...)} is true.
   *
   * @param searchInputs the case-sensitive CharSequences to find, may be empty or contain {@code
   *                     null}
   * @return execution boolean result
   */
  default boolean endsWithAny(Iterable<String> searchInputs) {
    return _get() != null
        && searchInputs != null
        && StringUtils.endsWithAny(_get(), toStringArray(searchInputs));
  }

  /**
   * Verify if result of {@link StringUtils#endsWithIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param suffix the suffix to find, may be {@code null}
   * @return execution boolean result
   */
  default boolean endsWithIgnoreCase(String suffix) {
    return _get() != null && suffix != null && StringUtils.endsWithIgnoreCase(_get(), suffix);
  }

  /**
   * Verify if result of {@link StringUtils#endsWithAny(CharSequence, CharSequence...)} is false.
   *
   * @param searchInputs the case-sensitive CharSequences to find, may be empty or contain {@code
   *                     null}
   * @return execution boolean result
   */
  default boolean endsWithNone(Iterable<String> searchInputs) {
    return _get() != null
        && searchInputs != null
        && !StringUtils.endsWithAny(_get(), toStringArray(searchInputs));
  }

  /**
   * Verify if {@link StringUtils#equals(CharSequence, CharSequence)} value equals the expected
   * value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean isEqual(String expected) {
    return Strings.CS.equals(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#equalsAny(CharSequence, CharSequence...)} is true
   *
   * @param expectedList a list of strings, may be {@code null}.
   * @return execution boolean result
   */
  default boolean equalsAny(Iterable<String> expectedList) {
    return _get() != null
        && expectedList != null
        && StringUtils.equalsAny(_get(), toStringArray(expectedList));
  }

  /**
   * Verify if result of {@link StringUtils#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * true, ignoring case.
   *
   * @param expectedList a list of strings, may be {@code null}.
   * @return execution boolean result
   */
  default boolean equalsAnyIgnoreCase(Iterable<String> expectedList) {
    return _get() != null
        && expectedList != null
        && StringUtils.equalsAnyIgnoreCase(_get(), toStringArray(expectedList));
  }

  /**
   * Verify if {@link StringUtils#equalsIgnoreCase(CharSequence, CharSequence)} value equals the
   * expected value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean equalsIgnoreCase(String expected) {
    return StringUtils.equalsIgnoreCase(_get(), expected);
  }

  /**
   * Verify if value is equal to expected after removing all WhiteSpaces from both.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean equalsIgnoreWhiteSpaces(String expected) {
    return Strings.CS.equals(removePattern(_get(), "\\s"), removePattern(expected, "\\s"));
  }

  /**
   * Verify if result of {@link StringUtils#equalsAny(CharSequence, CharSequence...)} is false
   *
   * @param expectedList a list of strings, may be {@code null}.
   * @return execution boolean result
   */
  default boolean equalsNone(Iterable<String> expectedList) {
    return _get() != null
        && expectedList != null
        && !StringUtils.equalsAny(_get(), toStringArray(expectedList));
  }

  /**
   * Verify if result of {@link StringUtils#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * false, ignoring case.
   *
   * @param expectedList a list of strings, may be {@code null}.
   * @return execution boolean result
   */
  default boolean equalsNoneIgnoreCase(Iterable<String> expectedList) {
    return _get() != null
        && expectedList != null
        && !StringUtils.equalsAnyIgnoreCase(_get(), toStringArray(expectedList));
  }

  /**
   * Verify if result of {@link StringUtils#isAlpha(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isAlpha() {
    return _get() != null && StringUtils.isAlpha(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isAlphaSpace(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isAlphaSpace() {
    return _get() != null && StringUtils.isAlphaSpace(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isAlphanumeric(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isAlphanumeric() {
    return _get() != null && CRegExUtil.isAlphaNumeric(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isAlphanumericSpace(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isAlphanumericSpace() {
    return _get() != null && CRegExUtil.isAlphaNumericSpace(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isAsciiPrintable(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isAsciiPrintable() {
    return _get() != null && StringUtils.isAsciiPrintable(_get());
  }

  /**
   * Verify if String value is blank (Null or Empty)
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isBlank() {
    return StringUtils.isBlank(_get());
  }

  /**
   * Verify if string value is Blank or the result of {@link StringUtils#isAlpha(CharSequence)} is
   * true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isBlankOrAlpha() {
    String a = _get();
    return StringUtils.isBlank(a) || StringUtils.isAlpha(a);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * StringUtils#isAlphanumeric(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isBlankOrAlphanumeric() {
    String a = _get();
    return StringUtils.isBlank(a) || StringUtils.isAlphanumeric(a);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * StringUtils#isAlphanumeric(CharSequence)} is true and string length is between minLength and
   * maxLength.
   *
   * @param minLength minimum expected side of string if it is not Blank
   * @param maxLength maximum expected side of string if it is not Blank
   * @return execution boolean result
   */
  default boolean isBlankOrAlphanumeric(int minLength, int maxLength) {
    String a = _get();
    return a != null
        && (StringUtils.isBlank(a)
        || (StringUtils.isAlphanumeric(a)
        && a.length() >= minLength
        && a.length() <= maxLength));
  }

  /**
   * Verify if string value is Blank or the result of {@link StringUtils#isAlpha(CharSequence)} is
   * false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isBlankOrNotAlpha() {
    String a = _get();
    return StringUtils.isBlank(a) || !StringUtils.isAlpha(a);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * StringUtils#isAlphanumeric(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isBlankOrNotAlphanumeric() {
    String a = _get();
    return StringUtils.isBlank(a) || !StringUtils.isAlphanumeric(a);
  }

  /**
   * Verify if string is Blank or the result of {@link StringUtils#isNumeric(CharSequence)} is
   * false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isBlankOrNotNumeric() {
    String a = _get();
    return a != null && (StringUtils.isBlank(a) || !StringUtils.isNumeric(a));
  }

  /**
   * Verify if string is Blank or the result of {@link StringUtils#isNumeric(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isBlankOrNumeric() {
    String a = _get();
    return a != null && StringUtils.isBlank(a) || StringUtils.isNumeric(a);
  }

  /**
   * Verify if string value is Blank or the result of {@link StringUtils#isNumeric(CharSequence)} is
   * true and string length is between minLength and maxLength.
   *
   * @param minLength minimum expected side of string if it is not Blank
   * @param maxLength maximum expected side of string if it is not Blank
   * @return execution boolean result
   */
  default boolean isBlankOrNumeric(int minLength, int maxLength) {
    String a = _get();
    return a != null
        && (StringUtils.isBlank(a)
        || (StringUtils.isNumeric(a) && a.length() >= minLength && a.length() <= maxLength));
  }

  /**
   * Verify if String value is empty
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isEmpty() {
    return StringUtils.isEmpty(_get());
  }

  /**
   * Verify if string value is empty or the result of {@link StringUtils#isAlpha(CharSequence)} is
   * true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isEmptyOrAlpha() {
    String a = _get();
    return StringUtils.isEmpty(a) || StringUtils.isAlpha(a);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * StringUtils#isAlphanumeric(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isEmptyOrAlphanumeric() {
    String a = _get();
    return StringUtils.isEmpty(a) || StringUtils.isAlphanumeric(a);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * StringUtils#isAlphanumeric(CharSequence)} is true and string length is between minLength and
   * maxLength.
   *
   * @param minLength minimum expected side of string if it is not empty
   * @param maxLength maximum expected side of string if it is not empty
   * @return execution boolean result
   */
  default boolean isEmptyOrAlphanumeric(int minLength, int maxLength) {
    String a = _get();
    return a != null
        && (StringUtils.isEmpty(a)
        || (StringUtils.isAlphanumeric(a)
        && a.length() >= minLength
        && a.length() <= maxLength));
  }

  /**
   * Verify if string value is empty or the result of {@link StringUtils#isAlpha(CharSequence)} is
   * false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isEmptyOrNotAlpha() {
    String a = _get();
    return StringUtils.isEmpty(a) || !StringUtils.isAlpha(a);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * StringUtils#isAlphanumeric(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isEmptyOrNotAlphanumeric() {
    String a = _get();
    return a != null
        && (StringUtils.isEmpty(a) || (a.contains(" ") || !CRegExUtil.isAlphaNumericSpace(a)));
  }

  /**
   * Verify if string is empty or the result of {@link StringUtils#isNumeric(CharSequence)} is
   * false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isEmptyOrNotNumeric() {
    String a = _get();
    return a != null && (StringUtils.isEmpty(a) || !StringUtils.isNumeric(a));
  }

  /**
   * Verify if string is empty or the result of {@link StringUtils#isNumeric(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isEmptyOrNumeric() {
    String a = _get();
    return a != null && StringUtils.isEmpty(a) || StringUtils.isNumeric(a);
  }

  /**
   * Verify if string value is empty or the result of {@link StringUtils#isNumeric(CharSequence)} is
   * true and string length is between minLength and maxLength.
   *
   * @param minLength minimum expected side of string if it is not empty
   * @param maxLength maximum expected side of string if it is not empty
   * @return execution boolean result
   */
  default boolean isEmptyOrNumeric(int minLength, int maxLength) {
    String a = _get();
    return a != null
        && (StringUtils.isEmpty(a)
        || (StringUtils.isNumeric(a) && a.length() >= minLength && a.length() <= maxLength));
  }

  /**
   * Verify if result of {@link StringUtils#isAlpha(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotAlpha() {
    return _get() != null && !StringUtils.isAlpha(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isAlphaSpace(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotAlphaSpace() {
    return _get() != null && !StringUtils.isAlphaSpace(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isAlphanumeric(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotAlphanumeric() {
    return _get() != null && !CRegExUtil.isAlphaNumeric(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isAlphanumericSpace(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotAlphanumericSpace() {
    return _get() != null && !CRegExUtil.isAlphaNumericSpace(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isAsciiPrintable(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotAsciiPrintable() {
    return _get() != null && !StringUtils.isAsciiPrintable(_get());
  }

  /**
   * Verify if String value is not blank (Null or Empty)
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotBlank() {
    return StringUtils.isNotBlank(_get());
  }

  /**
   * Verify String value is not empty
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotEmpty() {
    return StringUtils.isNotEmpty(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isNumeric(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotNumeric() {
    return _get() != null && !StringUtils.isNumeric(_get());
  }

  /**
   * Verify if result of {@link StringUtils#isNumericSpace(CharSequence)} is false.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotNumericSpace() {
    String a = _get();
    return a != null && !StringUtils.isNumericSpace(a);
  }

  /**
   * Verify if result of {@link StringUtils#isNumeric(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNumeric() {
    return _get() != null && StringUtils.isNumeric(_get());
  }

  /**
   * Verify if string value is empty or the result of {@link StringUtils#isNumeric(CharSequence)} is
   * true and string length is between minLength and maxLength.
   *
   * @param minLength minimum expected side of string if it is not empty
   * @param maxLength maximum expected side of string if it is not empty
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNumeric(int minLength, int maxLength) {
    String a = _get();
    return a != null
        && (StringUtils.isNumeric(a) && a.length() >= minLength && a.length() <= maxLength);
  }

  /**
   * Verify if result of {@link StringUtils#isNumericSpace(CharSequence)} is true.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNumericSpace() {
    String a = _get();
    return a != null && StringUtils.isNumericSpace(a);
  }

  /**
   * Verify if result of {@link StringUtils#leftPad(String, int, String)} is equals to expected
   * value.
   *
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean leftPadEquals(int size, String padStr, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.leftPad(_get(), size, padStr), expected);
  }

  /**
   * Verify if result of {@link StringUtils#leftPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean leftPadNotEquals(int size, String padStr, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.leftPad(_get(), size, padStr), expected);
  }

  /**
   * Verify if result of {@link StringUtils#left(String, int)} equals to expected value.
   *
   * @param expected the expected result.
   * @param len      the length of the required String
   * @return execution boolean result
   */
  default boolean leftValueEquals(int len, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.left(_get(), len), expected);
  }

  /**
   * Verify if result of {@link StringUtils#left(String, int)} NOT equals to expected value.
   *
   * @param expected the expected result.
   * @param len      the length of the required String
   * @return execution boolean result
   */
  default boolean leftValueNotEquals(int len, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.left(_get(), len), expected);
  }

  /**
   * Verify if result of {@link StringUtils#length(CharSequence)} is equals to expected value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean lengthEquals(int expected) {
    return StringUtils.length(_get()) == expected;
  }

  /**
   * Verify if result of {@link StringUtils#length(CharSequence)} is NOT equals to expected value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean lengthNotEquals(int expected) {
    return StringUtils.length(_get()) != expected;
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param pattern regular expression pattern
   * @return execution boolean result
   */
  default boolean matches(final Pattern pattern) {
    return _get() != null && pattern != null && CRegExUtil.isMatch(_get(), pattern);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param pattern regular expression pattern
   * @return execution boolean result
   */
  default boolean matches(final String pattern) {
    return _get() != null && pattern != null && CRegExUtil.isMatch(_get(), pattern);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param patterns list of regular expression pattern
   * @return execution boolean result
   */
  default boolean matchAny(final List<Pattern> patterns) {
    return _get() != null
        && patterns != null
        && new CList<>(patterns).has(pattern -> CRegExUtil.isMatch(_get(), pattern));
  }

  /**
   * Verify if result of {@link StringUtils#mid(String, int, int)} equals to expected value.
   *
   * @param expected the expected result.
   * @param pos      the position to start from, negative treated as zero
   * @param len      the length of the required String
   * @return execution boolean result
   */
  default boolean midValueEquals(int pos, int len, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.mid(_get(), pos, len), expected);
  }

  /**
   * Verify if result of {@link StringUtils#mid(String, int, int)} NOT equals to expected value.
   *
   * @param expected the expected result.
   * @param pos      the position to start from, negative treated as zero
   * @param len      the length of the required String
   * @return execution boolean result
   */
  default boolean midValueNotEquals(int pos, int len, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.mid(_get(), pos, len), expected);
  }

  /**
   * Verify if result of {@link StringUtils#contains(CharSequence, CharSequence)} is false.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean notContains(String expected) {
    return _get() != null && expected != null && !StringUtils.contains(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#containsIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean notContainsIgnoreCase(String expected) {
    return _get() != null && expected != null && !StringUtils.containsIgnoreCase(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#endsWith(CharSequence, CharSequence)} is false
   *
   * @param suffix the suffix to find, may be {@code null}
   * @return execution boolean result
   */
  default boolean notEndsWith(String suffix) {
    return _get() != null && suffix != null && !StringUtils.endsWith(_get(), suffix);
  }

  /**
   * Verify if result of {@link StringUtils#endsWithIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param suffix the suffix to find, may be {@code null}
   * @return execution boolean result
   */
  default boolean notEndsWithIgnoreCase(String suffix) {
    return _get() != null && suffix != null && !StringUtils.endsWithIgnoreCase(_get(), suffix);
  }

  /**
   * Verify if {@link StringUtils#equals(CharSequence, CharSequence)} value NOT equals the expected
   * value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean isNotEqual(String expected) {
    return !Strings.CS.equals(_get(), expected);
  }

  /**
   * Verify if {@link StringUtils#equalsIgnoreCase(CharSequence, CharSequence)} value NOT equals the
   * expected value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean notEqualsIgnoreCase(String expected) {
    return !StringUtils.equalsIgnoreCase(_get(), expected);
  }

  /**
   * Verify if value is not equal to expected after removing all WhiteSpaces from both.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean notEqualsIgnoreWhiteSpaces(String expected) {
    return !Strings.CS.equals(removePattern(_get(), "\\s"), removePattern(expected, "\\s"));
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param pattern regular expression pattern
   * @return execution boolean result
   */
  default boolean notMatches(final Pattern pattern) {
    return _get() != null && pattern != null && !CRegExUtil.isMatch(_get(), pattern);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param pattern regular expression pattern
   * @return execution boolean result
   */
  default boolean notMatches(final String pattern) {
    return _get() != null && pattern != null && !CRegExUtil.isMatch(_get(), pattern);
  }

  /**
   * Verify if String value does NOT match any of provided pattern
   *
   * @param patterns list of regular expression pattern
   * @return execution boolean result
   */
  default boolean matchNone(final List<Pattern> patterns) {
    return _get() != null
        && patterns != null
        && new CList<>(patterns).hasNot(pattern -> CRegExUtil.isMatch(_get(), pattern));
  }

  /**
   * Verify if result of {@link StringUtils#startsWith(CharSequence, CharSequence)} is false
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean notStartsWith(String expected) {
    return _get() != null && expected != null && !StringUtils.startsWith(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean notStartsWithIgnoreCase(String expected) {
    return _get() != null && expected != null && !StringUtils.startsWithIgnoreCase(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#countMatches(CharSequence, CharSequence)} is equals to
   * expected value.
   *
   * @param subString the substring to count, may be null
   * @param expected  the expected result.
   * @return execution boolean result
   */
  default boolean numberOfMatchesEquals(String subString, int expected) {
    return _get() != null && StringUtils.countMatches(_get(), subString) == expected;
  }

  /**
   * Verify if result of {@link StringUtils#countMatches(CharSequence, CharSequence)} is NOT equals
   * to expected value.
   *
   * @param subString the substring to count, may be null
   * @param expected  the expected result.
   * @return execution boolean result
   */
  default boolean numberOfMatchesNotEquals(String subString, int expected) {
    return _get() != null && StringUtils.countMatches(_get(), subString) != expected;
  }

  /**
   * Verify if result of {@link StringUtils#removeEnd(String, String)} is equals to expected value.
   *
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeEndEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(Strings.CS.removeEnd(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeEndIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeEndIgnoreCaseEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.removeEndIgnoreCase(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeEndIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeEndIgnoreCaseNotEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.removeEndIgnoreCase(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeEnd(String, String)} is NOT equals to expected
   * value.
   *
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeEndNotEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(Strings.CS.removeEnd(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#remove(String, String)} is equals to expected value.
   *
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.remove(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeIgnoreCase(String, String)} is equals to expected
   * value.
   *
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeIgnoreCaseEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.removeIgnoreCase(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeIgnoreCaseNotEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.removeIgnoreCase(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#remove(String, String)} is NOT equals to expected value.
   *
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeNotEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.remove(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeStart(String, String)} is equals to expected
   * value.
   *
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeStartEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.removeStart(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeStartIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeStartIgnoreCaseEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.removeStartIgnoreCase(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeStartIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeStartIgnoreCaseNotEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.removeStartIgnoreCase(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#removeStart(String, String)} is NOT equals to expected
   * value.
   *
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean removeStartNotEquals(String remove, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.removeStart(_get(), remove), expected);
  }

  /**
   * Verify if result of {@link StringUtils#replace(String, String, String)} is equals to expected
   * value.
   *
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   * @return execution boolean result
   */
  default boolean replaceEquals(String searchString, String replacement, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.replace(_get(), searchString, replacement), expected);
  }

  /**
   * Verify if result of {@link StringUtils#replaceIgnoreCase(String, String, String)} is equals to
   * expected value.
   *
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   * @return execution boolean result
   */
  default boolean replaceIgnoreCaseEquals(
      String searchString, String replacement, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(
        StringUtils.replaceIgnoreCase(_get(), searchString, replacement), expected);
  }

  /**
   * Verify if result of {@link StringUtils#replaceIgnoreCase(String, String, String)} is NOT equals
   * to expected value.
   *
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   * @return execution boolean result
   */
  default boolean replaceIgnoreCaseNotEquals(
      String searchString, String replacement, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(
        StringUtils.replaceIgnoreCase(_get(), searchString, replacement), expected);
  }

  /**
   * Verify if result of {@link StringUtils#replace(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   * @return execution boolean result
   */
  default boolean replaceNotEquals(String searchString, String replacement, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.replace(_get(), searchString, replacement), expected);
  }

  /**
   * Verify if result of {@link StringUtils#replaceOnce(String, String, String)} is equals to
   * expected value.
   *
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   * @return execution boolean result
   */
  default boolean replaceOnceEquals(String searchString, String replacement, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.replaceOnce(_get(), searchString, replacement), expected);
  }

  /**
   * Verify if result of {@link StringUtils#replaceOnceIgnoreCase(String, String, String)} is equals
   * to expected value.
   *
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   * @return execution boolean result
   */
  default boolean replaceOnceIgnoreCaseEquals(
      String searchString, String replacement, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(
        StringUtils.replaceOnceIgnoreCase(_get(), searchString, replacement), expected);
  }

  /**
   * Verify if result of {@link StringUtils#replaceOnceIgnoreCase(String, String, String)} is NOT
   * equals to expected value.
   *
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   * @return execution boolean result
   */
  default boolean replaceOnceIgnoreCaseNotEquals(
      String searchString, String replacement, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(
        StringUtils.replaceOnceIgnoreCase(_get(), searchString, replacement), expected);
  }

  /**
   * Verify if result of {@link StringUtils#replaceOnce(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   * @return execution boolean result
   */
  default boolean replaceOnceNotEquals(String searchString, String replacement, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.replaceOnce(_get(), searchString, replacement), expected);
  }

  /**
   * Verify if result of {@link StringUtils#reverse(String)} is equals to expected value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean reverseEquals(String expected) {
    String a = _get();
    return a != null && expected != null && Strings.CS.equals(StringUtils.reverse(a), expected);
  }

  /**
   * Verify if result of {@link StringUtils#reverse(String)} is NOT equals to expected value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean reverseNotEquals(String expected) {
    String a = _get();
    return a != null && expected != null && !Strings.CS.equals(StringUtils.reverse(a), expected);
  }

  /**
   * Verify if result of {@link StringUtils#rightPad(String, int, String)} is equals to expected
   * value.
   *
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean rightPadEquals(int size, String padStr, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.rightPad(_get(), size, padStr), expected);
  }

  /**
   * Verify if result of {@link StringUtils#rightPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean rightPadNotEquals(int size, String padStr, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.rightPad(_get(), size, padStr), expected);
  }

  /**
   * Verify if result of {@link StringUtils#right(String, int)} equals to expected value.
   *
   * @param expected the expected result.
   * @param len      the length of the required String
   * @return execution boolean result
   */
  default boolean rightValueEquals(int len, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.right(_get(), len), expected);
  }

  /**
   * Verify if result of {@link StringUtils#right(String, int)} NOT equals to expected value.
   *
   * @param expected the expected result.
   * @param len      the length of the required String
   * @return execution boolean result
   */
  default boolean rightValueNotEquals(int len, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.right(_get(), len), expected);
  }

  /**
   * Verify if result of {@link StringUtils#startsWith(CharSequence, CharSequence)} is true
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean startsWith(String expected) {
    return _get() != null && expected != null && StringUtils.startsWith(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#startsWithAny(CharSequence, CharSequence...)} is true
   *
   * @param searchInputs the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                     null}
   * @return execution boolean result
   */
  default boolean startsWithAny(Iterable<String> searchInputs) {
    return _get() != null
        && searchInputs != null
        && StringUtils.startsWithAny(_get(), toStringArray(searchInputs));
  }

  /**
   * Verify if result of {@link StringUtils#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * true.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean startsWithIgnoreCase(String expected) {
    return _get() != null && expected != null && StringUtils.startsWithIgnoreCase(_get(), expected);
  }

  /**
   * Verify if result of {@link StringUtils#startsWithAny(CharSequence, CharSequence...)} is false
   *
   * @param searchInputs the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                     null}
   * @return execution boolean result
   */
  default boolean startsWithNone(Iterable<String> searchInputs) {
    return _get() != null
        && searchInputs != null
        && !StringUtils.startsWithAny(_get(), toStringArray(searchInputs));
  }

  /**
   * Verify if {@link StringUtils#stripEnd(String, String)} value equals the expected value.
   *
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @return execution boolean result
   */
  default boolean stripedEndValue(String stripChars, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.stripEnd(_get(), stripChars), expected);
  }

  /**
   * Verify if {@link StringUtils#stripEnd(String, String)} value NOT equals the expected value.
   *
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @return execution boolean result
   */
  default boolean stripedEndValueNot(String stripChars, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.stripEnd(_get(), stripChars), expected);
  }

  /**
   * Verify if {@link StringUtils#stripStart(String, String)} value equals the expected value.
   *
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @return execution boolean result
   */
  default boolean stripedStartValue(String stripChars, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.stripStart(_get(), stripChars), expected);
  }

  /**
   * Verify if {@link StringUtils#stripStart(String, String)} value NOT equals the expected value.
   *
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @return execution boolean result
   */
  default boolean stripedStartValueNot(String stripChars, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.stripStart(_get(), stripChars), expected);
  }

  /**
   * Verify if {@link StringUtils#strip(String)} value equals the expected value.
   *
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @return execution boolean result
   */
  default boolean stripedValue(String stripChars, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.strip(_get(), stripChars), expected);
  }

  /**
   * Verify if {@link StringUtils#strip(String)} value NOT equals the expected value.
   *
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @return execution boolean result
   */
  default boolean stripedValueNot(String stripChars, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.strip(_get(), stripChars), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringAfter(String, String)} equals to expected
   * value.
   *
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @return execution boolean result
   */
  default boolean substringAfterEquals(String separator, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.substringAfter(_get(), separator), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringAfterLast(String, String)} equals to expected
   * value.
   *
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @return execution boolean result
   */
  default boolean substringAfterLastEquals(String separator, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.substringAfterLast(_get(), separator), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringAfterLast(String, String)} NOT equals to
   * expected value.
   *
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @return execution boolean result
   */
  default boolean substringAfterLastNotEquals(String separator, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.substringAfterLast(_get(), separator), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringAfter(String, String)} NOT equals to expected
   * value.
   *
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @return execution boolean result
   */
  default boolean substringAfterNotEquals(String separator, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.substringAfter(_get(), separator), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringBefore(String, String)} equals to expected
   * value.
   *
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @return execution boolean result
   */
  default boolean substringBeforeEquals(String separator, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.substringBefore(_get(), separator), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringBeforeLast(String, String)} equals to expected
   * value.
   *
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @return execution boolean result
   */
  default boolean substringBeforeLastEquals(String separator, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.substringBeforeLast(_get(), separator), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringBeforeLast(String, String)} NOT equals to
   * expected value.
   *
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @return execution boolean result
   */
  default boolean substringBeforeLastNotEquals(String separator, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.substringBeforeLast(_get(), separator), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringBefore(String, String)} NOT equals to expected
   * value.
   *
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @return execution boolean result
   */
  default boolean substringBeforeNotEquals(String separator, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.substringBefore(_get(), separator), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringBetween(String, String)} equals to expected
   * value.
   *
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @return execution boolean result
   */
  default boolean substringBetweenEquals(String open, String close, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.substringBetween(_get(), open, close), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringBetween(String, String)} NOT equals to expected
   * value.
   *
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @return execution boolean result
   */
  default boolean substringBetweenNotEquals(String open, String close, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.substringBetween(_get(), open, close), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substring(String, int)} equals to expected value.
   *
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @return execution boolean result
   */
  default boolean substringEquals(int start, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.substring(_get(), start), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substring(String, int, int)} equals to expected value.
   *
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @param end      the position to end at (exclusive), negative means count back from the end of the
   *                 String
   * @return execution boolean result
   */
  default boolean substringEquals(int start, int end, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.substring(_get(), start, end), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substring(String, int)} NOT equals to expected value.
   *
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @return execution boolean result
   */
  default boolean substringNotEquals(int start, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.substring(_get(), start), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substring(String, int, int)} NOT equals to expected
   * value.
   *
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @param end      the position to end at (exclusive), negative means count back from the end of the
   *                 String
   * @return execution boolean result
   */
  default boolean substringNotEquals(int start, int end, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.substring(_get(), start, end), expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringsBetween(String, String, String)} contains to
   * expected value.
   *
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @return execution boolean result
   */
  default boolean substringsBetweenContains(String open, String close, String expected) {
    return _get() != null
        && expected != null
        && new CList<>(StringUtils.substringsBetween(_get(), open, close)).contains(expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringsBetween(String, String, String)} equals to
   * expected value.
   *
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @return execution boolean result
   */
  default boolean substringsBetweenEquals(String open, String close, Iterable<String> expected) {
    String[] substring = StringUtils.substringsBetween(_get(), open, close);
    if (_get() == null || expected == null || substring == null) {
      return false;
    }
    CList<String> strings = new CList<>(expected);
    return substring.length == strings.size() && Arrays.equals(substring, strings.toArray());
  }

  /**
   * Verify if result of {@link StringUtils#substringsBetween(String, String, String)} NOT contains
   * to expected value.
   *
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @return execution boolean result
   */
  default boolean substringsBetweenNotContains(String open, String close, String expected) {
    return _get() != null
        && expected != null
        && !new CList<>(StringUtils.substringsBetween(_get(), open, close)).contains(expected);
  }

  /**
   * Verify if result of {@link StringUtils#substringsBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @return execution boolean result
   */
  default boolean substringsBetweenNotEquals(String open, String close, Iterable<String> expected) {
    String[] substring = StringUtils.substringsBetween(_get(), open, close);
    if (_get() == null || expected == null || substring == null) {
      return false;
    }
    CList<String> strings = new CList<>(expected);
    return (substring.length != strings.size() || !Arrays.equals(substring, strings.toArray()));
  }

  /**
   * Verify if {@link StringUtils#trim(String)} value equals the expected value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean trimmedValueEquals(String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.trim(_get()), expected);
  }

  /**
   * Verify if {@link StringUtils#trim(String)} value NOT equals the expected value.
   *
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean trimmedValueNotEquals(String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.trim(_get()), expected);
  }

  /**
   * Verify if {@link StringUtils#truncate(String, int)} value equals the expected value.
   *
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean truncatedValueEquals(int maxWidth, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.truncate(_get(), maxWidth), expected);
  }

  /**
   * Verify if {@link StringUtils#truncate(String, int, int)} value equals the expected value.
   *
   * @param offset   left edge of string to start truncate from
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean truncatedValueEquals(int offset, int maxWidth, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.truncate(_get(), offset, maxWidth), expected);
  }

  /**
   * Verify if {@link StringUtils#truncate(String, int)} value NOT equals the expected value.
   *
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean truncatedValueNotEquals(int maxWidth, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.truncate(_get(), maxWidth), expected);
  }

  /**
   * Verify if {@link StringUtils#truncate(String, int, int)} value NOT equals the expected value.
   *
   * @param offset   left edge of string to start truncate from
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   * @return execution boolean result
   */
  default boolean truncatedValueNotEquals(int offset, int maxWidth, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.truncate(_get(), offset, maxWidth), expected);
  }

  private String[] toStringArray(Iterable<String> input) {
    if (input == null) {
      return null;
    }
    return new CList<>(input).toArray(new String[0]);
  }
}
