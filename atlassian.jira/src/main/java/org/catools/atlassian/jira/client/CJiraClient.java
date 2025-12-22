package org.catools.atlassian.jira.client;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.*;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.LinkIssuesInput;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.atlassian.jira.configs.CJiraConfigs;
import org.catools.atlassian.jira.exception.CJiraClientException;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.concurrent.CParallelIO;
import org.catools.common.date.CDate;
import org.catools.common.functions.CMemoize;
import org.catools.common.utils.CRetry;
import org.catools.common.utils.CSleeper;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility class for interacting with the Jira REST API. Provides methods for retrieving projects,
 * issues, users, and performing operations such as creating issues, linking issues, transitioning
 * issues, and executing JQL searches.
 */
@Slf4j
@UtilityClass
public class CJiraClient {
  private static final CList<IssueRestClient.Expandos> expandos =
      new CList<>(
          IssueRestClient.Expandos.CHANGELOG,
          IssueRestClient.Expandos.SCHEMA,
          IssueRestClient.Expandos.TRANSITIONS,
          IssueRestClient.Expandos.NAMES);

  private static final CMemoize<JiraRestClient> restClient =
      new CMemoize<>(
          () ->
              new CAsynchronousJiraRestClientFactory()
                  .createWithBasicHttpAuthentication(
                      CJiraConfigs.Jira.getHomeUri(),
                      CJiraConfigs.Jira.getUserName(),
                      CJiraConfigs.Jira.getPassword()));

  /**
   * Retrieves all projects from the Jira instance.
   *
   * @return A set of all projects.
   */
  public static CSet<BasicProject> getProjects() {
    return doAction(
        restClient -> {
          log.info("Request all projects from jira client.");
          return new CSet<>(restClient.getProjectClient().getAllProjects().claim());
        });
  }

  /**
   * Retrieves the current project based on the configured project key.
   *
   * @return The current project.
   */
  public static BasicProject getCurrentProject() {
    return getProject(CJiraConfigs.Jira.getProjectKey());
  }

  /**
   * Retrieves a specific project by its name.
   *
   * @param projectName The name of the project to retrieve.
   * @return The project with the specified name.
   */
  public static BasicProject getProject(String projectName) {
    return doAction(
        restClient -> {
          log.info("Request all projects from jira client.");
          return restClient.getProjectClient().getProject(projectName).claim();
        });
  }

  /**
   * Retrieves all versions for a specific project.
   *
   * @param projectName The name of the project.
   * @return A set of versions associated with the project.
   */
  public static CSet<Version> getProjectVersions(String projectName) {
    return doAction(
        restClient -> {
          log.info("Request all versions for {} project from jira client.", projectName);
          return new CSet<>(
              restClient.getProjectClient().getProject(projectName).claim().getVersions());
        });
  }

  /**
   * Retrieves all issue types for a specific project.
   *
   * @param projectName The name of the project.
   * @return A set of issue types associated with the project.
   */
  public static CSet<IssueType> getProjectItemTypes(String projectName) {
    return doAction(
        restClient -> {
          log.info("Request all Issue Types for {} project from jira client.", projectName);
          return new CSet<>(
              restClient.getProjectClient().getProject(projectName).claim().getIssueTypes());
        });
  }

  /**
   * Retrieves a user by their username.
   *
   * @param username The username of the user.
   * @return The user with the specified username.
   */
  public static User getUser(String username) {
    return doAction(
        restClient -> {
          log.info("Request user information for user with {} username.", username);
          return restClient.getUserClient().getUser(username).claim();
        });
  }

  /**
   * Retrieves an issue by its key.
   *
   * @param issueKey The key of the issue.
   * @return The issue with the specified key.
   */
  public static Issue getIssue(String issueKey) {
    return doAction(
        jc -> {
          log.info("Request issue information for issue with {} key.", issueKey);
          return jc.getIssueClient().getIssue(issueKey, expandos).claim();
        });
  }

  /**
   * Creates a new issue in Jira.
   *
   * @param issueInput The input data for the issue to be created.
   * @return The created issue.
   */
  public static BasicIssue createIssue(IssueInput issueInput) {
    return doAction(
        jc -> {
          log.info("Request creating issue {}.", issueInput);
          return jc.getIssueClient().createIssue(issueInput).claim();
        });
  }

  /**
   * Links two issues in Jira.
   *
   * @param linkIssuesInput The input data for linking the issues.
   */
  public static void linkIssue(LinkIssuesInput linkIssuesInput) {
    doAction(
        jc -> {
          log.info("Request creating link {}.", linkIssuesInput);
          return jc.getIssueClient().linkIssue(linkIssuesInput).claim();
        });
  }

  /**
   * Transitions an issue to a new state.
   *
   * @param issue The issue to transition.
   * @param transitionInput The transition input data.
   */
  public static void doTransition(Issue issue, TransitionInput transitionInput) {
    doAction(
        jc -> {
          log.info("Request creating link {} with transitions {}.", issue, transitionInput);
          jc.getIssueClient().transition(issue, transitionInput).claim();
          return true;
        });
  }

