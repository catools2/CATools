package org.catools.common.tests.verify.noretry;

import org.catools.common.extensions.verify.CDateVerification;

import java.util.function.Consumer;

public class CDateVerificationVerifyTest extends CDateVerificationBaseTest {
  @Override
  public void verify(Consumer<CDateVerification> action) {
    action.accept(verify.Date);
  }
}
