package org.catools.common.tests.verify.retry;

import org.catools.common.extensions.verify.CNumberVerification;
import org.catools.common.extensions.verify.CVerifier;

import java.util.function.Consumer;

public class NumberVerificationVerifierTest extends NumberVerificationBaseTest {
  @Override
  public void verifyBigDecimal(Consumer<CNumberVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.BigDecimal);
    verifier.verify();
  }

  @Override
  public void verifyDouble(Consumer<CNumberVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.Double);
    verifier.verify();
  }

  @Override
  public void verifyInt(Consumer<CNumberVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.Int);
    verifier.verify();
  }

  @Override
  public void verifyLong(Consumer<CNumberVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.Long);
    verifier.verify();
  }
}
