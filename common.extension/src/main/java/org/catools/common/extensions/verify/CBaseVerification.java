package org.catools.common.extensions.verify;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CBaseVerification<T extends CVerificationQueue> {
  protected T verifier;

  protected CBaseVerification(T verifier) {
    this.verifier = verifier;
  }

  protected T queue(CVerificationInfo verificationInfo) {
    return (T) verifier.queue(verificationInfo);
  }
}
