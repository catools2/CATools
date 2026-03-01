package org.catools.athena.scale.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import lombok.Data;
import lombok.experimental.Accessors;
import org.catools.common.collections.CSet;

@Data
@Accessors(chain = true)
public class CTestExecutionInfo {
  private CSet<String> unExecutedFromLastCycleNameWithSqlLikePatterns;
  private CSet<String> unPassedFromLastCycleNameWithSqlLikePatterns;
  private CSet<String> unExecutedFromCycleIds;
  private CSet<String> unPassedFromCycleIds;
  private CSet<String> excludeQualifiedMatches;
  private CSet<String> excludeIssueKeys;
  private CSet<String> includeQualifiedMatches;
  private CSet<String> includeIssueKeys;
  private String includeQuery;
  private String excludeQuery;
  private boolean testShouldHaveId = true;

  @JsonIgnore
  public boolean isShouldSyncJira() {
    return (unExecutedFromLastCycleNameWithSqlLikePatterns != null
            && unExecutedFromLastCycleNameWithSqlLikePatterns.isNotEmpty())
        || (unPassedFromLastCycleNameWithSqlLikePatterns != null
            && unPassedFromLastCycleNameWithSqlLikePatterns.isNotEmpty())
        || (unExecutedFromCycleIds != null && unExecutedFromCycleIds.isNotEmpty())
        || (unPassedFromCycleIds != null && unPassedFromCycleIds.isNotEmpty());
  }

  /**
   * Adds name patterns to filter test cases that were unexecuted in the last cycles. The patterns
   * is SQL like pattern (*).
   *
   * @param patterns
   */
  public void addUnExecutedFromLasCycleNameWithSqlLikePatterns(String... patterns) {
    if (unExecutedFromLastCycleNameWithSqlLikePatterns == null) {
      unExecutedFromLastCycleNameWithSqlLikePatterns = new CSet<>();
    }
    unExecutedFromLastCycleNameWithSqlLikePatterns.addAll(Arrays.asList(patterns));
  }

  /**
   * Adds name patterns to filter test cases that were unpassed in the last cycles. The patterns can
   * include wildcards (*).
   *
   * @param patterns
   */
  public void addUnPassedFromLasCycleNameWithSqlLikePatterns(String... patterns) {
    if (unPassedFromLastCycleNameWithSqlLikePatterns == null) {
      unPassedFromLastCycleNameWithSqlLikePatterns = new CSet<>();
    }
    unPassedFromLastCycleNameWithSqlLikePatterns.addAll(Arrays.asList(patterns));
  }

  public void addUnExecutedFromCycleIds(String... cycleIds) {
    if (unExecutedFromCycleIds == null) {
      unExecutedFromCycleIds = new CSet<>();
    }
    unExecutedFromCycleIds.addAll(Arrays.asList(cycleIds));
  }

  public void addUnPassedFromCycleIds(String... cycleIds) {
    if (unPassedFromCycleIds == null) {
      unPassedFromCycleIds = new CSet<>();
    }
    unPassedFromCycleIds.addAll(Arrays.asList(cycleIds));
  }

  public void addExcludeQualifiedMatches(String... matches) {
    if (excludeQualifiedMatches == null) {
      excludeQualifiedMatches = new CSet<>();
    }
    excludeQualifiedMatches.addAll(Arrays.asList(matches));
  }

  public void addExcludeIssueKeys(String... issueKeys) {
    if (excludeIssueKeys == null) {
      excludeIssueKeys = new CSet<>();
    }
    excludeIssueKeys.addAll(Arrays.asList(issueKeys));
  }

  public void addIncludeQualifiedMatches(String... matches) {
    if (includeQualifiedMatches == null) {
      includeQualifiedMatches = new CSet<>();
    }
    includeQualifiedMatches.addAll(Arrays.asList(matches));
  }

  public void addIncludeIssueKeys(String... issueKeys) {
    if (includeIssueKeys == null) {
      includeIssueKeys = new CSet<>();
    }
    includeIssueKeys.addAll(Arrays.asList(issueKeys));
  }
}
