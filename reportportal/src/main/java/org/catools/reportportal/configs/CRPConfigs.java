package org.catools.reportportal.configs;

import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CStringUtil;
import org.catools.reportportal.utils.CReportPortalAttributeUtil;

import java.util.Set;

public class CRPConfigs {
  public static boolean addPackageNameToMethodDescription() {
    return CHocon.get(Configs.RP_ADD_PACKAGE_NAME_TO_METHOD_DESCRIPTION).asBoolean(false);
  }

  public static boolean addClassNameToMethodDescription() {
    return CHocon.get(Configs.RP_ADD_CLASS_NAME_TO_METHOD_DESCRIPTION).asBoolean(true);
  }

  public static boolean isReportPortalEnable() {
    return CStringUtil.isNotBlank(getLaunchId());
  }

  public static String getLaunchId() {
    return System.getProperty("rp.launch.id");
  }

  public static Set<ItemAttributesRQ> getAttributes() {
    return CReportPortalAttributeUtil.getAttributes(System.getProperty("rp.attributes", ""));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    RP_ADD_PACKAGE_NAME_TO_METHOD_DESCRIPTION("catools.report_portal.add_package_name_to_method_description"),
    RP_ADD_CLASS_NAME_TO_METHOD_DESCRIPTION("catools.report_portal.add_class_name_to_method_description");

    private final String path;
  }
}
