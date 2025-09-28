package org.catools.atlassian.zapi.rest.cycle;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.catools.atlassian.zapi.CZApiClient;
import org.catools.atlassian.zapi.configs.CZApiConfigs;
import org.catools.atlassian.zapi.exception.CZApiException;
import org.catools.atlassian.zapi.model.*;
import org.catools.atlassian.zapi.parser.CZApiCycleParser;
import org.catools.atlassian.zapi.parser.CZApiCyclesParser;
import org.catools.atlassian.zapi.rest.CZApiRestClient;
import org.catools.common.extensions.verify.CVerify;
import org.codehaus.jettison.json.JSONObject;

/**
 * Client class for managing test cycles in the ZAPI system.
 *
 * <p>This class provides methods to retrieve, create, and clone test cycles using the ZAPI REST API.</p>
 */
public class CZApiCycleClient extends CZApiRestClient {

  /**
   * Default constructor for the CZApiCycleClient.
   */
  public CZApiCycleClient() {
    super();
  }

  /**
   * Retrieves the executions associated with a specific test cycle.
   *
   * @param cycle the test cycle for which executions are being retrieved
   * @return a {@link CZApiExecutions} object containing the executions for the specified cycle
   */
  public CZApiExecutions getExecutions(CZApiCycle cycle) {
    return CZApiClient.Search.getExecutions(
        String.format(
            "project = \"%s\" AND fixVersion = \"%s\" AND cycleName = \"%s\"",
            cycle.getProject().getName(), cycle.getVersion().getName(), cycle.getName()));
  }

  /**
   * Retrieves all test cycles for a specific project and version.
   *
   * @param project the project for which cycles are being retrieved
   * @param version the version for which cycles are being retrieved
   * @return a {@link CZApiCycles} object containing all cycles for the specified project and version
   */
  public CZApiCycles getAllCycle(CZApiProject project, CZApiVersion version) {
    RequestSpecification specification =
        RestAssured.given()
            .baseUri(CZApiConfigs.ZApi.getZApiUri())
            .basePath("/cycle")
            .queryParam("projectId", project.getId())
            .queryParam("versionId", version.getId());
    return CZApiCyclesParser.parse(project, get(specification));
  }

  /**
   * Retrieves a test cycle by its unique identifier.
   *
   * @param cycleId the unique identifier of the test cycle
   * @return the {@link CZApiCycle} object corresponding to the specified ID
   */
  public CZApiCycle getCycleById(long cycleId) {
    CZApiProjects projects = CZApiClient.Project.getProjects();
    return CZApiCycleParser.parse(projects, get(String.format("/cycle/%s", cycleId)));
  }

  /**
   * Creates a new test cycle with the specified details.
   *
   * @param name the name of the test cycle
   * @param build the build associated with the test cycle
   * @param environment the environment associated with the test cycle
   * @param description a description of the test cycle
   * @param startDate the start date of the test cycle
   * @param endDate the end date of the test cycle
   * @param projectId the ID of the project associated with the test cycle
   * @param versionId the ID of the version associated with the test cycle
   * @return the unique identifier of the created test cycle
   */
  public long createCycle(
      String name,
      String build,
      String environment,
      String description,
      String startDate,
      String endDate,
      Long projectId,
      Long versionId) {
    return cloneCycle(
        null, name, build, environment, description, startDate, endDate, projectId, versionId);
  }

  /**
   * Clones an existing test cycle or creates a new one with the specified details.
   *
   * @param clonedCycleId the ID of the cycle to be cloned (or {@code null} to create a new cycle)
   * @param name the name of the new or cloned test cycle
   * @param build the build associated with the test cycle
   * @param environment the environment associated with the test cycle
   * @param description a description of the test cycle
   * @param startDate the start date of the test cycle
   * @param endDate the end date of the test cycle
   * @param projectId the ID of the project associated with the test cycle
   * @param versionId the ID of the version associated with the test cycle
   * @return the unique identifier of the created or cloned test cycle
   * @throws CZApiException if an error occurs during the creation or cloning process
   */
  public long cloneCycle(
      Long clonedCycleId,
      String name,
      String build,
      String environment,
      String description,
      String startDate,
      String endDate,
      Long projectId,
      Long versionId) {
    JSONObject entity = new JSONObject();
    try {
      entity
          .put("clonedCycleId", clonedCycleId)
          .put("name", name)
          .put("build", build)
          .put("environment", environment)
          .put("description", description)
          .put("startDate", startDate)
          .put("endDate", endDate)
          .put("projectId", projectId)
          .put("versionId", versionId);
    } catch (Exception e) {
      throw new CZApiException("Could convert", e);
    }

    RequestSpecification specification =
        RestAssured.given()
            .baseUri(CZApiConfigs.ZApi.getZApiUri())
            .basePath("/cycle")
            .body(entity.toString());
    Response response = post(specification);

    try {
      String id = response.body().jsonPath().get("id");
      CVerify.String.isNotBlank(id, "Test cycle has been created.");
      return Long.valueOf(id);
    } catch (Exception e) {
      throw new CZApiException("Failed to parse response\n" + response.getBody(), e);
    }
  }
}
