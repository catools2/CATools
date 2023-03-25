package org.catools.reportportal.utils;

import com.epam.reportportal.service.ReportPortal;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.Level;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.date.CDate;
import org.catools.common.io.CFile;
import org.catools.common.security.CSensitiveDataMaskingManager;
import org.catools.common.utils.CStringUtil;
import org.catools.reportportal.configs.CRPConfigs;

@UtilityClass
public class CReportPortalUtil {
  private static final CMap<Level, String> RP_LOG_LEVEL = new CHashMap<>();

  static {
    RP_LOG_LEVEL.put(Level.FATAL, "FATAL");
    RP_LOG_LEVEL.put(Level.ERROR, "ERROR");
    RP_LOG_LEVEL.put(Level.WARN, "WARN");
    RP_LOG_LEVEL.put(Level.INFO, "INFO");
    RP_LOG_LEVEL.put(Level.TRACE, "TRACE");
    RP_LOG_LEVEL.put(Level.DEBUG, "DEBUG");
  }

  public static void sendToReportPortal(Level level, String msg) {
    sendToReportPortal(level, msg, null);
  }

  public static void sendToReportPortal(Level level, String msg, CFile file) {
    if (CRPConfigs.isReportPortalEnable() && RP_LOG_LEVEL.containsKey(level)) {
      String message = CSensitiveDataMaskingManager.mask(msg);
      if (CStringUtil.isNotBlank(message) || file != null) {
        if (file == null) {
          ReportPortal.emitLog(message, RP_LOG_LEVEL.get(level), CDate.now());
        } else {
          ReportPortal.emitLog(message, RP_LOG_LEVEL.get(level), CDate.now(), file);
        }
      }
    }
  }
}
