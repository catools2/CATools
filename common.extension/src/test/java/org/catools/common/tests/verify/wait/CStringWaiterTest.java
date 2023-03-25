package org.catools.common.tests.verify.wait;

import org.catools.common.collections.CList;
import org.catools.common.extensions.wait.interfaces.CStringWaiter;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

@Test(priority = 10)
public class CStringWaiterTest extends CBaseUnitTest {
  private static final String CSTRING1 =
      "This is the first String with some 1209op31mk2w9@# values.";
  private String NULL = null;
  private String EMPTY = "";

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testCenterPadEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCenterPadEquals(10, "@", "  some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ")
            .waitCenterPadEquals(30, "@", "@@@@@@  some string    @@@@@@@"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCenterPadEquals(10, NULL, "  some string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testCenterPadNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCenterPadNotEquals(10, "@", " some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ")
            .waitCenterPadNotEquals(30, "@", "@@@@@  some string    @@@@@@@"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCenterPadNotEquals(10, NULL, " some string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testCompare() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCompare("  some string    ", 0), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  SOME string    ").waitCompare("  some string    ", -32), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(NULL).waitCompare(null, 0), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCompare("  some String    ", 32), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("  some string    ").waitCompare(null, 1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(NULL).waitCompare("  some string    ", -1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testCompareIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCompareIgnoreCase("  SOME string    ", 0),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  SOME string    ").waitCompareIgnoreCase("  some string    ", 0),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(toWaiter(NULL).waitCompareIgnoreCase(null, 0), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCompareIgnoreCase("  some xtring    ", -5),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCompareIgnoreCase(null, 1), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter(NULL).waitCompareIgnoreCase("  some string    ", -1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testContains() {
    verify.Bool.isTrue(toWaiter("  some string    ").waitContains("so"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testContainsIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  Some string    ").waitContainsIgnoreCase(" so"), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some $tring    ").waitContainsIgnoreCase("$TRING"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEndsWith() {
    verify.Bool.isTrue(toWaiter("  some string   s ").waitEndsWith("   s "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEndsWithAny() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitEndsWithAny(new CList<>("A", null, " s ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEndsWithIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitEndsWithIgnoreCase("   s "), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitEndsWithIgnoreCase("   S "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEndsWithNone() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitEndsWithNone(new CList<>("A", " s  ")),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitEndsWithNone(new CList<>("A", " S ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEquals("  some string    "), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(NULL).waitEquals(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEqualsAny() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsAny(new CList<>("", "  some string    ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEqualsAnyIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitEqualsAnyIgnoreCase(new CList<>("", "  SOME string    ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEqualsIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsIgnoreCase("  SOME string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(toWaiter(NULL).waitEqualsIgnoreCase(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEqualsIgnoreWhiteSpaces() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsIgnoreWhiteSpaces(" s o me s t r ing    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsIgnoreWhiteSpaces("somestring"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEqualsNone() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsNone(new CList<>("", "  some String    ")),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsNone(new CList<>("", null)), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEqualsNoneIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ")
            .waitEqualsNoneIgnoreCase(new CList<>("", "  $ome string    ")),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsNoneIgnoreCase(new CList<>("", null)),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsAlpha() {
    verify.Bool.isTrue(toWaiter("aiulajksn").waitIsAlpha(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsEmptyOrAlpha() {
    verify.Bool.isTrue(toWaiter("aiulajksn").waitIsEmptyOrAlpha(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("").waitIsEmptyOrAlpha(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsAlphaSpace() {
    verify.Bool.isTrue(toWaiter(" aiul ajk sn").waitIsAlphaSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsAlphanumeric() {
    verify.Bool.isTrue(toWaiter("aiulaj45872ksn1").waitIsAlphanumeric(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("blkajsblas").waitIsAlphanumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsEmptyOrAlphanumeric() {
    verify.Bool.isTrue(
        toWaiter("aiulaj6265opksn").waitIsEmptyOrAlphanumeric(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("").waitIsEmptyOrAlphanumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsAlphanumericSpace() {
    verify.Bool.isTrue(toWaiter("ai1ul90jksn").waitIsAlphanumericSpace(1), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter(" ai1ul90 ajk sn").waitIsAlphanumericSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsAsciiPrintable() {
    char[] chars = "5rtfghuik".toCharArray();

    chars[5] = 32;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsAsciiPrintable(1), "%s#%s", getParams());

    chars[5] = 33;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsAsciiPrintable(1), "%s#%s", getParams());

    chars[5] = 125;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsAsciiPrintable(1), "%s#%s", getParams());

    chars[5] = 126;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsAsciiPrintable(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsBlank() {
    verify.Bool.isTrue(toWaiter(NULL).waitIsBlank(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(EMPTY).waitIsBlank(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testWait() {
    verify.Bool.isTrue(toWaiter(EMPTY).wait(s -> s.isEmpty()), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testWait2() {
    verify.Bool.isFalse(toWaiter(EMPTY).wait(s -> !s.isEmpty()), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsEmpty() {
    verify.Bool.isTrue(toWaiter(EMPTY).waitIsEmpty(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotAlpha() {
    verify.Bool.isTrue(toWaiter("123aasf2").waitIsNotAlpha(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("").waitIsNotAlpha(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsEmptyOrNotAlpha() {
    verify.Bool.isTrue(toWaiter("aiulaj626").waitIsEmptyOrNotAlpha(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("").waitIsEmptyOrNotAlpha(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotAlphaSpace() {
    verify.Bool.isTrue(toWaiter("aiulaj626").waitIsNotAlphaSpace(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("@ a").waitIsNotAlphaSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotAlphanumeric() {
    verify.Bool.isTrue(toWaiter("aiulaj626!5opksn").waitIsNotAlphanumeric(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("@#.*").waitIsNotAlphanumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsEmptyOrNotAlphanumeric() {
    verify.Bool.isTrue(
        toWaiter("aiulaj626 5opksn").waitIsEmptyOrNotAlphanumeric(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("@#.*").waitIsEmptyOrNotAlphanumeric(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("").waitIsEmptyOrNotAlphanumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotAlphanumericSpace() {
    verify.Bool.isTrue(
        toWaiter("aiulaj626 !5opksn").waitIsNotAlphanumericSpace(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("@#.*").waitIsNotAlphanumericSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotAsciiPrintable() {
    char[] chars = "5rtfghuik".toCharArray();
    chars[5] = 30;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsNotAsciiPrintable(1), "%s#%s", getParams());

    chars[5] = 31;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsNotAsciiPrintable(1), "%s#%s", getParams());

    chars[5] = 127;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsNotAsciiPrintable(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotBlank() {
    verify.Bool.isTrue(toWaiter(CSTRING1).waitIsNotBlank(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotEmpty() {
    verify.Bool.isTrue(toWaiter(CSTRING1).waitIsNotEmpty(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotNumeric() {
    verify.Bool.isTrue(toWaiter("a1234567").waitIsNotNumeric(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(" ").waitIsNotNumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsEmptyOrNotNumeric() {
    verify.Bool.isTrue(toWaiter("a123 4567").waitIsEmptyOrNotNumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotNumericSpace() {
    verify.Bool.isTrue(toWaiter("a123 4567").waitIsNotNumericSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNumeric() {
    verify.Bool.isTrue(toWaiter("1234567").waitIsNumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsEmptyOrNumeric() {
    verify.Bool.isTrue(toWaiter("1234567").waitIsEmptyOrNumeric(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("").waitIsEmptyOrNumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNumericSpace() {
    verify.Bool.isTrue(toWaiter("2345678").waitIsNumericSpace(1), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(" 1254 786 1").waitIsNumericSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testLeftPadEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftPadEquals(10, "@", "  some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftPadEquals(30, "@", "@@@@@@@@@@@@@  some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftPadEquals(10, NULL, "  some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitLeftPadEquals(30, "", "              some string   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testLeftPadNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftPadNotEquals(10, "@", " some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ")
            .waitLeftPadNotEquals(30, "@", "@@@@@@@@@@@@  some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftPadNotEquals(10, NULL, " some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitLeftPadNotEquals(30, "", "             some string   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testLeftValueEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftValueEquals(7, "  some "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testLeftValueNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftValueNotEquals(7, " some "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testLengthEquals() {
    verify.Bool.isTrue(toWaiter("  some string   s ").waitLengthEquals(18), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testLengthNotEquals() {
    verify.Bool.isTrue(toWaiter("aasa").waitLengthNotEquals(0), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testMidValueEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitMidValueEquals(2, 4, "some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testMidValueNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitMidValueNotEquals(2, 4, "some "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotContains() {
    verify.Bool.isTrue(toWaiter("  some string    ").waitNotContains("sox"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotContainsIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  Some string    ").waitNotContainsIgnoreCase(" sox"), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some $tring    ").waitNotContainsIgnoreCase("x$TRING"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEndsWith() {
    verify.Bool.isTrue(toWaiter("  some string   s ").waitNotEndsWith(".* "), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter("  some string   s ").waitNotEndsWith("S "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEndsWithIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitNotEndsWithIgnoreCase("   $ "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEqualsIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitNotEqualsIgnoreCase("  $OME string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(toWaiter(NULL).waitNotEqualsIgnoreCase(""), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEqualsIgnoreWhiteSpaces() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitNotEqualsIgnoreWhiteSpaces("  $OME string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotStartsWith() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitNotStartsWith(" some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotStartsWithIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitNotStartsWithIgnoreCase(" Some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNumberOfMatchesEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitNumberOfMatchesEquals("s", 3), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitNumberOfMatchesEquals("s", 2), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some $tring   s ").waitNumberOfMatchesEquals("$", 1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNumberOfMatchesNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitNumberOfMatchesNotEquals("s", 1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveEndEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveEndEquals("  some ", "  some string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveEndEquals("some string   s ", "  "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveEndEquals("  some string   s ", ""),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitRemoveEndEquals(null, "  some String   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitRemoveEndEquals("tring   s ", "  some S"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some $tring   s ").waitRemoveEndEquals("tring   s ", "  some $"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveEndIgnoreCaseEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitRemoveEndIgnoreCaseEquals("  Some ", "  some string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveEndIgnoreCaseEquals("some String   s ", "  "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveEndIgnoreCaseEquals("  sOME string   s ", ""),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitRemoveEndIgnoreCaseEquals(null, "  some String   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitRemoveEndIgnoreCaseEquals("tring   S ", "  some S"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some $tring   s ").waitRemoveEndIgnoreCaseEquals("TRING   s ", "  some $"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveEndIgnoreCaseNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitRemoveEndIgnoreCaseNotEquals(" ", "  STRING    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveEndNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitRemoveEndNotEquals("STRING    ", "  SOME "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveEquals("s", "  ome tring    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitRemoveEquals("so", "  me String    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveIgnoreCaseEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveIgnoreCaseEquals("s", "  ome tring    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitRemoveIgnoreCaseEquals("SO", "  me String    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveIgnoreCaseNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitRemoveIgnoreCaseNotEquals(" ", "  some STRING "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitRemoveNotEquals("STRING   ", "  some "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveStartEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveStartEquals("  some ", "string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitRemoveStartEquals("some string   s ", "  some string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveStartEquals("  some string   s ", ""),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitRemoveStartEquals(null, "  some String   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitRemoveStartEquals("  some S", "tring   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some $tring   s ").waitRemoveStartEquals("  some $", "tring   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveStartIgnoreCaseEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveStartIgnoreCaseEquals("  some ", "string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveStartIgnoreCaseEquals("  Some ", "string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitRemoveStartIgnoreCaseEquals("Some string   s ", "  some string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveStartIgnoreCaseEquals("  Some string   s ", ""),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitRemoveStartIgnoreCaseEquals(null, "  some String   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitRemoveStartIgnoreCaseEquals("  some s", "tring   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some $tring   s ").waitRemoveStartIgnoreCaseEquals("  some $", "tring   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveStartIgnoreCaseNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some $tring   s ")
            .waitRemoveStartIgnoreCaseNotEquals("  some ", " $tring   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveStartNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveStartNotEquals("  some ", "String   S "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReplaceEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitReplaceEquals("s", "", "  ome tring    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitReplaceEquals("so", "XX", "  XXme String   XX "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReplaceIgnoreCaseEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitReplaceIgnoreCaseEquals("s", "|", "  |ome |tring   | "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitReplaceIgnoreCaseEquals("SO", "x", "  xme String   x "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReplaceIgnoreCaseNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitReplaceIgnoreCaseNotEquals(" s", "x", " ome string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReplaceNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitReplaceNotEquals(" s", "x", " ome string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReplaceOnceEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitReplaceOnceEquals("s", "", "  ome string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitReplaceOnceEquals("so", "XX", "  XXme String   so "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReplaceOnceIgnoreCaseEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitReplaceOnceIgnoreCaseEquals("s", "|", "  |ome string   s "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some String   so ")
            .waitReplaceOnceIgnoreCaseEquals("SO", "x", "  xme String   so "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReplaceOnceIgnoreCaseNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ")
            .waitReplaceOnceIgnoreCaseNotEquals(" s", "x", " ome string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReplaceOnceNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitReplaceOnceNotEquals(" s", "x", " ome string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReverseEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitReverseEquals(" s   gnirts emos  "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some @#$%^&*.   so ").waitReverseEquals(" os   .*&^%$#@ emos  "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testReverseNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string  s ").waitReverseNotEquals(" s   gnirts emos  "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some @#$%^*.   so ").waitReverseNotEquals(" os   .*&^%$#@ emos  "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRightPadEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitRightPadEquals(10, "@", "  some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitRightPadEquals(30, "@", "  some string    @@@@@@@@@@@@@"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitRightPadEquals(10, NULL, "  some string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRightPadNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitRightPadNotEquals(40, "x", "  some string   s xxxxxxxxxxxxxxxxxxxxx"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRightValueEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitRightValueEquals(7, "ing    "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRightValueNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitRightValueNotEquals(7, "iNg    "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStartsWith() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWith("  some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStartsWithAny() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWithAny(new CList<>("A", null, "  some")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStartsWithIgnoreCase() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWithIgnoreCase("  some"), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWithIgnoreCase("  Some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStartsWithNone() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWithNone(new CList<>(" some", "     ", " s ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStripedEndValue() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedEndValue(" ", "  some string"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedEndValue(null, "  some string"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedEndValue("|", "|some string"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedEndValue(null, "|some string||||"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStripedEndValueNot() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedEndValueNot(" ", " some string"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedEndValueNot(null, "  somestring"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedEndValueNot("|", "|some string|"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedEndValueNot(null, "|some string|||"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStripedStartValue() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedStartValue(" ", "some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedStartValue(null, "some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedStartValue("|", "some string||||"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedStartValue(null, "|some string||||"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStripedStartValueNot() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedStartValueNot(" ", "some string   "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedStartValueNot(null, "some string   "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedStartValueNot("|", "some string|||"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedStartValueNot(null, "|some string|||"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStripedValue() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedValue(" ", "some string"), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedValue(null, "some string"), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedValue("|", "some string"), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedValue(null, "|some string||||"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testStripedValueNot() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedValueNot(" ", " some string"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedValueNot(null, "somestring"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedValueNot("|", "some string|"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("|some string||||").waitStripedValueNot(null, "|some string|||"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringAfterEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterEquals(" s", "ome string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterEquals(null, ""), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringAfterLastEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterLastEquals(" s", "tring    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterLastEquals(null, ""), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringAfterLastNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterLastNotEquals(" s", "trinG    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringAfterNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterNotEquals(" s", "omE string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringBeforeEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeEquals(" st", "  some"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeEquals(null, "  some string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringBeforeLastEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeLastEquals(" s", "  some"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeLastEquals(null, "  some string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringBeforeLastNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeLastNotEquals(" s", "  somE"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringBeforeNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeNotEquals(" st", "  Some"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringBetweenEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBetweenEquals("  ", "    ", "some string"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringBetweenNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBetweenNotEquals("  ", "    ", "some String"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringEquals(0, "  some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringEquals(0, 3, "  s"), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringEquals(2, 4, "so"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringNotEquals(0, " some string    "),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringNotEquals(0, 3, " s"), "%s#%s", getParams());
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringNotEquals(2, 4, "so "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringsBetweenContains() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitSubstringsBetweenContains(" ", "s", " "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringsBetweenEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitSubstringsBetweenEquals(" ", "s", new CList<>(" ", "", "  ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringsBetweenNotContains() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitSubstringsBetweenNotContains(" ", "s", "x"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testSubstringsBetweenNotEquals() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitSubstringsBetweenNotEquals(" ", "s", new CList<>(" ", "S", "  ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testTrimmedValue() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTrimmedValueEquals("some string"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testTrimmedValueNot() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTrimmedValueNotEquals(" some string"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testTruncatedValue() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTruncatedValueEquals(10, "some strin"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTruncatedValueEquals(4, 10, " string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testTruncatedValueNot() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTruncatedValueNotEquals(10, "some string"),
        "%s#%s",
        getParams());
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTruncatedValueNotEquals(4, 10, " string  "),
        "%s#%s",
        getParams());
  }

  // Negative Scenarios
  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testCenterPadEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCenterPadEquals(10, NULL, "  somestring    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testCenterPadNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCenterPadNotEquals(10, NULL, "  some string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testCompare_Negative() {
    verify.Bool.isTrue(toWaiter(NULL).waitCompare("  some string    ", 0), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testCompareIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitCompareIgnoreCase("  ScOME string    ", 1),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testContains_Negative() {
    verify.Bool.isTrue(toWaiter("  some string    ").waitContains("sso"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testContainsIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  Some string    ").waitContainsIgnoreCase(" sco"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEndsWith_Negative() {
    verify.Bool.isTrue(toWaiter("  some string   s ").waitEndsWith("   x "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEndsWithAny_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitEndsWithAny(new CList<>("X", null, " D ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEndsWithIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitEndsWithIgnoreCase("   xs "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEndsWithNone_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitEndsWithNone(new CList<>("a", " s ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_Negative() {
    verify.Bool.isTrue(toWaiter(NULL).waitEquals("x"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEqualsAny_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsAny(new CList<>("", "  sxme string    ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEqualsAnyIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitEqualsAnyIgnoreCase(new CList<>("", "  SXME string    ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEqualsIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsIgnoreCase("  SXME string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEqualsIgnoreWhiteSpaces_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitEqualsIgnoreWhiteSpaces(" s x me s t r ing    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEqualsNone_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ")
            .waitEqualsNone(new CList<>("  some string    ", "  sxe String    ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEqualsNoneIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ")
            .waitEqualsNoneIgnoreCase(new CList<>("  some string    ", null)),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsAlpha_Negative() {
    verify.Bool.isTrue(toWaiter("aiul@ajksn").waitIsAlpha(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsEmptyOrAlpha_Negative() {
    verify.Bool.isTrue(toWaiter("&").waitIsEmptyOrAlpha(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsAlphaSpace_Negative() {
    verify.Bool.isTrue(toWaiter(" aiu@l ajk sn").waitIsAlphaSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsAlphanumeric_Negative() {
    verify.Bool.isTrue(toWaiter("blka$jsblas").waitIsAlphanumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsAlphanumericSpace_Negative() {
    verify.Bool.isTrue(
        toWaiter(" ai1ul#@90 ajk sn").waitIsAlphanumericSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsAsciiPrintable_Negative() {
    char[] chars = "5rtfghuik".toCharArray();
    chars[5] = 30;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsAsciiPrintable(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsBlank_Negative() {
    verify.Bool.isTrue(toWaiter("asas").waitIsBlank(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsEmpty_Negative() {
    verify.Bool.isTrue(toWaiter("asas").waitIsEmpty(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotAlpha_Negative() {
    verify.Bool.isTrue(toWaiter("aasf").waitIsNotAlpha(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsEmptyOrNotAlpha_Negative() {
    verify.Bool.isTrue(toWaiter("aiulaj").waitIsEmptyOrNotAlpha(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotAlphaSpace_Negative() {
    verify.Bool.isTrue(toWaiter("aiulaj").waitIsNotAlphaSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotAlphanumeric_Negative() {
    verify.Bool.isTrue(toWaiter("aiulaj6265opksn").waitIsNotAlphanumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsEmptyOrNotAlphanumeric_Negative() {
    verify.Bool.isTrue(
        toWaiter("aiulaj6265opksn").waitIsEmptyOrNotAlphanumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotAlphanumericSpace_Negative() {
    verify.Bool.isTrue(
        toWaiter("aiulaj6265opksn").waitIsNotAlphanumericSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotAsciiPrintable_Negative() {
    char[] chars = "5rtfghuik".toCharArray();
    chars[5] = 32;
    verify.Bool.isTrue(
        toWaiter(String.valueOf(chars)).waitIsNotAsciiPrintable(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotBlank_Negative() {
    verify.Bool.isTrue(toWaiter("").waitIsNotBlank(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotEmpty_Negative() {
    verify.Bool.isTrue(toWaiter(EMPTY).waitIsNotEmpty(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotNumeric_Negative() {
    verify.Bool.isTrue(toWaiter("1").waitIsNotNumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsEmptyOrNotNumeric_Negative() {
    verify.Bool.isTrue(toWaiter("1234567").waitIsEmptyOrNotNumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotNumericSpace_Negative() {
    verify.Bool.isTrue(toWaiter("").waitIsNotNumericSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNumeric_Negative() {
    verify.Bool.isTrue(toWaiter("123a4567").waitIsNumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsEmptyOrNumeric_Negative() {
    verify.Bool.isTrue(toWaiter("1a234567").waitIsEmptyOrNumeric(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNumericSpace_Negative() {
    verify.Bool.isTrue(toWaiter("23a45678").waitIsNumericSpace(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testLeftPadEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitLeftPadEquals(30, "", "            some string   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testLeftPadNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitLeftPadNotEquals(30, "", "              some string   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testLeftValueEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftValueEquals(7, " some "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testLeftValueNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitLeftValueNotEquals(7, "  some "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testLengthEquals_Negative() {
    verify.Bool.isTrue(toWaiter("  some string   s ").waitLengthEquals(7), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testLengthNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitLengthNotEquals(18), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testMidValueEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitMidValueEquals(2, 4, "some "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testMidValueNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitMidValueNotEquals(2, 4, "some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotContains_Negative() {
    verify.Bool.isTrue(toWaiter("  some string    ").waitNotContains("some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotContainsIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  Some string    ").waitNotContainsIgnoreCase(" Some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEndsWith_Negative() {
    verify.Bool.isTrue(toWaiter("  some string   s ").waitNotEndsWith("s "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEndsWithIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitNotEndsWithIgnoreCase("   S "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEqualsIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitNotEqualsIgnoreCase("  SOME string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEqualsIgnoreWhiteSpaces_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitNotEqualsIgnoreWhiteSpaces("  so me string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotStartsWith_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitNotStartsWith("  some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotStartsWithIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitNotStartsWithIgnoreCase("  some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNumberOfMatchesEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitNumberOfMatchesEquals("s", 2), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNumberOfMatchesNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitNumberOfMatchesNotEquals("s", 2), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveEndEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveEndEquals("  some ", "  some string   S "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveEndIgnoreCaseEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some $tring   s ").waitRemoveEndIgnoreCaseEquals("TRING   x ", "  some $"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveEndIgnoreCaseNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitRemoveEndIgnoreCaseNotEquals(" ", "  some STRING   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveEndNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitRemoveEndNotEquals("STRING    ", "  some "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitRemoveEquals("so", "  me string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveIgnoreCaseEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitRemoveIgnoreCaseEquals("SO", "  me Xtring    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveIgnoreCaseNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitRemoveIgnoreCaseNotEquals(" ", "someSTRING"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some STRING    ").waitRemoveNotEquals("STRING   ", "  some  "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveStartEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveStartEquals("  some ", "  some string   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveStartIgnoreCaseEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitRemoveStartIgnoreCaseEquals("  some ", "some string   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveStartIgnoreCaseNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some $tring   s ").waitRemoveStartIgnoreCaseNotEquals("  some ", "$tring   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRemoveStartNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitRemoveStartNotEquals("  some ", "string   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReplaceEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitReplaceEquals("so", "XX", "  XXme String   XX"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReplaceIgnoreCaseEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitReplaceIgnoreCaseEquals("SO", "x", "  xme String   x"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReplaceIgnoreCaseNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitReplaceIgnoreCaseNotEquals(" s", "x", " xomextring  x "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReplaceNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitReplaceNotEquals(" s", "x", " xome String  x "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReplaceOnceEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   so ").waitReplaceOnceEquals("so", "XX", "  some String   so "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReplaceOnceIgnoreCaseEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitReplaceOnceIgnoreCaseEquals("s", "|", "  |ome string   s"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReplaceOnceIgnoreCaseNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ")
            .waitReplaceOnceIgnoreCaseNotEquals(" s", "x", " xome String   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReplaceOnceNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some String   s ").waitReplaceOnceNotEquals(" s", "x", " xome String   s "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReverseEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some @#$%^&*.   so ").waitReverseEquals(" os   .*&^%$#@ emos "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testReverseNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string  s ").waitReverseNotEquals(" s  gnirts emos  "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRightPadEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitRightPadEquals(10, "@", "  some string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRightPadNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitRightPadNotEquals(40, "x", "  some string   s xxxxxxxxxxxxxxxxxxxxxx"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRightValueEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitRightValueEquals(7, "ing   "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testRightValueNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitRightValueNotEquals(7, "ing    "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStartsWith_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWith(" some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStartsWithAny_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWithAny(new CList<>("A", null, "some")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStartsWithIgnoreCase_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWithIgnoreCase("some"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStartsWithNone_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitStartsWithNone(new CList<>("  some", "     ", "s ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStripedEndValue_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedEndValue(" ", "  some string "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStripedEndValueNot_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedEndValueNot(" ", "  some string"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStripedStartValue_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedStartValue(" ", "some string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStripedStartValueNot_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedStartValueNot(" ", "some string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStripedValue_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedValue(" ", "some string "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testStripedValueNot_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitStripedValueNot(" ", "some string"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringAfterEquals1_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterEquals(" s", "ome string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringAfterEquals2_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterEquals(null, "s"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringAfterLastEquals1_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterLastEquals(null, "s"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringAfterLastEquals2_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterLastEquals(" s", "tring   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringAfterLastNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterLastNotEquals(" s", "tring    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringAfterNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringAfterNotEquals(" s", "ome string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringBeforeEquals1_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeEquals(" st", "  some "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringBeforeEquals2_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeEquals(null, "  some string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringBeforeLastEquals1_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeLastEquals(" s", "  some "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringBeforeLastEquals2_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeLastEquals(" s", "  some "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringBeforeLastNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeLastNotEquals(" s", "  some"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringBeforeNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBeforeNotEquals(" st", "  some"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringBetweenEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBetweenEquals("  ", "    ", "some sstring"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringBetweenNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringBetweenNotEquals("  ", "    ", "some string"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringEquals1_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringEquals(0, "  some strin    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringEquals2_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringEquals(0, 3, "  sx"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringEquals3_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringEquals(2, 4, "sxo"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringNotEquals1_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringNotEquals(0, "  some string    "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringNotEquals2_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringNotEquals(0, 3, "  s"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringNotEquals3_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string    ").waitSubstringNotEquals(2, 4, "so"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringsBetweenContains_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitSubstringsBetweenContains(" ", "s", "x"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringsBetweenEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitSubstringsBetweenEquals(" ", "s", new CList<>(" ", "S")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringsBetweenNotContains_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ").waitSubstringsBetweenNotContains(" ", "s", " "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testSubstringsBetweenNotEquals_Negative() {
    verify.Bool.isTrue(
        toWaiter("  some string   s ")
            .waitSubstringsBetweenNotEquals(" ", "s", new CList<>(" ", "", "  ")),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testTrimmedValue_Negative() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTrimmedValueEquals("some String"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testTrimmedValueNot_Negative() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTrimmedValueNotEquals("some string"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testTruncatedValue1_Negative() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTruncatedValueEquals(10, "some sxtrin"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testTruncatedValue2_Negative() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTruncatedValueEquals(4, 10, " sxtring   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testTruncatedValueNot1_Negative() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTruncatedValueNotEquals(10, "some strin"),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testTruncatedValueNot2_Negative() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitTruncatedValueNotEquals(4, 10, " string   "),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsMatches() {
    verify.Bool.isTrue(toWaiter("some string    ").waitMatches("[a-z ]+"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsMatches_ExpectedNull() {
    verify.Bool.isTrue(toWaiter(null).waitMatches(" tring   "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsMatches_Negative() {
    verify.Bool.isTrue(toWaiter("some string    ").waitMatches("\\d+"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsMatchesPattern() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitMatches(Pattern.compile("[a-z ]+")), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsMatchesPattern_ExpectedNull() {
    verify.Bool.isTrue(toWaiter(null).waitMatches(" tring   "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsMatchesPattern_Negative() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitMatches(Pattern.compile("\\d+")), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotMatches() {
    verify.Bool.isTrue(toWaiter("some string    ").waitNotMatches("\\d+"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotMatches_ExpectedNull() {
    verify.Bool.isTrue(toWaiter(null).waitNotMatches(" tring   "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotMatches_Negative() {
    verify.Bool.isTrue(toWaiter("some string    ").waitNotMatches("[a-z ]+"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotMatchesPattern() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitNotMatches(Pattern.compile("\\d+")), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotMatchesPattern_ExpectedNull() {
    verify.Bool.isTrue(toWaiter(null).waitNotMatches(" tring   "), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsNotMatchesPattern_Negative() {
    verify.Bool.isTrue(
        toWaiter("some string    ").waitNotMatches(Pattern.compile("[a-z ]+")),
        "%s#%s",
        getParams());
  }

  private CStringWaiter toWaiter(String val) {
    return () -> val;
  }
}
