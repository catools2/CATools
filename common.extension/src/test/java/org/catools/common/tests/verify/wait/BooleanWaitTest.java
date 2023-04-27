package org.catools.common.tests.verify.wait;

import org.catools.common.extensions.wait.CBooleanWait;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.testng.annotations.Test;

public class BooleanWaitTest extends CBaseUnitTest {
  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals() {
    verify.Bool.isTrue(new CBooleanWait().waitEquals(true, true, 0, 100), "%s#%s", getParams());
    verify.Bool.isTrue(new CBooleanWait().waitEquals(false, false, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_ActualNull() {
    verify.Bool.isTrue(new CBooleanWait().waitEquals(null, false, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals_BothNull() {
    verify.Bool.isTrue(new CBooleanWait().waitEquals(null, null, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_ExpectedNull() {
    verify.Bool.isTrue(new CBooleanWait().waitEquals(true, null, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_N() {
    verify.Bool.isTrue(new CBooleanWait().waitEquals(true, false, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsFalse() {
    verify.Bool.isTrue(new CBooleanWait().waitIsFalse(false, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsFalse_N() {
    verify.Bool.isTrue(new CBooleanWait().waitIsFalse(true, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsTrue() {
    verify.Bool.isTrue(new CBooleanWait().waitIsTrue(true, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsTrue_N() {
    verify.Bool.isTrue(new CBooleanWait().waitIsTrue(false, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals() {
    verify.Bool.isTrue(new CBooleanWait().waitNotEquals(false, true, 0, 100), "%s#%s", getParams());
    verify.Bool.isTrue(
        new CBooleanWait().waitNotEquals(true, Boolean.valueOf(false), 0, 100),
        "%s#%s",
        getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals_ActualNull() {
    verify.Bool.isTrue(new CBooleanWait().waitNotEquals(null, false, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEquals_BothNull() {
    verify.Bool.isTrue(new CBooleanWait().waitNotEquals(null, null, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals_ExpectedNull() {
    verify.Bool.isTrue(new CBooleanWait().waitNotEquals(true, null, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEquals_N() {
    verify.Bool.isTrue(new CBooleanWait().waitNotEquals(true, true, 0, 100), "%s#%s", getParams());
  }
}
