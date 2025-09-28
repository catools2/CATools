package org.catools.atlassian.zapi.rest.util;

import io.restassured.response.Response;
import org.catools.atlassian.zapi.model.CZApiExecutionStatuses;
import org.catools.atlassian.zapi.rest.CZApiRestClient;
import org.catools.common.utils.CJsonUtil;

/**
 * Client class for retrieving execution statuses in the ZAPI system.
 *
 * <p>This class provides methods to interact with the ZAPI REST API to fetch test execution statuses.</p>
 */
public class CZApiExecutionStatusClient extends CZApiRestClient {

  /**
   * Default constructor for the CZApiExecutionStatusClient.
   */
  public CZApiExecutionStatusClient() {
    super();
  }

  /**
   * Retrieves the test execution statuses from the ZAPI system.
   *
   * @return a {@link CZApiExecutionStatuses} object containing the execution statuses
   */
  public CZApiExecutionStatuses getExecutionStatus() {
    // Sends a GET request to the ZAPI endpoint for test execution statuses
    Response response = get("/util/testExecutionStatus");
    // Parses the response body into a CZApiExecutionStatuses object
    return CJsonUtil.read(response.getBody().print(), CZApiExecutionStatuses.class);
  }
}
