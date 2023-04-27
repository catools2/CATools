package org.catools.common.tests.verify.wait;

import org.catools.common.extensions.wait.interfaces.CBooleanWaiter;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.testng.annotations.Test;

public class BooleanWaiterTest extends CBaseUnitTest {
  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals() {
    verify.Bool.isTrue(toWaiter(true).waitEquals(true), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(false).waitEquals(false), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_ActualNull() {
    verify.Bool.isTrue(toWaiter(null).waitEquals(false), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals_BothNull() {
    verify.Bool.isTrue(toWaiter(null).waitEquals(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_ExpectedNull() {
    verify.Bool.isTrue(toWaiter(true).waitEquals(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_N() {
    verify.Bool.isTrue(toWaiter(true).waitEquals(false), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsFalse() {
    verify.Bool.isTrue(toWaiter(false).waitIsFalse(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsFalse_N() {
    verify.Bool.isTrue(toWaiter(true).waitIsFalse(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsTrue() {
    verify.Bool.isTrue(toWaiter(true).waitIsTrue(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testIsTrue_N() {
    verify.Bool.isTrue(toWaiter(false).waitIsTrue(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals() {
    verify.Bool.isTrue(toWaiter(false).waitNotEquals(true), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(true).waitNotEquals(Boolean.valueOf(false)), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals_ActualNull() {
    verify.Bool.isTrue(toWaiter(null).waitNotEquals(false), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEquals_BothNull() {
    verify.Bool.isTrue(toWaiter(null).waitNotEquals(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals_ExpectedNull() {
    verify.Bool.isTrue(toWaiter(true).waitNotEquals(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEquals_N() {
    verify.Bool.isTrue(toWaiter(true).waitNotEquals(true), "%s#%s", getParams());
  }

  private CBooleanWaiter toWaiter(Boolean val) {
    return () -> val;
  }
}
