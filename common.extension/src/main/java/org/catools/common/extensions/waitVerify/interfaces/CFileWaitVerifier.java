package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.extensions.states.interfaces.CFileState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CFileVerifier;
import org.catools.common.io.CFile;

import java.io.File;

/**
 * CFileVerifier is an interface for File verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 */
public interface CFileWaitVerifier extends CFileVerifier, CObjectWaitVerifier<File, CFileState> {
  default CFileState _toState(Object e) {
    return () -> (File) e;
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedFile      file to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final File expectedFile,
      final int waitInSeconds) {
    verifyEqualsStringContent(
        verificationQueue, expectedFile, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedFile      file to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final File expectedFile,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsStringContent(
        verificationQueue,
        expectedFile,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedFile           file to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final File expectedFile,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEqualsStringContent(
        verificationQueue,
        expectedFile,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("String Content Equals"));
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedFile           file to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final File expectedFile,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedFile,
        false,
        (f1, f2) -> _toState(f1).equalsStringContent(f2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that the file exists
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyExists(final CVerificationQueue verificationQueue, final int waitInSeconds) {
    this.verifyExists(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that the file exists
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyExists(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    this.verifyExists(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that the file exists
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyExists(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyExists(
        verificationQueue, waitInSeconds, intervalInMilliSeconds, getDefaultMessage("Exists"));
  }

  /**
   * Verify that the file exists
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyExists(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (file, aBoolean) -> get().exists(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that the file does not exists
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotExists(
      final CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotExists(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that the file does not exists
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotExists(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotExists(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that the file does not exists
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotExists(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotExists(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Does Not Exist"));
  }

  /**
   * Verify that the file does not exists
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotExists(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (file, aBoolean) -> !get().exists(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedFile      file to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final CFile expectedFile,
      final int waitInSeconds) {
    verifyNotEqualsStringContent(
        verificationQueue, expectedFile, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedFile      file to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final CFile expectedFile,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEqualsStringContent(
        verificationQueue,
        expectedFile,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedFile           file to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final CFile expectedFile,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotEqualsStringContent(
        verificationQueue,
        expectedFile,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("String Content Not Equals"));
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedFile           file to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final CFile expectedFile,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedFile,
        false,
        (f1, f2) -> _toState(f1).notEqualsStringContent(f2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }
}
