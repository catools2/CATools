package org.catools.common.tests.verify.soft;

import java.util.function.Consumer;
import org.catools.common.extensions.verify.CVerifier;
import org.catools.common.extensions.verify.hard.CObjectVerification;
import org.catools.common.tests.verify.CObjectVerificationBaseTest;

public class CObjectVerificationVerifierTest extends CObjectVerificationBaseTest {
  @Override
  public void verify(Consumer<CObjectVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.Object);
    verifier.verify();
  }
}
