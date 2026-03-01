package org.catools.common.tests.verify.hard;

import java.util.function.Consumer;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.extensions.verify.hard.CCollectionVerification;
import org.catools.common.tests.verify.CCollectionVerificationBaseTest;

public class CCollectionVerificationVerifyTest extends CCollectionVerificationBaseTest {
  @Override
  public void verify(Consumer<CCollectionVerification> action) {
    action.accept(CVerify.Collection);
  }
}
