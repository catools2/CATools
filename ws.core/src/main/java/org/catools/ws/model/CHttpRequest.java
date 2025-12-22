package org.catools.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.utils.CStringUtil;
import org.catools.ws.config.CProxyConfigs;
import org.catools.ws.enums.CHttpRequestType;
import org.catools.ws.rest.CFilterListener;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Data
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class CHttpRequest {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private CHttpRequestType requestType;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String target;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String path;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private boolean useProxy;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ContentType contentType;

  @JsonIgnore private final CMap<String, CFilterListener> filterListener = new CHashMap<>();

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final CList<MultiPartSpecification> multiParts = new CList<>();

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final CRequestQueryParameters queryParameter = new CRequestQueryParameters();

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final CRequestParameters formParameters = new CRequestParameters();

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final CRequestParameters parameters = new CRequestParameters();

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final CRequestHeaders headers = new CRequestHeaders();

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private boolean urlEncodingEnabled = true;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object entity;

  public CHttpRequest(CHttpRequest request) {
    this(request.getRequestType(), request.getTarget(), request.getPath(), request.useProxy);
    this.setContentType(request.getContentType());
    this.filterListener.putAll(request.getFilterListener());
    this.multiParts.addAll(request.getMultiParts());
    this.formParameters.putAll(request.getFormParameters());
    this.parameters.putAll(request.getParameters());
    this.headers.putAll(request.getHeaders());
    this.setUrlEncodingEnabled(request.isUrlEncodingEnabled());
    this.setEntity(request.getEntity());
  }

  public CHttpRequest(CHttpRequestType requestType, String target, boolean useProxy) {
    this(requestType, target, null, useProxy);
  }

  public CHttpRequest(CHttpRequestType requestType, String target, String path, boolean useProxy) {
    this.requestType = requestType;
    this.target = target;
    this.path = path;
    this.useProxy = useProxy;
  }

  public URI getUri() {
    UriBuilder uriBuilder = UriBuilder.fromPath(getTarget());
    if (getPath() != null) {
      uriBuilder.path(getPath());
    }
    return uriBuilder.build();
  }

  public boolean useProxy() {
    return useProxy;
  }

  public CHttpRequest addQueryParameter(String name, Object value) {
    queryParameter.put(name, value);
    return this;
  }

  public CHttpRequest addFormParameter(String name, Object value) {
    formParameters.put(name, value);
    return this;
  }

  public CHttpRequest addParameter(String name, Object value) {
    parameters.put(name, value);
    return this;
  }

  public CHttpRequest addHeader(String name, String value) {
    headers.put(name, value);
    return this;
  }

  public RequestSpecification toRequestSpecification(RestAssuredConfig config) {
    RequestSpecification requestSpecification = getRequestSpecification(config);

    if (CStringUtil.isNotBlank(getPath())) {
      requestSpecification.basePath(getPath());
    }

    getHeaders().forEach((n, v) -> requestSpecification.header(new Header(n, v)));

    getParameters().forEach(requestSpecification::param);

    getQueryParameter().forEach(requestSpecification::queryParam);

    getFormParameters().forEach(requestSpecification::formParam);

    getMultiParts().forEach(requestSpecification::multiPart);

    requestSpecification.urlEncodingEnabled(isUrlEncodingEnabled());

    if (getContentType() != null) {
      requestSpecification.contentType(getContentType());
    }

    if (getEntity() != null) {
      requestSpecification.body(getEntity().toString());
    }
    return requestSpecification;
  }

  private RequestSpecification getRequestSpecification(RestAssuredConfig config) {
    if (useProxy() && CProxyConfigs.isEnabled()) {
      log.debug("{} uses proxy {} for request processing", getTarget(), CProxyConfigs.getProxy());
      return RestAssured.given()
          .proxy(CProxyConfigs.getProxy())
          .config(config)
          .baseUri(getTarget());
    }

    return RestAssured.given().config(config).baseUri(getTarget());
  }
}
