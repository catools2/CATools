package org.catools.common.tests.types.interfaces;

import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CCollection;
import org.catools.common.extensions.verify.CVerifier;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.testng.annotations.Test;

public class CCollectionVerifierTest extends CBaseUnitTest {

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyHas() {
    new CList<>("A", "B", "D", "C")
        .verifyHas(this, s -> s.equals("D"), "CCollectionTest ::> verifyContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyHas1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyHas(verifier, s -> s.equals("D"), "CCollectionTest ::> verifyContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyHas1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyHas(verifier, s -> s.equals("x"), "CCollectionTest ::> verifyContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContains() {
    new CList<>("A", "B", "D", "C").verifyContains(this, "D", "CCollectionTest ::> verifyContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContains1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyContains(verifier, "D", "CCollectionTest ::> verifyContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContains1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyContains(verifier, "X", "CCollectionTest ::> verifyContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContainsAll() {
    new CList<>("A", "B", "D", "C")
        .verifyContainsAll(
            this, new CSet<>("A", "B", "B"), "CCollectionTest ::> verifyContainsAll");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContainsAll1() {
    CVerifier verifier = new CVerifier();
    new CSet<>("A", "B", "D", "C")
        .verifyContainsAll(
            verifier, new CList<>("A", "B", "B"), "CCollectionTest ::> verifyContainsAll");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContainsAll1_N() {
    CVerifier verifier = new CVerifier();
    new CSet<>("A", "B", "D", "C")
        .verifyContainsAll(
            verifier, new CList<>("A", "C", "X"), "CCollectionTest ::> verifyContainsAll");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContainsAll_N() {
    new CList<>("A", "B", "D", "C")
        .verifyContainsAll(
            this, new CSet<>("A", "B", "X"), "CCollectionTest ::> verifyContainsAll");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContainsNone() {
    new CList<>("A", "B", "D", "C")
        .verifyContainsNone(this, new CSet<>("Z", "X"), "CCollectionTest ::> verifyContainsNone");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContainsNone1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyContainsNone(
            verifier, new CSet<>("Z", "X"), "CCollectionTest ::> verifyContainsNone");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContainsNone1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyContainsNone(
            verifier, new CSet<>("C", "B"), "CCollectionTest ::> verifyContainsNone");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContainsNone_N() {
    new CList<>("A", "B", "D", "C")
        .verifyContainsNone(this, new CSet<>("A", "B"), "CCollectionTest ::> verifyContainsNone");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContains_N() {
    new CList<>("A", "B", "D", "C").verifyContains(this, "X", "CCollectionTest ::> verifyContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEmptyOrContains() {
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrContains(this, "C", "CCollectionTest ::> verifyEmptyOrContains");
    ((CCollection) new CList<>())
        .verifyEmptyOrContains(this, "Z", "CCollectionTest ::> verifyEmptyOrContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEmptyOrContains1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrContains(verifier, "D", "CCollectionTest ::> verifyEmptyOrContains");
    ((CCollection) new CList<>())
        .verifyEmptyOrContains(verifier, "X", "CCollectionTest ::> verifyEmptyOrContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEmptyOrContains1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrContains(verifier, "X", "CCollectionTest ::> verifyEmptyOrContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEmptyOrContains2_N() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrContains(verifier, null, "CCollectionTest ::> verifyEmptyOrContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEmptyOrContains_N() {
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrContains(this, "X", "CCollectionTest ::> verifyEmptyOrContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEmptyOrNotContains() {
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrNotContains(this, "X", "CCollectionTest ::> verifyEmptyOrNotContains");
    ((CCollection) new CList<>())
        .verifyEmptyOrNotContains(this, "Z", "CCollectionTest ::> verifyEmptyOrNotContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEmptyOrNotContains1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrNotContains(verifier, "Y", "CCollectionTest ::> verifyEmptyOrNotContains");
    ((CCollection) new CList<>())
        .verifyEmptyOrNotContains(verifier, "X", "CCollectionTest ::> verifyEmptyOrNotContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEmptyOrNotContains1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrNotContains(verifier, "C", "CCollectionTest ::> verifyEmptyOrNotContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEmptyOrNotContains_N() {
    new CList<>("A", "B", "D", "C")
        .verifyEmptyOrNotContains(this, "D", "CCollectionTest ::> verifyEmptyOrNotContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEquals() {
    new CList<>("A", "B", "D", "C")
        .verifyEquals(this, new CSet<>("A", "B", "C", "D"), "CCollectionTest ::> verifyEquals");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEquals1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyEquals(verifier, new CSet<>("A", "B", "C", "D"), "CCollectionTest ::> verifyEquals");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEquals1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyEquals(verifier, new CSet<>("A", "B", "X", "D"), "CCollectionTest ::> verifyEquals");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEquals_N() {
    new CList<>("A", "B", "D", "C")
        .verifyEquals(this, new CSet<>("A", "B", "X", "D"), "CCollectionTest ::> verifyEquals");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyIsEmpty() {
    new CList<>().verifyIsEmpty(this, "CCollectionTest ::> verifyIsEmpty");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyIsEmpty1() {
    CVerifier verifier = new CVerifier();
    new CList<>().verifyIsEmpty(verifier, "CCollectionTest ::> verifyIsEmpty");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyIsEmpty1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>(1).verifyIsEmpty(verifier, "CCollectionTest ::> verifyIsEmpty");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyIsEmpty_N() {
    new CList<>(1).verifyIsEmpty(this, "CCollectionTest ::> verifyIsEmpty");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyIsNotEmpty() {
    new CList<>("A", "B", "D", "C").verifyIsNotEmpty(this, "CCollectionTest ::> verifyIsNotEmpty");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyIsNotEmpty1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyIsNotEmpty(verifier, "CCollectionTest ::> verifyIsNotEmpty");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyIsNotEmpty1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>().verifyIsNotEmpty(verifier, "CCollectionTest ::> verifyIsNotEmpty");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyIsNotEmpty_N() {
    new CList<>().verifyIsNotEmpty(this, "CCollectionTest ::> verifyIsNotEmpty");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContains() {
    new CList<>("A", "B", "D", "C")
        .verifyNotContains(this, "Z", "CCollectionTest ::> verifyNotContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContains1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyNotContains(verifier, "X", "CCollectionTest ::> verifyNotContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyNotContains1_N() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifyNotContains(verifier, "C", "CCollectionTest ::> verifyNotContains");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContainsAll() {
    new CList<>("A", "B", "D", "C")
        .verifyNotContainsAll(
            this, new CSet<>("A", "B", "X"), "CCollectionTest ::> verifyNotContainsAll");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContainsAll1() {
    CVerifier verifier = new CVerifier();
    new CSet<>("A", "B", "D", "C")
        .verifyNotContainsAll(
            verifier, new CList<>("A", "B", "X"), "CCollectionTest ::> verifyNotContainsAll");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyNotContainsAll1_N() {
    CVerifier verifier = new CVerifier();
    new CSet<>("A", "B", "D", "C")
        .verifyNotContainsAll(
            verifier, new CList<>("A", "B", "C"), "CCollectionTest ::> verifyNotContainsAll");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyNotContainsAll_N() {
    new CList<>("A", "B", "D", "C")
        .verifyNotContainsAll(
            this, new CSet<>("A", "B", "C"), "CCollectionTest ::> verifyNotContainsAll");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyNotContains_N() {
    new CList<>("A", "B", "D", "C")
        .verifyNotContains(this, "C", "CCollectionTest ::> verifyNotContains");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeEquals() {
    new CList<>("A", "B", "D", "C")
        .verifySizeEquals(this, 4, "CCollectionTest ::> verifySizeEquals");
    new CSet<>("A", "B", "B", "C")
        .verifySizeEquals(this, 3, "CCollectionTest ::> verifySizeEquals");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeEquals1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifySizeEquals(verifier, 4, "CCollectionTest ::> verifySizeEquals");
    new CSet<>("A", "B", "B", "C")
        .verifySizeEquals(verifier, 3, "CCollectionTest ::> verifySizeEquals");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeIsGreaterThan() {
    new CList<>("A", "B", "D", "C")
        .verifySizeIsGreaterThan(this, 3, "CCollectionTest ::> verifySizeIsGreaterThan");
    new CSet<>("A", "B", "B", "C")
        .verifySizeIsGreaterThan(this, 2, "CCollectionTest ::> verifySizeIsGreaterThan");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeIsGreaterThan1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifySizeIsGreaterThan(verifier, 3, "CCollectionTest ::> verifySizeIsGreaterThan");
    new CSet<>("A", "B", "B", "C")
        .verifySizeIsGreaterThan(verifier, 2, "CCollectionTest ::> verifySizeIsGreaterThan");
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeIsLessThan() {
    new CList<>("A", "B", "D", "C")
        .verifySizeIsLessThan(this, 5, "CCollectionTest ::> verifySizeIsLessThan");
    new CSet<>("A", "B", "B", "C")
        .verifySizeIsLessThan(this, 4, "CCollectionTest ::> verifySizeIsLessThan");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeIsLessThan1() {
    CVerifier verifier = new CVerifier();
    new CList<>("A", "B", "D", "C")
        .verifySizeIsLessThan(verifier, 5, "CCollectionTest ::> verifySizeIsLessThan");
    new CSet<>("A", "B", "B", "C")
        .verifySizeIsLessThan(verifier, 4, "CCollectionTest ::> verifySizeIsLessThan");
    verifier.verify();
  }
}
