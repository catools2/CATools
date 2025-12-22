package org.catools.atlassian.zapi.parser;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.Strings;
import org.catools.atlassian.zapi.exception.CZApiClientException;
import org.catools.atlassian.zapi.model.CZApiCycle;
import org.catools.atlassian.zapi.model.CZApiProjects;
import org.catools.atlassian.zapi.model.CZApiVersion;

/**
 * Utility class for parsing cycle data in the ZAPI system.
 *
 * <p>This class provides methods to parse a {@link CZApiCycle} object from a JSON response.
 */
@UtilityClass
public class CZApiCycleParser {

  /**
   * Parses a {@link CZApiCycle} object from the given projects and response.
   *
   * @param projects the collection of projects to retrieve project details
   * @param response the HTTP response containing the cycle data
   * @return the parsed {@link CZApiCycle} object
   * @throws CZApiClientException if the response cannot be parsed into a JSON object
   */
  public static CZApiCycle parse(CZApiProjects projects, Response response) {
    JsonPath json;

    try {
      // Extracts the JSON body from the response
      json = response.body().jsonPath();
      CZApiCycle cycle = new CZApiCycle();

      // Sets the cycle properties from the JSON data
      cycle.setId(json.getLong("id"));
      cycle.setProject(projects.getById(json.getLong("projectId")));
      cycle.setVersion(new CZApiVersion(json.getLong("versionId"), json.getString("versionName")));
      cycle.setDescription(json.getString("description"));
      cycle.setStartDate(CZApiBaseParser.getDate(json, "startDate"));
      cycle.setEndDate(CZApiBaseParser.getDate(json, "endDate"));
      cycle.setEnvironment(json.getString("environment"));
      cycle.setBuild(json.getString("get"));
      cycle.setName(json.getString("name"));
      cycle.setModifiedBy(Strings.CS.removeEnd(json.getString("modifiedBy"), "(Inactive)"));

      return cycle;
    } catch (Exception e) {
      // Throws an exception if parsing fails
      throw new CZApiClientException(
          "Could not parse input to JSON Array. input: " + response.body().asString(), e);
    }
  }
}
