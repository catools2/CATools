package org.catools.atlassian.scale.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.catools.atlassian.scale.configs.CZScaleConfigs;
import org.catools.common.collections.CList;
import org.catools.common.date.CDate;

import java.io.IOException;
import java.util.Date;

/**
 * Custom deserializer for `Date` objects.
 *
 * <p>This class extends the Jackson `StdDeserializer` to provide custom deserialization
 * logic for `Date` objects. It uses a list of date formats defined in the `CZScaleConfigs`
 * to parse the date strings.</p>
 */
public class CustomDateDeserializer extends StdDeserializer<Date> {
  /**
   * List of date formats used for parsing date strings.
   */
  private static final CList<String> dateFormats = CZScaleConfigs.Scale.getDateFormats();

  /**
   * Default constructor.
   *
   * <p>Initializes the deserializer with no specific class type.</p>
   */
  public CustomDateDeserializer() {
    this(null);
  }

  /**
   * Constructor with a specific class type.
   *
   * @param vc the class type to deserialize (can be null)
   */
  public CustomDateDeserializer(Class<?> vc) {
    super(vc);
  }

  /**
   * Deserializes a JSON string into a `Date` object.
   *
   * <p>This method parses the date string from the JSON input using the predefined
   * list of date formats.</p>
   *
   * @param jsonparser the JSON parser
   * @param context the deserialization context
   * @return the deserialized `Date` object
   * @throws IOException if an error occurs during deserialization
   */
  @Override
  public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
    String date = jsonparser.getText();
    return CDate.valueOf(date, dateFormats.toArray(new String[dateFormats.size()]));
  }
}