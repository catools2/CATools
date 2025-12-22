package org.catools.common.extensions.verify;

import org.catools.common.configs.CAnsiConfigs;
import org.catools.common.extensions.CTypeExtensionConfigs;
import org.catools.common.extensions.verify.interfaces.CBaseVerify;
import org.catools.common.utils.CAnsiUtil;
import org.catools.common.utils.CStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Build a sequence of verifications using method from different verification classes */
public interface CVerificationQueue {
  Logger logger = LoggerFactory.getLogger(CBaseVerify.class);

  default void queue(CVerificationInfo<?, ?> expectation) {
    StringBuilder messages =
        new StringBuilder(
            CAnsiConfigs.isPrintInColorAvailable() ? CAnsiUtil.RESET : CStringUtil.EMPTY);
    boolean result = expectation.test(messages);
    String verificationMessages = messages.toString();
    if (!result) {
      logger.error(verificationMessages);
      throw new AssertionError(verificationMessages);
    } else if (CTypeExtensionConfigs.printPassedVerification()) {
      logger.info(verificationMessages);
    }
  }
}
