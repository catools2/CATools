package org.catools.common.tests.verify.noretry;

import org.catools.common.extensions.verify.CMapVerification;
import org.catools.common.extensions.verify.CVerifier;

import java.util.function.Consumer;

public class CMapVerificationVerifierTest extends CMapVerificationBaseTest {
  @Override
  public void verify(Consumer<CMapVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.Map);
    verifier.verify();
  }
}
