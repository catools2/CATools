package org.catools.web.controls;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.utils.CFileUtil;
import org.catools.common.utils.CRetry;
import org.catools.common.utils.CSleeper;
import org.catools.web.config.CGridConfigs;
import org.catools.web.config.CWebConfigs;
import org.catools.web.drivers.CDriver;
import org.catools.web.utils.CGridUtil;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;

import java.awt.*;
import java.io.File;
import java.util.regex.Pattern;

public interface CWebElementActions<DR extends CDriver> extends CWebElementStates<DR> {

  // Actions
  default void moveTo() {
    moveTo(0, 0);
  }

  default void moveTo(int waitSec) {
    moveTo(0, 0, waitSec);
  }

  default void moveTo(int xOffset, int yOffset) {
    moveTo(xOffset, yOffset, getWaitSec());
  }

  default void moveTo(int xOffset, int yOffset, int waitSec) {
    isPresent(waitSec);
    getDriver().moveToElement(getLocator(), xOffset, yOffset, 0);
  }

  default void dropTo(int xOffset, int yOffset) {
    dropTo(xOffset, yOffset, getWaitSec());
  }

  default void dropTo(int xOffset, int yOffset, int waitSec) {
    getDriver().dropTo(getLocator(), xOffset, yOffset, 0);
  }

  default void dragAndDropTo(int xOffset2, int yOffset2) {
    dragAndDropTo(0, 0, xOffset2, yOffset2);
  }

  default void dragAndDropTo(int xOffset2, int yOffset2, int waitSec) {
    dragAndDropTo(0, 0, xOffset2, yOffset2, waitSec);
  }

  default void dragAndDropTo(int xOffset1, int yOffset1, int xOffset2, int yOffset2) {
    dragAndDropTo(xOffset1, yOffset1, xOffset2, yOffset2, getWaitSec());
  }

  default void dragAndDropTo(int xOffset1, int yOffset1, int xOffset2, int yOffset2, int waitSec) {
    isPresent(waitSec);
    getDriver().dragAndDropTo(getLocator(), xOffset1, yOffset1, xOffset2, yOffset2, 0);
  }

  default <R> R executeScript(String script) {
    return executeScript(script, getWaitSec());
  }

  default <R> R executeScript(String script, int waitSec) {
    return getDriver().executeScript(getLocator(), waitSec, script);
  }

  default Point getLocation() {
    org.openqa.selenium.Point p =
        waitUntil("Get Position", getWaitSec(), null, el -> el.getLocation());
    return new Point(p.x, p.y);
  }

  default void sendKeys(CharSequence... keys) {
    getDriver().sendKeys(getLocator(), getWaitSec(), keys);
  }

  default void mouseClick() {
    mouseClick(getWaitSec());
  }

  default void mouseClick(int waitSec) {
    moveTo(waitSec);
    getDriver().mouseClick(getLocator(), waitSec);
  }

  default void mouseDoubleClick() {
    mouseDoubleClick(getWaitSec());
  }

  default void mouseDoubleClick(int waitSec) {
    moveTo(waitSec);
    getDriver().mouseDoubleClick(getLocator(), waitSec);
  }

  default void scrollIntoView(boolean scrollDown) {
    scrollIntoView(scrollDown, getWaitSec());
  }

  default void scrollIntoView(boolean scrollDown, int waitSec) {
    getDriver().scrollIntoView(getLocator(), scrollDown, waitSec);
  }

  default void setStyle(String style, String color) {
    setAttribute("style", style, color);
  }

  default void setAttribute(String attributeName, String style, String color) {
    executeScript(
        String.format(
            "arguments[0][\"%s\"][\"%s\"]=\"%s\";",
            Quotes.escape(attributeName), Quotes.escape(style), Quotes.escape(color)));
  }

  default void select() {
    select(isUseJS());
  }

  default void select(boolean useJS) {
    select(useJS, getWaitSec());
  }

  default void select(int waitSec) {
    select(isUseJS(), waitSec);
  }

  default void select(boolean useJS, int waitSec) {
    waitUntil(
        "Select",
        waitSec,
        false,
        webElement -> {
          if (!webElement.isSelected()) {
            _click(useJS, webElement);
          }
          return true;
        });
  }

  default void selectInvisible() {
    selectInvisible(getWaitSec());
  }

  default void selectInvisible(int waitSec) {
    select(true, waitSec);
  }

  default void deselect() {
    deselect(isUseJS());
  }

