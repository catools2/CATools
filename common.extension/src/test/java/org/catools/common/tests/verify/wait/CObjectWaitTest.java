package org.catools.common.tests.verify.wait;

import org.catools.common.extensions.wait.CObjectWait;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.testng.annotations.Test;

public class CObjectWaitTest extends CBaseUnitTest {

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals() {
    Object obj = new Object();
    verify.Bool.isTrue(toWaiter().waitEquals(obj, obj, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals_BothNull() {
    verify.Bool.isTrue(toWaiter().waitEquals(null, null, 0, 100), "%s#%s", getParams());
  }

  // Negative
  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_N1() {
    verify.Bool.isTrue(
        toWaiter().waitEquals(new Object(), new Object(), 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_N2() {
    verify.Bool.isTrue(toWaiter().waitEquals(new Object(), null, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals_N3() {
    verify.Bool.isFalse(toWaiter().waitEquals(null, new Object(), 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotNull() {
    verify.Bool.isTrue(toWaiter().waitIsNotNull(new Object(), 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotNull_N() {
    verify.Bool.isFalse(toWaiter().waitIsNotNull(null, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals() {
    verify.Bool.isTrue(
        toWaiter().waitNotEquals(new Object(), new Object(), 0, 100), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter().waitNotEquals(null, new Object(), 0, 100), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter().waitNotEquals(new Object(), null, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals_BothNull() {
    verify.Bool.isFalse(toWaiter().waitNotEquals(null, null, 0, 100), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEquals_N() {
    Object obj = new Object();
    verify.Bool.isTrue(toWaiter().waitNotEquals(obj, obj, 0, 100), "%s#%s", getParams());
  }

  private CObjectWait toWaiter() {
    return new CObjectWait();
  }
}
