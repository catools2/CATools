package org.catools.atlassian.scale.rest.cycle;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.scale.configs.CZScaleConfigs;
import org.catools.atlassian.scale.model.*;
import org.catools.atlassian.scale.rest.CZScaleRestClient;
import org.catools.common.collections.CList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Client for managing test runs in the Scale system.
 *
 * <p>This class provides methods to interact with the Scale system's test run API, including
 * retrieving, creating, updating, and deleting test runs and their associated results. It uses
 * RestAssured for HTTP requests and supports parallel processing for fetching large datasets.
 */
@Slf4j
public class CZScaleTestRunClient extends CZScaleRestClient {

  /**
   * Default constructor.
   *
   * <p>Initializes the client with default configurations.
   */
  public CZScaleTestRunClient() {
    super();
  }

  /**
   * Retrieves all test runs for a given project and folder.
   *
   * @param projectKey the key of the project
   * @param folder the folder containing the test runs
   * @param fields the fields to include in the response
   * @return a collection of test runs
   */
  public CZScaleTestRuns getAllTestRuns(String projectKey, String folder, String fields) {
    return getAllTestRuns(projectKey, folder, fields, 1, 1, null);
  }

  /**
   * Retrieves all test runs for a given project and folder with parallel processing.
   *
   * @param projectKey the key of the project
   * @param folder the folder containing the test runs
   * @param fields the fields to include in the response
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param onAction a consumer to process each test run
   * @return a collection of test runs
   */
  public CZScaleTestRuns getAllTestRuns(
      String projectKey,
      String folder,
      String fields,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CZScaleTestRun> onAction) {
    Set<CZScaleTestRun> results =
        readAllInParallel(
            "Get Test Runs",
            parallelInputCount,
            parallelOutputCount,
            (start, max) -> _getAllTestRuns(projectKey, folder, fields, start, max),
            onAction);
    return new CZScaleTestRuns(results);
  }

  /**
   * Helper method to retrieve a subset of test runs.
   *
   * @param projectKey the key of the project
   * @param folder the folder containing the test runs
   * @param fields the fields to include in the response
   * @param startAt the starting index for pagination
   * @param maxResults the maximum number of results to retrieve
   * @return a collection of test runs
   */
  private CZScaleTestRuns _getAllTestRuns(
      String projectKey, String folder, String fields, int startAt, int maxResults) {
    log.debug(
        "All Test Runs, projectKey: {}, fields: {}, startAT: {}, maxResult: {}",
        projectKey,
        fields,
        startAt,
        maxResults);
    RequestSpecification specification =
        getRequestSpecification("/testrun/search")
            .queryParam("startAt", startAt)
            .queryParam("maxResults", maxResults)
            .queryParam(
                "query",
                String.format("projectKey = \"%s\" AND folder = \"%s\"", projectKey, folder));

    if (StringUtils.isNotEmpty(fields)) {
      specification.queryParam("fields", fields);
    }

    Response response = get(specification);

    if (response.statusCode() != 200) {
      log.warn("Response::\n{}", response.body().asString());
    }

    response.then().statusCode(200);
    return response.body().as(CZScaleTestRuns.class);
  }

  /**
   * Retrieves a specific test run by its key.
   *
   * @param testRunKey the key of the test run
   * @return the test run details
   */
  public CZScaleTestRun getTestRun(String testRunKey) {
    RequestSpecification specification = getRequestSpecification("/testrun/" + testRunKey);

    Response response = get(specification);

    if (response.statusCode() != 200) log.trace("Response::\n{}", response.body().asString());

    response.then().statusCode(200);
    return response.body().as(CZScaleTestRun.class);
  }

  /**
   * Retrieves all test results for a specific test run.
   *
   * @param testRunKey the key of the test run
   * @return a collection of test results
   */
  public CZScaleTestResults getTestResults(String testRunKey) {
    return getTestResults(testRunKey, StringUtils.EMPTY, 1, 1, null);
  }

  /**
   * Retrieves all test results for a specific test run with parallel processing.
   *
   * @param testRunKey the key of the test run
   * @param fields the fields to include in the response
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param onAction a consumer to process each test result
   * @return a collection of test results
   */
  public CZScaleTestResults getTestResults(
      String testRunKey,
      String fields,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CZScaleTestResult> onAction) {
    Set<CZScaleTestResult> get_test_results =
        readAllInParallel(
            "Get Test Results",
            parallelInputCount,
            parallelOutputCount,
            (start, max) -> _getTestResults(testRunKey, fields, start, max),
            onAction);
    return new CZScaleTestResults(get_test_results);
  }

