package org.catools.common.extensions.verify;

import org.catools.common.extensions.verify.interfaces.CFileVerifier;
import org.catools.common.io.CFile;

import java.io.File;

/**
 * File Content verification class contains all verification method which is related to File and its
 * content
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CFileVerification<T extends CVerificationBuilder> extends CBaseVerification<T> {

  public CFileVerification(T verifier) {
    super(verifier);
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param actualFile   file full name to compare
   * @param expectedFile file full name to compare
   */
  public void equalsStringContent(final String actualFile, final String expectedFile) {
    equalsStringContent(new CFile(actualFile), new CFile(expectedFile));
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param actualFile   file full name to compare
   * @param expectedFile file full name to compare
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void equalsStringContent(
      final String actualFile,
      final String expectedFile,
      final String message,
      final Object... params) {
    equalsStringContent(new CFile(actualFile), new CFile(expectedFile), message, params);
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param actualFile   file to compare
   * @param expectedFile file to compare
   */
  public void equalsStringContent(final CFile actualFile, final CFile expectedFile) {
    toVerifier(actualFile).verifyEqualsStringContent(verifier, expectedFile);
  }

  /**
   * Verify that actual and expected file have the exact same content.
   *
   * @param actualFile   file to compare
   * @param expectedFile file to compare
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void equalsStringContent(
      final CFile actualFile,
      final CFile expectedFile,
      final String message,
      final Object... params) {
    toVerifier(actualFile).verifyEqualsStringContent(verifier, expectedFile, message, params);
  }

  /**
   * Verify that the file exists
   *
   * @param actualFile file full name to compare
   */
  public void exists(final String actualFile) {
    toVerifier(new File(actualFile)).verifyExists(verifier);
  }

  /**
   * Verify that the file exists
   *
   * @param actualFile file full name to compare
   * @param message    information about the propose of this verification
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void exists(final String actualFile, final String message, final Object... params) {
    toVerifier(new File(actualFile)).verifyExists(verifier, message, params);
  }

  /**
   * Verify that the file exists
   *
   * @param actualFile file full name to compare
   */
  public void exists(final CFile actualFile) {
    toVerifier(actualFile).verifyExists(verifier);
  }

  /**
   * Verify that the file exists
   *
   * @param actualFile file full name to compare
   * @param message    information about the propose of this verification
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void exists(final CFile actualFile, final String message, final Object... params) {
    toVerifier(actualFile).verifyExists(verifier, message, params);
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param actualFile   file full name to compare
   * @param expectedFile file full name to compare
   */
  public void notEqualsStringContent(final String actualFile, final String expectedFile) {
    notEqualsStringContent(new CFile(actualFile), new CFile(expectedFile));
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param actualFile   file full name to compare
   * @param expectedFile file full name to compare
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void notEqualsStringContent(
      final String actualFile,
      final String expectedFile,
      final String message,
      final Object... params) {
    notEqualsStringContent(new CFile(actualFile), new CFile(expectedFile), message, params);
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param actualFile   file to compare
   * @param expectedFile file to compare
   */
  public void notEqualsStringContent(final CFile actualFile, final CFile expectedFile) {
    toVerifier(actualFile).verifyNotEqualsStringContent(verifier, expectedFile);
  }

  /**
   * Verify that actual and expected file does not have the exact same content.
   *
   * @param actualFile   file to compare
   * @param expectedFile file to compare
   * @param message      information about the propose of this verification
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void notEqualsStringContent(
      final CFile actualFile,
      final CFile expectedFile,
      final String message,
      final Object... params) {
    toVerifier(actualFile).verifyNotEqualsStringContent(verifier, expectedFile, message, params);
  }

  /**
   * Verify that the file does not exists
   *
   * @param actualFile file full name to compare
   */
  public void notExists(final String actualFile) {
    toVerifier(new File(actualFile)).verifyIsNotExists(verifier);
  }

  /**
   * Verify that the file does not exists
   *
   * @param actualFile file full name to compare
   * @param message    information about the propose of this verification
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void notExists(final String actualFile, final String message, final Object... params) {
    toVerifier(new File(actualFile)).verifyIsNotExists(verifier, message, params);
  }

  /**
   * Verify that the file does not exists
   *
   * @param actualFile file full name to compare
   */
  public void notExists(final CFile actualFile) {
    toVerifier(actualFile).verifyIsNotExists(verifier);
  }

  /**
   * Verify that the file does not exists
   *
   * @param actualFile file full name to compare
   * @param message    information about the propose of this verification
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void notExists(final CFile actualFile, final String message, final Object... params) {
    toVerifier(actualFile).verifyIsNotExists(verifier, message, params);
  }

  private CFileVerifier toVerifier(File actual) {
    return new CFileVerifier() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public File get() {
        return actual;
      }
    };
  }
}
