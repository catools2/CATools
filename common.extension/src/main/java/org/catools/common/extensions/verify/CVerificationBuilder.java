package org.catools.common.extensions.verify;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Build a sequence of verifications using method from different verification classes
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 * @see CBooleanVerification
 * @see CCollectionVerification
 * @see CDateVerification
 * @see CFileVerification
 * @see CNumberVerification
 * @see CObjectVerification
 * @see CStringVerification
 */
@Slf4j
public abstract class CVerificationBuilder<T extends CVerificationBuilder<T>> {
  public final CObjectVerification Object = new CObjectVerification() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CCollectionVerification Collection = new CCollectionVerification() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CMapVerification Map = new CMapVerification() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CBooleanVerification Bool = new CBooleanVerification() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CDateVerification Date = new CDateVerification() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CStringVerification String = new CStringVerification() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CFileVerification File = new CFileVerification() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CNumberVerification<Long> Long = new CNumberVerification<>() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CNumberVerification<BigDecimal> BigDecimal = new CNumberVerification<>() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CNumberVerification<Double> Double = new CNumberVerification<>() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CNumberVerification<Float> Float = new CNumberVerification<>() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };
  public final CNumberVerification<Integer> Int = new CNumberVerification<>() {
    @Override
    protected void queue(CVerificationInfo expectation) {
      CVerificationBuilder.this.queue(expectation);
    }
  };

  public CVerificationBuilder() {
    super();
  }

  public abstract void queue(CVerificationInfo verificationInfo);
}
