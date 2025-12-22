package org.catools.common.faker.util;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.catools.common.exception.CInvalidRangeException;
import org.catools.common.faker.CRandom;
import org.catools.common.utils.CIterableUtil;

/**
 * Utility class for generating Lorem Ipsum style text with customizable parameters.
 *
 * <p>This class provides methods to generate random text at different levels: words, statements,
 * and paragraphs. Each level can be customized with length ranges and other parameters. The
 * generated text uses lowercase letters and digits, with proper capitalization and punctuation
 * applied automatically.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Generate a single word between 3-8 characters
 * String word = CLoremIpsum.getWord(3, 8);
 *
 * // Generate a paragraph with custom parameters
 * String paragraph = CLoremIpsum.getParagraph(2, 10, 20, 50, 100, 200);
 * }</pre>
 *
 * @since 1.0
 */
public class CLoremIpsum {
  private static final Set<String> specialChars =
      new HashSet<>(List.of("!?,;......".split(StringUtils.EMPTY)));

  /**
   * Generates a single paragraph with customizable word, statement, and paragraph length
   * parameters.
   *
   * <p>The paragraph is constructed by combining multiple statements until the target paragraph
   * length is reached. Each statement starts with a capital letter and ends with a random
   * punctuation mark. The final paragraph is trimmed to the exact specified length and ends with a
   * special character.
   *
   * @param minWordLength minimum length for each word (must be non-negative)
   * @param maxWordLength maximum length for each word (must be >= minWordLength)
   * @param minStatementLength minimum length for each statement (must be non-negative)
   * @param maxStatementLength maximum length for each statement (must be >= minStatementLength)
   * @param minParagraphLength minimum length for the paragraph (must be non-negative)
   * @param maxParagraphLength maximum length for the paragraph (must be >= minParagraphLength)
   * @return a randomly generated paragraph within the specified length range
   * @throws CInvalidRangeException if any minimum value exceeds its corresponding maximum value, or
   *     if any value is negative
   * @example
   *     <pre>{@code
   * // Generate a paragraph with words 3-8 chars, statements 15-30 chars, paragraph 80-150 chars
   * String paragraph = CLoremIpsum.getParagraph(3, 8, 15, 30, 80, 150);
   * // Result: "Abc defgh ijkl mnop. Qrstu vwxyz abc defg hij! Klmno pqrstu..."
   * }</pre>
   */
  public static String getParagraph(
      int minWordLength,
      int maxWordLength,
      int minStatementLength,
      int maxStatementLength,
      int minParagraphLength,
      int maxParagraphLength) {
    if (minParagraphLength > maxParagraphLength) {
      throw new CInvalidRangeException(
          "Min paragraph length value must be smaller or equal to max paragraph length.");
    }
    if (minParagraphLength < 0) {
      throw new CInvalidRangeException("Both paragraph length range values must be non-negative.");
    }
    StringBuilder sb = new StringBuilder();
    int length = CRandom.Int.next(minParagraphLength, maxParagraphLength);
    while (sb.toString().trim().length() < length) {
      sb.append(
          getStatement(minWordLength, maxWordLength, minStatementLength, maxStatementLength).trim()
              + " ");
    }
    return sb.toString().trim().substring(0, length - 1) + CIterableUtil.getRandom(specialChars);
  }

  /**
   * Generates multiple paragraphs with the specified parameters.
   *
   * <p>Each paragraph is generated independently using the same length parameters, and all
   * paragraphs are joined together with spaces.
   *
   * @param minWordLength minimum length for each word (must be non-negative)
   * @param maxWordLength maximum length for each word (must be >= minWordLength)
   * @param minStatementLength minimum length for each statement (must be non-negative)
   * @param maxStatementLength maximum length for each statement (must be >= minStatementLength)
   * @param minParagraphLength minimum length for each paragraph (must be non-negative)
   * @param maxParagraphLength maximum length for each paragraph (must be >= minParagraphLength)
   * @param count number of paragraphs to generate (should be positive)
   * @return multiple paragraphs joined with spaces
   * @throws CInvalidRangeException if any minimum value exceeds its corresponding maximum value, or
   *     if any value is negative
   * @example
   *     <pre>{@code
   * // Generate 3 paragraphs with words 2-6 chars, statements 10-25 chars, paragraphs 50-100 chars
   * String paragraphs = CLoremIpsum.getParagraphs(2, 6, 10, 25, 50, 100, 3);
   * // Result: "Abc def ghij klm. Nop qrstu! Lorem ipsum dolor sit amet. Consectetur adipiscing..."
   * }</pre>
   */
  public static String getParagraphs(
      int minWordLength,
      int maxWordLength,
      int minStatementLength,
      int maxStatementLength,
      int minParagraphLength,
      int maxParagraphLength,
      int count) {
    List<String> sb = new ArrayList<>();
    while (count-- > 0) {
      sb.add(
          getParagraph(
              minWordLength,
              maxWordLength,
              minStatementLength,
              maxStatementLength,
              minParagraphLength,
              maxParagraphLength));
    }
    return StringUtils.join(sb, " ");
  }