  default void deselect(boolean useJS) {
    deselect(useJS, getWaitSec());
  }

  default void deselect(int waitSec) {
    deselect(isUseJS(), waitSec);
  }

  default void deselect(boolean useJS, int waitSec) {
    waitUntil(
        "Deselect",
        waitSec,
        false,
        webElement -> {
          if (webElement.isSelected()) {
            _click(useJS, webElement);
          }
          return true;
        });
  }

  default void deselectInvisible() {
    deselectInvisible(getWaitSec());
  }

  default void deselectInvisible(int waitSec) {
    deselect(true, waitSec);
  }

  default void click() {
    click(isUseJS(), getWaitSec());
  }

  default void click(boolean useJS) {
    click(useJS, getWaitSec());
  }

  default void click(int waitSec) {
    getDriver().click(getLocator(), waitSec);
  }

  default void click(boolean useJS, int waitSec) {
    waitUntil(
        "Click",
        waitSec,
        false,
        webElement -> {
          _click(useJS, webElement);
          return true;
        });
  }

  default <R> R click(com.google.common.base.Function<DR, R> postCondition) {
    return click(isUseJS(), postCondition);
  }

  default <R> R click(boolean useJS, com.google.common.base.Function<DR, R> postCondition) {
    return click(useJS, 2, 2000, postCondition);
  }

  default <R> R click(
      int retryCount, int interval, com.google.common.base.Function<DR, R> postCondition) {
    return click(isUseJS(), retryCount, interval, postCondition);
  }

  default <R> R click(
      boolean useJS,
      int retryCount,
      int interval,
      com.google.common.base.Function<DR, R> postCondition) {
    return CRetry.retry(
        idx -> {
          click(useJS);
          return postCondition.apply(getDriver());
        },
        retryCount,
        interval);
  }

  default void clickInvisible() {
    clickInvisible(getWaitSec());
  }

  default void clickInvisible(int waitSec) {
    getDriver().clickInvisible(getLocator(), waitSec);
  }

  default <R> R clickInvisible(com.google.common.base.Function<DR, R> postCondition) {
    return clickInvisible(2, 2000, postCondition);
  }

  default <R> R clickInvisible(
      int retryCount, int interval, com.google.common.base.Function<DR, R> postCondition) {
    return CRetry.retry(
        idx -> {
          clickInvisible();
          return postCondition.apply(getDriver());
        },
        retryCount,
        interval);
  }

  default void openHref() {
    openHref(getWaitSec());
  }

  default void openHref(int waitSec) {
    getDriver().open(getAttribute("href", waitSec));
  }

  default <R> R openHref(com.google.common.base.Function<DR, R> postCondition) {
    return openHref(2, 2000, postCondition);
  }

  default <R> R openHref(
      int retryCount, int interval, com.google.common.base.Function<DR, R> postCondition) {
    return CRetry.retry(
        idx -> {
          openHref();
          return postCondition.apply(getDriver());
        },
        retryCount,
        interval);
  }

  // Download File
  default CFile downloadFile(String filename, String renameTo) {
    return downloadFile(isUseJS(), getWaitSec(), filename, renameTo, false);
  }

  default CFile downloadFile(boolean useJS, String filename, String renameTo) {
    return downloadFile(useJS, getWaitSec(), filename, renameTo, false);
  }

  default CFile downloadFile(int downloadWait, String filename, String renameTo) {
    return downloadFile(isUseJS(), downloadWait, filename, renameTo, false);
  }

  default CFile downloadFile(boolean useJS, int downloadWait, String filename, String renameTo) {
    return downloadFile(useJS, downloadWait, filename, renameTo, false);
  }

  default CFile downloadFile(int clickWait, int downloadWait, String filename, String renameTo) {
    return downloadFile(isUseJS(), clickWait, downloadWait, filename, renameTo, false);
  }

  default CFile downloadFile(
      boolean useJS, int clickWait, int downloadWait, String filename, String renameTo) {
    return downloadFile(useJS, clickWait, downloadWait, filename, renameTo, false);
  }

  default CFile downloadFile(
      int downloadWait, String filename, String renameTo, boolean expectedAlert) {
    return downloadFile(isUseJS(), getWaitSec(), downloadWait, filename, renameTo, expectedAlert);
  }

  default CFile downloadFile(
      boolean useJS, int downloadWait, String filename, String renameTo, boolean expectedAlert) {
    return downloadFile(useJS, getWaitSec(), downloadWait, filename, renameTo, expectedAlert);
  }

