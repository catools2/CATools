package org.catools.mcp.server.converter;

import java.lang.reflect.Parameter;
import java.util.Map;
import org.catools.mcp.annotation.CMcpToolParam;
import org.catools.mcp.util.CTypeConverter;

/**
 * This class is used to convert the value of a parameter annotated with {@link CMcpToolParam} to
 * the required type.
 */
public class CMcpToolParameterConverter implements CParameterConverter<CMcpToolParam> {
  /**
   * Converts the value of the parameter annotated with {@link CMcpToolParam} to the required type.
   *
   * @param parameter the parameter annotated with {@link CMcpToolParam}
   * @param annotation the annotation instance
   * @param args the arguments passed to the method
   * @return the converted value of the parameter
   */
  @Override
  public Object convert(Parameter parameter, CMcpToolParam annotation, Map<String, Object> args) {
    Object rawValue = args.get(annotation.name());
    return CTypeConverter.convert(rawValue, parameter.getType());
  }

  /**
   * Returns the type of the annotation this converter handles.
   *
   * @return the type of the annotation this converter handles
   */
  @Override
  public Class<CMcpToolParam> getAnnotationType() {
    return CMcpToolParam.class;
  }
}
