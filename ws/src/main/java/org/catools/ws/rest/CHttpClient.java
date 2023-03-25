package org.catools.ws.rest;

import io.restassured.config.RestAssuredConfig;
import io.restassured.internal.print.RequestPrinter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.extensions.verify.CVerifier;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.tests.CTest;
import org.catools.ws.enums.CHttpRequestType;
import org.catools.ws.enums.CHttpStatusCode;
import org.catools.ws.model.CHttpRequest;
import org.catools.ws.model.CHttpResponse;
import org.catools.ws.utils.CRestAssuredUtil;

import java.io.PrintStream;
import java.util.Map;
import java.util.function.BiConsumer;

import static io.restassured.filter.log.LogDetail.ALL;

@Getter
@Setter
@Slf4j
@Accessors(chain = true)
public abstract class CHttpClient<T extends CTest, O> {
  protected final CVerify verify;
  protected final T testInstance;

  private final CHttpRequest request;

  public CHttpClient(T testInstance, CHttpRequestType requestType, String targetURI) {
    this(testInstance, requestType, targetURI, null);
  }

  public CHttpClient(
      T testInstance, CHttpRequestType requestType, String targetURI, String targetPath) {
    this.request = new CHttpRequest(requestType, targetURI, targetPath);
    this.testInstance = testInstance;
    this.verify = testInstance.verify;
  }

  public <R extends CHttpClient<T, O>> R setEntity(Object obj) {
    this.request.setEntity(obj);
    return (R) this;
  }

  public CHttpResponse send() {
    CHttpResponse response =
        CRestAssuredUtil.send(getConfig(), beforeCall(request), getRequestLoggerFilterListener());
    log.info("Response << {}", response);
    return response;
  }

  public CHttpResponse send(CHttpStatusCode expectedCode) {
    return send(expectedCode, (BiConsumer) null);
  }

  public CHttpResponse send(
      CHttpStatusCode expectedCode, BiConsumer<CHttpResponse, CVerifier> afterCall) {
    CHttpResponse response = send();
    CVerifier verifier = new CVerifier();
    response.StatusCode.verifyEquals(verifier, expectedCode);
    if (afterCall != null) {
      afterCall.accept(response, verifier);
    }
    verifier.verify();
    return response;
  }

  public CHttpResponse send(CHttpStatusCode expectedCode, String entityKey, Object entityValue) {
    return send(expectedCode, entityKey, entityValue, null);
  }

  public CHttpResponse send(
      CHttpStatusCode expectedCode,
      String entityKey,
      Object entityValue,
      BiConsumer<CHttpResponse, CVerifier> afterCall) {
    return send(expectedCode, new CList<>(Map.entry(entityKey, entityValue)), afterCall);
  }

  public CHttpResponse send(
      CHttpStatusCode expectedCode, CList<Map.Entry<String, Object>> expectedEntityValues) {
    return send(expectedCode, expectedEntityValues, null);
  }

  public CHttpResponse send(
      CHttpStatusCode expectedCode,
      CList<Map.Entry<String, Object>> expectedEntityValues,
      BiConsumer<CHttpResponse, CVerifier> afterCall) {
    CHttpResponse response = send();
    CVerifier verifier = new CVerifier();
    response.StatusCode.verifyEquals(verifier, expectedCode);
    CSet<Map.Entry<String, Object>> entrySet = new CSet<>(response.ContentMap.entrySet());
    for (Map.Entry<String, Object> entityValue : expectedEntityValues) {
      entrySet.verifyContains(verifier, entityValue, "ContentMap matches expected value.");
    }

    if (afterCall != null) {
      afterCall.accept(response, verifier);
    }
    verifier.verify();
    return response;
  }

  protected CHttpRequest beforeCall(CHttpRequest request) {
    return request;
  }

  protected RestAssuredConfig getConfig() {
    return RestAssuredConfig.newConfig();
  }

  private CFilterListener getRequestLoggerFilterListener() {
    return (reqSpec, responseSpec, ctx) -> {
      log.info(
          "Request >> {}",
          RequestPrinter.print(
              reqSpec,
              reqSpec.getMethod(),
              reqSpec.getURI(),
              ALL,
              new PrintStream(PrintStream.nullOutputStream()),
              true));
    };
  }

  public <C extends CHttpClient<T, O>> C setUrlEncodingEnabled(boolean urlEncodingEnabled) {
    this.getRequest().setUrlEncodingEnabled(urlEncodingEnabled);
    return (C) this;
  }

  public abstract O process();
}
