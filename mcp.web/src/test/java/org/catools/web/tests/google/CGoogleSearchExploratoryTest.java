package org.catools.web.tests.google;

import org.catools.common.extensions.verify.CVerify;
import org.catools.mcp.web.CMcpWebTest;
import org.testng.annotations.Test;

/**
 * Google Search Exploratory Test - CATools Web Style
 * <p>
 * This test class demonstrates comprehensive Google search functionality testing
 * including search queries, result validation, and search box interactions.
 * <p>
 * Generated from exploratory testing session on December 14, 2025
 */
public class CGoogleSearchExploratoryTest extends CMcpWebTest {

  private static final String GOOGLE_URL = "https://www.google.com";
  private static final String SEARCH_BOX_NAME = "q";

  /**
   * Test 1: Basic search functionality with Enter key
   * Tests searching for "Selenium automation testing" and validates results appear
   */
  @Test(description = "Verify basic search functionality using Enter key")
  public void testBasicSearchWithEnterKey() {
    // Navigate to Google
    open(GOOGLE_URL);

    // Find and verify search box is present
    var searchBox = getDriver().$(SEARCH_BOX_NAME, "[name='" + SEARCH_BOX_NAME + "']");
    CVerify.Object.isNotNull(searchBox, "Search box should be present");
    searchBox.Present.verifyIsTrue("Search box should be present on page");

    // Set search query text
    String searchQuery = "Selenium automation testing";
    searchBox.type(searchQuery);

    // Verify text was entered using extension method
    searchBox.Value.verifyContains("Selenium", "Search box should contain typed query");

    // Submit search with Enter key
    searchBox.pressKey("Enter");

    // Use Url extension method for verification - it will wait automatically
    getDriver().Url.verifyContains("search", "URL should change to search results");
  }

  /**
   * Test 2: Search box validation
   * Tests various search box interactions and validations
   */
  @Test(description = "Verify search box interactions and validations")
  public void testSearchBoxValidation() {
    // Navigate to Google
    open(GOOGLE_URL);

    // Find search box
    var searchBox = getDriver().$(SEARCH_BOX_NAME, "[name='" + SEARCH_BOX_NAME + "']");
    CVerify.Object.isNotNull(searchBox, "Search box should be present");

    // Verify search box is present using extension method
    searchBox.Present.verifyIsTrue("Search box should exist");

    // Set query text
    String testQuery = "CATools framework";
    searchBox.setText(testQuery);

    // Verify value using extension method
    searchBox.Value.verifyEquals(testQuery, "Search box should contain typed text");

    // Clear the search box
    searchBox.clear();

    // Verify search box is empty using extension method
    searchBox.Value.verifyIsBlank("Search box should be empty after clear");
  }

  /**
   * Test 3: Multiple consecutive searches
   * Tests performing multiple searches in sequence
   */
  @Test(description = "Verify multiple consecutive searches work correctly")
  public void testMultipleSearches() {
    // Navigate to Google
    open(GOOGLE_URL);

    // First search
    var searchBox = getDriver().$(SEARCH_BOX_NAME, "[name='" + SEARCH_BOX_NAME + "']");
    searchBox.setTextAndEnter("Selenium automation");

    // Use Url extension method for verification - it will wait automatically
    getDriver().Url.verifyContains("search", "First search should navigate to results");

    // Perform second search - find search box on results page
    var searchBoxOnResults = getDriver().$(SEARCH_BOX_NAME, "[name='" + SEARCH_BOX_NAME + "']", 5);
    CVerify.Object.isNotNull(searchBoxOnResults, "Search box should be present on results page");

    // Clear and enter new query
    searchBoxOnResults.clear();
    searchBoxOnResults.setTextAndEnter("Java test automation");

    // Use Url extension method for verification - it will wait automatically
    getDriver().Url.verifyContains("Java", "Second search URL should contain new query");
  }

  /**
   * Test 4: End-to-end search scenario
   * Complete search workflow from homepage to results validation
   */
  @Test(description = "Verify complete end-to-end search workflow")
  public void testEndToEndSearchScenario() {
    // Step 1: Navigate to Google
    open(GOOGLE_URL);

    // Step 2: Verify page loaded correctly using extension method
    getDriver().Url.verifyContains("google", "Should be on Google domain");

    // Step 3: Find search box
    var searchBox = getDriver().$(SEARCH_BOX_NAME, "[name='" + SEARCH_BOX_NAME + "']");
    CVerify.Object.isNotNull(searchBox, "Search box should be present");

    // Step 4: Enter search query
    String searchQuery = "Test automation best practices";
    searchBox.type(searchQuery);

    // Step 5: Verify text was entered correctly using extension method
    searchBox.Value.verifyContains("automation", "Query should be in search box");

    // Step 6: Submit search
    searchBox.pressKey("Enter");

    // Step 7: Verify results page loaded using extension method - it will wait automatically
    getDriver().Url.verifyContains("search", "Should navigate to search results");
  }

  /**
   * Test 5: Search query validation
   * Tests that different queries work correctly
   */
  @Test(description = "Verify various search queries work")
  public void testVariousSearchQueries() {
    String[] queries = {
        "Playwright testing",
        "CATools framework",
        "TestNG parallel execution"
    };

    for (String query : queries) {
      // Navigate to Google
      open(GOOGLE_URL);

      // Enter and submit query
      var searchBox = getDriver().$(SEARCH_BOX_NAME, "[name='" + SEARCH_BOX_NAME + "']");
      searchBox.setTextAndEnter(query);

      // Verify navigation to results using extension method - it will wait automatically
      getDriver().Url.verifyContains("search", "Should navigate to results for query: " + query);
    }
  }

}

