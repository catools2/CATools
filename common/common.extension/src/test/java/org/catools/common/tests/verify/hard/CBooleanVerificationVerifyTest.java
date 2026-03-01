package org.catools.common.tests.verify.hard;

import java.util.function.Consumer;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.extensions.verify.hard.CBooleanVerification;
import org.catools.common.tests.verify.CBooleanVerificationBaseTest;

public class CBooleanVerificationVerifyTest extends CBooleanVerificationBaseTest {
  @Override
  public void verify(Consumer<CBooleanVerification> action) {
    action.accept(CVerify.Bool);
  }
}
