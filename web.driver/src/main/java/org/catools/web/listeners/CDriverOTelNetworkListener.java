package org.catools.web.listeners;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.catools.common.otel.COTelConfig;
import org.catools.common.utils.CStringUtil;
import org.catools.web.config.CDriverConfigs;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v138.network.Network;
import org.openqa.selenium.devtools.v138.network.model.Request;
import org.openqa.selenium.devtools.v138.network.model.Response;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CDriverOTelNetworkListener {
  private final Map<String, Span> inflightSpans = new ConcurrentHashMap<>();
  private final Map<String, Instant> startTimes = new ConcurrentHashMap<>();
  private final Tracer tracer;
  private final Span parent;

  public CDriverOTelNetworkListener(String tracerName, String actionName) {
    this.tracer = COTelConfig.getOpenTelemetry().getTracer(tracerName);
    this.parent = tracer.spanBuilder(actionName)
        .setSpanKind(SpanKind.CLIENT)
        .startSpan();
  }

  public void attach(DevTools devTools) {
    if (!CDriverConfigs.isCollectNetworkMetricsEnable() || !COTelConfig.isEnable())
      return;

    devTools.addListener(Network.requestWillBeSent(), event -> {
      Request req = event.getRequest();
      try (Scope scope = parent.makeCurrent()) {
        // Any spans started here will be children of parent

        URI uri = URI.create(req.getUrl());

        Span span = tracer.spanBuilder("%s %s".formatted(req.getMethod(), uri.getHost()))
            .setSpanKind(SpanKind.CLIENT)
            .startSpan();

        inflightSpans.put(event.getRequestId().toString(), span);
        startTimes.put(event.getRequestId().toString(), Instant.now());

        addUriAttributes(span, uri);
        span.setAttribute("method", req.getMethod());
        req.getHeaders().forEach((k, v) -> span.setAttribute("header." + k, String.valueOf(v)));
      }
    });

    devTools.addListener(Network.responseReceived(), event -> {
      String id = event.getRequestId().toString();
      Span span = inflightSpans.remove(id);
      Instant start = startTimes.remove(id);

      if (span != null) {
        try (Scope scope = span.makeCurrent()) {
          Span child = tracer.spanBuilder("response")
              .setSpanKind(SpanKind.SERVER)
              .startSpan();

          // Duration
          if (start != null) {
            long latency = Instant.now().toEpochMilli() - start.toEpochMilli();
            child.setAttribute("latency_ms", latency);
          }

          Response res = event.getResponse();
          URI uri = URI.create(res.getUrl());
          addUriAttributes(child, uri);
          child.setAttribute("status", res.getStatus());
          child.setAttribute("mime", res.getMimeType());
          res.getProtocol().ifPresent(p -> child.setAttribute("protocol", p));
          res.getHeaders().forEach((k, v) -> child.setAttribute("header." + k, String.valueOf(v)));

          if (res.getStatus() >= 400) {
            child.setStatus(StatusCode.ERROR, "HTTP " + res.getStatus());
          }

          child.end();
        } finally {
          span.end();
        }
      }
    });

    // Cleanup in a case when request failed
    devTools.addListener(Network.loadingFailed(), fail -> {
      String id = fail.getRequestId().toString();
      Span span = inflightSpans.remove(id);
      startTimes.remove(id);

      if (span != null) {
        span.setStatus(StatusCode.ERROR, fail.getErrorText());
        span.setAttribute("error.type", "network");
        span.end();
      }
    });
  }

  public void detach() {
    if (!CDriverConfigs.isCollectNetworkMetricsEnable() || !COTelConfig.isEnable())
      return;

    parent.end();
  }

  private static void addUriAttributes(Span span, URI uri) {
    if (CStringUtil.isNotBlank(uri.getScheme()))
      span.setAttribute("schema", uri.getScheme());

    if (CStringUtil.isNotBlank(uri.getHost()))
      span.setAttribute("host", uri.getHost());

    if (CStringUtil.isNotBlank(uri.getPath()))
      span.setAttribute("path", uri.getPath());

    if (uri.getPort() > 0)
      span.setAttribute("port", uri.getPort());
  }
}
