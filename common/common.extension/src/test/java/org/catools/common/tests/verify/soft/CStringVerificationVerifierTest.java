package org.catools.common.tests.verify.soft;

import java.util.function.Consumer;
import org.catools.common.extensions.verify.CVerifier;
import org.catools.common.extensions.verify.hard.CStringVerification;
import org.catools.common.tests.verify.CStringVerificationBaseTest;

public class CStringVerificationVerifierTest extends CStringVerificationBaseTest {
  @Override
  public void verify(Consumer<CStringVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.String);
    verifier.verify();
  }
}
