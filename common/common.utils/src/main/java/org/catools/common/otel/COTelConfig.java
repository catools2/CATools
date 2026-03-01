package org.catools.common.otel;

import static io.opentelemetry.semconv.ServiceAttributes.SERVICE_NAME;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

@UtilityClass
public class COTelConfig {

  private static OpenTelemetry openTelemetry;

  public static OpenTelemetry getOpenTelemetry() {
    if (openTelemetry == null) {
      buildOTel();
    }
    return openTelemetry;
  }

  /**
   * If true then send all tracking data to the endpoint
   *
   * @return true if otel data should be collected, otherwise it returns false
   */
  public static boolean isEnable() {
    return CHocon.asBoolean(Configs.CATOOLS_OTEL_ENABLE);
  }

  /**
   * The endpoint to send data to
   *
   * @return endpoint
   */
  public static String getEndpoint() {
    return CHocon.asString(Configs.CATOOLS_OTEL_ENDPOINT);
  }

  /**
   * The service name to use for otel
   *
   * @return service name
   */
  public static String getServiceName() {
    return CHocon.asString(Configs.CATOOLS_OTEL_SERVICE_NAME);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_OTEL_ENABLE("catools.otel.enable"),
    CATOOLS_OTEL_ENDPOINT("catools.otel.endpoint"),
    CATOOLS_OTEL_SERVICE_NAME("catools.otel.service_name");

    private final String path;
  }

  public static void buildOTel() {
    Resource resource =
        Resource.getDefault()
            .merge(
                Resource.create(
                    io.opentelemetry.api.common.Attributes.of(SERVICE_NAME, getServiceName())));

    OtlpGrpcSpanExporter exporter =
        OtlpGrpcSpanExporter.builder().setEndpoint(getEndpoint()).build();

    SdkTracerProvider tracerProvider =
        SdkTracerProvider.builder()
            .setResource(resource)
            .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
            .build();

    openTelemetry =
        OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).buildAndRegisterGlobal();
  }
}
