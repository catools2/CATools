package org.catools.atlassian.zapi.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.catools.atlassian.zapi.configs.CZApiConfigs;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.utils.CSleeper;

/**
 * Base class for interacting with the ZAPI REST API.
 *
 * <p>This class provides common methods for making HTTP requests (GET, POST, PUT) and handling
 * authentication, response verification, and request decoration.</p>
 */
public class CZApiRestClient {

  /**
   * Sends a GET request to the specified path.
   *
   * @param path the endpoint path to send the GET request to
   * @return the {@link Response} object containing the server's response
   */
  protected Response get(String path) {
    return get(RestAssured.given().baseUri(CZApiConfigs.ZApi.getZApiUri()).basePath(path));
  }

  /**
   * Sends a GET request using the provided request specification.
   *
   * @param request the {@link RequestSpecification} object containing the request details
   * @return the {@link Response} object containing the server's response
   */
  protected Response get(RequestSpecification request) {
    return verifyResponse(decorate(request).get());
  }

  /**
   * Sends a POST request using the provided request specification.
   *
   * @param request the {@link RequestSpecification} object containing the request details
   * @return the {@link Response} object containing the server's response
   */
  protected Response post(RequestSpecification request) {
    return verifyResponse(decorate(request).post());
  }

  /**
   * Sends a PUT request using the provided request specification.
   *
   * @param request the {@link RequestSpecification} object containing the request details
   * @return the {@link Response} object containing the server's response
   */
  protected Response put(RequestSpecification request) {
    return verifyResponse(decorate(request).put());
  }

  /**
   * Verifies the response status code to ensure it is within the successful range (200-204).
   *
   * @param response the {@link Response} object to verify
   * @return the same {@link Response} object if the status code is valid
   * @throws IllegalArgumentException if the status code is outside the valid range
   */
  private Response verifyResponse(Response response) {
    int statusCode = response.statusCode();
    if (statusCode < 200 || statusCode > 204) {
      CVerify.Int.betweenInclusive(
          statusCode, 200, 204, "Request processed successfully. response " + response.print());
    }
    return response;
  }

  /**
   * Decorates the request specification with authentication, content type, and delay handling.
   *
   * @param request the {@link RequestSpecification} object to decorate
   * @return the decorated {@link RequestSpecification} object
   */
  protected RequestSpecification decorate(RequestSpecification request) {
    CSleeper.sleepTight(CZApiConfigs.ZApi.getDelayBetweenCallsInMilliseconds());
    return request
        .auth()
        .preemptive()
        .basic(CZApiConfigs.ZApi.getUserName(), CZApiConfigs.ZApi.getPassword())
        .contentType(ContentType.JSON);
  }
}
