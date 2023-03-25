package org.catools.common.tests.verify.retry;

import org.catools.common.extensions.verify.CNumberVerification;

import java.util.function.Consumer;

public class NumberVerificationVerifyTest extends NumberVerificationBaseTest {
  @Override
  public void verifyBigDecimal(Consumer<CNumberVerification> action) {
    action.accept(verify.BigDecimal);
  }

  @Override
  public void verifyDouble(Consumer<CNumberVerification> action) {
    action.accept(verify.Double);
  }

  @Override
  public void verifyInt(Consumer<CNumberVerification> action) {
    action.accept(verify.Int);
  }

  @Override
  public void verifyLong(Consumer<CNumberVerification> action) {
    action.accept(verify.Long);
  }
}
