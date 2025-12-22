package org.catools.common.tests.verify.hard;

import java.util.function.Consumer;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.extensions.verify.hard.CObjectVerification;
import org.catools.common.tests.verify.CObjectVerificationBaseTest;

public class CObjectVerificationVerifyTest extends CObjectVerificationBaseTest {
  @Override
  public void verify(Consumer<CObjectVerification> action) {
    action.accept(CVerify.Object);
  }
}
