package org.catools.common.tests.verify.retry;

import org.catools.common.extensions.verify.CMapVerification;

import java.util.function.Consumer;

public class CMapVerificationVerifyTest extends CMapVerificationBaseTest {
  @Override
  public void verify(Consumer<CMapVerification> action) {
    action.accept(verify.Map);
  }
}
