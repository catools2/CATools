package org.catools.common.tests.types.interfaces;

import com.google.common.collect.ImmutableMap;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CLinkedMap;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.extensions.verify.CVerifier;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.testng.annotations.Test;

public class CMapVerifierTest extends CBaseUnitTest {

  private static CMap<String, String> getStringLinkedMap1() {
    CLinkedMap<String, String> stringCLinkedMap = new CLinkedMap<>();
    stringCLinkedMap.put("C", "3");
    stringCLinkedMap.put("A", "1");
    stringCLinkedMap.put("B", "2");
    return stringCLinkedMap;
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContains() {
    getStringLinkedMap1().verifyContains(this, "A", "1", "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContainsAll() {
    getStringLinkedMap1()
        .verifyContainsAll(this, getStringLinkedMap1().get(), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContainsAll_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1()
        .verifyContainsAll(verifier, getStringLinkedMap1().get(), "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContainsAll_CVerifier_NotAll() {
    CMap<String, String> stringLinkedMap1 = getStringLinkedMap1();
    stringLinkedMap1.get().remove("A");
    CVerifier verifier = new CVerifier();
    stringLinkedMap1.verifyContainsAll(verifier, getStringLinkedMap1().get(), "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContainsAll_NotAll() {
    CMap<String, String> stringLinkedMap1 = getStringLinkedMap1();
    stringLinkedMap1.get().remove("A");
    stringLinkedMap1.verifyContainsAll(this, getStringLinkedMap1().get(), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContainsNone() {
    getStringLinkedMap1()
        .verifyContainsNone(this, ImmutableMap.of("Z", "2", "Y", "5"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContainsNone_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1()
        .verifyContainsNone(verifier, ImmutableMap.of("Z", "2", "Y", "5"), "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContainsNone_CVerifier_Contains_One() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1()
        .verifyContainsNone(verifier, ImmutableMap.of("A", "1", "Y", "5"), "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContainsNone_N() {
    getStringLinkedMap1()
        .verifyContainsNone(this, ImmutableMap.of("A", "1", "Y", "5"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyContains_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyContains(verifier, "A", "1", "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContains_CVerifier_NotContains() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyContains(verifier, "A", "2", "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyContains_N() {
    getStringLinkedMap1().verifyContains(this, "A", "2", "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEmptyOrContains2() {
    getStringLinkedMap1().verifyEmptyOrContains(this, "A", "1", "%s#%s", getParams());
    new CHashMap<String, String>().verifyEmptyOrContains(this, "A", "2", "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEmptyOrContains_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyEmptyOrContains(verifier, "A", "1", "%s#%s", getParams());
    new CHashMap<String, String>().verifyEmptyOrContains(verifier, "A", "2", "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEmptyOrNotContains() {
    getStringLinkedMap1().verifyEmptyOrNotContains(this, "A", "2", "%s#%s", getParams());
    new CHashMap<String, String>().verifyEmptyOrNotContains(this, "A", "1", "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEmptyOrNotContains_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyEmptyOrNotContains(verifier, "A", "2", "%s#%s", getParams());
    new CHashMap<String, String>()
        .verifyEmptyOrNotContains(verifier, "A", "1", "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEmptyOrNotContains_CVerifier_Contains() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyEmptyOrNotContains(verifier, "A", "1", "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyEmptyOrNotContains_Contains() {
    getStringLinkedMap1().verifyEmptyOrNotContains(this, "A", "1", "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEquals1() {
    CMap<String, String> stringLinkedMap1 = getStringLinkedMap1();
    stringLinkedMap1.verifyEquals(this, stringLinkedMap1.get(), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyEquals_CVerifier() {
    CVerifier verifier = new CVerifier();
    CMap<String, String> stringLinkedMap1 = getStringLinkedMap1();
    stringLinkedMap1.verifyEquals(verifier, stringLinkedMap1.get(), "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyIsEmpty() {
    new CLinkedMap<String, String>().verifyIsEmpty(this, "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyIsEmpty_CVerifier() {
    CVerifier verifier = new CVerifier();
    new CLinkedMap<String, String>().verifyIsEmpty(verifier, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyIsEmpty_NotEmpty() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyIsEmpty(verifier, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyIsNotEmpty() {
    getStringLinkedMap1().verifyIsNotEmpty(this, "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyIsNotEmpty_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyIsNotEmpty(verifier, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyIsNotEmpty_CVerifier_Empty() {
    CVerifier verifier = new CVerifier();
    new CLinkedMap<String, String>().verifyIsNotEmpty(verifier, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContains() {
    getStringLinkedMap1().verifyNotContains(this, "A", "2", "%s#%s", getParams());
    getStringLinkedMap1().verifyNotContains(this, "Z", "1", "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContainsAll() {
    getStringLinkedMap1()
        .verifyNotContainsAll(this, ImmutableMap.of("A", "2"), "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContainsAll_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1()
        .verifyNotContainsAll(verifier, ImmutableMap.of("A", "2", "B", "1"), "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifyNotContainsAll_CVerifier_ContainsAll() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1()
        .verifyNotContainsAll(verifier, getStringLinkedMap1().get(), "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContainsKey() {
    getStringLinkedMap1().verifyNotContains(this, "Z", "1", "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContainsKey_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyNotContains(verifier, "Z", "1", "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifyNotContains_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifyNotContains(verifier, "A", "2", "%s#%s", getParams());
    getStringLinkedMap1().verifyNotContains(verifier, "Z", "1", "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeEquals() {
    getStringLinkedMap1().verifySizeEquals(this, 3, "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeEquals_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifySizeEquals(verifier, 3, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifySizeEquals_CVerifier_N() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifySizeEquals(verifier, 1, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifySizeEquals_N() {
    getStringLinkedMap1().verifySizeEquals(this, 1, "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeIsGreaterThan() {
    getStringLinkedMap1().verifySizeIsGreaterThan(this, 2, "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeIsGreaterThan1() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifySizeIsGreaterThan(verifier, 2, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifySizeIsGreaterThan1_N() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifySizeIsGreaterThan(verifier, 10, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifySizeIsGreaterThan_N() {
    getStringLinkedMap1().verifySizeIsGreaterThan(this, 10, "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeIsLessThan() {
    getStringLinkedMap1().verifySizeIsLessThan(this, 4, "%s#%s", getParams());
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testVerifySizeIsLessThan_CVerifier() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifySizeIsLessThan(verifier, 4, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifySizeIsLessThan_CVerifier_N() {
    CVerifier verifier = new CVerifier();
    getStringLinkedMap1().verifySizeIsLessThan(verifier, 3, "%s#%s", getParams());
    verifier.verify();
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
  public void testVerifySizeIsLessThan_N() {
    getStringLinkedMap1().verifySizeIsLessThan(this, 3, "%s#%s", getParams());
  }
}
