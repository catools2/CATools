package org.catools.ws.rest;

import org.catools.common.tests.CTest;
import org.catools.ws.enums.CHttpRequestType;
import org.catools.ws.model.CRequestParameters;

public abstract class CHttpRestFormClient<T extends CTest, O> extends CHttpClient<T, O> {

  public CHttpRestFormClient(
      T testInstance,
      CHttpRequestType requestType,
      String targetURL,
      CRequestParameters formParameters) {
    this(testInstance, requestType, targetURL, null, formParameters);
  }

  public CHttpRestFormClient(
      T testInstance,
      CHttpRequestType requestType,
      String targetURL,
      String targetPath,
      CRequestParameters formParameters) {
    super(testInstance, requestType, targetURL, targetPath);
    if (formParameters != null) {
      this.getRequest().getFormParameters().putAll(formParameters);
    }
  }
}
