package org.catools.common.tests.verify.wait;

import org.catools.common.extensions.wait.interfaces.CObjectWaiter;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.testng.annotations.Test;

public class CObjectWaiterTest extends CBaseUnitTest {

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals() {
    Object obj = new Object();
    verify.Bool.isTrue(toWaiter(obj).waitEquals(obj), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testEquals_BothNull() {
    verify.Bool.isTrue(toWaiter(null).waitEquals(null), "%s#%s", getParams());
  }

  // Negative
  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_N1() {
    verify.Bool.isTrue(toWaiter(new Object()).waitEquals(new Object()), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_N2() {
    verify.Bool.isTrue(toWaiter(new Object()).waitEquals(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testEquals_N3() {
    verify.Bool.isTrue(toWaiter(null).waitEquals(new Object()), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotNull() {
    verify.Bool.isTrue(toWaiter(new Object()).waitIsNotNull(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testIsNotNull_N() {
    verify.Bool.isFalse(toWaiter(null).waitIsNotNull(1), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testNotEquals() {
    verify.Bool.isTrue(toWaiter(new Object()).waitNotEquals(new Object()), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(null).waitNotEquals(new Object()), "%s#%s", getParams());
    verify.Bool.isTrue(toWaiter(new Object()).waitNotEquals(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEquals_BothNull() {
    verify.Bool.isTrue(toWaiter(null).waitNotEquals(null), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testNotEquals_N() {
    Object obj = new Object();
    verify.Bool.isTrue(toWaiter(obj).waitNotEquals(obj), "%s#%s", getParams());
  }

  private CObjectWaiter toWaiter(Object val) {
    return () -> val;
  }
}
