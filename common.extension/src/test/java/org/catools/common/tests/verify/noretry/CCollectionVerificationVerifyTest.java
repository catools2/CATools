package org.catools.common.tests.verify.noretry;

import org.catools.common.extensions.verify.CCollectionVerification;

import java.util.function.Consumer;

public class CCollectionVerificationVerifyTest extends CCollectionVerificationBaseTest {
  @Override
  public void verify(Consumer<CCollectionVerification> action) {
    action.accept(verify.Collection);
  }
}
