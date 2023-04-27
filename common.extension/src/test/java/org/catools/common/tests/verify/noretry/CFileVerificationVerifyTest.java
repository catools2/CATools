package org.catools.common.tests.verify.noretry;

import org.catools.common.extensions.verify.CFileVerification;

import java.util.function.Consumer;

public class CFileVerificationVerifyTest extends CFileVerificationBaseTest {
  public CFileVerificationVerifyTest() {
    super("V2");
  }

  @Override
  public void verify(Consumer<CFileVerification> action) {
    action.accept(verify.File);
  }
}
