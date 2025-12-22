package org.catools.media.extensions.verify.soft;

import java.awt.image.BufferedImage;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.media.extensions.verify.hard.CImageComparisonVerification;
import org.catools.media.extensions.verify.interfaces.base.CImageComparisonVerify;

/**
 * Boolean verification class contains all verification method which is related to Boolean
 *
 * @param <T> represent any classes which extent {@link CVerificationQueue}.
 */
public class CImageComparisonVerifierImpl<T extends CVerificationQueue>
    extends CImageComparisonVerification {
  private final T verifier;

  public CImageComparisonVerifierImpl(T verifier) {
    this.verifier = verifier;
  }

  @Override
  protected CImageComparisonVerify toVerifier(BufferedImage actual) {
    return new CImageComparisonVerify() {
      @Override
      public CVerificationQueue getVerificationQueue() {
        return verifier;
      }

      @Override
      public BufferedImage _get() {
        return actual;
      }
    };
  }
}
