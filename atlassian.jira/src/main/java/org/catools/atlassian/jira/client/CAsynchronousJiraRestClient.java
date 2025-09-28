package org.catools.atlassian.jira.client;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClient;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Custom implementation of the AsynchronousJiraRestClient.
 * This class extends the default AsynchronousJiraRestClient and provides
 * a custom implementation for the SearchRestClient.
 */
public class CAsynchronousJiraRestClient extends AsynchronousJiraRestClient
    implements JiraRestClient {

  // Custom implementation of the SearchRestClient
  private final SearchRestClient searchRestClient;

  /**
   * Constructs a new instance of CAsynchronousJiraRestClient.
   *
   * @param serverUri  The base URI of the Jira server.
   * @param httpClient The HTTP client used for making requests.
   */
  public CAsynchronousJiraRestClient(URI serverUri, DisposableHttpClient httpClient) {
    super(serverUri, httpClient);
    // Build the base URI for the REST API
    final URI baseUri = UriBuilder.fromUri(serverUri).path("/rest/api/latest").build();
    // Initialize the custom SearchRestClient
    searchRestClient = new CAsynchronousSearchRestClient(baseUri, httpClient);
  }

  /**
   * Retrieves the custom SearchRestClient.
   *
   * @return The custom implementation of the SearchRestClient.
   */
  @Override
  public SearchRestClient getSearchClient() {
    return searchRestClient;
  }
}