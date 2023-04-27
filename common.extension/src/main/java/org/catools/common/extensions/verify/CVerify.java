package org.catools.common.extensions.verify;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.configs.CAnsiConfigs;
import org.catools.common.utils.CAnsiUtil;
import org.slf4j.Logger;

import java.util.function.Function;

@Slf4j
public class CVerify extends CVerificationBuilder<CVerify> {

  @Override
  public void queue(CVerificationInfo expectation) {
    perform(expectation::test);
  }

  private void perform(Function<StringBuilder, Boolean> supplier) {
    StringBuilder messages = new StringBuilder(CAnsiConfigs.isPrintInColorAvailable() ? CAnsiUtil.RESET : "");
    boolean result = supplier.apply(messages);
    String verificationMessages = messages.toString();
    if (!result) {
      log.error(verificationMessages);
      throw new AssertionError(verificationMessages);
    }
    log.info(verificationMessages);
  }
}
