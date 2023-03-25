package org.catools.media.extensions.verify;

import org.catools.common.extensions.verify.CBaseVerification;
import org.catools.common.extensions.verify.CVerificationBuilder;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.media.extensions.verify.interfaces.CImageComparisionVerifier;

import java.awt.image.BufferedImage;

/**
 * Image verification class contains all verification method which is related to Image
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CImageComparisionVerification<T extends CVerificationBuilder>
    extends CBaseVerification<T> {

  public CImageComparisionVerification(T verifier) {
    super(verifier);
  }

  /**
   * Verify that actual and expected have same boolean value or be null
   *
   * @param actual       value to compare
   * @param expected     value to compare
   * @param diffFileName the file name for diff image which should be generated in case if images
   *                     did not match
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void equals(
      final BufferedImage actual,
      final BufferedImage expected,
      final String diffFileName,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, diffFileName, message, params);
  }

  /**
   * Verify that actual and expected have same boolean value or be null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equals(
      final BufferedImage actual,
      final CFile expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected have same boolean value or be null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equals(
      final BufferedImage actual,
      final CResource expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected has different boolean value
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEquals(
      final BufferedImage actual,
      final BufferedImage expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected has different boolean value
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEquals(
      final BufferedImage actual,
      final CFile expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected has different boolean value
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEquals(
      final BufferedImage actual,
      final CResource expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  private CImageComparisionVerifier toVerifier(BufferedImage actual) {
    return new CImageComparisionVerifier() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public BufferedImage get() {
        return actual;
      }
    };
  }
}
