package org.catools.mcp.server.converter;

import org.catools.mcp.util.CTypeConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This interface is used to convert the value of a parameter annotated with {@link A} to the
 * required type.
 *
 * @param <A> the type of the annotation
 */
public interface CParameterConverter<A extends Annotation> {
  /**
   * Converts the value of the specified parameter annotated with the specified annotation to the
   * required type.
   *
   * @param parameter  the parameter to convert
   * @param annotation the annotation that annotates the parameter
   * @param args       the arguments passed to the method
   * @return the converted value of the parameter
   */
  Object convert(Parameter parameter, A annotation, Map<String, Object> args);

  /**
   * Returns the type of the annotation that this converter supports.
   *
   * @return the type of the annotation that this converter supports
   */
  Class<A> getAnnotationType();


  /**
   * Converts the values of all parameters annotated with the specified annotation to the required
   * types.
   *
   * @param methodParameters the parameters of the method
   * @param args             the arguments passed to the method
   * @return the converted values of all parameters
   */
  default List<Object> convertAll(Parameter[] methodParameters, Map<String, Object> args) {
    List<Object> convertedParameters = new ArrayList<>(methodParameters.length);

    for (Parameter param : methodParameters) {
      A annotation = param.getAnnotation(getAnnotationType());
      Object converted;
      // Fill in a default value when the parameter is not specified or unannotated
      // to ensure that the parameter type is correct when calling method.invoke()
      if (annotation == null) {
        converted = CTypeConverter.convert(null, param.getType());
      } else {
        converted = convert(param, annotation, args);
      }
      convertedParameters.add(converted);
    }

    return convertedParameters;
  }
}
