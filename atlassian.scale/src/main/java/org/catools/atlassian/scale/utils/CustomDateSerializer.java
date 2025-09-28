package org.catools.atlassian.scale.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.catools.atlassian.scale.configs.CZScaleConfigs;
import org.catools.common.collections.CList;
import org.catools.common.date.CDate;

import java.io.IOException;
import java.util.Date;

/**
 * Custom serializer for `Date` objects.
 *
 * <p>This class extends the Jackson `StdSerializer` to provide custom serialization
 * logic for `Date` objects. It uses a list of date formats defined in the `CZScaleConfigs`
 * to format the date strings.</p>
 */
public class CustomDateSerializer extends StdSerializer<Date> {
  /**
   * List of date formats used for formatting date strings.
   */
  private static final CList<String> dateFormats = CZScaleConfigs.Scale.getDateFormats();

  /**
   * Default constructor.
   *
   * <p>Initializes the serializer with no specific class type.</p>
   */
  public CustomDateSerializer() {
    this(null);
  }

  /**
   * Constructor with a specific class type.
   *
   * @param t the class type to serialize (can be null)
   */
  public CustomDateSerializer(Class<Date> t) {
    super(t);
  }

  /**
   * Serializes a `Date` object into a JSON string.
   *
   * <p>This method formats the `Date` object using the first date format
   * from the predefined list of date formats.</p>
   *
   * @param date the `Date` object to serialize
   * @param jsonGenerator the JSON generator
   * @param serializerProvider the serializer provider
   * @throws IOException if an error occurs during serialization
   */
  @Override
  public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeString(CDate.valueOf(date).toFormat(dateFormats.getFirst()));
  }
}