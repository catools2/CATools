package org.catools.common.extensions.verify;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;
import org.catools.common.configs.CAnsiConfigs;
import org.catools.common.extensions.CTypeExtensionConfigs;
import org.catools.common.extensions.verify.soft.CBooleanVerifierImpl;
import org.catools.common.extensions.verify.soft.CCollectionVerifierImpl;
import org.catools.common.extensions.verify.soft.CDateVerifierImpl;
import org.catools.common.extensions.verify.soft.CFileVerifierImpl;
import org.catools.common.extensions.verify.soft.CMapVerifierImpl;
import org.catools.common.extensions.verify.soft.CNumberVerifierImpl;
import org.catools.common.extensions.verify.soft.CObjectVerifierImpl;
import org.catools.common.extensions.verify.soft.CStringVerifierImpl;
import org.catools.common.utils.CAnsiUtil;

// We should not catch any exception here as based on strategy exception might have different
// impact.
// All exception handling should be done inside expectation
@Slf4j
public class CVerifier implements CVerificationQueue {
  private static final java.lang.String line =
      "------------------------------------------------------------";
  public final CObjectVerifierImpl<CVerifier> Object = new CObjectVerifierImpl<>(this);
  public final CCollectionVerifierImpl<?> Collection = new CCollectionVerifierImpl<>(this);
  public final CMapVerifierImpl<?> Map = new CMapVerifierImpl<>(this);
  public final CBooleanVerifierImpl<?> Bool = new CBooleanVerifierImpl<>(this);
  public final CDateVerifierImpl<?> Date = new CDateVerifierImpl<>(this);
  public final CStringVerifierImpl<?> String = new CStringVerifierImpl<>(this);
  public final CFileVerifierImpl<?> File = new CFileVerifierImpl<>(this);
  public final CNumberVerifierImpl<?, Long> Long = new CNumberVerifierImpl<>(this);
  public final CNumberVerifierImpl<?, java.math.BigDecimal> BigDecimal =
      new CNumberVerifierImpl<>(this);
  public final CNumberVerifierImpl<?, Double> Double = new CNumberVerifierImpl<>(this);
  public final CNumberVerifierImpl<?, Float> Float = new CNumberVerifierImpl<>(this);
  public final CNumberVerifierImpl<?, Integer> Int = new CNumberVerifierImpl<>(this);
  protected final CList<CVerificationInfo<?, ?>> expectations = new CList<>();

  public CVerifier() {
    super();
  }

  public void queue(CVerificationInfo<?, ?> verificationInfo) {
    expectations.add(verificationInfo);
  }

  public void verify() {
    verify(StringUtils.EMPTY);
  }

  public void verify(final java.lang.String header) {
    perform(
        header,
        "Verify All",
        messages -> expectations.getAll(exp -> !exp.test(messages)).isEmpty());
  }

  public void verifyAny() {
    verifyAny(StringUtils.EMPTY);
  }

  public void verifyAny(final java.lang.String header) {
    perform(
        header,
        "Verify Any",
        messages -> expectations.getFirstOrNull(exp -> exp.test(messages)) != null);
  }

  public void verifyNone() {
    verifyNone(StringUtils.EMPTY);
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

    boolean hasHeader = StringUtils.isNotBlank(header);

    if (hasHeader) {
      messages.append(line).append(System.lineSeparator());
      messages.append(header).append(System.lineSeparator());
      messages.append(line).append(System.lineSeparator());
    }

    try {
      boolean result = supplier.apply(messages);

      if (result && !CTypeExtensionConfigs.printPassedVerification()) {
        return;
      }

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
