package org.catools.web.controls;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.extensions.verify.CVerify;
import org.catools.media.utils.CImageUtil;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.drivers.CDriver;
import org.openqa.selenium.*;
import org.slf4j.Logger;

import java.awt.image.BufferedImage;
import java.util.function.Function;

public interface CWebElementStates<DR extends CDriver> {
  int DEFAULT_TIMEOUT = CDriverConfigs.getTimeout();

  boolean isUseJS();

  DR getDriver();

  int getWaitSec();

  By getLocator();

  default CVerify getVerify() {
    return getDriver().getVerify();
  }

  default Logger getLogger() {
    return getDriver().getLogger();
  }

  // Getters
  default Integer getOffset() {
    return getOffset(0);
  }

  default Integer getOffset(int waitSec) {
    return waitUntil(
        "Get Offset", waitSec, null, el -> Integer.valueOf(el.getAttribute("offsetTop")));
  }

  default boolean isStaleness() {
    return isStaleness(0);
  }

  default boolean isStaleness(int waitSec) {
    return waitUntil(
        "Is Staleness",
        waitSec,
        false,
        el -> {
          try {
            return !el.isEnabled();
          } catch (StaleElementReferenceException | NullPointerException var3) {
            return true;
          }
        });
  }

  default boolean isNotStaleness() {
    return isNotStaleness(0);
  }

  default boolean isNotStaleness(int waitSec) {
    return waitUntil(
        "Is Not Staleness",
        waitSec,
        true,
        el -> {
          try {
            return el.isEnabled();
          } catch (StaleElementReferenceException | NullPointerException var3) {
            return false;
          }
        });
  }

  default boolean isPresent() {
    return isPresent(0);
  }

  default boolean isPresent(int waitSec) {
    return waitUntil("Is Present", waitSec, false, el -> el != null);
  }

  default boolean isNotPresent() {
    return isNotPresent(0);
  }

  default boolean isNotPresent(int waitSec) {
    return getDriver()
        .waitUntil(
            "Is Not Present",
            waitSec,
            false,
            webDriver -> {
              try {
                return webDriver.findElement(getLocator()) == null;
              } catch (NoSuchElementException | NoSuchFrameException | NoSuchWindowException e) {
                return true;
              }
            });
  }

  default boolean isVisible() {
    return isVisible(0);
  }

  default boolean isVisible(int waitSec) {
    return waitUntil("Is Visible", waitSec, false, el -> el.isDisplayed());
  }

  default boolean isNotVisible() {
    return isNotVisible(0);
  }

  default boolean isNotVisible(int waitSec) {
    return getDriver()
        .waitUntil(
            "Is Not Visible",
            waitSec,
            false,
            webDriver -> {
              try {
                WebElement el = webDriver.findElement(getLocator());
                return el == null || !el.isDisplayed();
              } catch (NoSuchElementException | NoSuchFrameException | NoSuchWindowException e) {
                return true;
              }
            });
  }

  default boolean isEnabled() {
    return isEnabled(0);
  }

  default boolean isEnabled(int waitSec) {
    return waitUntil(
        "Is Enable",
        waitSec,
        false,
        el -> {
          return el != null
              && el.isEnabled()
              && StringUtils.isBlank(el.getAttribute("readonly"))
              && StringUtils.isBlank(el.getAttribute("disabled"))
              && el.findElements(
                  By.xpath(
                      "./ancestor::*[contains(@class, 'disabled') or contains(@class, 'readonly') or contains(@class, 'hidden')]"))
              .isEmpty();
        });
  }

  default boolean isNotEnabled() {
    return isNotEnabled(0);
  }

  default boolean isNotEnabled(int waitSec) {
    return getDriver()
        .waitUntil(
            "Is Not Enabled",
            waitSec,
            false,
            webDriver -> {
              try {
                WebElement el = webDriver.findElement(getLocator());
                return el == null || !el.isEnabled();
              } catch (NoSuchElementException | NoSuchFrameException | NoSuchWindowException e) {
                return true;
              }
            });
  }

  default boolean isSelected() {
    return isSelected(0);
  }

  default boolean isSelected(int waitSec) {
    return waitUntil("Is Selected", waitSec, false, el -> el.isSelected());
  }

  default boolean isNotSelected() {
    return isNotSelected(0);
  }

  default boolean isNotSelected(int waitSec) {
    return getDriver()
        .waitUntil(
            "Is Not Selected",
            waitSec,
            false,
            webDriver -> {
              try {
                WebElement el = webDriver.findElement(getLocator());
                return el == null || !el.isSelected();
              } catch (NoSuchElementException | NoSuchFrameException | NoSuchWindowException e) {
                return true;
              }
            });
  }

  default boolean isClickable() {
    return isClickable(0);
  }

  default boolean isClickable(int waitSec) {
    return isEnabled(waitSec);
  }

  default boolean isNotClickable() {
    return isNotClickable(0);
  }

  default boolean isNotClickable(int waitSec) {
    return isNotEnabled(waitSec);
  }

  default String getText() {
    return getText(0);
  }

  default String getText(int waitSec) {
    return waitUntil("Get Text", waitSec, "", el -> el.getText());
  }

  default String getValue() {
    return getValue(0);
  }

  default String getValue(int waitSec) {
    return waitUntil("Get Value", waitSec, "", el -> el.getAttribute("value"));
  }

  default String getInnerHTML() {
    return getInnerHTML(0);
  }

  default String getInnerHTML(int waitSec) {
    return waitUntil(
        "Get Inner HTML",
        waitSec,
        "",
        el -> getDriver().executeScript("return arguments[0].innerHTML", el));
  }

  default String getTagName() {
    return getTagName(0);
  }

  default String getTagName(int waitSec) {
    return waitUntil("Get Tag", waitSec, "", el -> el.getTagName());
  }

  default String getCss(final String cssKey) {
    return getCss(cssKey, 0);
  }

  default String getCss(final String cssKey, int waitSec) {
    return waitUntil("Get Css", waitSec, "", el -> el.getCssValue(cssKey));
  }

  default String getAttribute(final String attribute) {
    return getAttribute(attribute, 0);
  }

  default String getAttribute(final String attribute, int waitSec) {
    return waitUntil("Get Attribute", waitSec, "", el -> el.getAttribute(attribute));
  }

  default BufferedImage getScreenShot() {
    return getScreenShot(0);
  }

  default BufferedImage getScreenShot(int waitSec) {
    return waitUntil(
        "Get ScreenShot",
        waitSec,
        null,
        el -> CImageUtil.readImageOrNull(el.getScreenshotAs(OutputType.BYTES)));
  }

  // Protected
  default <C> C waitUntil(
      String actionName, int waitSec, C defaultTo, Function<WebElement, C> action) {
    return getDriver()
        .waitUntil(
            actionName,
            waitSec,
            defaultTo,
            webDriver -> {
              WebElement element = webDriver.findElement(getLocator());
              if (element == null) {
                return null;
              }
              return action.apply(element);
            });
  }
}
