package org.catools.atlassian.jira.client;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousSearchRestClient;
import com.atlassian.jira.rest.client.internal.json.SearchResultJsonParser;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import io.atlassian.util.concurrent.Promise;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.net.URI;
import java.util.Set;
import java.util.stream.Stream;

import static com.atlassian.jira.rest.client.api.IssueRestClient.Expandos.*;

/**
 * Custom implementation of the AsynchronousSearchRestClient.
 * This class extends the default AsynchronousSearchRestClient to provide
 * additional functionality for handling JQL queries with both GET and POST methods.
 */
public class CAsynchronousSearchRestClient extends AsynchronousSearchRestClient {
  // Function to convert Expandos to their lowercase string representation
  private static final Function<IssueRestClient.Expandos, String> EXPANDO_TO_PARAM =
      from -> from.name().toLowerCase();

  // Constants for query parameter names and limits
  private static final String START_AT_ATTRIBUTE = "startAt";
  private static final String MAX_RESULTS_ATTRIBUTE = "maxResults";
  private static final int MAX_JQL_LENGTH_FOR_HTTP_GET = 500;
  private static final String JQL_ATTRIBUTE = "jql";
  private static final String SEARCH_URI_PREFIX = "search";
  private static final String EXPAND_ATTRIBUTE = "expand";
  private static final String FIELDS_ATTRIBUTE = "fields";

  // JSON parser for parsing search results
  private final SearchResultJsonParser searchResultJsonParser = new SearchResultJsonParser();

  // Base URI for search operations
  private final URI searchUri;

  /**
   * Constructs a new instance of CAsynchronousSearchRestClient.
   *
   * @param baseUri The base URI of the Jira REST API.
   * @param asyncHttpClient The asynchronous HTTP client used for making requests.
   */
  public CAsynchronousSearchRestClient(final URI baseUri, final HttpClient asyncHttpClient) {
    super(baseUri, asyncHttpClient);
    this.searchUri = UriBuilder.fromUri(baseUri).path(SEARCH_URI_PREFIX).build();
  }

  /**
   * Executes a JQL search query using default parameters.
   *
   * @param jql The JQL query string.
   * @return A Promise containing the search results.
   */
  @Override
  public Promise<SearchResult> searchJql(String jql) {
    return searchJql(jql, null, null, null);
  }

  /**
   * Executes a JQL search query with optional parameters.
   *
   * @param jql The JQL query string.
   * @param maxResults The maximum number of results to return.
   * @param startAt The starting index of the results.
   * @param fields The fields to include in the results.
   * @return A Promise containing the search results.
   */
  @Override
  public Promise<SearchResult> searchJql(
      String jql,
      Integer maxResults,
      Integer startAt,
      Set<String> fields) {
    final Iterable<String> expandosValues =
        Stream.of(SCHEMA, NAMES, CHANGELOG, TRANSITIONS).map(EXPANDO_TO_PARAM).toList();
    final String notNullJql = StringUtils.defaultString(jql);
    if (notNullJql.length() > MAX_JQL_LENGTH_FOR_HTTP_GET) {
      return postJql(maxResults, startAt, expandosValues, notNullJql, fields);
    } else {
      return getJql(maxResults, startAt, expandosValues, notNullJql, fields);
    }
  }

  /**
   * Executes a JQL search query using the HTTP GET method.
   *
   * @param maxResults The maximum number of results to return.
   * @param startAt The starting index of the results.
   * @param expandosValues The expandos to include in the results.
   * @param jql The JQL query string.
   * @param fields The fields to include in the results.
   * @return A Promise containing the search results.
   */
  private Promise<SearchResult> getJql(
      Integer maxResults,
      Integer startAt,
      Iterable<String> expandosValues,
      String jql,
      Set<String> fields) {
    final UriBuilder uriBuilder =
        UriBuilder.fromUri(searchUri)
            .queryParam(JQL_ATTRIBUTE, jql)
            .queryParam(EXPAND_ATTRIBUTE, Joiner.on(",").join(expandosValues));

    if (fields != null) {
      uriBuilder.queryParam(FIELDS_ATTRIBUTE, Joiner.on(",").join(fields));
    }
    addOptionalParam(uriBuilder, MAX_RESULTS_ATTRIBUTE, maxResults);
    addOptionalParam(uriBuilder, START_AT_ATTRIBUTE, startAt);

    return getAndParse(uriBuilder.build(), searchResultJsonParser);
  }

  /**
   * Adds an optional query parameter to the URI builder.
   *
   * @param uriBuilder The URI builder.
   * @param key The name of the query parameter.
   * @param values The values of the query parameter.
   */
  private void addOptionalParam(
      final UriBuilder uriBuilder, final String key, final Object... values) {
    if (values != null && values.length > 0 && values[0] != null) {
      uriBuilder.queryParam(key, values);
    }
  }

  /**
   * Executes a JQL search query using the HTTP POST method.
   *
   * @param maxResults The maximum number of results to return.
   * @param startAt The starting index of the results.
   * @param expandosValues The expandos to include in the results.
   * @param jql The JQL query string.
   * @param fields The fields to include in the results.
   * @return A Promise containing the search results.
   */
  private Promise<SearchResult> postJql(
      Integer maxResults,
      Integer startAt,
      Iterable<String> expandosValues,
      String jql,
      Set<String> fields) {
    final JSONObject postEntity = new JSONObject();

    try {
      postEntity
          .put(JQL_ATTRIBUTE, jql)
          .put(EXPAND_ATTRIBUTE, ImmutableList.copyOf(expandosValues))
          .putOpt(START_AT_ATTRIBUTE, startAt)
          .putOpt(MAX_RESULTS_ATTRIBUTE, maxResults);

      if (fields != null) {
        postEntity.put(FIELDS_ATTRIBUTE, fields); // putOpt doesn't work with collections
      }
    } catch (JSONException e) {
      throw new RestClientException(e);
    }
    return postAndParse(searchUri, postEntity, searchResultJsonParser);
  }
}