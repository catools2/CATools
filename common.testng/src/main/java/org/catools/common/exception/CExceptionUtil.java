package org.catools.common.exception;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;

@UtilityClass
@Slf4j
public class CExceptionUtil {
  public static String getMessageAndStackTrace(Throwable t) {
    if (t == null) {
      return StringUtils.EMPTY;
    }
    return t + "\n\n" + getStackTrace(t);
  }

  public static String getStackTrace(Throwable t) {
    return t == null
        ? StringUtils.EMPTY
        : new CList<>(t.getStackTrace()).join(System.lineSeparator());
  }

  public static void printCurrentStackTrace() {
    log.trace(getCurrentStackTrace());
  }

  private static String getCurrentStackTrace() {
    return new CList<>(Thread.currentThread().getStackTrace()).join(System.lineSeparator());
  }
}
