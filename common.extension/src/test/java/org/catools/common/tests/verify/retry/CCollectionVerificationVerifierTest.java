package org.catools.common.tests.verify.retry;

import org.catools.common.extensions.verify.CCollectionVerification;
import org.catools.common.extensions.verify.CVerifier;

import java.util.function.Consumer;

public class CCollectionVerificationVerifierTest extends CCollectionVerificationBaseTest {
  @Override
  public void verify(Consumer<CCollectionVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.Collection);
    verifier.verify();
  }
}
