package org.catools.atlassian.scale;

import org.catools.atlassian.scale.rest.cycle.CZScaleTestRunClient;
import org.catools.atlassian.scale.rest.testcase.CZScaleTestCaseClient;

/**
 * Main client for interacting with the Scale system.
 *
 * <p>This class provides static instances of clients for managing test runs and test cases
 * in the Scale system. It acts as an entry point for accessing these clients.</p>
 */
public class CZScaleClient {

  /**
   * Static instance of the test run client.
   *
   * <p>Provides methods to interact with the Scale system's test run API.</p>
   */
  public static final CZScaleTestRunClient TestRuns = new CZScaleTestRunClient();

  /**
   * Static instance of the test case client.
   *
   * <p>Provides methods to interact with the Scale system's test case API.</p>
   */
  public static final CZScaleTestCaseClient TestCases = new CZScaleTestCaseClient();
}