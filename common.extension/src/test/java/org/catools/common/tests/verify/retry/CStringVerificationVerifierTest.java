package org.catools.common.tests.verify.retry;

import org.catools.common.extensions.verify.CStringVerification;
import org.catools.common.extensions.verify.CVerifier;

import java.util.function.Consumer;

public class CStringVerificationVerifierTest extends CStringVerificationBaseTest {
  @Override
  public void verify(Consumer<CStringVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.String);
    verifier.verify();
  }
}