  default CFile downloadFile(
      int clickWait, int downloadWait, String filename, String renameTo, boolean expectedAlert) {
    return downloadFile(isUseJS(), clickWait, downloadWait, filename, renameTo, expectedAlert);
  }

  default CFile downloadFile(
      boolean useJS,
      int clickWait,
      int downloadWait,
      String filename,
      String renameTo,
      boolean expectedAlert) {
    click(useJS, clickWait);
    if (expectedAlert) {
      getDriver().getAlert().accept();
    }
    CFile downloadFolder =
        getDriver()
            .performActionOnDriver(
                "Get Download Folder", webDriver -> CWebConfigs.getDownloadFolder(webDriver));
    CFile downloadedFile = new CFile(downloadFolder, filename);
    downloadedFile.verifyExists(
        getVerify(), downloadWait, 500, "File downloaded! file:" + downloadedFile);
    return downloadedFile.moveTo(CPathConfigs.getTempChildFile(renameTo));
  }

  default CFile downloadFile(Pattern filename, String renameTo) {
    return downloadFile(isUseJS(), getWaitSec(), filename, renameTo, false);
  }

  default CFile downloadFile(boolean useJS, Pattern filename, String renameTo) {
    return downloadFile(useJS, getWaitSec(), filename, renameTo, false);
  }

  default CFile downloadFile(int downloadWait, Pattern filename, String renameTo) {
    return downloadFile(isUseJS(), downloadWait, filename, renameTo, false);
  }

  default CFile downloadFile(boolean useJS, int downloadWait, Pattern filename, String renameTo) {
    return downloadFile(useJS, downloadWait, filename, renameTo, false);
  }

  default CFile downloadFile(int clickWait, int downloadWait, Pattern filename, String renameTo) {
    return downloadFile(isUseJS(), clickWait, downloadWait, filename, renameTo, false);
  }

  default CFile downloadFile(
      boolean useJS, int clickWait, int downloadWait, Pattern filename, String renameTo) {
    return downloadFile(useJS, clickWait, downloadWait, filename, renameTo, false);
  }

  default CFile downloadFile(
      int downloadWait, Pattern filename, String renameTo, boolean expectedAlert) {
    return downloadFile(isUseJS(), getWaitSec(), downloadWait, filename, renameTo, expectedAlert);
  }

  default CFile downloadFile(
      boolean useJS, int downloadWait, Pattern filename, String renameTo, boolean expectedAlert) {
    return downloadFile(useJS, getWaitSec(), downloadWait, filename, renameTo, expectedAlert);
  }

  default CFile downloadFile(
      int clickWait, int downloadWait, Pattern filename, String renameTo, boolean expectedAlert) {
    return downloadFile(isUseJS(), clickWait, downloadWait, filename, renameTo, expectedAlert);
  }

  default CFile downloadFile(
      boolean useJS,
      int clickWait,
      int downloadWait,
      Pattern filename,
      String renameTo,
      boolean expectedAlert) {
    click(useJS, clickWait);
    if (expectedAlert) {
      getDriver().getAlert().accept();
    }
    CFile downloadFolder =
        getDriver()
            .performActionOnDriver(
                "Get Download Folder", webDriver -> CWebConfigs.getDownloadFolder(webDriver));
    File downloadedFile =
        CRetry.retryIfFalse(
            idx ->
                new CList<>(downloadFolder.listFiles())
                    .getFirstOrNull(file -> filename.matcher(file.getName()).matches()),
            downloadWait * 2,
            500);
    getVerify().Bool.isTrue(downloadedFile.exists(), "File downloaded properly!");
    return new CFile(downloadedFile).moveTo(CPathConfigs.getTempChildFolder(renameTo));
  }

  // Upload

  default void uploadFile(File file) {
    uploadFile(CFileUtil.getCanonicalPath(file));
  }

  default void uploadFile(String filePath) {
    getLogger().info("Uploading file " + filePath);
    if (!CGridConfigs.isUseLocalFileDetectorInOn()) {
      String fullFileName =
          getDriver()
              .performActionOnDriver(
                  "Copy File To Node",
                  webDriver -> CGridUtil.copyFileToNode(webDriver, new File(filePath)));
      sendKeys(fullFileName);
    } else {
      sendKeys(filePath);
    }
    getDriver().pressTab();
  }

