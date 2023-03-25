package org.catools.ws.rest;

import io.restassured.http.ContentType;
import org.catools.common.tests.CTest;
import org.catools.ws.enums.CHttpRequestType;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class CHttpRestJsonClient<T extends CTest, O> extends CHttpRestClient<T, O> {

  public CHttpRestJsonClient(T testInstance, CHttpRequestType requestType, String targetURL) {
    this(testInstance, requestType, targetURL, null);
  }

  public CHttpRestJsonClient(
      T testInstance, CHttpRequestType requestType, String targetURL, String targetPath) {
    super(testInstance, requestType, targetURL, targetPath);
    this.getRequest().setContentType(ContentType.JSON);
  }

  public JSONObject getEntityAsJson() {
    return (JSONObject) getRequest().getEntity();
  }

  public JSONArray getEntityAsJsonArray() {
    return (JSONArray) getRequest().getEntity();
  }
}
