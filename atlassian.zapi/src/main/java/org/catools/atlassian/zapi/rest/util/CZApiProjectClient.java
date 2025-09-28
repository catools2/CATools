package org.catools.atlassian.zapi.rest.util;

import io.restassured.response.Response;
import org.catools.atlassian.zapi.model.CZApiProjects;
import org.catools.atlassian.zapi.rest.CZApiRestClient;
import org.catools.common.utils.CJsonUtil;

/**
 * Client class for retrieving project information in the ZAPI system.
 *
 * <p>This class provides methods to interact with the ZAPI REST API to fetch project details.</p>
 */
public class CZApiProjectClient extends CZApiRestClient {

  /**
   * Default constructor for the CZApiProjectClient.
   */
  public CZApiProjectClient() {
    super();
  }

  /**
   * Retrieves the list of projects from the ZAPI system.
   *
   * @return a {@link CZApiProjects} object containing the list of projects
   */
  public CZApiProjects getProjects() {
    // Sends a GET request to the ZAPI endpoint for project list
    Response response = get("/util/project-list");
    // Parses the response body into a CZApiProjects object
    return CJsonUtil.read(
        CJsonUtil.toString(response.body().jsonPath().get("options")), CZApiProjects.class);
  }
}
