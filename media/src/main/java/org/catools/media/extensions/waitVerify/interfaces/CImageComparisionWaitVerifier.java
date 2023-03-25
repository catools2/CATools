package org.catools.media.extensions.waitVerify.interfaces;

import org.catools.common.extensions.verify.CVerificationBuilder;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.tests.CTest;
import org.catools.media.extensions.verify.interfaces.CImageComparisionVerifier;
import org.catools.media.extensions.wait.interfaces.CImageComparisionWaiter;
import org.catools.media.utils.CImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.catools.media.enums.CImageComparisonType.GRAY_FLOAT_32;

/**
 * CImageComparisionWaitVerifier is an interface for BufferedImage verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 */
public interface CImageComparisionWaitVerifier extends CImageComparisionVerifier {
  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance  instance of test related to this verification
   * @param expected      value to compare
   * @param diffFileName  the file name for diff image which should be generated in case if images
   *                      did not match
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance,
      final BufferedImage expected,
      final String diffFileName,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        testInstance.verify,
        expected,
        diffFileName,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance  instance of test related to this verification
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance,
      final CFile expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        testInstance.verify,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance  instance of test related to this verification
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance,
      final CResource expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        testInstance.verify,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param diffFileName      the file name for diff image which should be generated in case if images
   *                          did not match
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final BufferedImage expected,
      final String diffFileName,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        verificationQueue,
        expected,
        diffFileName,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final CFile expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final CResource expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance           instance of test related to this verification
   * @param expected               value to compare
   * @param diffFileName           the file name for diff image which should be generated in case if images
   *                               did not match
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance,
      final BufferedImage expected,
      final String diffFileName,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        testInstance.verify,
        expected,
        diffFileName,
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance           instance of test related to this verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance,
      final CFile expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        testInstance.verify, expected, waitInSeconds, intervalInMilliSeconds, message, params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance           instance of test related to this verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CTest testInstance,
      final CResource expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        testInstance.verify, expected, waitInSeconds, intervalInMilliSeconds, message, params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param diffFileName           the file name for diff image which should be generated in case if images
   *                               did not match
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final BufferedImage expected,
      final String diffFileName,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).isEqual(b),
        (a, b) -> CImageUtil.generateDiffFile((BufferedImage) a, b, diffFileName, GRAY_FLOAT_32),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final CFile expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        verificationQueue,
        CImageUtil.readImageOrNull(expected),
        expected.getName(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      final CResource expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        verificationQueue,
        CImageUtil.readImageOrNull(expected),
        expected.getResourceName(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param testInstance  instance of test related to this verification
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CTest testInstance,
      final CFile expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEquals(
        testInstance.verify,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param testInstance  instance of test related to this verification
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CTest testInstance,
      final CResource expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEquals(
        testInstance.verify,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CVerificationQueue verificationQueue,
      final CFile expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CVerificationQueue verificationQueue,
      final CResource expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param testInstance           instance of test related to this verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CTest testInstance,
      final CFile expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyNotEquals(
        testInstance.verify, expected, waitInSeconds, intervalInMilliSeconds, message, params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param testInstance           instance of test related to this verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CTest testInstance,
      final CResource expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyNotEquals(
        testInstance.verify, expected, waitInSeconds, intervalInMilliSeconds, message, params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CVerificationQueue verificationQueue,
      final CFile expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).isNotEqual(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected are not equal
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyNotEquals(
      CVerificationQueue verificationQueue,
      final CResource expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).isNotEqual(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance       instance of test related to this verification
   * @param diffFileNamePrefix the file name for diff image which should be generated in case if
   *                           images did not match. Please Note that we add index to the end to avoid duplicate file
   *                           name.
   * @param expected           value to compare
   * @param waitInSeconds      maximum wait time
   * @param message            information about the propose of this verification
   * @param params             parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEqualsAny(
      CTest testInstance,
      final Iterable expected,
      final String diffFileNamePrefix,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsAny(
        testInstance.verify,
        expected,
        diffFileNamePrefix,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue  CTest, CVerifier or any other verification queue instance
   * @param diffFileNamePrefix the file name for diff image which should be generated in case if
   *                           images did not match. Please Note that we add index to the end to avoid duplicate file
   *                           name.
   * @param expected           value to compare
   * @param waitInSeconds      maximum wait time
   * @param message            information about the propose of this verification
   * @param params             parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEqualsAny(
      CVerificationQueue verificationQueue,
      final Iterable expected,
      final String diffFileNamePrefix,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsAny(
        verificationQueue,
        expected,
        diffFileNamePrefix,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param testInstance           instance of test related to this verification
   * @param diffFileNamePrefix     the file name for diff image which should be generated in case if
   *                               images did not match. Please Note that we add index to the end to avoid duplicate file
   *                               name.
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEqualsAny(
      CTest testInstance,
      final Iterable expected,
      final String diffFileNamePrefix,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyEqualsAny(
        testInstance.verify,
        expected,
        diffFileNamePrefix,
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected are equal
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param diffFileNamePrefix     the file name for diff image which should be generated in case if
   *                               images did not match. Please Note that we add index to the end to avoid duplicate file
   *                               name.
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   * @caller {@link CVerificationBuilder} so we can do chain calls
   */
  default void verifyEqualsAny(
      CVerificationQueue verificationQueue,
      final Iterable expected,
      final String diffFileNamePrefix,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).equalsAny(e),
        (a, e) -> generateDiffForAllExpected(verificationQueue, diffFileNamePrefix, e),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  private CImageComparisionWaiter _toWaiter(Object o) {
    if (o instanceof File) {
      return () -> CImageUtil.readImageOrNull((File) o);
    }
    if (o instanceof CResource) {
      return () -> CImageUtil.readImageOrNull((CResource) o);
    }
    if (o == null) {
      return () -> null;
    }
    return () -> (BufferedImage) o;
  }
}
