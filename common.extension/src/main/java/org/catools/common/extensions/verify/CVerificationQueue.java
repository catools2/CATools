package org.catools.common.extensions.verify;

import org.slf4j.Logger;

/**
 * Build a sequence of verifications using method from different verification classes
 */
public interface CVerificationQueue {

  Logger getLogger();

  <T extends CVerificationBuilder<T>> T queue(CVerificationInfo verificationInfo);
}
