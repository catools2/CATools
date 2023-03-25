package org.catools.common.tests.utils;

import org.catools.common.collections.CList;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.catools.common.utils.CStringUtil;
import org.testng.annotations.Test;

public class CStringUtilTest extends CBaseUnitTest {

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testRemoveWhiteSpaces() {
    verify.String.equals(
        CStringUtil.removeWhiteSpaces("Q !\tw\r2E#r4"), "Q!w2E#r4", "removeWhiteSpaces works fine");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testScrunch() {
    verify.String.equals(CStringUtil.scrunch("Q!w2E#r4"), "QW2ER4", "Scrunch works fine");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testScrunch_DontChangeCase() {
    verify.String.equals(CStringUtil.scrunch("Q!w2E#r4", false), "Qw2Er4", "Scrunch works fine");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testWordWrap() {
    String original = "AQ! SW e3 ".repeat(10);
    CList<String> strings = CList.of(CStringUtil.wordWrap(original, 10));
    strings.verifySizeEquals(this, 10, "wordWrap split long string to small ones");
    strings.forEach(
        s ->
            verify.Int.lessOrEqual(
                s.length(), 10, "String length is less than or equal to max Length"));
    verify.String.equals(strings.join(" "), original, "Strings match");
  }
}
