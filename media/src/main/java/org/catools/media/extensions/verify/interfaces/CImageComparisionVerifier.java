package org.catools.media.extensions.verify.interfaces;

import org.catools.common.extensions.verify.CVerificationBuilder;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CObjectVerifier;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.tests.CTest;
import org.catools.media.extensions.states.interfaces.CImageComparisionState;
import org.catools.media.extensions.verify.CImageComparisionVerification;
import org.catools.media.utils.CImageComparisionUtil;
import org.catools.media.utils.CImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.catools.media.enums.CImageComparisonType.GRAY_FLOAT_32;
import static org.catools.media.utils.CImageComparisionUtil.toBufferedImageList;

/**
 * CImageComparisionVerifier is an interface for BufferedImage verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 *
 * <p>Please Note that we should extend manually {@link CImageComparisionVerification} for each new
 * added verification here
 */
public interface CImageComparisionVerifier
    extends CObjectVerifier<BufferedImage, CImageComparisionState> {
  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance instance of test related to this verification
   * @param expected     value to compare
   * @param diffFileName the file name for diff image which should be generated in case if images
   *                     did not match
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance,
      final BufferedImage expected,
      final String diffFileName,
      final String message,
      final Object... params) {
    verifyEquals(testInstance.verify, expected, diffFileName, message, params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance instance of test related to this verification
   * @param expected     value to compare
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance, final CFile expected, final String message, final Object... params) {
    verifyEquals(testInstance.verify, expected, message, params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance instance of test related to this verification
   * @param expected     value to compare
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance, final CResource expected, final String message, final Object... params) {
    verifyEquals(testInstance.verify, expected, message, params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param diffFileName      the file name for diff image which should be generated in case if images
   *                          did not match
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final BufferedImage expected,
      final String diffFileName,
      final String message,
      final Object... params) {
    if (_useWaiter()) {
      _verify(
          verificationQueue,
          expected,
          false,
          (a, b) -> _toState(a).isEqual(b),
          (a, b) -> CImageUtil.generateDiffFile((BufferedImage) a, b, diffFileName, GRAY_FLOAT_32),
          getDefaultWaitInSeconds(),
          getDefaultWaitIntervalInMilliSeconds(),
          message,
          params);
    } else {
      _verify(
          verificationQueue,
          expected,
          false,
          (a, b) -> _toState(a).isEqual(b),
          (a, b) -> CImageUtil.generateDiffFile(a, b, diffFileName, GRAY_FLOAT_32),
          message,
          params);
    }
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final CFile expected,
      final String message,
      final Object... params) {
    verifyEquals(
        verificationQueue,
        CImageUtil.readImageOrNull(expected),
        expected.getName(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final CResource expected,
      final String message,
      final Object... params) {
    verifyEquals(
        verificationQueue,
        CImageUtil.readImageOrNull(expected),
        expected.getResourceName(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param testInstance instance of test related to this verification
   * @param expected     value to compare
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CTest testInstance, final CFile expected, final String message, final Object... params) {
    verifyNotEquals(testInstance.verify, expected, message, params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param testInstance instance of test related to this verification
   * @param expected     value to compare
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CTest testInstance, final CResource expected, final String message, final Object... params) {
    verifyNotEquals(testInstance.verify, expected, message, params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CVerificationQueue verificationQueue,
      final CFile expected,
      final String message,
      final Object... params) {
    if (_useWaiter()) {
      _verify(
          verificationQueue,
          expected,
          false,
          (a, b) -> _toState(a).isNotEqual(b),
          getDefaultWaitInSeconds(),
          getDefaultWaitIntervalInMilliSeconds(),
          message,
          params);
    } else {
      _verify(
          verificationQueue, expected, false, (a, b) -> _toState(a).isNotEqual(b), message, params);
    }
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CVerificationQueue verificationQueue,
      final CResource expected,
      final String message,
      final Object... params) {
    if (_useWaiter()) {
      _verify(
          verificationQueue,
          expected,
          false,
          (a, b) -> _toState(a).isNotEqual(b),
          getDefaultWaitInSeconds(),
          getDefaultWaitIntervalInMilliSeconds(),
          message,
          params);
    } else {
      _verify(
          verificationQueue, expected, false, (a, b) -> _toState(a).isNotEqual(b), message, params);
    }
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance       instance of test related to this verification
   * @param diffFileNamePrefix the file name for diff image which should be generated in case if
   *                           images did not match. Please Note that we add index to the end to avoid duplicate file
   *                           name.
   * @param expected           value to compare
   * @param message            information about the propose of this verification
   * @param params             parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEqualsAny(
      CTest testInstance,
      final Iterable expected,
      final String diffFileNamePrefix,
      final String message,
      final Object... params) {
    verifyEqualsAny(testInstance.verify, expected, diffFileNamePrefix, message, params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue  CTest, CVerifier or any other verification queue instance
   * @param diffFileNamePrefix the file name for diff image which should be generated in case if
   *                           images did not match. Please Note that we add index to the end to avoid duplicate file
   *                           name.
   * @param expected           value to compare
   * @param message            information about the propose of this verification
   * @param params             parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEqualsAny(
      CVerificationQueue verificationQueue,
      final Iterable expected,
      final String diffFileNamePrefix,
      final String message,
      final Object... params) {
    if (_useWaiter()) {
      _verify(
          verificationQueue,
          expected,
          false,
          (a, e) -> _toState(a).equalsAny(e),
          (a, e) -> generateDiffForAllExpected(verificationQueue, diffFileNamePrefix, e),
          getDefaultWaitInSeconds(),
          getDefaultWaitIntervalInMilliSeconds(),
          message,
          params);
    } else {
      _verify(
          verificationQueue,
          expected,
          false,
          (a, e) -> _toState(a).equalsAny(e),
          (a, e) -> generateDiffForAllExpected(verificationQueue, diffFileNamePrefix, e),
          message,
          params);
    }
  }

  default void generateDiffForAllExpected(
      CVerificationQueue verificationQueue, String diffFileNamePrefix, Iterable expectedList) {
    int counter = 0;
    BufferedImage actual = get();
    for (BufferedImage exp : toBufferedImageList(expectedList)) {
      CImageUtil.generateDiffFile(
          actual,
          exp,
          diffFileNamePrefix + "_" + ++counter,
          GRAY_FLOAT_32);
    }
  }

  default CImageComparisionState _toState(Object o) {
    return new CImageComparisionState() {
      @Override
      public boolean isEqual(BufferedImage expected) {
        if (expected == null) {
          return false;
        }
        return CImageComparisionUtil.getDiffs(get(), expected, GRAY_FLOAT_32).isEmpty();
      }

      @Override
      public BufferedImage get() {
        if (o instanceof CFile) {
          return CImageUtil.readImageOrNull((File) o);
        }
        if (o instanceof CResource) {
          return CImageUtil.readImageOrNull((CResource) o);
        }
        if (o == null) {
          return null;
        }
        return (BufferedImage) o;
      }
    };
  }
}