  default void uploadResource(String resourceName, Class clazz) {
    uploadResource(new CResource(resourceName, clazz));
  }

  default void uploadResource(CResource resource) {
    CFile filePath = resource.saveToFolder(CPathConfigs.getTempFolder()).getFirst();
    uploadFile(filePath.getCanonicalPath());
  }

  // Input

  default void setText(String text) {
    setText(text, getWaitSec());
  }

  default void setText(String text, int waitSec) {
    waitUntil(
        "Set Text",
        waitSec,
        null,
        el -> {
          if (isUseJS()) {
            getDriver()
                .executeScript(
                    "arguments[0].value=" + Quotes.escape(StringUtils.defaultString(text)) + ";",
                    el);
          } else {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), text);
          }
          return true;
        });
  }

  default void setTextAndEnter(String text) {
    setTextAndEnter(text, getWaitSec());
  }

  default void setTextAndEnter(String text, int waitSec) {
    waitUntil(
        "Set Text And Enter",
        waitSec,
        null,
        el -> {
          el.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), text, Keys.ENTER);
          return true;
        });
  }

  default void setTextAndTab(String text) {
    setTextAndTab(text, getWaitSec());
  }

  default void setTextAndTab(String text, int waitSec) {
    waitUntil(
        "Set Text And Tab",
        waitSec,
        null,
        el -> {
          el.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), text, Keys.TAB);
          return true;
        });
  }

  default void clear() {
    clear(getWaitSec());
  }

  default void clear(int waitSec) {
    waitUntil(
        "Clear",
        waitSec,
        null,
        el -> {
          el.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
          return true;
        });
  }

  default void type(String text) {
    type(text, getWaitSec());
  }

  default void type(String text, int waitSec) {
    type(text, waitSec, 0L);
  }

  default void type(String text, long intervalInMilliSeconds) {
    type(text, getWaitSec(), intervalInMilliSeconds);
  }

  default void type(String text, int waitSec, long intervalInMilliSeconds) {
    waitUntil(
        "Type",
        waitSec,
        null,
        el -> {
          el.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
          if (intervalInMilliSeconds < 10) {
            el.sendKeys(StringUtils.defaultString(text).split(""));
          } else {
            for (String c : StringUtils.defaultString(text).split("")) {
              el.sendKeys(c);
              CSleeper.sleepTight(intervalInMilliSeconds);
            }
          }
          return true;
        });
  }

  default void typeAndTab(String text) {
    typeAndTab(text, getWaitSec());
  }

  default void typeAndTab(String text, int waitSec) {
    typeAndTab(text, waitSec, 0L);
  }

  default void typeAndTab(String text, long intervalInMilliSeconds) {
    typeAndTab(text, getWaitSec(), intervalInMilliSeconds);
  }

  default void typeAndTab(String text, int waitSec, long intervalInMilliSeconds) {
    typeAnd(text, Keys.TAB, waitSec, intervalInMilliSeconds);
  }

  default void typeAndEnter(String text) {
    typeAndEnter(text, getWaitSec());
  }

  default void typeAndEnter(String text, int waitSec) {
    typeAndEnter(text, waitSec, 0L);
  }

  default void typeAndEnter(String text, long intervalInMilliSeconds) {
    typeAndEnter(text, getWaitSec(), intervalInMilliSeconds);
  }

  default void typeAndEnter(String text, int waitSec, long intervalInMilliSeconds) {
    typeAnd(text, Keys.ENTER, waitSec, intervalInMilliSeconds);
  }

  default void typeAnd(String text, Keys keys) {
    typeAnd(text, keys, getWaitSec());
  }

  default void typeAnd(String text, Keys keys, int waitSec) {
    typeAnd(text, keys, waitSec, 0L);
  }

  default void typeAnd(String text, Keys keys, long intervalInMilliSeconds) {
    typeAnd(text, keys, getWaitSec(), intervalInMilliSeconds);
  }

  default void typeAnd(String text, Keys keys, int waitSec, long intervalInMilliSeconds) {
    type(text, waitSec, intervalInMilliSeconds);
    getDriver().sendKeys(keys);
  }

  private void _click(boolean useJS, WebElement webElement) {
    if (useJS) {
      _clickWithJS(webElement);
    } else {
      try {
        webElement.click();
      } catch (WebDriverException t) {
        _clickWithJS(webElement);
      }
    }
  }

  private void _clickWithJS(WebElement webElement) {
    getDriver().executeScript("arguments[0].click();", webElement);
  }
}
