package org.catools.atlassian.scale.rest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.catools.atlassian.scale.configs.CZScaleConfigs;
import org.catools.atlassian.scale.exception.CZScaleClientException;
import org.catools.common.concurrent.CParallelCollectionIO;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.utils.CRetry;
import org.catools.common.utils.CSleeper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Base REST client for interacting with the Scale system.
 *
 * <p>This class provides common functionality for making HTTP requests, handling retries, and
 * processing data in parallel. It is designed to be extended by other REST client classes to
 * interact with specific endpoints in the Scale system.
 */
public class CZScaleRestClient {

  /** Default constructor. */
  public CZScaleRestClient() {}

  /**
   * Reads all data in parallel using the specified input and output executors.
   *
   * @param actionName the name of the action being performed
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param requestProcessor a function to process requests and retrieve data
   * @param onAction a consumer to process each retrieved item
   * @param <T> the type of data being processed
   * @return a set of all retrieved data
   */
  protected static <T> Set<T> readAllInParallel(
      String actionName,
      int parallelInputCount,
      int parallelOutputCount,
      BiFunction<Integer, Integer, Set<T>> requestProcessor,
      Consumer<T> onAction) {

    Set<T> output = Collections.synchronizedSet(new HashSet<>());

    CParallelCollectionIO<T> parallelIO =
        new CParallelCollectionIO<>(actionName, parallelInputCount, parallelOutputCount);

    int maxResult = CZScaleConfigs.Scale.getSearchBufferSize();
    AtomicInteger counter = new AtomicInteger(0);

    parallelIO.setInputExecutor(
        eof -> {
          int startAt = counter.getAndIncrement() * maxResult;
          Set<T> result =
              CRetry.retry(integer -> requestProcessor.apply(startAt, maxResult), 3, 5000);
          if (result == null || result.isEmpty()) {
            eof.set(true);
            CSleeper.sleepTight(500);
          } else {
            output.addAll(result);
            eof.set(false);
          }
          return result;
        });

    parallelIO.setOutputExecutor(
        (eof, issue) -> {
          if (onAction != null && issue != null) {
            onAction.accept(issue);
          }
        });

    try {
      parallelIO.run();
    } catch (Throwable t) {
      throw new CZScaleClientException("Could not finish search.", t);
    }

    return output;
  }

  /**
   * Sends a GET request without verifying the response.
   *
   * @param request the request specification
   * @return the response from the server
   */
  protected Response getWithoutVerification(RequestSpecification request) {
    return CRetry.retry(integer -> decorate(request).get(), 5, 1000);
  }

  /**
   * Sends a GET request and verifies the response.
   *
   * @param request the request specification
   * @return the verified response from the server
   */
  protected Response get(RequestSpecification request) {
    return CRetry.retry(integer -> verifyResponse(decorate(request).get()), 5, 1000);
  }

  /**
   * Sends a DELETE request and verifies the response.
   *
   * @param request the request specification
   * @return the verified response from the server
   */
  protected Response delete(RequestSpecification request) {
    return CRetry.retry(integer -> verifyResponse(decorate(request).delete()), 5, 1000);
  }

  /**
   * Sends a POST request and verifies the response.
   *
   * @param request the request specification
   * @return the verified response from the server
   */
  protected Response post(RequestSpecification request) {
    return CRetry.retry(integer -> verifyResponse(decorate(request).post()), 5, 1000);
  }

  /**
   * Sends a PUT request and verifies the response.
   *
   * @param request the request specification
   * @return the verified response from the server
   */
  protected Response put(RequestSpecification request) {
    return CRetry.retry(integer -> verifyResponse(decorate(request).put()), 5, 1000);
  }

  /**
   * Verifies the response status code and ensures it is within the acceptable range.
   *
   * @param response the response to verify
   * @return the verified response
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
   * Decorates the request specification with authentication and content type.
   *
   * @param request the request specification to decorate
   * @return the decorated request specification
   */
  protected RequestSpecification decorate(RequestSpecification request) {
    CSleeper.sleepTight(CZScaleConfigs.Scale.getDelayBetweenCallsInMilliseconds());
    return request
        .auth()
        .preemptive()
        .basic(CZScaleConfigs.Scale.getUserName(), CZScaleConfigs.Scale.getPassword())
        .contentType(ContentType.JSON);
  }
}
