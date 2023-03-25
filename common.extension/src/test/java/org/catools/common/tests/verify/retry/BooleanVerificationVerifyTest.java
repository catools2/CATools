package org.catools.common.tests.verify.retry;

import org.catools.common.extensions.verify.CBooleanVerification;

import java.util.function.Consumer;

public class BooleanVerificationVerifyTest extends BooleanVerificationBaseTest {
  @Override
  public void verify(Consumer<CBooleanVerification> action) {
    action.accept(verify.Bool);
  }
}
