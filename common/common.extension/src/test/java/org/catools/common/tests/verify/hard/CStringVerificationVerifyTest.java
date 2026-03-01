package org.catools.common.tests.verify.hard;

import java.util.function.Consumer;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.extensions.verify.hard.CStringVerification;
import org.catools.common.tests.verify.CStringVerificationBaseTest;

public class CStringVerificationVerifyTest extends CStringVerificationBaseTest {
  @Override
  public void verify(Consumer<CStringVerification> action) {
    action.accept(CVerify.String);
  }
}
