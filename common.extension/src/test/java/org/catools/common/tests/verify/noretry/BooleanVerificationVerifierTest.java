package org.catools.common.tests.verify.noretry;

import org.catools.common.extensions.verify.CBooleanVerification;
import org.catools.common.extensions.verify.CVerifier;

import java.util.function.Consumer;

public class BooleanVerificationVerifierTest extends BooleanVerificationBaseTest {
  @Override
  public void verify(Consumer<CBooleanVerification> action) {
    CVerifier verifier = new CVerifier();
    action.accept(verifier.Bool);
    verifier.verify();
  }
}
