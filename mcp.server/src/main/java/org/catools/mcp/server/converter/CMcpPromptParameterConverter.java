package org.catools.mcp.server.converter;

import org.catools.mcp.annotation.CMcpPromptParam;
import org.catools.mcp.util.CTypeConverter;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * This class is used to convert the value of a parameter annotated with {@link CMcpPromptParam} to
 * the required type.
 */
public class CMcpPromptParameterConverter implements CParameterConverter<CMcpPromptParam> {
  /**
   * Converts the value of the parameter annotated with {@link CMcpPromptParam} to the required type.
   *
   * @param parameter  the parameter annotated with {@link CMcpPromptParam}
   * @param annotation the annotation instance
   * @param args       the arguments passed to the method
   * @return the converted value of the parameter
   */
  @Override
  public Object convert(Parameter parameter, CMcpPromptParam annotation, Map<String, Object> args) {
    Object rawValue = args.get(annotation.name());
    return CTypeConverter.convert(rawValue, parameter.getType());
  }

  /**
   * Returns the type of the annotation this converter handles.
   *
   * @return the type of the annotation this converter handles
   */
  @Override
  public Class<CMcpPromptParam> getAnnotationType() {
    return CMcpPromptParam.class;
  }
}
