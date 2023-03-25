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
public abstract class CVerificationBuilder<T extends CVerificationBuilder<T>> implements CVerificationQueue {
  public final CObjectVerification<T> Object;
  public final CCollectionVerification<T> Collection;
  public final CMapVerification<T> Map;
  public final CBooleanVerification<T> Bool;
  public final CDateVerification<T> Date;
  public final CStringVerification<T> String;
  public final CFileVerification<T> File;
  public final CNumberVerification<T, Long> Long;
  public final CNumberVerification<T, BigDecimal> BigDecimal;
  public final CNumberVerification<T, Double> Double;
  public final CNumberVerification<T, Float> Float;
  public final CNumberVerification<T, Integer> Int;

  public CVerificationBuilder() {
    super();
    this.Object = new CObjectVerification<>((T) this);
    this.Collection = new CCollectionVerification<>((T) this);
    this.Map = new CMapVerification<>((T) this);
    this.Bool = new CBooleanVerification<>((T) this);
    this.Date = new CDateVerification<>((T) this);
    this.String = new CStringVerification<>((T) this);
    this.File = new CFileVerification<>((T) this);

    this.BigDecimal = new CNumberVerification<>((T) this);
    this.Double = new CNumberVerification<>((T) this);
    this.Float = new CNumberVerification<>((T) this);
    this.Long = new CNumberVerification<>((T) this);
    this.Int = new CNumberVerification<>((T) this);
  }

  public abstract T queue(CVerificationInfo verificationInfo);
}
