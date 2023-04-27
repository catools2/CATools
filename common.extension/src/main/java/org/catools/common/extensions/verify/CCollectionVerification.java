package org.catools.common.extensions.verify;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.extensions.verify.interfaces.CCollectionVerifier;
import org.slf4j.Logger;

import java.util.Collection;

/**
 * Collection verification class contains all verification method which is related to Collection
 */
@Slf4j
public class CCollectionVerification extends CIterableVerification {
  
  /**
   * Verify the map size is equal to expected value.
   *
   * @param expected value to compare
   */
  public <C extends Collection> void verifySizeEquals(C actual, int expected) {
    toVerifier(actual).verifySizeEquals(expected);
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param expected value to compare
   * @param message  information about the purpose of verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public <C extends Collection> void verifySizeEquals(C actual, int expected, final String message, final Object... params) {
    toVerifier(actual).verifySizeEquals(expected, message, params);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param expected value to compare
   */
  public <C extends Collection> void verifySizeIsGreaterThan(C actual, int expected) {
    toVerifier(actual).verifySizeIsGreaterThan(expected);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param expected value to compare
   * @param message  information about the purpose of verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public <C extends Collection> void verifySizeIsGreaterThan(C actual, int expected, final String message, final Object... params) {
    toVerifier(actual).verifySizeIsGreaterThan(expected, message, params);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param expected value to compare
   */
  public <C extends Collection> void verifySizeIsLessThan(C actual, int expected) {
    toVerifier(actual).verifySizeIsLessThan(expected);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param expected value to compare
   * @param message  information about the purpose of verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public <C extends Collection> void verifySizeIsLessThan(C actual, int expected, final String message, final Object... params) {
    toVerifier(actual).verifySizeIsLessThan(expected, message, params);
  }

  private <C> CCollectionVerifier<C> toVerifier(Collection actual) {
    CBaseVerification that = this;
    return new CCollectionVerifier<C>() {
      @Override
      public Logger getLogger() {
        return CCollectionVerification.log;
      }

      @Override
      public void queue(CVerificationInfo expectation) {
        that.queue(expectation);
      }

      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public Iterable<C> get() {
        return actual;
      }
    };
  }
}
