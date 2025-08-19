package org.catools.web.entities;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.util.Strings;
import org.openqa.selenium.WebDriver;

@Getter
@Accessors(chain = true)
public class CWebPageInfo {
  public static final CWebPageInfo BLANK_PAGE = new CWebPageInfo();
  private final String title;
  private final String url;

  private CWebPageInfo() {
    this.title = Strings.EMPTY;
    this.url = Strings.EMPTY;
  }

  public CWebPageInfo(WebDriver driver) {
    this.title = driver.getTitle();
    this.url = driver.getCurrentUrl();
  }
}
