package org.catools.common.tests.verify.noretry;

import org.catools.common.extensions.verify.CBooleanVerification;

import java.util.function.Consumer;

public class BooleanVerificationVerifyTest extends BooleanVerificationBaseTest {
  @Override
  public void verify(Consumer<CBooleanVerification> action) {
    action.accept(verify.Bool);
  }
}
