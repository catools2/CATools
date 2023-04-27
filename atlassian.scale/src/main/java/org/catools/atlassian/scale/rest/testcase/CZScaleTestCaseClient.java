package org.catools.atlassian.scale.rest.testcase;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.scale.configs.CZScaleConfigs;
import org.catools.atlassian.scale.model.CZScaleTestCase;
import org.catools.atlassian.scale.model.CZScaleTestCases;
import org.catools.atlassian.scale.rest.CZScaleRestClient;
import org.catools.common.utils.CStringUtil;

import java.util.Set;
import java.util.function.Consumer;

@Slf4j
public class CZScaleTestCaseClient extends CZScaleRestClient {

  public CZScaleTestCaseClient() {
    super();
  }

  public CZScaleTestCases getProjectTestCases(String project, String folder, String fields) {
    return getProjectTestCases(project, folder, fields, 1, 1, null);
  }

  public CZScaleTestCases getProjectTestCases(String project,
                                              String folder,
                                              String fields,
                                              int parallelInputCount,
                                              int parallelOutputCount,
                                              Consumer<CZScaleTestCase> onAction) {
    String query = String.format("projectKey = \"%s\"", project);
    if (CStringUtil.isNotBlank(folder))
      query += String.format(" AND folder = \"%s\"", folder);

    return getAllTestCases(
        query,
        fields,
        parallelInputCount,
        parallelOutputCount,
        onAction);
  }


  public CZScaleTestCases getAllTestCases(String project, String status, String fields) {
    return getAllTestCases(project, status, fields, 1, 1, null);
  }

  public CZScaleTestCases getAllTestCases(String project,
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

  public CZScaleTestCases getAllTestCases(String project, String status, String folder, String fields) {
    return getAllTestCases(project, status, fields, folder, 1, 1, null);
  }

  public CZScaleTestCases getAllTestCases(String project,
                                          String status,
                                          String folder,
                                          String fields,
                                          int parallelInputCount,
                                          int parallelOutputCount,
                                          Consumer<CZScaleTestCase> onAction) {
    return getAllTestCases(
        String.format("projectKey = \"%s\" AND status = \"%s\" AND folder = \"%s\"", project, status, folder),
        fields,
        parallelInputCount,
        parallelOutputCount,
        onAction
    );
  }

  public CZScaleTestCases getAllTestCases(String query,
                                          String fields,
                                          int parallelInputCount,
                                          int parallelOutputCount,
                                          Consumer<CZScaleTestCase> onAction) {
    Set<CZScaleTestCase> result = readAllInParallel(
        "Get Test Results",
        parallelInputCount,
        parallelOutputCount,
        (start, max) -> _getAllTestCases(query, fields, start, max),
        onAction);
    return new CZScaleTestCases(result);
  }

  private CZScaleTestCases _getAllTestCases(String query, String fields, int startAt, int maxResults) {
    String homeUri = CZScaleConfigs.Scale.getHomeUri();
    log.trace("Send Request to {}, query: {}, fields: {}, startAT: {}, maxResult: {}", homeUri, query, fields, startAt, maxResults);
    RequestSpecification specification =
        RestAssured.given()
            .baseUri(homeUri)
            .basePath("/testcase/search")
            .queryParam("startAt", startAt)
            .queryParam("maxResults", maxResults)
            .queryParam("query", query);

    if (StringUtils.isNotEmpty(fields)) {
      specification.queryParam("fields", fields);
    }

    Response response = get(specification);

    if (response.statusCode() != 200)
      log.warn("Response::\n{}", response.body().asString());

    response.then().statusCode(200);
    return response.body().as(CZScaleTestCases.class);
  }

  public CZScaleTestCase getTestCase(String testCaseKey) {
    String homeUri = CZScaleConfigs.Scale.getHomeUri();
    log.trace("Get TestCase from {}, projectKey: {}", homeUri, testCaseKey);
    RequestSpecification specification =
        RestAssured.given()
            .baseUri(homeUri)
            .basePath("/testcase/" + testCaseKey);

    Response response = get(specification);

    if (response.statusCode() != 200)
      log.warn("Response::\n{}", response.body().asString());

    response.then().statusCode(200);
    return response.body().as(CZScaleTestCase.class);
  }
}
