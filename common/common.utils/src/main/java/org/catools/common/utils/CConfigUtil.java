package org.catools.common.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.utils.CHoconUtils;

@UtilityClass
/** */
public class CConfigUtil {
  public static String getRunName() {
    return StringUtils.defaultString(CHoconUtils.getProperty("RUN_NAME"));
  }

  public static void setRunName(String value) {
    setProperty("RUN_NAME", value);
  }

  public static <T extends Enum<T>> void setProperty(T config, String value) {
    setProperty(config.name(), value);
  }

  public static void setProperty(String config, String value) {
    System.setProperty(config, StringUtils.defaultString(value));
  }
}
