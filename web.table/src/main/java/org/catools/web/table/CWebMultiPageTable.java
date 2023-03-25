package org.catools.web.table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;
import org.catools.common.utils.CSleeper;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.openqa.selenium.By;

import javax.ws.rs.NotSupportedException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Getter
@Setter
@Accessors(chain = true)
public abstract class CWebMultiPageTable<
    DR extends CDriver, R extends CWebTableRow<DR, ? extends CWebTable<DR, R>>>
    extends CWebTable<DR, R> {
  private CWebElement<DR> firstLink;
  private CWebElement<DR> previousLink;
  private CWebElement<DR> nextLink;
  private CWebElement<DR> lastLink;
  private int maxNumberOfPageToIterate;

  public CWebMultiPageTable(
      String name,
      DR driver,
      String baseXpath,
      By firstLocator,
      By previousLocator,
      By nextLocator,
      By lastLocator) {
    this(
        name,
        driver,
        baseXpath,
        firstLocator,
        previousLocator,
        nextLocator,
        lastLocator,
        DEFAULT_TIMEOUT);
  }

  public CWebMultiPageTable(
      String name,
      DR driver,
      String baseXpath,
      By firstLocator,
      By previousLocator,
      By nextLocator,
      By lastLocator,
      int waitSec) {
    this(
        name,
        driver,
        baseXpath,
        firstLocator,
        previousLocator,
        nextLocator,
        lastLocator,
        waitSec,
        100);
  }

  public CWebMultiPageTable(
      String name,
      DR driver,
      String baseXpath,
      By firstLocator,
      By previousLocator,
      By nextLocator,
      By lastLocator,
      int waitSec,
      int maxNumberOfPageToIterate) {
    super(name, driver, baseXpath, waitSec);
    this.firstLink = new CWebElement<>("First", driver, firstLocator);
    this.previousLink = new CWebElement<>("Previous", driver, previousLocator);
    this.nextLink = new CWebElement<>("Next", driver, nextLocator);
    this.lastLink = new CWebElement<>("Last", driver, lastLocator);
    this.maxNumberOfPageToIterate = maxNumberOfPageToIterate;
  }

  public abstract String getCurrentPageNumber();

  @Override
  public CList<R> getAll(Map<String, String> searchCriteria) {
    setSearchCriteria(searchCriteria);
    return getAll();
  }

  @Override
  public CList<R> getAll(Map<String, String> searchCriteria, Predicate<R> predicate) {
    setSearchCriteria(searchCriteria);
    return getAll(predicate);
  }

  @Override
  public R getAny(Map<String, String> searchCriteria) {
    setSearchCriteria(searchCriteria);
    return getRandom();
  }

  @Override
  public R getFirst(Map<String, String> searchCriteria) {
    int counter = maxNumberOfPageToIterate;
    while (counter-- > 0 && isDataAvailable()) {
      R element = super.getFirstOrNull(searchCriteria);
      if (element != null) {
        return element;
      } else if (!gotoNextPage()) {
        break;
      }
    }
    return null;
  }

  @Override
  public R getFirst(Map<String, String> searchCriteria, Predicate<R> predicate) {
    setSearchCriteria(searchCriteria);
    return getFirst(predicate);
  }

  @Override
  public R getFirstOrElse(Map<String, String> searchCriteria, R other) {
    setSearchCriteria(searchCriteria);
    return getFirstOrElse(other);
  }

  @Override
  public R getFirstOrElse(Map<String, String> searchCriteria, Predicate<R> predicate, R other) {
    setSearchCriteria(searchCriteria);
    return getFirstOrElse(predicate, other);
  }

  @Override
  public R getFirstOrElseGet(Map<String, String> searchCriteria, Supplier<R> other) {
    setSearchCriteria(searchCriteria);
    return getFirstOrElseGet(other);
  }

  @Override
  public R getFirstOrElseGet(
      Map<String, String> searchCriteria, Predicate<R> predicate, Supplier<R> other) {
    setSearchCriteria(searchCriteria);
    return getFirstOrElseGet(predicate, other);
  }

  @Override
  public R getFirstOrNull(Map<String, String> searchCriteria) {
    setSearchCriteria(searchCriteria);
    return getFirstOrNull();
  }

  @Override
  public R getFirstOrNull(Map<String, String> searchCriteria, Predicate<R> predicate) {
    setSearchCriteria(searchCriteria);
    return getFirstOrNull(predicate);
  }

  @Override
  public R getFirstOrAny(Map<String, String> searchCriteria, Predicate<R> predicate) {
    setSearchCriteria(searchCriteria);
    return getFirstOrAny(predicate);
  }

  @Override
  public R getFirstOrThrow(Map<String, String> searchCriteria, RuntimeException e) {
    setSearchCriteria(searchCriteria);
    return getFirstOrThrow(e);
  }

  @Override
  public <X extends RuntimeException> R getFirstOrThrow(
      Map<String, String> searchCriteria,
      Predicate<R> predicate,
      Supplier<? extends X> exceptionSupplier) {
    setSearchCriteria(searchCriteria);
    return getFirstOrThrow(predicate, exceptionSupplier);
  }

  @Override
  public R getFirstOrElse(
      Map<String, String> searchCriteria, Predicate<R> predicate, Supplier<R> other) {
    setSearchCriteria(searchCriteria);
    return getFirstOrElse(predicate, other);
  }

  public boolean gotoFirstPage() {
    logger.trace("Go to first page.");
    if (firstLink.isNotClickable()) {
      if (previousLink.isNotClickable()) {
        return false;
      }
      int counter = maxNumberOfPageToIterate;
      while (gotoPreviousPage() && counter-- > 0) {
        ;
      }
      return true;
    }
    firstLink.click();
    CSleeper.sleepTightInSeconds(1);
    return true;
  }

  public boolean gotoPreviousPage() {
    if (previousLink.isNotClickable()) {
      return false;
    }
    String currentPageNumber = getCurrentPageNumber();
    logger.trace("Go to previous page from page {}.", currentPageNumber);
    previousLink.click();
    if (StringUtils.isNotBlank(currentPageNumber)) {
      isDataAvailable();
      if (currentPageNumber.equals(getCurrentPageNumber())) {
        return false;
      }
    }
    CSleeper.sleepTightInSeconds(1);
    return true;
  }

  public boolean gotoNextPage() {
    if (nextLink.isNotClickable()) {
      return false;
    }
    String currentPageNumber = getCurrentPageNumber();
    logger.trace("Go to next page from page {}.", currentPageNumber);
    nextLink.click();
    return StringUtils.isBlank(currentPageNumber)
        || !currentPageNumber.equals(getCurrentPageNumber());
  }

  public void gotoLastPage() {
    logger.trace("Go to last page.");
    if (lastLink.isNotClickable()) {
      lastLink.click();
    } else {
      int counter = maxNumberOfPageToIterate;
      while (gotoNextPage() && counter-- > 0) {
        ;
      }
    }
  }

  @Override
  public Iterator<R> iterator() {
    gotoFirstPage();
    return new Iterator<>() {
      int counter = maxNumberOfPageToIterate;
      int cursor = 0;

      @Override
      public boolean hasNext() {
        boolean found = false;

        while (counter-- > 0 && !(found = hasRecord(cursor)) && gotoNextPage()) {
          // go to next page if no record found;
          cursor = 0;
        }
        return found;
      }

      @SuppressWarnings("unchecked")
      @Override
      public R next() {
        return getRecord(cursor++);
      }

      @Override
      public void remove() {
        throw new NotSupportedException();
      }
    };
  }
}
