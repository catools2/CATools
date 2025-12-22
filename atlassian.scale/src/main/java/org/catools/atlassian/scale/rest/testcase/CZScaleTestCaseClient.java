package org.catools.atlassian.scale.rest.testcase;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.scale.configs.CZScaleConfigs;
import org.catools.atlassian.scale.model.CZScaleChangeHistories;
import org.catools.atlassian.scale.model.CZScaleTestCase;
import org.catools.atlassian.scale.model.CZScaleTestCases;
import org.catools.atlassian.scale.rest.CZScaleRestClient;
import org.catools.common.utils.CSleeper;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Client for managing test cases in the Scale system.
 *
 * <p>This class provides methods to interact with the Scale system's test case API, including
 * retrieving, creating, and updating test cases. It supports parallel processing for fetching large
 * datasets and handles API requests using RestAssured.
 */
@Slf4j
public class CZScaleTestCaseClient extends CZScaleRestClient {

  /**
   * Default constructor.
   *
   * <p>Initializes the client with default configurations.
   */
  public CZScaleTestCaseClient() {
    super();
  }

  /**
   * Retrieves all test cases for a given project and folder.
   *
   * @param project the key of the project
   * @param folder the folder containing the test cases
   * @param fields the fields to include in the response
   * @return a collection of test cases
   */
  public CZScaleTestCases getProjectTestCases(String project, String folder, String fields) {
    return getProjectTestCases(project, folder, fields, 1, 1, null);
  }

  /**
   * Retrieves all test cases for a given project and folder with parallel processing.
   *
   * @param project the key of the project
   * @param folder the folder containing the test cases
   * @param fields the fields to include in the response
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param onAction a consumer to process each test case
   * @return a collection of test cases
   */
  public CZScaleTestCases getProjectTestCases(
      String project,
      String folder,
      String fields,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CZScaleTestCase> onAction) {
    String query = String.format("projectKey = \"%s\"", project);
    if (StringUtils.isNotBlank(folder)) query += String.format(" AND folder = \"%s\"", folder);

    return getAllTestCases(query, fields, parallelInputCount, parallelOutputCount, onAction);
  }

  /**
   * Retrieves all test cases for a given project and status.
   *
   * @param project the key of the project
   * @param status the status of the test cases
   * @param fields the fields to include in the response
   * @return a collection of test cases
   */
  public CZScaleTestCases getAllTestCases(String project, String status, String fields) {
    return getAllTestCases(project, status, fields, 1, 1, null);
  }

  /**
   * Retrieves all test cases for a given project and status with parallel processing.
   *
   * @param project the key of the project
   * @param status the status of the test cases
   * @param fields the fields to include in the response
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param onAction a consumer to process each test case
   * @return a collection of test cases
   */
  public CZScaleTestCases getAllTestCases(
      String project,
      String status,
      String fields,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CZScaleTestCase> onAction) {
    return getAllTestCases(
        String.format("projectKey = \"%s\" AND status = \"%s\"", project, status),
        fields,
        parallelInputCount,
        parallelOutputCount,
        onAction);
  }

  /**
   * Retrieves all test cases for a given project, status, and folder.
   *
   * @param project the key of the project
   * @param status the status of the test cases
   * @param folder the folder containing the test cases
   * @param fields the fields to include in the response
   * @return a collection of test cases
   */
  public CZScaleTestCases getAllTestCases(
      String project, String status, String folder, String fields) {
    return getAllTestCases(project, status, fields, folder, 1, 1, null);
  }

  /**
   * Retrieves all test cases for a given project, status, and folder with parallel processing.
   *
   * @param project the key of the project
   * @param status the status of the test cases
   * @param folder the folder containing the test cases
   * @param fields the fields to include in the response
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param onAction a consumer to process each test case
   * @return a collection of test cases
   */
  public CZScaleTestCases getAllTestCases(
      String project,
      String status,
      String folder,
      String fields,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CZScaleTestCase> onAction) {
    return getAllTestCases(
        String.format(
            "projectKey = \"%s\" AND status = \"%s\" AND folder = \"%s\"", project, status, folder),
        fields,
        parallelInputCount,
        parallelOutputCount,
        onAction);
  }

