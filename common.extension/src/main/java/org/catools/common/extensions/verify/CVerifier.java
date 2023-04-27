package org.catools.common.extensions.verify;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.CList;
import org.catools.common.configs.CAnsiConfigs;
import org.catools.common.utils.CAnsiUtil;
import org.catools.common.utils.CStringUtil;
import org.slf4j.Logger;

import java.util.function.Function;

// We should not catch any exception here as based on strategy exception might have different
// impact.
// All exception handling should be done inside expectation
@Slf4j
public class CVerifier extends CVerificationBuilder<CVerifier> {
  private static final java.lang.String line =
      "------------------------------------------------------------";

  protected final CList<CVerificationInfo> expectations = new CList<>();

  public CVerifier() {
    super();
  }

  @Override
  public void queue(CVerificationInfo verificationInfo) {
    expectations.add(verificationInfo);
  }

  public void verify() {
    verify(CStringUtil.EMPTY);
  }

  public void verify(final java.lang.String header) {
    perform(
        header,
        "Verify All",
        messages -> expectations.getAll(exp -> !exp.test(messages)).isEmpty());
  }

  public void verifyAny() {
    verifyAny(CStringUtil.EMPTY);
  }

  public void verifyAny(final java.lang.String header) {
    perform(
        header,
        "Verify Any",
        messages -> expectations.getFirstOrNull(exp -> exp.test(messages)) != null);
  }

  public void verifyNone() {
    verifyNone(CStringUtil.EMPTY);
  }

  public void verifyNone(final java.lang.String header) {
    perform(
        header,
        "Verify None",
        messages -> expectations.getFirstOrNull(exp -> exp.test(messages)) == null);
  }

  private void perform(
      final java.lang.String header,
      final java.lang.String verificationType,
      Function<StringBuilder, Boolean> supplier) {
    StringBuilder messages = new StringBuilder();

    boolean hasHeader = CStringUtil.isNotBlank(header);
    if (hasHeader) {
      messages.append(line).append(System.lineSeparator());
      messages.append(header).append(System.lineSeparator());
      messages.append(line).append(System.lineSeparator());
    }
    try {
      boolean result = supplier.apply(messages);

      if (hasHeader) {
        messages.append(line).append(System.lineSeparator());
      }

      String headLine =
          System.lineSeparator()
              + "=============="
              + verificationType
              + (result ? " Passed" : " Failed")
              + " =============="
              + System.lineSeparator();
      if (CAnsiConfigs.isPrintInColorAvailable()) {
        messages.insert(0, result ? CAnsiUtil.toGreen(headLine) : CAnsiUtil.toRed(headLine));
      } else {
        messages.insert(0, headLine);
      }
      String verificationMessages = messages.toString();
      if (!result) {
        log.error(verificationMessages);
        throw new AssertionError(verificationMessages);
      }
      log.info(verificationMessages);
    } finally {
      expectations.clear();
    }
  }
}
