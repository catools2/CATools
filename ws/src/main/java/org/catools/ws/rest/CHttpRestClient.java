package org.catools.ws.rest;

import org.catools.common.tests.CTest;
import org.catools.ws.enums.CHttpRequestType;

public abstract class CHttpRestClient<T extends CTest, O> extends CHttpClient<T, O> {
  public CHttpRestClient(T testInstance, CHttpRequestType requestType, String targetURI) {
    super(testInstance, requestType, targetURI);
  }

  public CHttpRestClient(
      T testInstance, CHttpRequestType requestType, String targetURI, String targetPath) {
    super(testInstance, requestType, targetURI, targetPath);
  }
}
