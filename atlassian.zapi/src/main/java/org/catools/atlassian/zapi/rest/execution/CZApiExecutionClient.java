package org.catools.atlassian.zapi.rest.execution;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.catools.atlassian.zapi.configs.CZApiConfigs;
import org.catools.atlassian.zapi.exception.CZApiException;
import org.catools.atlassian.zapi.model.CZApiCycle;
import org.catools.atlassian.zapi.model.CZApiExecutions;
import org.catools.atlassian.zapi.rest.CZApiRestClient;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.codehaus.jettison.json.JSONObject;

/**
 * Client class for managing test executions in the ZAPI system.
 *
 * <p>This class provides methods to add tests to a cycle and update the status of executions in
 * bulk.
 */
@Slf4j
public class CZApiExecutionClient extends CZApiRestClient {

  /**
   * Adds a set of test issues to a specific test cycle.
   *
   * @param issueKeys the set of issue keys to be added to the cycle
   * @param cycle the test cycle to which the issues will be added
   * @param partitionSize the maximum number of issues to process in a single request
   */
  public void addTestsToCycle(CSet<String> issueKeys, CZApiCycle cycle, int partitionSize) {
    addTestsToCycle(
        cycle.getProject().getId(),
        cycle.getVersion().getId(),
        cycle.getId(),
        issueKeys,
        partitionSize);
  }

  /**
   * Adds a set of test issues to a specific test cycle using project, version, and cycle IDs.
   *
   * @param projectId the ID of the project associated with the test cycle
   * @param versionId the ID of the version associated with the test cycle
   * @param cycleId the ID of the test cycle
   * @param issueKeys the set of issue keys to be added to the cycle
   * @param partitionSize the maximum number of issues to process in a single request
   */
  public void addTestsToCycle(
      Long projectId, Long versionId, Long cycleId, CSet<String> issueKeys, int partitionSize) {
    for (CList<String> keys : issueKeys.partition(partitionSize)) {
      log.trace("Add executions for items: {}", keys);
      JSONObject entity;
      try {
        // Builds the JSON payload for the request
        entity =
            new JSONObject()
                .put("issues", keys)
                .put("method", "1")
                .put("cycleId", cycleId)
                .put("projectId", projectId)
                .put("versionId", versionId);
      } catch (Exception e) {
        throw new CZApiException("Failed to build JSONObject", e);
      }

      // Sends the request to add tests to the cycle
      RequestSpecification specification =
          RestAssured.given()
              .baseUri(CZApiConfigs.ZApi.getZApiUri())
              .basePath("/execution/addTestsToCycle")
              .body(entity.toString());
      post(specification);
    }
  }

  /**
   * Updates the status of a set of executions in bulk.
   *
   * @param executions the executions whose statuses will be updated
   * @param status the new status to be applied to the executions
   */
  public void updateBulkStatus(CZApiExecutions executions, CZApiExecutionStatus status) {
    updateBulkStatus(executions.mapToSet(e -> e.getId()), status);
  }

  /**
   * Updates the status of a set of executions in bulk using their IDs.
   *
   * @param executionIds the set of execution IDs whose statuses will be updated
   * @param status the new status to be applied to the executions
   */
  public void updateBulkStatus(CSet<Long> executionIds, CZApiExecutionStatus status) {
    updateBulkStatus(executionIds, status, 50);
  }

  /**
   * Updates the status of a set of executions in bulk using their IDs, with partitioning.
   *
   * @param executionIds the set of execution IDs whose statuses will be updated
   * @param status the new status to be applied to the executions
   * @param partitionSize the maximum number of executions to process in a single request
   */
  public void updateBulkStatus(
      CSet<Long> executionIds, CZApiExecutionStatus status, int partitionSize) {
    try {
      for (CList<Long> ids : executionIds.partition(partitionSize)) {
        // Builds the JSON payload for the request
        JSONObject entity =
            new JSONObject()
                .put("executions", ids)
                .put("status", CZApiConfigs.ZApi.getStatusMap().get(status.name()));
        RequestSpecification specification =
            RestAssured.given()
                .baseUri(CZApiConfigs.ZApi.getZApiUri())
                .basePath("/execution/updateBulkStatus")
                .body(entity.toString());
        Response response = put(specification);
        log.debug(
            "response code:" + response.statusCode() + ", message:" + response.body().asString());
      }
    } catch (Exception e) {
      throw new CZApiException("Could not update execution statuses", e);
    }
  }
}
