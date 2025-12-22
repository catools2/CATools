package org.catools.atlassian.zapi.rest.zql;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.catools.atlassian.zapi.configs.CZApiConfigs;
import org.catools.atlassian.zapi.exception.CZApiClientException;
import org.catools.atlassian.zapi.model.CZApiExecutions;
import org.catools.atlassian.zapi.parser.CZApiExecutionsParser;
import org.catools.atlassian.zapi.rest.CZApiRestClient;
import org.catools.common.concurrent.CParallelIO;
import org.catools.common.date.CDate;
import org.catools.common.utils.CRetry;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Client class for searching test executions in the ZAPI system using ZQL queries.
 *
 * <p>This class provides methods to execute ZQL queries and retrieve test executions with support
 * for parallel processing.
 */
@Slf4j
public class CZApiSearchClient extends CZApiRestClient {

  /** Default constructor for the CZApiSearchClient. */
  public CZApiSearchClient() {
    super();
  }

  /**
   * Retrieves test executions based on the provided ZQL query.
   *
   * @param zql the ZQL query string
   * @return a {@link CZApiExecutions} object containing the test executions
   */
  public CZApiExecutions getExecutions(String zql) {
    return getExecutions(zql, null, 1, 1, null);
  }

  /**
   * Retrieves test executions based on the provided ZQL query and last synchronization date.
   *
   * @param zql the ZQL query string
   * @param lastSyncDate the date to filter executions created or executed after this date
   * @return a {@link CZApiExecutions} object containing the test executions
   */
  public CZApiExecutions getExecutions(String zql, Date lastSyncDate) {
    return getExecutions(zql, lastSyncDate, 1, 1, null);
  }

  /**
   * Retrieves test executions based on the provided ZQL query with parallel input processing.
   *
   * @param zql the ZQL query string
   * @param parallelInputCount the number of parallel input threads
   * @return a {@link CZApiExecutions} object containing the test executions
   */
  public CZApiExecutions getExecutions(String zql, int parallelInputCount) {
    return getExecutions(zql, null, parallelInputCount, 1, null);
  }

  /**
   * Retrieves test executions based on the provided ZQL query, last synchronization date, and
   * parallel input processing.
   *
   * @param zql the ZQL query string
   * @param lastSyncDate the date to filter executions created or executed after this date
   * @param parallelInputCount the number of parallel input threads
   * @return a {@link CZApiExecutions} object containing the test executions
   */
  public CZApiExecutions getExecutions(String zql, Date lastSyncDate, int parallelInputCount) {
    return getExecutions(zql, lastSyncDate, parallelInputCount, 1, null);
  }

  /**
   * Retrieves test executions based on the provided ZQL query with parallel input and output
   * processing.
   *
   * @param zql the ZQL query string
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param supplier a consumer to process the retrieved executions
   * @return a {@link CZApiExecutions} object containing the test executions
   */
  public CZApiExecutions getExecutions(
      String zql,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CZApiExecutions> supplier) {
    return getExecutions(zql, null, parallelInputCount, parallelOutputCount, supplier);
  }

  /**
   * Retrieves test executions based on the provided ZQL query, last synchronization date, and
   * parallel processing.
   *
   * @param zql the ZQL query string
   * @param lastSyncDate the date to filter executions created or executed after this date
   * @param parallelInputCount the number of parallel input threads
   * @param parallelOutputCount the number of parallel output threads
   * @param supplier a consumer to process the retrieved executions
   * @return a {@link CZApiExecutions} object containing the test executions
   */
  public CZApiExecutions getExecutions(
      String zql,
      Date lastSyncDate,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CZApiExecutions> supplier) {
    CZApiExecutions executions = new CZApiExecutions();
    CParallelIO<CZApiExecutions> parallelIO =
        new CParallelIO<>("Search ZApi Executions", parallelInputCount, parallelOutputCount);

    int maxResult = CZApiConfigs.ZApi.getSearchBufferSize();
    AtomicInteger counter = new AtomicInteger(0);

    parallelIO.setInputExecutor(
        eof -> {
          int startAt = counter.getAndIncrement() * maxResult;
          CZApiExecutions search =
              CRetry.retry(
                  integer -> _getExecutions(zql, lastSyncDate, startAt, maxResult), 5, 5000);

          if (search == null || search.isEmpty()) {
            log.info("{} execution record returned for ZQL = {}", executions.size(), zql);
            eof.set(true);
          } else {
            executions.addAll(search);
            eof.set(false);
          }
          return search;
        });

    parallelIO.setOutputExecutor(
        (eof, issues) -> {
          if (supplier != null && issues != null && issues.isNotEmpty()) {
            supplier.accept(issues);
          }
        });

    try {
      parallelIO.run();
    } catch (Throwable t) {
      throw new CZApiClientException("Could not finish search.", t);
    }

    return executions;
  }

  /**
   * Executes the ZQL query and retrieves a subset of test executions based on the provided
   * parameters.
   *
   * @param zql the ZQL query string
   * @param lastSyncDate the date to filter executions created or executed after this date
   * @param offset the starting index for the query results
   * @param maxResults the maximum number of results to retrieve
   * @return a {@link CZApiExecutions} object containing the test executions
   */
  private CZApiExecutions _getExecutions(
      String zql, Date lastSyncDate, int offset, int maxResults) {
    if (lastSyncDate != null) {
      zql +=
          String.format(
              " AND (creationDate >= '%1$s' or executionDate >= '%1$s')",
              new CDate(lastSyncDate).toFormat("yyyy/MM/dd"));
    }

    log.info("Execute Search {}, maxResults:{}, startAt:{}", zql, maxResults, offset);
    RequestSpecification specification =
        RestAssured.given()
            .baseUri(CZApiConfigs.ZApi.getZApiUri())
            .basePath("/zql/executeSearch")
            .queryParam("zqlQuery", zql)
            .queryParam("offset", offset)
            .queryParam("maxRecords", maxResults);

    return CZApiExecutionsParser.parse(get(specification));
  }
}
