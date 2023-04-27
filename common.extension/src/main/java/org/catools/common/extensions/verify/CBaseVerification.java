package org.catools.common.extensions.verify;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.configs.CAnsiConfigs;
import org.catools.common.utils.CAnsiUtil;

@Slf4j
public class CBaseVerification {
  protected void queue(CVerificationInfo expectation) {
    StringBuilder messages = new StringBuilder(CAnsiConfigs.isPrintInColorAvailable() ? CAnsiUtil.RESET : "");
    boolean result = expectation.test(messages);
    String verificationMessages = messages.toString();
    if (!result) {
      log.error(verificationMessages);
      throw new AssertionError(verificationMessages);
    }
    log.info(verificationMessages);
  }
}
