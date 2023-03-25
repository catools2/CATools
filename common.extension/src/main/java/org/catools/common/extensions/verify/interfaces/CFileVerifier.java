package org.catools.common.extensions.verify.interfaces;

import org.catools.common.extensions.states.interfaces.CFileState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.io.CFile;

import java.io.File;

/**
 * CFileVerifier is an interface for File verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 */
public interface CFileVerifier extends CObjectVerifier<File, CFileState> {
  default CFileState _toState(Object e) {
    return () -> (File) e;
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedFile      file to compare
   */
  default void verifyEqualsStringContent(
      final CVerificationQueue verificationQueue, final File expectedFile) {
    verifyEqualsStringContent(
        verificationQueue, expectedFile, getDefaultMessage(("String Content Equals")));
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedFile      file to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final File expectedFile,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedFile,
        false,
        (f1, f2) -> _toState(f1).equalsStringContent(f2),
        message,
        params);
  }

  /**
   * Verify that the file exists
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyExists(final CVerificationQueue verificationQueue) {
    verifyExists(verificationQueue, getDefaultMessage(("Exists")));
  }

  /**
   * Verify that the file exists
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyExists(
      final CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(verificationQueue, true, false, (file, aBoolean) -> get().exists(), message, params);
  }

  /**
   * Verify that the file does not exists
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsNotExists(final CVerificationQueue verificationQueue) {
    verifyIsNotExists(verificationQueue, getDefaultMessage(("Does Not Exist")));
  }

  /**
   * Verify that the file does not exists
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotExists(
      final CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(verificationQueue, true, false, (file, aBoolean) -> !get().exists(), message, params);
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedFile      file to compare
   */
  default void verifyNotEqualsStringContent(
      final CVerificationQueue verificationQueue, final CFile expectedFile) {
    verifyNotEqualsStringContent(
        verificationQueue, expectedFile, getDefaultMessage(("String Content Not Equals")));
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedFile      file to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsStringContent(
      final CVerificationQueue verificationQueue,
      final CFile expectedFile,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedFile,
        false,
        (f1, f2) -> _toState(f1).notEqualsStringContent(f2),
        message,
        params);
  }
}
