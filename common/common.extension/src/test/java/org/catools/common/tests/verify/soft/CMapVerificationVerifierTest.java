package org.catools.common.tests.verify.soft;

import java.util.function.Consumer;
import org.catools.common.extensions.verify.CVerifier;
import org.catools.common.extensions.verify.hard.CMapVerification;
import org.catools.common.tests.verify.CMapVerificationBaseTest;

public class CMapVerificationVerifierTest extends CMapVerificationBaseTest {
  @Override
  public void verify(Consumer<CMapVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.Map);
    verifier.verify();
  }
}