  /**
   * Links two issues in Jira with additional details.
   *
   * @param fromIssueKey The key of the source issue.
   * @param toIssueKey The key of the target issue.
   * @param linkType The type of the link.
   * @param comment A comment to add to the link.
   */
  public static void linkIssue(
      String fromIssueKey, String toIssueKey, String linkType, String comment) {
    doAction(
        jc -> {
          log.info(
              "Request creating link from {} to {} with type {} and comment {}.",
              fromIssueKey,
              toIssueKey,
              linkType,
              comment);
          jc.getIssueClient()
              .linkIssue(
                  new LinkIssuesInput(fromIssueKey, toIssueKey, linkType, Comment.valueOf(comment)))
              .claim();
          return true;
        });
  }

  /**
   * Searches for issues in a project based on issue type and last synchronization date.
   *
   * @param projectName The name of the project.
   * @param issueType The type of issues to search for.
   * @param lastSync The last synchronization date.
   * @param parallelInputCount The number of parallel input threads.
   * @param parallelOutputCount The number of parallel output threads.
   * @param supplier A consumer to process the search results.
   * @return A set of issues matching the search criteria.
   */
  public static CSet<Issue> search(
      String projectName,
      String issueType,
      Date lastSync,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CSet<Issue>> supplier) {
    String jql = String.format("project = \"%s\" AND issuetype = \"%s\"", projectName, issueType);

    if (lastSync != null) {
      jql +=
          String.format(" AND updated >= \"%s\"", new CDate(lastSync).toFormat("yyyy-MM-dd HH:mm"));
    }
    return search(jql, parallelInputCount, parallelOutputCount, supplier);
  }

  /**
   * Searches for issues using a JQL query.
   *
   * @param jql The JQL query string.
   * @param parallelInputCount The number of parallel input threads.
   * @param parallelOutputCount The number of parallel output threads.
   * @param supplier A consumer to process the search results.
   * @return A set of issues matching the search criteria.
   */
  public static CSet<Issue> search(
      String jql, int parallelInputCount, int parallelOutputCount, Consumer<CSet<Issue>> supplier) {
    return search(jql, null, parallelInputCount, parallelOutputCount, supplier);
  }

  /**
   * Searches for issues using a JQL query with additional fields.
   *
   * @param jql The JQL query string.
   * @param fields The fields to include in the search results.
   * @param parallelInputCount The number of parallel input threads.
   * @param parallelOutputCount The number of parallel output threads.
   * @param supplier A consumer to process the search results.
   * @return A set of issues matching the search criteria.
   */
  public static CSet<Issue> search(
      String jql,
      Set<String> fields,
      int parallelInputCount,
      int parallelOutputCount,
      Consumer<CSet<Issue>> supplier) {
    CSet<Issue> items = new CSet<>();

    CParallelIO<CSet<Issue>> parallelIO =
        new CParallelIO<>("Search Jira Items", parallelInputCount, parallelOutputCount);

    int maxResult = CJiraConfigs.Jira.getSearchBufferSize();
    AtomicInteger counter = new AtomicInteger(0);

    parallelIO.setInputExecutor(
        eof -> {
          int startAt = counter.getAndIncrement() * maxResult;
          CSet<Issue> search = search(jql, startAt, maxResult, fields);
          if (search.isEmpty()) {
            eof.set(true);
          } else {
            items.addAll(search);
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
      throw new CJiraClientException("Could not finish search.", t);
    }

    return items;
  }

  /**
   * Searches for issues using a JQL query with pagination.
   *
   * @param jql The JQL query string.
   * @param startAt The starting index of the results.
   * @param maxResults The maximum number of results to return.
   * @param fields The fields to include in the search results.
   * @return A set of issues matching the search criteria.
   */
  public static CSet<Issue> search(String jql, int startAt, int maxResults, Set<String> fields) {
    return doAction(
        restClient -> {
          SearchResult searchResult =
              CRetry.retry(
                  integer -> {
                    log.info(
                        "Search JQL '{}' with maxResults:{}, startAt:{}, fields:{}, attempt: {}",
                        jql,
                        maxResults,
                        startAt,
                        fields,
                        integer);
                    return restClient
                        .getSearchClient()
                        .searchJql(jql, maxResults, startAt, fields)
                        .claim();
                  },
                  5,
                  30000);
          return new CSet<>(searchResult.getIssues());
        });
  }

  /**
   * Executes an action with the Jira REST client.
   *
   * @param supplier The function to execute.
   * @param <R> The return type of the function.
   * @return The result of the function.
   */
  public static synchronized <R> R doAction(Function<JiraRestClient, R> supplier) {
    CSleeper.sleepTight(CJiraConfigs.Jira.getDelayBetweenCallsInMilliseconds());
    return supplier.apply(restClient.get());
  }
}