  /**
   * Retrieves all test cases based on a query.
   *
   * @param query the query string to filter test cases
   * @param fields the fields to include in the response
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param onAction a consumer to process each test case
   * @return a collection of test cases
   */
  public CZScaleTestCases getAllTestCases(
      String query,
      String fields,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CZScaleTestCase> onAction) {
    Set<CZScaleTestCase> result =
        readAllInParallel(
            "Get Test Results",
            parallelInputCount,
            parallelOutputCount,
            (start, max) -> _getAllTestCases(query, fields, start, max),
            onAction);
    return new CZScaleTestCases(result);
  }

  /**
   * Retrieves a specific test case by its key.
   *
   * @param testCaseKey the key of the test case
   * @return the test case details
   */
  public CZScaleTestCase getTestCase(String testCaseKey) {
    String homeUri = CZScaleConfigs.Scale.getAtmUri();
    log.trace("Get TestCase from {}, projectKey: {}", homeUri, testCaseKey);
    RequestSpecification specification =
        getRequestSpecification(homeUri, "/testcase/" + testCaseKey);

    Response response = getWithoutVerification(specification);

    // Archived items are not accessible in scale, and we get 404
    if (response.statusCode() == 404) {
      log.warn("Failed to read {} item, potentially archived.", testCaseKey);
      return null;
    }

    if (response.statusCode() != 200) {
      log.warn("Response::\n{}", response.body().asString());
    }

    response.then().statusCode(200);
    CZScaleTestCase testcase = response.body().as(CZScaleTestCase.class);
    testcase.setHistories(_getTestCaseHistory(testCaseKey));
    return testcase;
  }

  /**
   * Helper method to retrieve a subset of test cases based on a query.
   *
   * @param query the query string to filter test cases
   * @param fields the fields to include in the response
   * @param startAt the starting index for pagination
   * @param maxResults the maximum number of results to retrieve
   * @return a collection of test cases
   */
  private CZScaleTestCases _getAllTestCases(
      String query, String fields, int startAt, int maxResults) {
    String homeUri = CZScaleConfigs.Scale.getAtmUri();
    log.trace(
        "Send Request to {}, query: {}, fields: {}, startAT: {}, maxResult: {}",
        homeUri,
        query,
        fields,
        startAt,
        maxResults);
    RequestSpecification specification =
        getRequestSpecification(homeUri, "/testcase/search")
            .queryParam("startAt", startAt)
            .queryParam("maxResults", maxResults)
            .queryParam("query", query);

    if (StringUtils.isNotEmpty(fields)) {
      specification.queryParam("fields", fields);
    }

    Response response = get(specification);

    if (response.statusCode() != 200) log.warn("Response::\n{}", response.body().asString());

    response.then().statusCode(200);
    return response.body().as(CZScaleTestCases.class);
  }

  /**
   * Retrieves the change history of a specific test case.
   *
   * @param testCaseKey the key of the test case
   * @return the change history of the test case
   */
  private CZScaleChangeHistories _getTestCaseHistory(String testCaseKey) {
    String homeUri = CZScaleConfigs.Scale.getTestsUri();
    log.trace("Send history for {} from {}", testCaseKey, homeUri);

    Integer id = _getTestId(testCaseKey);

    RequestSpecification specification =
        getRequestSpecification(homeUri, "/testcase/" + id + "/history");

    Response response = get(specification);

    if (response.statusCode() != 200) log.warn("Response::\n{}", response.body().asString());

    response.then().statusCode(200);
    return response.body().as(CZScaleChangeHistories.class);
  }

  /**
   * Retrieves the ID of a specific test case.
   *
   * @param testCaseKey the key of the test case
   * @return the ID of the test case
   */
  private Integer _getTestId(String testCaseKey) {
    String homeUri = CZScaleConfigs.Scale.getTestsUri();
    log.trace("Get {} id from {}", testCaseKey, homeUri);
    RequestSpecification specification =
        getRequestSpecification(homeUri, "/testcase/" + testCaseKey + "/allVersions")
            .queryParam("fields", "id");

    Response response = get(specification);

    if (response.statusCode() != 200) log.warn("Response::\n{}", response.body().asString());

    response.then().statusCode(200);
    return response.jsonPath().get("id[0]");
  }

  /**
   * Creates a request specification for a given API path.
   *
   * @param homeUri the base URI of the API
   * @param s the API path
   * @return the request specification
   */
  private static RequestSpecification getRequestSpecification(String homeUri, String s) {
    CSleeper.sleepTight(CZScaleConfigs.Scale.getDelayBetweenCallsInMilliseconds());
    return RestAssured.given().baseUri(homeUri).basePath(s);
  }
}
