package org.catools.common.tests.verify.hard;

import java.util.function.Consumer;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.extensions.verify.hard.CMapVerification;
import org.catools.common.tests.verify.CMapVerificationBaseTest;

public class CMapVerificationVerifyTest extends CMapVerificationBaseTest {
  @Override
  public void verify(Consumer<CMapVerification> action) {
    action.accept(CVerify.Map);
  }
}
