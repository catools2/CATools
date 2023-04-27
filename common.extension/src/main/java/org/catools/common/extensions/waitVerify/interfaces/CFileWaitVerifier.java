package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.extensions.states.interfaces.CFileState;
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
   * @param expectedFile  file to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyEqualsStringContent(final File expectedFile, final int waitInSeconds) {
    verifyEqualsStringContent(expectedFile, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param expectedFile  file to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the purpose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsStringContent(final File expectedFile, final int waitInSeconds, final String message, final Object... params) {
    verifyEqualsStringContent(expectedFile, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param expectedFile           file to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsStringContent(final File expectedFile, final int waitInSeconds, final int intervalInMilliSeconds) {
    verifyEqualsStringContent(expectedFile, waitInSeconds, intervalInMilliSeconds, getDefaultMessage("String Content Equals"));
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param expectedFile           file to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the purpose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsStringContent(final File expectedFile, final int waitInSeconds, final int intervalInMilliSeconds, final String message, final Object... params) {
    _verify(expectedFile, false, (f1, f2) -> _toState(f1).equalsStringContent(f2), waitInSeconds, intervalInMilliSeconds, message, params);
  }

  /**
   * Verify that the file exists
   *
   * @param waitInSeconds maximum wait time
   */
  default void verifyExists(final int waitInSeconds) {
    this.verifyExists(waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that the file exists
   *
   * @param waitInSeconds maximum wait time
   * @param message       information about the purpose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   */
  default void verifyExists(final int waitInSeconds, final String message, final Object... params) {
    this.verifyExists(waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that the file exists
   *
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyExists(final int waitInSeconds, final int intervalInMilliSeconds) {
    verifyExists(waitInSeconds, intervalInMilliSeconds, getDefaultMessage("Exists"));
  }

  /**
   * Verify that the file exists
   *
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the purpose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyExists(final int waitInSeconds, final int intervalInMilliSeconds, final String message, final Object... params) {
    _verify(true, false, (file, aBoolean) -> get().exists(), waitInSeconds, intervalInMilliSeconds, message, params);
  }

  /**
   * Verify that the file does not exist
   *
   * @param waitInSeconds maximum wait time
   */
  default void verifyIsNotExists(final int waitInSeconds) {
    verifyIsNotExists(waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that the file does not exist
   *
   * @param waitInSeconds maximum wait time
   * @param message       information about the purpose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotExists(final int waitInSeconds, final String message, final Object... params) {
    verifyIsNotExists(waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that the file does not exist
   *
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotExists(final int waitInSeconds, final int intervalInMilliSeconds) {
    verifyIsNotExists(waitInSeconds, intervalInMilliSeconds, getDefaultMessage("Does Not Exist"));
  }

  /**
   * Verify that the file does not exist
   *
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the purpose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotExists(final int waitInSeconds, final int intervalInMilliSeconds, final String message, final Object... params) {
    _verify(true, false, (file, aBoolean) -> !get().exists(), waitInSeconds, intervalInMilliSeconds, message, params);
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param expectedFile  file to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyNotEqualsStringContent(final CFile expectedFile, final int waitInSeconds) {
    verifyNotEqualsStringContent(expectedFile, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param expectedFile  file to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the purpose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsStringContent(final CFile expectedFile, final int waitInSeconds, final String message, final Object... params) {
    verifyNotEqualsStringContent(expectedFile, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param expectedFile           file to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotEqualsStringContent(final CFile expectedFile, final int waitInSeconds, final int intervalInMilliSeconds) {
    verifyNotEqualsStringContent(expectedFile, waitInSeconds, intervalInMilliSeconds, getDefaultMessage("String Content Not Equals"));
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param expectedFile           file to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the purpose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsStringContent(final CFile expectedFile, final int waitInSeconds, final int intervalInMilliSeconds, final String message, final Object... params) {
    _verify(expectedFile, false, (f1, f2) -> _toState(f1).notEqualsStringContent(f2), waitInSeconds, intervalInMilliSeconds, message, params);
  }
}