  /**
   * Generates a single statement with customizable word and statement length parameters.
   *
   * <p>A statement is constructed by combining multiple words until the target statement length is
   * reached. The statement starts with a random capital letter and ends with a random punctuation
   * mark (from the set: !?,;......). The final statement is trimmed to the exact specified length.
   *
   * @param minWordLength minimum length for each word (must be non-negative)
   * @param maxWordLength maximum length for each word (must be >= minWordLength)
   * @param minStatementLength minimum length for the statement (must be non-negative)
   * @param maxStatementLength maximum length for the statement (must be >= minStatementLength)
   * @return a randomly generated statement within the specified length range
   * @throws CInvalidRangeException if any minimum value exceeds its corresponding maximum value, or
   *     if any value is negative
   * @example
   *     <pre>{@code
   * // Generate a statement with words 2-7 chars and statement length 20-40 chars
   * String statement = CLoremIpsum.getStatement(2, 7, 20, 40);
   * // Result: "Lorem ipsum dolor sit amet!"
   * }</pre>
   */
  public static String getStatement(
      int minWordLength, int maxWordLength, int minStatementLength, int maxStatementLength) {
    if (minStatementLength > maxStatementLength) {
      throw new CInvalidRangeException(
          "Min statement length value must be smaller or equal to max statement length.");
    }
    if (minStatementLength < 0) {
      throw new CInvalidRangeException("Both statement length range values must be non-negative.");
    }
    StringBuilder sb = new StringBuilder();
    sb.append(CIterableUtil.getRandom("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(StringUtils.EMPTY)));
    int length = CRandom.Int.next(minStatementLength, maxStatementLength);
    while (sb.toString().trim().length() < length) {
      sb.append(getWord(minWordLength, maxWordLength) + " ");
    }
    return sb.toString().trim().substring(0, length - 1) + CIterableUtil.getRandom(specialChars);
  }

  /**
   * Generates multiple statements with the specified parameters.
   *
   * <p>Each statement is generated independently using the same length parameters, and all
   * statements are joined together with spaces.
   *
   * @param minWordLength minimum length for each word (must be non-negative)
   * @param maxWordLength maximum length for each word (must be >= minWordLength)
   * @param minStatementLength minimum length for each statement (must be non-negative)
   * @param maxStatementLength maximum length for each statement (must be >= minStatementLength)
   * @param count number of statements to generate (should be positive)
   * @return multiple statements joined with spaces
   * @throws CInvalidRangeException if any minimum value exceeds its corresponding maximum value, or
   *     if any value is negative
   * @example
   *     <pre>{@code
   * // Generate 4 statements with words 3-6 chars and statement length 15-25 chars
   * String statements = CLoremIpsum.getStatements(3, 6, 15, 25, 4);
   * // Result: "Lorem ipsum dolor! Sit amet consectetur? Adipiscing elit sed. Do eiusmod tempor;"
   * }</pre>
   */
  public static String getStatements(
      int minWordLength,
      int maxWordLength,
      int minStatementLength,
      int maxStatementLength,
      int count) {
    List<String> sb = new ArrayList<>();
    while (count-- > 0) {
      sb.add(getStatement(minWordLength, maxWordLength, minStatementLength, maxStatementLength));
    }
    return StringUtils.join(sb, " ");
  }

  /**
   * Generates a single word with a random length within the specified range.
   *
   * <p>The word consists of lowercase letters and digits, randomly selected from the character
   * range 'a' to 'z'. The actual length is randomly chosen between the minimum and maximum values
   * (inclusive).
   *
   * @param minWordLength minimum length for the word (must be non-negative)
   * @param maxWordLength maximum length for the word (must be >= minWordLength)
   * @return a randomly generated word within the specified length range
   * @throws CInvalidRangeException if minWordLength > maxWordLength or if any value is negative
   * @example
   *     <pre>{@code
   * // Generate a word between 4 and 10 characters long
   * String word = CLoremIpsum.getWord(4, 10);
   * // Result: "lorem" or "ipsum" or "dolorsit" (examples of possible outputs)
   * }</pre>
   */
  public static String getWord(int minWordLength, int maxWordLength) {
    if (minWordLength > maxWordLength) {
      throw new CInvalidRangeException(
          "Min word length value must be smaller or equal to max word length.");
    }
    if (minWordLength < 0) {
      throw new CInvalidRangeException("Both word length range values must be non-negative.");
    }
    return new RandomStringGenerator.Builder()
        .withinRange('a', 'z')
        .filteredBy(LETTERS, DIGITS)
        .build()
        .generate(minWordLength, maxWordLength);
  }

  /**
   * Generates multiple words with the specified length parameters.
   *
   * <p>Each word is generated independently using the same length parameters, and all words are
   * joined together with spaces.
   *
   * @param minWordLength minimum length for each word (must be non-negative)
   * @param maxWordLength maximum length for each word (must be >= minWordLength)
   * @param count number of words to generate (should be positive)
   * @return multiple words joined with spaces
   * @throws CInvalidRangeException if minWordLength > maxWordLength or if any value is negative
   * @example
   *     <pre>{@code
   * // Generate 5 words, each between 3 and 7 characters long
   * String words = CLoremIpsum.getWords(3, 7, 5);
   * // Result: "lorem ipsum dolor sit amet" (example of possible output)
   * }</pre>
   */
  public static String getWords(int minWordLength, int maxWordLength, int count) {
    List<String> sb = new ArrayList<>();
    while (count-- > 0) {
      sb.add(getWord(minWordLength, maxWordLength));
    }
    return StringUtils.join(sb, " ");
  }
}
