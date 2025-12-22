package org.catools.atlassian.zapi.rest.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.catools.atlassian.zapi.configs.CZApiConfigs;
import org.catools.atlassian.zapi.model.CZApiProject;
import org.catools.atlassian.zapi.model.CZApiProjectVersions;
import org.catools.atlassian.zapi.model.CZApiVersions;
import org.catools.atlassian.zapi.rest.CZApiRestClient;
import org.catools.common.utils.CJsonUtil;

/**
 * Client class for retrieving version information for a specific project in the ZAPI system.
 *
 * <p>This class provides methods to interact with the ZAPI REST API to fetch released and
 * unreleased versions of a project.
 */
public class CZApiVersionClient extends CZApiRestClient {

  /** Default constructor for the CZApiVersionClient. */
  public CZApiVersionClient() {
    super();
  }

  /**
   * Retrieves the released and unreleased versions for a specific project.
   *
   * @param project the project for which versions are being retrieved
   * @return a {@link CZApiProjectVersions} object containing the released and unreleased versions
   *     of the project
   */
  public CZApiProjectVersions getProjectVersions(CZApiProject project) {
    // Builds the request specification with the base URI, path, and query parameter for the project
    // ID
    RequestSpecification specification =
        RestAssured.given()
            .baseUri(CZApiConfigs.ZApi.getZApiUri())
            .basePath("/util/versionBoard-list")
            .queryParam("projectId", project.getId());

    // Sends the GET request and retrieves the response
    Response response = get(specification);

    // Parses the response to extract released and unreleased versions
    CZApiProjectVersions versions = new CZApiProjectVersions();
    versions.setReleasedVersions(
        CJsonUtil.read(
            CJsonUtil.toString(response.body().jsonPath().get("releasedVersions")),
            CZApiVersions.class));
    versions.setUnreleasedVersions(
        CJsonUtil.read(
            CJsonUtil.toString(response.body().jsonPath().get("unreleasedVersions")),
            CZApiVersions.class));
    return versions;
  }
}
