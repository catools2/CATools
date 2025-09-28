package org.catools.common.faker.tests;

import org.assertj.core.api.Assertions;
import org.catools.common.exception.CInvalidRangeException;
import org.catools.common.faker.CRandom;
import org.catools.common.faker.exception.CFakerCountryNotFoundException;
import org.catools.common.faker.model.CRandomAddress;
import org.catools.common.faker.model.CRandomCompany;
import org.catools.common.faker.model.CRandomName;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class CRandomTest {

  @Test
  public void CRandomIntThrowsExceptionForInvalidRange() {
    Assertions.assertThatThrownBy(() -> CRandom.Int.next(10, 5))
        .isInstanceOf(CInvalidRangeException.class)
        .hasMessage("Start value must be smaller or equal to end value.");
  }

  @Test
  public void CRandomLongThrowsExceptionForInvalidRange() {
    Assertions.assertThatThrownBy(() -> CRandom.Long.next(100L, 50L))
        .isInstanceOf(CInvalidRangeException.class)
        .hasMessage("Start value must be smaller or equal to end value.");
  }

  @Test
  public void CRandomFloatThrowsExceptionForInvalidRange() {
    Assertions.assertThatThrownBy(() -> CRandom.Float.next(10.5f, 5.5f))
        .isInstanceOf(CInvalidRangeException.class)
        .hasMessage("Start value must be smaller or equal to end value.");
  }

  @Test
  public void CRandomBigDecimalThrowsExceptionForInvalidRange() {
    Assertions.assertThatThrownBy(() -> CRandom.BigDecimal.next(BigDecimal.TEN, BigDecimal.ONE))
        .isInstanceOf(CInvalidRangeException.class)
        .hasMessage("Start value must be smaller or equal to end value.");
  }

  @Test
  public void CRandomDoubleThrowsExceptionForInvalidRange() {
    Assertions.assertThatThrownBy(() -> CRandom.Double.next(10.0, 5.0))
        .isInstanceOf(CInvalidRangeException.class)
        .hasMessage("Start value must be smaller or equal to end value.");
  }

  @Test
  public void CRandomStringThrowsExceptionForNegativeLength() {
    Assertions.assertThatThrownBy(() -> CRandom.String.randomNumeric(-5))
        .isInstanceOf(CInvalidRangeException.class)
        .hasMessageContaining("The length value should be greater than 0");
  }

  @Test
  public void CRandomStringThrowsExceptionForInvalidLengthRange() {
    Assertions.assertThatThrownBy(() -> CRandom.String.randomNumeric(10, 5))
        .isInstanceOf(CInvalidRangeException.class)
        .hasMessageContaining("The maxLengthExclusive should be equal or greater than minLengthInclusive");
  }

  @Test
  public void CRandomPersonNameThrowsExceptionForUnsupportedCountry() {
    Assertions.assertThatThrownBy(() -> CRandom.PersonName.next("XYZ"))
        .isInstanceOf(CFakerCountryNotFoundException.class)
        .hasMessageContaining("XYZ");
  }

  @Test
  public void CRandomAddressThrowsExceptionForUnsupportedCountry() {
    Assertions.assertThatThrownBy(() -> CRandom.Address.next("XYZ"))
        .isInstanceOf(CFakerCountryNotFoundException.class)
        .hasMessageContaining("XYZ");
  }

  @Test
  public void CRandomCompanyThrowsExceptionForUnsupportedCountry() {
    Assertions.assertThatThrownBy(() -> CRandom.Company.next("XYZ"))
        .isInstanceOf(CFakerCountryNotFoundException.class)
        .hasMessageContaining("XYZ");
  }


  @Test
  public void testCRandomInt() {
    int i = 10000;
    while (i-- > 0) {
      Assertions.assertThat(CRandom.Int.next(1, 10)).isBetween(1, 10);
      Assertions.assertThat(CRandom.Int.next(-20, 10)).isBetween(-20, 10);
      Assertions.assertThat(CRandom.Int.next(-10, -1)).isBetween(-10, -1);
    }
  }

  @Test
  public void testCRandomLong() {
    int i = 10000;
    while (i-- > 0) {
      Assertions.assertThat(CRandom.Long.next(1, 10)).isBetween(1L, 10L);
      Assertions.assertThat(CRandom.Long.next(-20, 10)).isBetween(-20L, 10L);
      Assertions.assertThat(CRandom.Long.next(-10, -1)).isBetween(-10L, -1L);
    }
  }

  @Test
  public void testCRandomFloat() {
    int i = 10000;
    while (i-- > 0) {
      Assertions.assertThat(CRandom.Float.next(1, 10)).isBetween(1F, 10F);
      Assertions.assertThat(CRandom.Float.next(-20, 10)).isBetween(-20F, 10F);
      Assertions.assertThat(CRandom.Float.next(-10, -1)).isBetween(-10F, -1F);
    }
  }

  @Test
  public void testCRandomBigDecimal() {
    int i = 10000;
    while (i-- > 0) {
      Assertions.assertThat(CRandom.BigDecimal.next(BigDecimal.ONE, BigDecimal.TEN))
          .isBetween(BigDecimal.ONE, BigDecimal.TEN);
      Assertions.assertThat(
              CRandom.BigDecimal.next(BigDecimal.valueOf(-20), BigDecimal.valueOf(10)))
          .isBetween(BigDecimal.valueOf(-20), BigDecimal.valueOf(10));
      Assertions.assertThat(
              CRandom.BigDecimal.next(BigDecimal.valueOf(-10), BigDecimal.valueOf(-1)))
          .isBetween(BigDecimal.valueOf(-10), BigDecimal.valueOf(-1));
    }
  }

  @Test
  public void testCRandomDouble() {
    int i = 10000;
    while (i-- > 0) {
      Assertions.assertThat(CRandom.Double.next(1d, 10d)).isBetween(1d, 10d);
      Assertions.assertThat(CRandom.Double.next(-20d, 10d)).isBetween(-20d, 10d);
      Assertions.assertThat(CRandom.Double.next(-10d, -1d)).isBetween(-10d, -1d);
    }
  }

  @Test
  public void testCRandomPerson() {
    int i = 10000;
    Set<CRandomName> values = new HashSet<>();
    while (i-- > 0) {
      values.add(CRandom.PersonName.next());
      values.add(CRandom.PersonName.nextFemale("USA"));
      values.add(CRandom.PersonName.nextMale("usa"));
    }
    Assertions.assertThat(values.size()).isGreaterThan(29990);
  }

  @Test
  public void testCRandomAddress() {
    int i = 10000;
    Set<CRandomAddress> values = new HashSet<>();
    while (i-- > 0) {
      values.add(CRandom.Address.next());
    }
    Assertions.assertThat(values.size()).isGreaterThan(9990);
  }

  @Test
  public void testCRandomCompany() {
    int i = 10000;
    Set<CRandomCompany> values = new HashSet<>();
    while (i-- > 0) {
      values.add(CRandom.Company.next());
    }
    Assertions.assertThat(values.size()).isGreaterThan(9990);
  }
}