  /**
   * Helper method to retrieve a subset of test results.
   *
   * @param testRunKey the key of the test run
   * @param fields the fields to include in the response
   * @param startAt the starting index for pagination
   * @param maxResults the maximum number of results to retrieve
   * @return a collection of test results
   */
  private CZScaleTestResults _getTestResults(
      String testRunKey, String fields, int startAt, int maxResults) {
    RequestSpecification specification =
        getRequestSpecification("/testrun/" + testRunKey + "/testresults")
            .queryParam("startAt", startAt)
            .queryParam("maxResults", maxResults);

    if (StringUtils.isNotEmpty(fields)) {
      specification.queryParam("fields", fields);
    }

    Response response = get(specification);

    if (response.statusCode() != 200) log.trace("Response::\n{}", response.body().asString());

    response.then().statusCode(200);
    return response.body().as(CZScaleTestResults.class);
  }

  /**
   * Creates a single test result for a specific test run.
   *
   * @param testRunKey the key of the test run
   * @param testResult the test result to create
   * @return the ID of the created test result
   */
  public Long createTestResult(String testRunKey, CZScaleTestResult testResult) {
    return createTestResults(testRunKey, new CZScaleTestResults(testResult)).get(0);
  }

  /**
   * Creates multiple test results for a specific test run.
   *
   * @param testRunKey the key of the test run
   * @param testResults the test results to create
   * @return a list of IDs of the created test results
   */
  public CList<Long> createTestResults(String testRunKey, CZScaleTestResults testResults) {
    RequestSpecification specification =
        getRequestSpecification("/testrun/" + testRunKey + "/testresults").body(testResults);
    Response response = post(specification);

    if (response.statusCode() != 201) log.trace("Response::\n{}", response.body().asString());

    response.then().statusCode(201);
    return CList.of(new JSONArray(response.body().asString()))
        .mapToList(o -> ((JSONObject) o).getLong("id"));
  }

  /**
   * Updates a specific test result for a test case in a test run.
   *
   * @param testRunKey the key of the test run
   * @param testCaseKey the key of the test case
   * @param testResult the updated test result details
   * @return the ID of the updated test result
   */
  public long updateTestResult(
      String testRunKey, String testCaseKey, CZScaleUpdateTestResultRequest testResult) {
    RequestSpecification specification =
        getRequestSpecification(
                "/testrun/" + testRunKey + "/testcase/" + testCaseKey + "/testresult")
            .body(testResult);

    Response response = put(specification);

    if (response.statusCode() != 200) log.trace("Response::\n{}", response.body().asString());

    response.then().statusCode(200);
    return new JSONObject(response.body().asString()).getLong("id");
  }

  /**
   * Deletes a specific test run.
   *
   * @param testRunKey the key of the test run to delete
   */
  public void deleteTestRun(String testRunKey) {
    RequestSpecification specification = getRequestSpecification("/testrun/" + testRunKey);

    Response response = delete(specification);

    if (response.statusCode() != 204) log.trace("Response::\n{}", response.body().asString());

    response.then().statusCode(204);
  }

  /**
   * Creates a new test run.
   *
   * @param planTestRun the details of the test run to create
   * @return the key of the created test run
   */
  public String createTestRun(CZScalePlanTestRun planTestRun) {
    RequestSpecification specification = getRequestSpecification("/testrun").body(planTestRun);

    Response response = post(specification);

    if (response.statusCode() != 201) log.trace("Response::\n{}", response.body().asString());

    response.then().statusCode(201);
    String testRunKey = response.body().jsonPath().get("key");

    CList<String> createdTestCases =
        getTestRun(testRunKey).getItems().mapToList(CZScaleTestExecution::getTestCaseKey);
    for (CZScalePlanExecution item : planTestRun.getItems()) {
      if (createdTestCases.contains(item.getTestCaseKey())) continue;
      CZScaleTestResult testResultToAdd = convertPlanExecutionToTestResult(item);
      createTestResult(testRunKey, testResultToAdd);
    }

    return testRunKey;
  }

  /**
   * Creates a request specification for a given API path.
   *
   * @param path the API path
   * @return the request specification
   */
  private static RequestSpecification getRequestSpecification(String path) {
    return RestAssured.given().baseUri(CZScaleConfigs.Scale.getAtmUri()).basePath(path);
  }

  /**
   * Converts a plan execution to a test result.
   *
   * @param item the plan execution to convert
   * @return the converted test result
   */
  private static CZScaleTestResult convertPlanExecutionToTestResult(CZScalePlanExecution item) {
    CZScaleTestResult testResultToAdd =
        new CZScaleTestResult()
            .setTestCaseKey(item.getTestCaseKey())
            .setStatus(item.getStatus())
            .setEnvironment(item.getEnvironment())
            .setComment(item.getComment())
            .setUserKey(item.getUserKey())
            .setExecutionDate(item.getExecutionDate())
            .setCustomFields(item.getCustomFields())
            .setScriptResults(item.getScriptResults());
    return testResultToAdd;
  }
}
