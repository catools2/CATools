package org.catools.common.tests.verify.retry;

import org.catools.common.extensions.verify.CStringVerification;

import java.util.function.Consumer;

public class CStringVerificationVerifyTest extends CStringVerificationBaseTest {
  @Override
  public void verify(Consumer<CStringVerification> action) {
    action.accept(verify.String);
  }
}
