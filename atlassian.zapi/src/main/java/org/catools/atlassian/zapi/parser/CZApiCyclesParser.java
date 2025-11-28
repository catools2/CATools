package org.catools.atlassian.zapi.parser;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.catools.atlassian.zapi.exception.CZApiClientException;
import org.catools.atlassian.zapi.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class for parsing cycle data in the ZAPI system.
 *
 * <p>This class provides methods to parse {@link CZApiCycles} objects from JSON responses.
 * It supports parsing cycles for a single project or multiple projects.</p>
 */
@UtilityClass
public class CZApiCyclesParser {

  /**
   * Parses a {@link CZApiCycles} object for a specific project from the given response.
   *
   * @param project the project for which cycles are being parsed
   * @param response the HTTP response containing the cycle data
   * @return the parsed {@link CZApiCycles} object
   * @throws CZApiClientException if the response cannot be parsed into a JSON object
   */
  public static CZApiCycles parse(CZApiProject project, Response response) {
    CZApiCycles output = new CZApiCycles();
    org.json.JSONObject input;

    try {
      // Converts the response body to a JSON object
      input = new JSONObject(response.body().asString());
      JSONArray names = input.names();
      for (int i = 0; i < names.length(); i++) {
        String key = names.getString(i);
        if (StringUtils.equalsIgnoreCase(key, "recordsCount")) {
          continue;
        }

        // Parses each cycle and adds it to the output
        JSONObject json = input.getJSONObject(key);
        if (project.getId() == json.optLong("projectId")) {
          output.add(parseCzApiCycle(project.setId(json.optLong("projectId")), key, json));
        }
      }
    } catch (Exception e) {
      // Throws an exception if parsing fails
      throw new CZApiClientException(
          "Could not parse input to JSON Array. input: " + response.body().asString(), e);
    }
    return output;
  }

  /**
   * Parses a {@link CZApiCycles} object for multiple projects from the given response.
   *
   * @param projects the collection of projects to retrieve project details
   * @param response the HTTP response containing the cycle data
   * @return the parsed {@link CZApiCycles} object
   * @throws CZApiClientException if the response cannot be parsed into a JSON object
   */
  public static CZApiCycles parse(CZApiProjects projects, Response response) {
    CZApiCycles output = new CZApiCycles();
    JSONObject input;

    try {
      // Converts the response body to a JSON object
      input = new JSONObject(response.body().asString());
      JSONArray names = input.names();
      for (int i = 0; i < names.length(); i++) {
        String key = names.getString(i);
        if (StringUtils.equalsIgnoreCase(key, "recordsCount")) {
          continue;
        }

        // Parses each cycle and adds it to the output
        JSONObject json = input.getJSONObject(key);
        output.add(parseCzApiCycle(projects.getById(json.optLong("projectId")), key, json));
      }
    } catch (Exception e) {
      // Throws an exception if parsing fails
      throw new CZApiClientException(
          "Could not parse input to JSON Array. input: " + response.body().asString(), e);
    }
    return output;
  }

  /**
   * Parses a single {@link CZApiCycle} object from the given project, key, and JSON data.
   *
   * @param project the project associated with the cycle
   * @param key the unique identifier of the cycle
   * @param json the JSON object containing the cycle data
   * @return the parsed {@link CZApiCycle} object
   */
  private static CZApiCycle parseCzApiCycle(CZApiProject project, String key, JSONObject json) {
    CZApiCycle cycle = new CZApiCycle();
    cycle.setId(Long.valueOf(key));
    cycle.setProject(project);
    cycle.setVersion(new CZApiVersion(json.optLong("versionId"), json.optString("versionName")));
    cycle.setDescription(json.optString("description"));
    cycle.setStartDate(CZApiBaseParser.getDate(json, "startDate"));
    cycle.setEndDate(CZApiBaseParser.getDate(json, "endDate"));
    cycle.setEnvironment(json.optString("environment"));
    cycle.setBuild(json.optString("build"));
    cycle.setName(json.optString("name"));
    cycle.setModifiedBy(Strings.CS.removeEnd(json.optString("modifiedBy"), "(Inactive)"));
    return cycle;
  }
}
