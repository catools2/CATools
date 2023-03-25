package org.catools.common.tests.verify.noretry.interfaces;

import org.catools.common.collections.CList;
import org.catools.common.extensions.verify.interfaces.CStringVerifier;
import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

@Test(retryAnalyzer = CTestRetryAnalyzer.class)
public class CStringVerifierTest extends CBaseUnitTest {

  private static CStringVerifier toCS(String s) {
    return new CStringVerifier() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public String get() {
        return s;
      }
    };
  }

  public class CenterPadEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testVerifyEquals() {
      toCS("  some string ")
          .verifyCenterPadEquals(
              CStringVerifierTest.this,
              40,
              "",
              "               some string              ",
              "%s#%s",
              getParams());
      toCS("  SOM@#$%^& o ")
          .verifyCenterPadEquals(
              CStringVerifierTest.this, 29, "&%", "&%&%&%&  SOM@#$%^& o &%&%&%&%");
      toCS("  SOM@#$%^& o ")
          .verifyCenterPadEquals(
              CStringVerifierTest.this, 20, "#$%^", "#$%  SOM@#$%^& o #$%", "%s#%s", getParams());
      toCS("  SOM@#$%^& o ")
          .verifyCenterPadEquals(CStringVerifierTest.this, 10, "", "  SOM@#$%^& o ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyCenterPadEquals(
              CStringVerifierTest.this,
              40,
              "",
              "               some string              ",
              "%s#%s",
              getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string ").verifyCenterPadEquals(CStringVerifierTest.this, 40, "", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string ")
          .verifyCenterPadEquals(
              CStringVerifierTest.this,
              40,
              "",
              "             some string              ",
              "%s#%s",
              getParams());
    }
  }

  public class CenterPadNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string ")
          .verifyCenterPadNotEquals(
              CStringVerifierTest.this, 40, "", "              some string              ");
      toCS("  SOM@#$%^& o ")
          .verifyCenterPadNotEquals(
              CStringVerifierTest.this, 20, "#$%^", "#$%  SOM@$%^& o #$%", "%s#%s", getParams());
      toCS("  SOM@#$%^& o ")
          .verifyCenterPadNotEquals(CStringVerifierTest.this, 10, "", "  SOM@#$%^& o");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyCenterPadNotEquals(
              CStringVerifierTest.this,
              40,
              "",
              "              some string              ",
              "%s#%s",
              getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string ").verifyCenterPadNotEquals(CStringVerifierTest.this, 40, "", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string ")
          .verifyCenterPadNotEquals(
              CStringVerifierTest.this,
              40,
              "",
              "               some string              ",
              "%s#%s",
              getParams());
    }
  }

  public class Compare {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ").verifyCompare(CStringVerifierTest.this, "  some string    ", 0);
      toCS("  SOME string    ")
          .verifyCompare(CStringVerifierTest.this, "  some string    ", -32, "%s#%s", getParams());
      toCS(null).verifyCompare(CStringVerifierTest.this, null, 0);
      toCS("  some string    ")
          .verifyCompare(CStringVerifierTest.this, "  some String    ", 32, "%s#%s", getParams());
      toCS("  some string    ").verifyCompare(CStringVerifierTest.this, null, 1);
      toCS(null)
          .verifyCompare(CStringVerifierTest.this, "  some string    ", -1, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyCompare(CStringVerifierTest.this, "  some string    ", 0);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyCompare(CStringVerifierTest.this, null, 0, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  SOME string    ").verifyCompare(CStringVerifierTest.this, "  some string    ", -1);
    }
  }

  public class CompareIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyCompareIgnoreCase(
              CStringVerifierTest.this, "  SOME string    ", 0, "%s#%s", getParams());
      toCS("  SOME string    ")
          .verifyCompareIgnoreCase(CStringVerifierTest.this, "  some string    ", 0);
      toCS(null).verifyCompareIgnoreCase(CStringVerifierTest.this, null, 0, "%s#%s", getParams());
      toCS("  some string    ")
          .verifyCompareIgnoreCase(CStringVerifierTest.this, "  some xtring    ", -5);
      toCS("  some string    ")
          .verifyCompareIgnoreCase(CStringVerifierTest.this, null, 1, "%s#%s", getParams());
      toCS(null).verifyCompareIgnoreCase(CStringVerifierTest.this, "  some string    ", -1);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyCompareIgnoreCase(
              CStringVerifierTest.this, "  SOME string    ", 0, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyCompareIgnoreCase(CStringVerifierTest.this, null, 0);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyCompareIgnoreCase(
              CStringVerifierTest.this, "SOME string    ", 0, "%s#%s", getParams());
    }
  }

  public class Contains {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ").verifyContains(CStringVerifierTest.this, "so");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyContains(CStringVerifierTest.this, "so", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyContains(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyContains(CStringVerifierTest.this, "sox", "%s#%s", getParams());
    }
  }

  public class ContainsIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  Some string    ").verifyContainsIgnoreCase(CStringVerifierTest.this, " so");
      toCS("  some $tring    ")
          .verifyContainsIgnoreCase(CStringVerifierTest.this, "$TRING", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyContainsIgnoreCase(CStringVerifierTest.this, " so");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  Some string    ")
          .verifyContainsIgnoreCase(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  Some string    ").verifyContainsIgnoreCase(CStringVerifierTest.this, " sox");
    }
  }

  public class EndsWith {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyEndsWith(CStringVerifierTest.this, "   s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyEndsWith(CStringVerifierTest.this, "   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyEndsWith(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ").verifyEndsWith(CStringVerifierTest.this, " a s ");
    }
  }

  public class EndsWithAny {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyEndsWithAny(
              CStringVerifierTest.this, new CList<>("A", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyEndsWithAny(CStringVerifierTest.this, new CList<>("A", null, " s "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyEndsWithAny(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyEndsWithAny(CStringVerifierTest.this, new CList<>("A", null, " sx "));
    }
  }

  public class EqualsAny {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS(" some s ")
          .verifyEqualsAny(
              CStringVerifierTest.this, new CList<>(" some s ", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyEqualsAny(
              CStringVerifierTest.this, new CList<>(" some s ", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS(" some s ").verifyEqualsAny(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS(" some s ")
          .verifyEqualsAny(
              CStringVerifierTest.this, new CList<>("some s ", null, " s "), "%s#%s", getParams());
    }
  }

  public class EqualsAnyIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS(" soMe s ")
          .verifyEqualsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>(" some s ", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyEqualsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>(" some s ", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS(" soMe s ")
          .verifyEqualsAnyIgnoreCase(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS(" soMe s ")
          .verifyEqualsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>("some s ", null, " s "), "%s#%s", getParams());
    }
  }

  public class ContainsAny {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS(" some s ")
          .verifyContainsAny(
              CStringVerifierTest.this, new CList<>(" some s ", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyContainsAny(
              CStringVerifierTest.this, new CList<>(" some s ", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS(" some s ").verifyContainsAny(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS(" somes ")
          .verifyContainsAny(
              CStringVerifierTest.this, new CList<>("some s ", null, " s "), "%s#%s", getParams());
    }
  }

  public class ContainsAnyIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS(" SoMe S ")
          .verifyContainsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>(" some s ", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyContainsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>(" some s ", null, " s "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS(" soMe s ")
          .verifyContainsAnyIgnoreCase(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS(" SoMeS ")
          .verifyContainsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>("some s ", null, " s "), "%s#%s", getParams());
    }
  }

  public class EndsWithIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyEndsWithIgnoreCase(CStringVerifierTest.this, "   s ", "%s#%s", getParams());
      toCS("  some string   s ").verifyEndsWithIgnoreCase(CStringVerifierTest.this, "   S ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyEndsWithIgnoreCase(CStringVerifierTest.this, "   S ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyEndsWithIgnoreCase(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyEndsWithIgnoreCase(CStringVerifierTest.this, "   SX ", "%s#%s", getParams());
    }
  }

  public class VerifyEndsWithNone {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyEndsWithNone(CStringVerifierTest.this, new CList<>("A", null, " S "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyEndsWithNone(
              CStringVerifierTest.this, new CList<>("A", null, " S "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyEndsWithNone(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyEndsWithNone(
              CStringVerifierTest.this, new CList<>("A", null, " s "), "%s#%s", getParams());
    }
  }

  public class VerifyEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ").verifyEquals(CStringVerifierTest.this, "  some string    ");
      toCS(null).verifyEquals(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyEquals(CStringVerifierTest.this, "  some string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyEquals(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifyEquals(CStringVerifierTest.this, "  some string ");
    }
  }

  public class VerifyEqualsAny {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyEqualsAny(
              CStringVerifierTest.this, new CList<>("", "  some string    "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyEqualsAny(CStringVerifierTest.this, new CList<>("", "  some string    "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyEqualsAny(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyEqualsAny(CStringVerifierTest.this, new CList<>("", "  sometring    "));
    }
  }

  public class VerifyEqualsAnyIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyEqualsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>("", "  some string    "), "%s#%s", getParams());
      toCS("  some STRING    ")
          .verifyEqualsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>("", "  SOME string    "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyEqualsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>("", "  some string    "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyEqualsAnyIgnoreCase(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyEqualsAnyIgnoreCase(
              CStringVerifierTest.this, new CList<>("", "  somestring    "), "%s#%s", getParams());
    }
  }

  public class VerifyEqualsIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyEqualsIgnoreCase(CStringVerifierTest.this, "  SOME string    ");
      toCS(null).verifyEqualsIgnoreCase(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyEqualsIgnoreCase(
              CStringVerifierTest.this, "  SOME string    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyEqualsIgnoreCase(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyEqualsIgnoreCase(CStringVerifierTest.this, "  SOME string", "%s#%s", getParams());
    }
  }

  public class VerifyEqualsIgnoreWhiteSpaces {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  SOME string    ")
          .verifyEqualsIgnoreWhiteSpaces(CStringVerifierTest.this, "SO ME st ring    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyEqualsIgnoreWhiteSpaces(
              CStringVerifierTest.this, "SO ME st ring    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  SOME string    ").verifyEqualsIgnoreWhiteSpaces(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  SOME string    ")
          .verifyEqualsIgnoreWhiteSpaces(
              CStringVerifierTest.this, "SME st ring    ", "%s#%s", getParams());
    }
  }

  public class VerifyEqualsNone {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyEqualsNone(CStringVerifierTest.this, new CList<>("", "  some", "string    "));
      toCS("  some STRING    ")
          .verifyEqualsNone(
              CStringVerifierTest.this, new CList<>("", "  SOME string    "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyEqualsNone(CStringVerifierTest.this, new CList<>("", "  some", "string    "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyEqualsNone(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyEqualsNone(CStringVerifierTest.this, new CList<>("", "some", "  some string    "));
    }
  }

  public class VerifyEqualsNoneIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyEqualsNoneIgnoreCase(
              CStringVerifierTest.this,
              new CList<>("", "  some", "string    "),
              "%s#%s",
              getParams());
      toCS("  some STRING    ")
          .verifyEqualsNoneIgnoreCase(
              CStringVerifierTest.this, new CList<>("", "  $OME string    "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyEqualsNoneIgnoreCase(
              CStringVerifierTest.this,
              new CList<>("", "  some", "string    "),
              "%s#%s",
              getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyEqualsNoneIgnoreCase(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyEqualsNoneIgnoreCase(
              CStringVerifierTest.this,
              new CList<>("", "  some", "  some string    "),
              "%s#%s",
              getParams());
    }
  }

  public class VerifyIsAlpha {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("aiulajksn").verifyIsAlpha(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsAlpha(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("aiu1!lajksn").verifyIsAlpha(CStringVerifierTest.this);
    }
  }

  public class VerifyIsEmptyOrAlpha {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("aiulajksn").verifyIsEmptyOrAlpha(CStringVerifierTest.this, "%s#%s", getParams());
      toCS("").verifyIsEmptyOrAlpha(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsEmptyOrAlpha(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("aiu1 lajksn").verifyIsEmptyOrAlpha(CStringVerifierTest.this);
    }
  }

  public class VerifyIsAlphaSpace {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS(" aiu ajk sn").verifyIsAlphaSpace(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsAlphaSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("1 aiu ajk sn").verifyIsAlphaSpace(CStringVerifierTest.this);
    }
  }

  public class VerifyIsAlphanumeric {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("aiulaj45872ksn1").verifyIsAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
      toCS("blkajsblas").verifyIsAlphanumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testInvalidChar() {
      toCS("aiulaj4\u5872ksn1").verifyIsAlphanumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("aiulaj4 5872ksn1").verifyIsAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsEmptyOrAlphanumeric {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("aiulaj6265opksn").verifyIsEmptyOrAlphanumeric(CStringVerifierTest.this);
      toCS("").verifyIsEmptyOrAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsEmptyOrAlphanumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("aiulaj6!265opksn")
          .verifyIsEmptyOrAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsAlphanumericSpace {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("ai1ul90jksn").verifyIsAlphanumericSpace(CStringVerifierTest.this);
      toCS(" ai1ul90 ajk sn")
          .verifyIsAlphanumericSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsAlphanumericSpace(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("aksaskjhas654!")
          .verifyIsAlphanumericSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsAsciiPrintable {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      char[] chars = "5rtfghuik".toCharArray();
      chars[5] = 32;
      toCS(new String(chars)).verifyIsAsciiPrintable(CStringVerifierTest.this);
      chars[5] = 33;
      toCS(new String(chars))
          .verifyIsAsciiPrintable(CStringVerifierTest.this, "%s#%s", getParams());
      chars[5] = 125;
      toCS(new String(chars)).verifyIsAsciiPrintable(CStringVerifierTest.this);
      chars[5] = 126;
      toCS(new String(chars))
          .verifyIsAsciiPrintable(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsAsciiPrintable(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      char[] chars = "5rtfghuik".toCharArray();
      chars[5] = 31;
      toCS(new String(chars))
          .verifyIsAsciiPrintable(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsNotAsciiPrintable {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      char[] chars = "5rtfghuik".toCharArray();
      chars[5] = 31;
      toCS(new String(chars)).verifyIsNotAsciiPrintable(CStringVerifierTest.this);
      chars[5] = 127;
      toCS(new String(chars))
          .verifyIsNotAsciiPrintable(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotAsciiPrintable(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("asasa").verifyIsNotAsciiPrintable(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsBlank {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS(null).verifyIsBlank(CStringVerifierTest.this);
      toCS("").verifyIsBlank(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("A").verifyIsBlank(CStringVerifierTest.this);
    }
  }

  public class VerifyIsEmpty {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("").verifyIsEmpty(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testActualNull() {
      toCS(null).verifyIsEmpty(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("s").verifyIsEmpty(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsNotAlpha {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("aiu1lajks1n").verifyIsNotAlpha(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotAlpha(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("alskdla").verifyIsNotAlpha(CStringVerifierTest.this);
    }
  }

  public class VerifyIsEmptyOrNotAlpha {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("a1").verifyIsEmptyOrNotAlpha(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsEmptyOrNotAlpha(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("abs").verifyIsEmptyOrNotAlpha(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsNotAlphaSpace {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("1aiul ajk sn").verifyIsNotAlphaSpace(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotAlphaSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("sdfghjk").verifyIsNotAlphaSpace(CStringVerifierTest.this);
    }
  }

  public class VerifyIsNotAlphanumeric {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("aiulaj4587 2ksn1")
          .verifyIsNotAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
      toCS("blkajsbla!s").verifyIsNotAlphanumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("aiulaj45872ksn1").verifyIsNotAlphanumeric(CStringVerifierTest.this);
    }
  }

  public class VerifyIsEmptyOrNotAlphanumeric {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("aiulaj6265!opksn")
          .verifyIsEmptyOrNotAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsEmptyOrNotAlphanumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("aiulaj6265opksn")
          .verifyIsEmptyOrNotAlphanumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsNotAlphanumericSpace {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("ai1ul90jks!n").verifyIsNotAlphanumericSpace(CStringVerifierTest.this);
      toCS("ai1ul90jks !")
          .verifyIsNotAlphanumericSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotAlphanumericSpace(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("ai1ul9 0jksn")
          .verifyIsNotAlphanumericSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsNotBlank {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("ai1ul90jks !").verifyIsNotBlank(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotBlank(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("").verifyIsNotBlank(CStringVerifierTest.this);
    }
  }

  public class VerifyIsNotEmpty {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("ai1ul90jks !").verifyIsNotEmpty(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotEmpty(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("").verifyIsNotEmpty(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsNotNumeric {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("1234567A").verifyIsNotNumeric(CStringVerifierTest.this);
      toCS("").verifyIsNotNumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotNumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("1234567").verifyIsNotNumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsEmptyOrNotNumeric {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("12345A67").verifyIsEmptyOrNotNumeric(CStringVerifierTest.this);
      toCS("A").verifyIsEmptyOrNotNumeric(CStringVerifierTest.this, "%s#%s", getParams());
      toCS("").verifyIsEmptyOrNotNumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsEmptyOrNotNumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("1234567").verifyIsEmptyOrNotNumeric(CStringVerifierTest.this);
    }
  }

  public class VerifyIsNotNumericSpace {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("234567!8").verifyIsNotNumericSpace(CStringVerifierTest.this, "%s#%s", getParams());
      toCS(" 1254 7A86 1").verifyIsNotNumericSpace(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNotNumericSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("2345678").verifyIsNotNumericSpace(CStringVerifierTest.this);
    }
  }

  public class VerifyIsNumeric {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("1234567").verifyIsNumeric(CStringVerifierTest.this, "%s#%s", getParams());
      toCS("1234567").verifyIsNumeric(CStringVerifierTest.this, "%s#%s", 3, 7, getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("A1234567").verifyIsNumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsEmptyOrNumeric {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("1234567").verifyIsEmptyOrNumeric(CStringVerifierTest.this);
      toCS("").verifyIsEmptyOrNumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsEmptyOrNumeric(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("1234A567").verifyIsEmptyOrNumeric(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyIsNumericSpace {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("2345678").verifyIsNumericSpace(CStringVerifierTest.this);
      toCS(" 1254 786 1").verifyIsNumericSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyIsNumericSpace(CStringVerifierTest.this);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("2345A678").verifyIsNumericSpace(CStringVerifierTest.this, "%s#%s", getParams());
    }
  }

  public class VerifyLeftValueEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ").verifyLeftValueEquals(CStringVerifierTest.this, 7, "  some ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyLeftValueEquals(CStringVerifierTest.this, 7, "  some ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyLeftValueEquals(CStringVerifierTest.this, 7, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyLeftValueEquals(CStringVerifierTest.this, 7, "some ", "%s#%s", getParams());
    }
  }

  public class VerifyLeftValueNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ").verifyLeftValueNotEquals(CStringVerifierTest.this, 3, "  some ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyLeftValueNotEquals(CStringVerifierTest.this, 3, "  some ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyLeftValueNotEquals(CStringVerifierTest.this, 3, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyLeftValueNotEquals(CStringVerifierTest.this, 7, "  some ", "%s#%s", getParams());
    }
  }

  public class VerifyLeftPadEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyLeftPadEquals(
              CStringVerifierTest.this, 40, "x", "xxxxxxxxxxxxxxxxxxxxxx  some string   s ");
      toCS("  some string   s ")
          .verifyLeftPadEquals(
              CStringVerifierTest.this,
              30,
              "",
              "              some string   s ",
              "%s#%s",
              getParams());
      toCS("  some string   s ")
          .verifyLeftPadEquals(CStringVerifierTest.this, 10, null, "  some string   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyLeftPadEquals(
              CStringVerifierTest.this,
              40,
              "x",
              "xxxxxxxxxxxxxxxxxxxxxx  some string   s ",
              "%s#%s",
              getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyLeftPadEquals(CStringVerifierTest.this, 40, "x", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyLeftPadEquals(
              CStringVerifierTest.this,
              40,
              "x",
              "xxxxxxxxxxxxxxxxxxxxx  some string   s ",
              "%s#%s",
              getParams());
    }
  }

  public class VerifyLeftPadNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyLeftPadNotEquals(
              CStringVerifierTest.this, 40, "x", "xxxxxxxxxxxxxxxxxxxxx  some string   s ");
      toCS("  some string   s ")
          .verifyLeftPadNotEquals(
              CStringVerifierTest.this, 10, null, "  some string  s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyLeftPadNotEquals(
              CStringVerifierTest.this, 40, "x", "xxxxxxxxxxxxxxxxxxxxx  some string   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyLeftPadNotEquals(CStringVerifierTest.this, 40, "x", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyLeftPadNotEquals(
              CStringVerifierTest.this, 40, "x", "xxxxxxxxxxxxxxxxxxxxxx  some string   s ");
    }
  }

  public class VerifyLength {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyLengthEquals(CStringVerifierTest.this, 18, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyLengthEquals(CStringVerifierTest.this, 18);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyLengthEquals(CStringVerifierTest.this, 17, "%s#%s", getParams());
    }
  }

  public class VerifyLengthNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyLengthNotEquals(CStringVerifierTest.this, 17);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testActualNull() {
      toCS(null).verifyLengthNotEquals(CStringVerifierTest.this, 17, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ").verifyLengthNotEquals(CStringVerifierTest.this, 18);
    }
  }

  public class VerifyMidValueEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyMidValueEquals(CStringVerifierTest.this, 2, 4, "some", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyMidValueEquals(CStringVerifierTest.this, 2, 4, "some");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyMidValueEquals(CStringVerifierTest.this, 2, 4, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifyMidValueEquals(CStringVerifierTest.this, 2, 3, "some");
    }
  }

  public class VerifyMidValueNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyMidValueNotEquals(CStringVerifierTest.this, 2, 5, "some", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyMidValueNotEquals(CStringVerifierTest.this, 2, 5, "some");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyMidValueNotEquals(CStringVerifierTest.this, 2, 5, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifyMidValueNotEquals(CStringVerifierTest.this, 2, 4, "some");
    }
  }

  public class VerifyNotContains {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyNotContains(CStringVerifierTest.this, "sO", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyNotContains(CStringVerifierTest.this, "sO");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyNotContains(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifyNotContains(CStringVerifierTest.this, "som");
    }
  }

  public class VerifyNotContainsIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  Some string    ")
          .verifyNotContainsIgnoreCase(CStringVerifierTest.this, " sX", "%s#%s", getParams());
      toCS("  some $tring    ").verifyNotContainsIgnoreCase(CStringVerifierTest.this, "STRING");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyNotContainsIgnoreCase(CStringVerifierTest.this, "STRING", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some $tring    ").verifyNotContainsIgnoreCase(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some $tring    ")
          .verifyNotContainsIgnoreCase(CStringVerifierTest.this, "TRING", "%s#%s", getParams());
    }
  }

  public class VerifyNotEndsWith {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyNotEndsWith(CStringVerifierTest.this, "   S ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyNotEndsWith(CStringVerifierTest.this, "   S ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyNotEndsWith(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyNotEndsWith(CStringVerifierTest.this, "   s ", "%s#%s", getParams());
    }
  }

  public class VerifyNotEndsWithIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyNotEndsWithIgnoreCase(CStringVerifierTest.this, "   x ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyNotEndsWithIgnoreCase(CStringVerifierTest.this, "   x ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyNotEndsWithIgnoreCase(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyNotEndsWithIgnoreCase(
              CStringVerifierTest.this, "tring   S ", "%s#%s", getParams());
    }
  }

  public class VerifyNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ").verifyNotEquals(CStringVerifierTest.this, "  some STRING    ");
      toCS("  some string    ")
          .verifyNotEquals(CStringVerifierTest.this, "  some String   ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testActualNull() {
      toCS(null).verifyNotEquals(CStringVerifierTest.this, "  some STRING    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyNotEquals(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifyNotEquals(CStringVerifierTest.this, "  some string    ");
    }
  }

  public class VerifyNotEqualsIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyNotEqualsIgnoreCase(
              CStringVerifierTest.this, "  SOME tring    ", "%s#%s", getParams());
      toCS(null).verifyNotEqualsIgnoreCase(CStringVerifierTest.this, "");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testActualNull() {
      toCS(null)
          .verifyNotEqualsIgnoreCase(
              CStringVerifierTest.this, "  SOME tring    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyNotEqualsIgnoreCase(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyNotEqualsIgnoreCase(
              CStringVerifierTest.this, "  some STRING    ", "%s#%s", getParams());
    }
  }

  public class VerifyNotEqualsIgnoreWhiteSpaces {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyNotEqualsIgnoreWhiteSpaces(CStringVerifierTest.this, "  SOME string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testActualNull() {
      toCS(null)
          .verifyNotEqualsIgnoreWhiteSpaces(
              CStringVerifierTest.this, "  SOME string    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifyNotEqualsIgnoreWhiteSpaces(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyNotEqualsIgnoreWhiteSpaces(
              CStringVerifierTest.this, "  some string    ", "%s#%s", getParams());
    }
  }

  public class VerifyNotStartsWith {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyNotStartsWith(CStringVerifierTest.this, "  S");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyNotStartsWith(CStringVerifierTest.this, "  S", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyNotStartsWith(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyNotStartsWith(CStringVerifierTest.this, "  s", "%s#%s", getParams());
    }
  }

  public class VerifyNotStartsWithIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyNotStartsWithIgnoreCase(CStringVerifierTest.this, "A");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyNotStartsWithIgnoreCase(CStringVerifierTest.this, "A", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyNotStartsWithIgnoreCase(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyNotStartsWithIgnoreCase(CStringVerifierTest.this, "  ", "%s#%s", getParams());
    }
  }

  public class VerifyNumberOfMatchesEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyNumberOfMatchesEquals(CStringVerifierTest.this, "s", 3);
      toCS("  some String   s ")
          .verifyNumberOfMatchesEquals(CStringVerifierTest.this, "s", 2, "%s#%s", getParams());
      toCS("  some $tring   s ").verifyNumberOfMatchesEquals(CStringVerifierTest.this, "$", 1);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyNumberOfMatchesEquals(CStringVerifierTest.this, "s", 3, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyNumberOfMatchesEquals(CStringVerifierTest.this, null, 3);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyNumberOfMatchesEquals(CStringVerifierTest.this, "s", 1, "%s#%s", getParams());
    }
  }

  public class VerifyNumberOfMatchesNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyNumberOfMatchesNotEquals(CStringVerifierTest.this, "s", 2);
      toCS("  some String   s ")
          .verifyNumberOfMatchesNotEquals(CStringVerifierTest.this, "s", 1, "%s#%s", getParams());
      toCS("  some $tring   s ").verifyNumberOfMatchesNotEquals(CStringVerifierTest.this, "$", 3);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyNumberOfMatchesNotEquals(CStringVerifierTest.this, "s", 2, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyNumberOfMatchesNotEquals(CStringVerifierTest.this, null, 2);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyNumberOfMatchesNotEquals(CStringVerifierTest.this, "s", 3, "%s#%s", getParams());
    }
  }

  public class VerifyRemoveEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveEquals(CStringVerifierTest.this, "s", "  ome tring    ");
      toCS("  some String   so ")
          .verifyRemoveEquals(
              CStringVerifierTest.this, "so", "  me String    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyRemoveEquals(CStringVerifierTest.this, "s", "  ome tring    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveEquals(CStringVerifierTest.this, "s", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveEquals(CStringVerifierTest.this, "s", "  ome string    ");
    }
  }

  public class VerifyRemoveEndEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveEndEquals(
              CStringVerifierTest.this, "  some ", "  some string   s ", "%s#%s", getParams());
      toCS("  some string   s ")
          .verifyRemoveEndEquals(CStringVerifierTest.this, "some string   s ", "  ");
      toCS("  some string   s ")
          .verifyRemoveEndEquals(
              CStringVerifierTest.this, "  some string   s ", "", "%s#%s", getParams());
      toCS("  some String   s ")
          .verifyRemoveEndEquals(CStringVerifierTest.this, null, "  some String   s ");
      toCS("  some String   s ")
          .verifyRemoveEndEquals(
              CStringVerifierTest.this, "tring   s ", "  some S", "%s#%s", getParams());
      toCS("  some $tring   s ")
          .verifyRemoveEndEquals(CStringVerifierTest.this, "tring   s ", "  some $");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRemoveEndEquals(
              CStringVerifierTest.this, "  some ", "  some string   s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyRemoveEndEquals(CStringVerifierTest.this, "  some ", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveEndEquals(
              CStringVerifierTest.this, "  some ", "some string   s ", "%s#%s", getParams());
    }
  }

  public class VerifyRemoveEndIgnoreCaseEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveEndIgnoreCaseEquals(
              CStringVerifierTest.this, "  Some ", "  some string   s ");
      toCS("  some string   s ")
          .verifyRemoveEndIgnoreCaseEquals(
              CStringVerifierTest.this, "some String   s ", "  ", "%s#%s", getParams());
      toCS("  some string   s ")
          .verifyRemoveEndIgnoreCaseEquals(CStringVerifierTest.this, "  sOME string   s ", "");
      toCS("  some String   s ")
          .verifyRemoveEndIgnoreCaseEquals(
              CStringVerifierTest.this, null, "  some String   s ", "%s#%s", getParams());
      toCS("  some String   s ")
          .verifyRemoveEndIgnoreCaseEquals(CStringVerifierTest.this, "tring   S ", "  some S");
      toCS("  some $tring   s ")
          .verifyRemoveEndIgnoreCaseEquals(
              CStringVerifierTest.this, "TRING   s ", "  some $", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRemoveEndIgnoreCaseEquals(
              CStringVerifierTest.this, "  Some ", "  some string   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveEndIgnoreCaseEquals(
              CStringVerifierTest.this, "  Some ", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveEndIgnoreCaseEquals(
              CStringVerifierTest.this, "  Some ", "  some string   s");
    }
  }

  public class VerifyRemoveEndIgnoreCaseNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveEndIgnoreCaseNotEquals(
              CStringVerifierTest.this, "  Some ", "  some String   s ", "%s#%s", getParams());
      toCS("  some String   s ")
          .verifyRemoveEndIgnoreCaseNotEquals(CStringVerifierTest.this, null, "  some string   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRemoveEndIgnoreCaseNotEquals(
              CStringVerifierTest.this, "  Some ", "  some String   s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveEndIgnoreCaseNotEquals(CStringVerifierTest.this, "  Some ", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveEndIgnoreCaseNotEquals(
              CStringVerifierTest.this, "  some ", "  some string   s ", "%s#%s", getParams());
    }
  }

  public class VerifyRemoveEndNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveEndNotEquals(CStringVerifierTest.this, "  some ", "ome string   s ");
      toCS("  some String   s ")
          .verifyRemoveEndNotEquals(
              CStringVerifierTest.this, null, "  some String   S", "%s#%s", getParams());
      toCS("  some String   s ")
          .verifyRemoveEndNotEquals(CStringVerifierTest.this, "tring   S ", "  some s");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRemoveEndNotEquals(
              CStringVerifierTest.this, "  some ", "ome string   s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveEndNotEquals(CStringVerifierTest.this, "  some ", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveEndNotEquals(
              CStringVerifierTest.this, "  some ", "  some string   s ", "%s#%s", getParams());
    }
  }

  public class VerifyRemoveIgnoreCaseEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveIgnoreCaseEquals(CStringVerifierTest.this, "s", "  ome tring    ");
      toCS("  some String   so ")
          .verifyRemoveIgnoreCaseEquals(
              CStringVerifierTest.this, "SO", "  me String    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyRemoveIgnoreCaseEquals(CStringVerifierTest.this, "s", "  ome tring    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveIgnoreCaseEquals(CStringVerifierTest.this, "s", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveIgnoreCaseEquals(CStringVerifierTest.this, "s", "  ome trng    ");
    }
  }

  public class VerifyRemoveIgnoreCaseNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveIgnoreCaseNotEquals(
              CStringVerifierTest.this, "s", "  ome Tring    ", "%s#%s", getParams());
      toCS("  some String   so ")
          .verifyRemoveIgnoreCaseNotEquals(CStringVerifierTest.this, "SO", "  me String    x");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRemoveIgnoreCaseNotEquals(
              CStringVerifierTest.this, "s", "  ome Tring    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveIgnoreCaseNotEquals(CStringVerifierTest.this, "s", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveIgnoreCaseNotEquals(
              CStringVerifierTest.this, "s", "  ome tring    ", "%s#%s", getParams());
    }
  }

  public class VerifyRemoveNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveNotEquals(CStringVerifierTest.this, "s", "  ome Tring    ");
      toCS("  some String   so ")
          .verifyRemoveNotEquals(
              CStringVerifierTest.this, null, "  me String    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyRemoveNotEquals(CStringVerifierTest.this, "s", "  ome Tring    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveNotEquals(CStringVerifierTest.this, "s", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveNotEquals(CStringVerifierTest.this, "s", "  ome tring    ");
    }
  }

  public class VerifyRemoveStartEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveStartEquals(
              CStringVerifierTest.this, "  some ", "string   s ", "%s#%s", getParams());
      toCS("  some string   s ")
          .verifyRemoveStartEquals(
              CStringVerifierTest.this, "some string   s ", "  some string   s ");
      toCS("  some string   s ")
          .verifyRemoveStartEquals(
              CStringVerifierTest.this, "  some string   s ", "", "%s#%s", getParams());
      toCS("  some String   s ")
          .verifyRemoveStartEquals(CStringVerifierTest.this, null, "  some String   s ");
      toCS("  some String   s ")
          .verifyRemoveStartEquals(
              CStringVerifierTest.this, "  some S", "tring   s ", "%s#%s", getParams());
      toCS("  some $tring   s ")
          .verifyRemoveStartEquals(CStringVerifierTest.this, "  some $", "tring   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRemoveStartEquals(
              CStringVerifierTest.this, "  some ", "string   s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyRemoveStartEquals(CStringVerifierTest.this, "  some ", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveStartEquals(
              CStringVerifierTest.this, "  some ", "string s ", "%s#%s", getParams());
    }
  }

  public class VerifyRemoveStartIgnoreCaseEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseEquals(CStringVerifierTest.this, "  some ", "string   s ");
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseEquals(
              CStringVerifierTest.this, "  Some ", "string   s ", "%s#%s", getParams());
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseEquals(
              CStringVerifierTest.this, "Some string   s ", "  some string   s ");
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseEquals(
              CStringVerifierTest.this, "  Some string   s ", "", "%s#%s", getParams());
      toCS("  some String   s ")
          .verifyRemoveStartIgnoreCaseEquals(CStringVerifierTest.this, null, "  some String   s ");
      toCS("  some String   s ")
          .verifyRemoveStartIgnoreCaseEquals(
              CStringVerifierTest.this, "  some s", "tring   s ", "%s#%s", getParams());
      toCS("  some $tring   s ")
          .verifyRemoveStartIgnoreCaseEquals(CStringVerifierTest.this, "  some $", "tring   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRemoveStartIgnoreCaseEquals(
              CStringVerifierTest.this, "  some ", "string   s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseEquals(CStringVerifierTest.this, "  some ", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseEquals(
              CStringVerifierTest.this, "  some ", "string s ", "%s#%s", getParams());
    }
  }

  public class VerifyRemoveStartIgnoreCaseNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseNotEquals(CStringVerifierTest.this, "  some ", "String   s ");
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseNotEquals(
              CStringVerifierTest.this, "  Some ", "string  s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRemoveStartIgnoreCaseNotEquals(CStringVerifierTest.this, "  some ", "String   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testRemoveNull() {
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseNotEquals(
              CStringVerifierTest.this, null, "string  s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseNotEquals(CStringVerifierTest.this, "  some ", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveStartIgnoreCaseNotEquals(
              CStringVerifierTest.this, "  some ", "string   s ", "%s#%s", getParams());
    }
  }

  public class VerifyRemoveStartNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRemoveStartNotEquals(CStringVerifierTest.this, "  some", "string   s ");
      toCS("  some string   s ")
          .verifyRemoveStartNotEquals(
              CStringVerifierTest.this, null, " Some string   s", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyRemoveStartNotEquals(CStringVerifierTest.this, "  some", "string   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRemoveStartNotEquals(
              CStringVerifierTest.this, "  some", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRemoveStartNotEquals(CStringVerifierTest.this, "  some ", "string   s ");
    }
  }

  public class VerifyReplaceEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReplaceEquals(
              CStringVerifierTest.this, "s", "", "  ome tring    ", "%s#%s", getParams());
      toCS("  some String   so ")
          .verifyReplaceEquals(CStringVerifierTest.this, "so", "XX", "  XXme String   XX ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyReplaceEquals(
              CStringVerifierTest.this, "so", "XX", "  XXme String   XX ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some String   so ").verifyReplaceEquals(CStringVerifierTest.this, "so", "XX", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some String   so ")
          .verifyReplaceEquals(
              CStringVerifierTest.this, "so", "XX", "  XXme String   S ", "%s#%s", getParams());
    }
  }

  public class VerifyReplaceIgnoreCaseEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReplaceIgnoreCaseEquals(CStringVerifierTest.this, "s", "|", "  |ome |tring   | ");
      toCS("  some String   so ")
          .verifyReplaceIgnoreCaseEquals(
              CStringVerifierTest.this, "SO", "x", "  xme String   x ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyReplaceIgnoreCaseEquals(CStringVerifierTest.this, "SO", "x", "  xme String   x ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some String   so ")
          .verifyReplaceIgnoreCaseEquals(
              CStringVerifierTest.this, "SO", "x", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some String   so ")
          .verifyReplaceIgnoreCaseEquals(CStringVerifierTest.this, "SO", "x", "  some String   x ");
    }
  }

  public class VerifyReplaceIgnoreCaseNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReplaceIgnoreCaseNotEquals(
              CStringVerifierTest.this, "s", "|", "  |ome string   | ", "%s#%s", getParams());
      toCS("  some String   so ")
          .verifyReplaceIgnoreCaseNotEquals(
              CStringVerifierTest.this, "SO", "x", "  xme tring   x ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyReplaceIgnoreCaseNotEquals(
              CStringVerifierTest.this, "SO", "x", "  xme tring   x ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some String   so ")
          .verifyReplaceIgnoreCaseNotEquals(CStringVerifierTest.this, "SO", "x", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyReplaceIgnoreCaseNotEquals(
              CStringVerifierTest.this, "s", "|", "  |ome |tring   | ", "%s#%s", getParams());
    }
  }

  public class VerifyReplaceNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReplaceNotEquals(CStringVerifierTest.this, "s", "", "  ome String    ");
      toCS("  some String   so ")
          .verifyReplaceNotEquals(
              CStringVerifierTest.this, "so", "XX", "  XXme XXtring   XX ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyReplaceNotEquals(CStringVerifierTest.this, "so", "XX", "  XXme XXtring   XX ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some String   so ")
          .verifyReplaceNotEquals(CStringVerifierTest.this, "so", "XX", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some String   so ")
          .verifyReplaceNotEquals(CStringVerifierTest.this, "so", "XX", "  XXme String   XX ");
    }
  }

  public class VerifyReplaceOnceEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReplaceOnceEquals(
              CStringVerifierTest.this, "s", "", "  ome string   s ", "%s#%s", getParams());
      toCS("  some String   so ")
          .verifyReplaceOnceEquals(CStringVerifierTest.this, "so", "XX", "  XXme String   so ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyReplaceOnceEquals(
              CStringVerifierTest.this, "so", "XX", "  XXme String   so ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some String   so ")
          .verifyReplaceOnceEquals(CStringVerifierTest.this, "so", "XX", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some String   so ")
          .verifyReplaceOnceEquals(
              CStringVerifierTest.this, "so", "XX", "  Xome String   so ", "%s#%s", getParams());
    }
  }

  public class VerifyReplaceOnceIgnoreCaseEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReplaceOnceIgnoreCaseEquals(
              CStringVerifierTest.this, "s", "|", "  |ome string   s ");
      toCS("  some String   so ")
          .verifyReplaceOnceIgnoreCaseEquals(
              CStringVerifierTest.this, "SO", "x", "  xme String   so ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyReplaceOnceIgnoreCaseEquals(
              CStringVerifierTest.this, "s", "|", "  |ome string   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyReplaceOnceIgnoreCaseEquals(
              CStringVerifierTest.this, "s", "|", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyReplaceOnceIgnoreCaseEquals(
              CStringVerifierTest.this, "s", "|", "  |some string   s ");
    }
  }

  public class VerifyReplaceOnceIgnoreCaseNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReplaceOnceIgnoreCaseNotEquals(
              CStringVerifierTest.this, "s", "|", "  \\|ome string   s ", "%s#%s", getParams());
      toCS("  some String   so ")
          .verifyReplaceOnceIgnoreCaseNotEquals(
              CStringVerifierTest.this, "SO", "x", "  .*e String   so ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyReplaceOnceIgnoreCaseNotEquals(
              CStringVerifierTest.this, "SO", "x", "  .*e String   so ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some String   so ")
          .verifyReplaceOnceIgnoreCaseNotEquals(CStringVerifierTest.this, "SO", "x", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyReplaceOnceIgnoreCaseNotEquals(
              CStringVerifierTest.this, "s", "|", "  |ome string   s ", "%s#%s", getParams());
    }
  }

  public class VerifyReplaceOnceNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReplaceOnceNotEquals(CStringVerifierTest.this, "s", "", "  ome String   s ");
      toCS("  some String   so ")
          .verifyReplaceOnceNotEquals(
              CStringVerifierTest.this, "so", "XX", "  XXme String   XX ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyReplaceOnceNotEquals(CStringVerifierTest.this, "s", "", "  ome String   s ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyReplaceOnceNotEquals(
              CStringVerifierTest.this, "s", "", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyReplaceOnceNotEquals(CStringVerifierTest.this, "s", "", "  ome string   s ");
    }
  }

  public class VerifyReverseEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReverseEquals(
              CStringVerifierTest.this, " s   gnirts emos  ", "%s#%s", getParams());
      toCS("  some @#$%^&*.   so ")
          .verifyReverseEquals(CStringVerifierTest.this, " os   .*&^%$#@ emos  ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyReverseEquals(
              CStringVerifierTest.this, " os   .*&^%$#@ emos  ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some @#$%^&*.   so ").verifyReverseEquals(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some @#$%^&*.   so ")
          .verifyReverseEquals(
              CStringVerifierTest.this, " os   .*&^%# emos  ", "%s#%s", getParams());
    }
  }

  public class VerifyReverseNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyReverseNotEquals(CStringVerifierTest.this, " s   gnirt emos  ");
      toCS("  some @#$%^&*.   so ")
          .verifyReverseNotEquals(
              CStringVerifierTest.this, " os   .*%$#@ emos  ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyReverseNotEquals(CStringVerifierTest.this, " os   .*%$#@ emos  ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some @#$%^&*.   so ")
          .verifyReverseNotEquals(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyReverseNotEquals(CStringVerifierTest.this, " s   gnirts emos  ");
    }
  }

  public class VerifyRightValueEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyRightValueEquals(CStringVerifierTest.this, 7, "ing    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyRightValueEquals(CStringVerifierTest.this, 7, "ing    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyRightValueEquals(CStringVerifierTest.this, 7, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifyRightValueEquals(CStringVerifierTest.this, 7, "ing   ");
    }
  }

  public class VerifyRightValueNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyRightValueNotEquals(CStringVerifierTest.this, 6, "ing    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyRightValueNotEquals(CStringVerifierTest.this, 6, "ing    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyRightValueNotEquals(CStringVerifierTest.this, 6, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifyRightValueNotEquals(CStringVerifierTest.this, 7, "ing    ");
    }
  }

  public class VerifyRightPadEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRightPadEquals(
              CStringVerifierTest.this,
              40,
              "x",
              "  some string   s xxxxxxxxxxxxxxxxxxxxxx",
              "%s#%s",
              getParams());
      toCS("  some string   s ")
          .verifyRightPadEquals(CStringVerifierTest.this, 30, "", "  some string   s             ");
      toCS("  some string   s ")
          .verifyRightPadEquals(
              CStringVerifierTest.this, 10, null, "  some string   s ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRightPadEquals(
              CStringVerifierTest.this, 40, "x", "  some string   s xxxxxxxxxxxxxxxxxxxxxx");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyRightPadEquals(CStringVerifierTest.this, 40, "x", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRightPadEquals(CStringVerifierTest.this, 40, "x", "  some string   s ");
    }
  }

  public class VerifyRightPadNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyRightPadNotEquals(
              CStringVerifierTest.this,
              40,
              "x",
              "  some string   s xxxxxxxxxxxxxxxxxxxxx",
              "%s#%s",
              getParams());
      toCS("  some string   s ")
          .verifyRightPadNotEquals(CStringVerifierTest.this, 10, null, "  some string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyRightPadNotEquals(
              CStringVerifierTest.this,
              40,
              "x",
              "  some string   s xxxxxxxxxxxxxxxxxxxxx",
              "%s#%s",
              getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyRightPadNotEquals(CStringVerifierTest.this, 40, "x", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyRightPadNotEquals(
              CStringVerifierTest.this,
              40,
              "x",
              "  some string   s xxxxxxxxxxxxxxxxxxxxxx",
              "%s#%s",
              getParams());
    }
  }

  public class VerifyStartsWith {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyStartsWith(CStringVerifierTest.this, "  some");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyStartsWith(CStringVerifierTest.this, "  some", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyStartsWith(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyStartsWith(CStringVerifierTest.this, "some", "%s#%s", getParams());
    }
  }

  public class VerifyStartsWithAny {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyStartsWithAny(CStringVerifierTest.this, new CList<>("A", null, "  some"));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyStartsWithAny(
              CStringVerifierTest.this, new CList<>("A", null, "  some"), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ").verifyStartsWithAny(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyStartsWithAny(
              CStringVerifierTest.this, new CList<>("A", null, "some"), "%s#%s", getParams());
    }
  }

  public class VerifyStartsWithIgnoreCase {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ").verifyStartsWithIgnoreCase(CStringVerifierTest.this, "  some");
      toCS("  some string   s ")
          .verifyStartsWithIgnoreCase(CStringVerifierTest.this, "  Some", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyStartsWithIgnoreCase(CStringVerifierTest.this, "  some");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyStartsWithIgnoreCase(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ").verifyStartsWithIgnoreCase(CStringVerifierTest.this, " some");
    }
  }

  public class VerifyStartsWithNone {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifyStartsWithNone(
              CStringVerifierTest.this, new CList<>("A", null, "  Some"), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyStartsWithNone(CStringVerifierTest.this, new CList<>("A", null, "  Some"));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifyStartsWithNone(CStringVerifierTest.this, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifyStartsWithNone(CStringVerifierTest.this, new CList<>("A", "  some", " Some"));
    }
  }

  public class VerifyStripedEndValue {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyStripedEndValue(
              CStringVerifierTest.this, " ", "  some string", "%s#%s", getParams());
      toCS("  some string    ")
          .verifyStripedEndValue(CStringVerifierTest.this, null, "  some string");
      toCS("|some string||||")
          .verifyStripedEndValue(
              CStringVerifierTest.this, "|", "|some string", "%s#%s", getParams());
      toCS("|some string||||")
          .verifyStripedEndValue(CStringVerifierTest.this, null, "|some string||||");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyStripedEndValue(
              CStringVerifierTest.this, "|", "|some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("|some string||||").verifyStripedEndValue(CStringVerifierTest.this, "|", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("|some string||||")
          .verifyStripedEndValue(
              CStringVerifierTest.this, "|", "|som string", "%s#%s", getParams());
    }
  }

  public class VerifyStripedEndValueNot {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyStripedEndValueNot(CStringVerifierTest.this, " ", "  ome string");
      toCS("  some string    ")
          .verifyStripedEndValueNot(
              CStringVerifierTest.this, null, "  ome string", "%s#%s", getParams());
      toCS("|some string||||")
          .verifyStripedEndValueNot(CStringVerifierTest.this, "|", "|som string");
      toCS("|some string||||")
          .verifyStripedEndValueNot(
              CStringVerifierTest.this, null, "|soe string||||", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyStripedEndValueNot(CStringVerifierTest.this, "|", "|som string");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("|some string||||")
          .verifyStripedEndValueNot(CStringVerifierTest.this, "|", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("|some string||||")
          .verifyStripedEndValueNot(CStringVerifierTest.this, "|", "|some string");
    }
  }

  public class VerifyStripedStartValue {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyStripedStartValue(
              CStringVerifierTest.this, " ", "some string    ", "%s#%s", getParams());
      toCS("  some string    ")
          .verifyStripedStartValue(CStringVerifierTest.this, null, "some string    ");
      toCS("|some string||||")
          .verifyStripedStartValue(
              CStringVerifierTest.this, "|", "some string||||", "%s#%s", getParams());
      toCS("|some string||||")
          .verifyStripedStartValue(CStringVerifierTest.this, null, "|some string||||");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyStripedStartValue(
              CStringVerifierTest.this, "|", "some string||||", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("|some string||||").verifyStripedStartValue(CStringVerifierTest.this, "|", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("|some string||||")
          .verifyStripedStartValue(
              CStringVerifierTest.this, "|", "some string|", "%s#%s", getParams());
    }
  }

  public class VerifyStripedStartValueNot {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyStripedStartValueNot(CStringVerifierTest.this, " ", "ome string    ");
      toCS("  some string    ")
          .verifyStripedStartValueNot(
              CStringVerifierTest.this, null, "ome string    ", "%s#%s", getParams());
      toCS("|some string||||")
          .verifyStripedStartValueNot(CStringVerifierTest.this, "|", "ome string||||");
      toCS("|some string||||")
          .verifyStripedStartValueNot(
              CStringVerifierTest.this, null, "|ome string||||", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyStripedStartValueNot(CStringVerifierTest.this, " ", "ome string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyStripedStartValueNot(CStringVerifierTest.this, " ", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifyStripedStartValueNot(CStringVerifierTest.this, " ", "some string    ");
    }
  }

  public class VerifyStripedValue {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifyStripedValue(CStringVerifierTest.this, " ", "some string", "%s#%s", getParams());
      toCS("  some string    ").verifyStripedValue(CStringVerifierTest.this, null, "some string");
      toCS("|some string||||")
          .verifyStripedValue(CStringVerifierTest.this, "|", "some string", "%s#%s", getParams());
      toCS("|some string||||")
          .verifyStripedValue(CStringVerifierTest.this, null, "|some string||||");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyStripedValue(CStringVerifierTest.this, "|", "some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("|some string||||").verifyStripedValue(CStringVerifierTest.this, "|", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("|some string||||")
          .verifyStripedValue(CStringVerifierTest.this, "|", "some String", "%s#%s", getParams());
    }
  }

  public class VerifyStripedValueNot {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ").verifyStripedValueNot(CStringVerifierTest.this, " ", "somestring");
      toCS("  some string    ")
          .verifyStripedValueNot(
              CStringVerifierTest.this, null, "som string", "%s#%s", getParams());
      toCS("|some string||||").verifyStripedValueNot(CStringVerifierTest.this, "|", "somestring");
      toCS("|some string||||")
          .verifyStripedValueNot(
              CStringVerifierTest.this, null, "|soe string||||", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyStripedValueNot(CStringVerifierTest.this, " ", "somestring");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifyStripedValueNot(CStringVerifierTest.this, " ", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifyStripedValueNot(CStringVerifierTest.this, " ", "some string");
    }
  }

  public class VerifySubstringEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringEquals(
              CStringVerifierTest.this, 0, "  some string    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifySubstringEquals(CStringVerifierTest.this, 0, "  some string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringEquals(CStringVerifierTest.this, 0, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ").verifySubstringEquals(CStringVerifierTest.this, 0, "  some string");
    }
  }

  public class VerifySubstringEquals_WithEnd {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringEquals(CStringVerifierTest.this, 0, 3, "  s", "%s#%s", getParams());
      toCS("  some string    ").verifySubstringEquals(CStringVerifierTest.this, 2, 4, "so");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifySubstringEquals(CStringVerifierTest.this, 0, 3, "  s", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifySubstringEquals(CStringVerifierTest.this, 0, 3, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringEquals(CStringVerifierTest.this, 0, 3, "  some", "%s#%s", getParams());
    }
  }

  public class VerifySubstringAfterEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringAfterEquals(CStringVerifierTest.this, " s", "ome string    ");
      toCS("  some string    ")
          .verifySubstringAfterEquals(CStringVerifierTest.this, null, "", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifySubstringAfterEquals(CStringVerifierTest.this, " s", "ome string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringAfterEquals(CStringVerifierTest.this, " s", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringAfterEquals(CStringVerifierTest.this, " s", "Some string    ");
    }
  }

  public class VerifySubstringAfterLastEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringAfterLastEquals(
              CStringVerifierTest.this, " s", "tring    ", "%s#%s", getParams());
      toCS("  some string    ").verifySubstringAfterLastEquals(CStringVerifierTest.this, null, "");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringAfterLastEquals(
              CStringVerifierTest.this, " s", "tring    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringAfterLastEquals(CStringVerifierTest.this, " s", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringAfterLastEquals(
              CStringVerifierTest.this, " s", "String    ", "%s#%s", getParams());
    }
  }

  public class VerifySubstringAfterLastNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringAfterLastNotEquals(CStringVerifierTest.this, " s", "trng    ");
      toCS("  some string    ")
          .verifySubstringAfterLastNotEquals(
              CStringVerifierTest.this, null, "something", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifySubstringAfterLastNotEquals(CStringVerifierTest.this, " s", "trng    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringAfterLastNotEquals(
              CStringVerifierTest.this, " s", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringAfterLastNotEquals(CStringVerifierTest.this, " s", "tring    ");
    }
  }

  public class VerifySubstringAfterNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringAfterNotEquals(
              CStringVerifierTest.this, " s", "ome string   ", "%s#%s", getParams());
      toCS("  some string    ").verifySubstringAfterNotEquals(CStringVerifierTest.this, null, "X");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringAfterNotEquals(
              CStringVerifierTest.this, " s", "ome string   ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifySubstringAfterNotEquals(CStringVerifierTest.this, " s", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringAfterNotEquals(
              CStringVerifierTest.this, " s", "ome string    ", "%s#%s", getParams());
    }
  }

  public class VerifySubstringBeforeEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringBeforeEquals(CStringVerifierTest.this, " st", "  some");
      toCS("  some string    ")
          .verifySubstringBeforeEquals(
              CStringVerifierTest.this, null, "  some string    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifySubstringBeforeEquals(CStringVerifierTest.this, " st", "  some");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringBeforeEquals(CStringVerifierTest.this, " st", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringBeforeEquals(CStringVerifierTest.this, " st", "some");
    }
  }

  public class VerifySubstringBeforeLastEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringBeforeLastEquals(
              CStringVerifierTest.this, " s", "  some", "%s#%s", getParams());
      toCS("  some string    ")
          .verifySubstringBeforeLastEquals(CStringVerifierTest.this, null, "  some string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringBeforeLastEquals(
              CStringVerifierTest.this, " s", "  some", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringBeforeLastEquals(CStringVerifierTest.this, " s", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringBeforeLastEquals(
              CStringVerifierTest.this, " s", "some", "%s#%s", getParams());
    }
  }

  public class SubstringBeforeLastNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringBeforeLastNotEquals(CStringVerifierTest.this, " s", " some");
      toCS("  some string    ")
          .verifySubstringBeforeLastNotEquals(
              CStringVerifierTest.this, null, " some string    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringBeforeLastNotEquals(CStringVerifierTest.this, "s", " some string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringBeforeLastNotEquals(
              CStringVerifierTest.this, "S ", null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringBeforeLastNotEquals(CStringVerifierTest.this, null, "  some string    ");
    }
  }

  public class VerifySubstringBeforeNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringBeforeNotEquals(
              CStringVerifierTest.this, " st", " some", "%s#%s", getParams());
      toCS("  some string    ")
          .verifySubstringBeforeNotEquals(CStringVerifierTest.this, null, "  some string   ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringBeforeNotEquals(
              CStringVerifierTest.this, " st", " some", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringBeforeNotEquals(CStringVerifierTest.this, " st", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringBeforeNotEquals(
              CStringVerifierTest.this, " st", "  some", "%s#%s", getParams());
    }
  }

  public class VerifySubstringBetweenEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringBetweenEquals(CStringVerifierTest.this, "  ", "    ", "some string");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringBetweenEquals(
              CStringVerifierTest.this, "  ", "    ", "some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testOpenNull() {
      toCS("  some string    ")
          .verifySubstringBetweenEquals(CStringVerifierTest.this, null, "    ", "some string");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testCloseNull() {
      toCS("  some string    ")
          .verifySubstringBetweenEquals(
              CStringVerifierTest.this, "  ", null, "some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringBetweenEquals(CStringVerifierTest.this, "  ", "    ", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringBetweenEquals(
              CStringVerifierTest.this, "  ", "    ", "sme string", "%s#%s", getParams());
    }
  }

  public class VerifySubstringBetweenNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringBetweenNotEquals(CStringVerifierTest.this, "  ", "    ", "sme string");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringBetweenNotEquals(
              CStringVerifierTest.this, "  ", "    ", "sme string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringBetweenNotEquals(CStringVerifierTest.this, "  ", "    ", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringBetweenNotEquals(
              CStringVerifierTest.this, "  ", "    ", "some string", "%s#%s", getParams());
    }
  }

  public class VerifySubstringNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringNotEquals(CStringVerifierTest.this, 0, " some string    ");
      toCS("  some string    ")
          .verifySubstringNotEquals(
              CStringVerifierTest.this, 2, "ome string    ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifySubstringNotEquals(CStringVerifierTest.this, 0, " some string    ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ")
          .verifySubstringNotEquals(CStringVerifierTest.this, 0, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringNotEquals(CStringVerifierTest.this, 0, "  some string    ");
    }
  }

  public class VerifySubstringNotEquals_WithEnd {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string    ")
          .verifySubstringNotEquals(CStringVerifierTest.this, 0, 3, " s", "%s#%s", getParams());
      toCS("  some string    ").verifySubstringNotEquals(CStringVerifierTest.this, 2, 4, "sox");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringNotEquals(CStringVerifierTest.this, 0, 3, " s", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string    ").verifySubstringNotEquals(CStringVerifierTest.this, 0, 3, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string    ")
          .verifySubstringNotEquals(CStringVerifierTest.this, 0, 3, "  s", "%s#%s", getParams());
    }
  }

  public class SubstringsBetweenEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifySubstringsBetweenEquals(
              CStringVerifierTest.this, " ", "s", new CList<>(" ", "", "  "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringsBetweenEquals(
              CStringVerifierTest.this, " ", "s", new CList<>(" ", "", "  "), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testOpenNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenEquals(
              CStringVerifierTest.this, null, "s", new CList<>(" ", "", "  "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testCloseNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenEquals(
              CStringVerifierTest.this,
              " ",
              null,
              new CList<>(" ", "", "  "),
              "%s#%s",
              getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenEquals(CStringVerifierTest.this, " ", "s", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegativeOnSize() {
      toCS("  some string   s ")
          .verifySubstringsBetweenEquals(
              CStringVerifierTest.this, " ", "s", new CList<>(" ", ""), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifySubstringsBetweenEquals(
              CStringVerifierTest.this, " ", "s", new CList<>(" ", "", " "));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative2() {
      toCS("  some string   s ")
          .verifySubstringsBetweenEquals(
              CStringVerifierTest.this,
              " ",
              "s",
              new CList<>(" ", "", "  ", " "),
              "%s#%s",
              getParams());
    }
  }

  public class SubstringsBetweenContains {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifySubstringsBetweenContains(CStringVerifierTest.this, " ", "s", " ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringsBetweenContains(
              CStringVerifierTest.this, " ", "s", "some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testOpenNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenContains(CStringVerifierTest.this, null, "s", "some string");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testCloseNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenContains(
              CStringVerifierTest.this, " ", null, "some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenContains(CStringVerifierTest.this, " ", "s", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifySubstringsBetweenContains(
              CStringVerifierTest.this, " ", "s", "some string", "%s#%s", getParams());
    }
  }

  public class SubstringsBetweenNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotEquals(
              CStringVerifierTest.this,
              " ",
              "s",
              new CList<>(" ", "  "),
              "some string",
              "SubstringsBetweenNotEquals#");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringsBetweenNotEquals(
              CStringVerifierTest.this,
              " ",
              "s",
              new CList<>(" ", "  "),
              "some string",
              "SubstringsBetweenNotEquals#");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testOpenNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotEquals(
              CStringVerifierTest.this,
              null,
              "s",
              new CList<>(" ", "  "),
              "some string",
              "SubstringsBetweenNotEquals #");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testCloseNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotEquals(
              CStringVerifierTest.this,
              " ",
              null,
              new CList<>(" ", "  "),
              "some string",
              "SubstringsBetweenNotEquals #");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotEquals(
              CStringVerifierTest.this,
              " ",
              "s",
              null,
              "some string",
              "SubstringsBetweenNotEquals #");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotEquals(
              CStringVerifierTest.this, " ", "s", new CList<>(" ", "", "  "));
    }
  }

  public class SubstringsBetweenNotContains {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotContains(
              CStringVerifierTest.this, " ", "s", "some string", "%s#%s", getParams());
      toCS("  some string   s ")
          .verifySubstringsBetweenNotContains(
              CStringVerifierTest.this, "some ", "ing", "some string");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifySubstringsBetweenNotContains(
              CStringVerifierTest.this, "some ", "ing", "some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testOpenNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotContains(CStringVerifierTest.this, null, "s", "some string");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testCloseNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotContains(
              CStringVerifierTest.this, " ", null, "some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotContains(CStringVerifierTest.this, "some ", "ing", null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("  some string   s ")
          .verifySubstringsBetweenNotContains(
              CStringVerifierTest.this, "some ", "ing", "str", "%s#%s", getParams());
    }
  }

  public class TrimmedValueEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ").verifyTrimmedValueEquals(CStringVerifierTest.this, "some string");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyTrimmedValueEquals(CStringVerifierTest.this, "some string", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ").verifyTrimmedValueEquals(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ")
          .verifyTrimmedValueEquals(CStringVerifierTest.this, "some st$ng", "%s#%s", getParams());
    }
  }

  public class TrimmedValueNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ").verifyTrimmedValueNotEquals(CStringVerifierTest.this, "some strin");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyTrimmedValueNotEquals(
              CStringVerifierTest.this, "some strin", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ").verifyTrimmedValueNotEquals(CStringVerifierTest.this, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ")
          .verifyTrimmedValueNotEquals(
              CStringVerifierTest.this, "some string", "%s#%s", getParams());
    }
  }

  public class TruncatedValueEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ")
          .verifyTruncatedValueEquals(CStringVerifierTest.this, 10, "some strin");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null)
          .verifyTruncatedValueEquals(
              CStringVerifierTest.this, 4, " string   ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ").verifyTruncatedValueEquals(CStringVerifierTest.this, 4, null);
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ").verifyTruncatedValueEquals(CStringVerifierTest.this, 5, " string   ");
    }
  }

  public class TruncatedValueEqualsWithEnd {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ")
          .verifyTruncatedValueEquals(
              CStringVerifierTest.this, 4, 10, " string   ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyTruncatedValueEquals(CStringVerifierTest.this, 4, 10, " string   ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ")
          .verifyTruncatedValueEquals(CStringVerifierTest.this, 4, 10, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ")
          .verifyTruncatedValueEquals(CStringVerifierTest.this, 5, 10, " string   ");
    }
  }

  public class TruncatedValueNotEquals {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ")
          .verifyTruncatedValueNotEquals(
              CStringVerifierTest.this, 10, "ome strin", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyTruncatedValueNotEquals(CStringVerifierTest.this, 10, "ome strin");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ")
          .verifyTruncatedValueNotEquals(CStringVerifierTest.this, 10, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ").verifyTruncatedValueNotEquals(CStringVerifierTest.this, 5, "some ");
    }
  }

  public class TruncatedValueNotEqualsWithEnd {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ")
          .verifyTruncatedValueNotEquals(
              CStringVerifierTest.this, 4, 10, " tring   ", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyTruncatedValueNotEquals(CStringVerifierTest.this, 4, 10, " tring   ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ")
          .verifyTruncatedValueNotEquals(
              CStringVerifierTest.this, 4, 10, null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ")
          .verifyTruncatedValueNotEquals(CStringVerifierTest.this, 4, 10, " string   ");
    }
  }

  public class IsMatches {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ")
          .verifyMatches(CStringVerifierTest.this, "[a-z ]+", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyMatches(CStringVerifierTest.this, " tring   ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ")
          .verifyMatches(CStringVerifierTest.this, (String) null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ").verifyMatches(CStringVerifierTest.this, "\\d+");
    }
  }

  public class IsMatchesPattern {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ")
          .verifyMatches(
              CStringVerifierTest.this, Pattern.compile("[a-z ]+"), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyMatches(CStringVerifierTest.this, Pattern.compile("[a-z ]+"));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ")
          .verifyMatches(CStringVerifierTest.this, (Pattern) null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ").verifyMatches(CStringVerifierTest.this, Pattern.compile("^[A-Z ]+$"));
    }
  }

  public class IsNotMatches {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ")
          .verifyNotMatches(CStringVerifierTest.this, "^[A-Z ]+$", "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyNotMatches(CStringVerifierTest.this, " tring   ");
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ")
          .verifyNotMatches(CStringVerifierTest.this, (String) null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ").verifyNotMatches(CStringVerifierTest.this, "[a-z ]+");
    }
  }

  public class IsNotMatchesPattern {
    @Test(retryAnalyzer = CTestRetryAnalyzer.class)
    public void testPositive() {
      toCS("some string    ")
          .verifyNotMatches(
              CStringVerifierTest.this, Pattern.compile("^[A-Z ]+$"), "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testActualNull() {
      toCS(null).verifyNotMatches(CStringVerifierTest.this, Pattern.compile("[a-z ]+"));
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testExpectedNull() {
      toCS("some string    ")
          .verifyNotMatches(CStringVerifierTest.this, (Pattern) null, "%s#%s", getParams());
    }

    @Test(retryAnalyzer = CTestRetryAnalyzer.class, expectedExceptions = AssertionError.class)
    public void testNegative() {
      toCS("some string    ")
          .verifyNotMatches(
              CStringVerifierTest.this, Pattern.compile("[a-z ]+"), "%s#%s", getParams());
    }
  }
}
