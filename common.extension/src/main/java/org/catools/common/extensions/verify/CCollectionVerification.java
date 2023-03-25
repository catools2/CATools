package org.catools.common.extensions.verify;

import org.catools.common.extensions.verify.interfaces.CCollectionVerifier;

import java.util.Collection;

/**
 * Collection verification class contains all verification method which is related to Collection
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CCollectionVerification<T extends CVerificationBuilder>
    extends CIterableVerification<T> {
  public CCollectionVerification(T verifier) {
    super(verifier);
  }

  private <C> CCollectionVerifier<C> toVerifier(Collection<C> actual) {
    return new CCollectionVerifier<C>() {
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
