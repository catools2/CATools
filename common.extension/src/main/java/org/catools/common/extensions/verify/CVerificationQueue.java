package org.catools.common.extensions.verify;

import org.catools.common.configs.CAnsiConfigs;
import org.catools.common.utils.CAnsiUtil;
import org.slf4j.Logger;

/**
 * Build a sequence of verifications using method from different verification classes
 */
public interface CVerificationQueue {
  Logger getLogger();

  default void queue(CVerificationInfo expectation) {
    StringBuilder messages = new StringBuilder(CAnsiConfigs.isPrintInColorAvailable() ? CAnsiUtil.RESET : "");
    boolean result = expectation.test(messages);
    String verificationMessages = messages.toString();
    if (!result) {
      getLogger().error(verificationMessages);
      throw new AssertionError(verificationMessages);
    }
    getLogger().info(verificationMessages);
  }
}
