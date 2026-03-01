package org.catools.web.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.util.Strings;

/**
 * Represents information about a web page, including its title and URL. This class provides a
 * convenient way to capture and store basic web page metadata from a Selenium Page instance.
 *
 * <p>The class is immutable and uses Lombok annotations for getter generation with method chaining
 * support.
 *
 * <p><strong>Usage Examples:</strong>
 *
 * <pre>{@code
 * // Create page info from a Page instance
 * Page driver = new ChromeDriver();
 * driver.get("https://example.com");
 * CWebPageInfo pageInfo = new CWebPageInfo(driver);
 * System.out.println("Title: " + pageInfo.getTitle());
 * System.out.println("URL: " + pageInfo.getUrl());
 *
 * // Use the blank page constant for default/empty state
 * CWebPageInfo blankPage = CWebPageInfo.BLANK_PAGE;
 * System.out.println("Blank page title: " + blankPage.getTitle()); // prints empty string
 * }</pre>
 *
 * @author Generated documentation
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true)
public class CWebPageInfo {
  /**
   * A constant representing a blank/empty web page with empty title and URL. This can be used as a
   * default value or placeholder when no actual page information is available.
   *
   * <p><strong>Usage Example:</strong>
   *
   * <pre>{@code
   * // Use as a default value
   * CWebPageInfo currentPage = CWebPageInfo.BLANK_PAGE;
   *
   * // Check if a page info is blank
   * if (pageInfo.getTitle().isEmpty() && pageInfo.getUrl().isEmpty()) {
   *     // Handle blank page case
   *     System.out.println("Page is blank, same as: " + CWebPageInfo.BLANK_PAGE.getTitle());
   * }
   * }</pre>
   */
  public static final CWebPageInfo BLANK_PAGE = new CWebPageInfo();

  /**
   * The title of the web page as retrieved from the Page. This field is populated during
   * construction and cannot be modified after creation. For blank pages, this will be an empty
   * string.
   */
  private final String title;

  /**
   * The current URL of the web page as retrieved from the Page. This field is populated during
   * construction and cannot be modified after creation. For blank pages, this will be an empty
   * string.
   */
  private final String url;

  private CWebPageInfo() {
    this.title = Strings.EMPTY;
    this.url = Strings.EMPTY;
  }
}
