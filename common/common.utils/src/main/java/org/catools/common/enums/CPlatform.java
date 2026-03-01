package org.catools.common.enums;

import java.util.EnumSet;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/** Enum for common platforms. */
public enum CPlatform {
  WINDOWS("^.*win.*$"),
  LINUX("^.*(nix|nux|aix).*$"),
  MAC("^.*mac.*$");

  private Pattern namePattern;

  CPlatform(String nameRegEx) {
    this.namePattern = Pattern.compile(nameRegEx);
  }

  /**
   * Get platform from name.
   *
   * @param input the input
   * @return the platform
   */
  public static CPlatform fromName(String input) {
    return EnumSet.allOf(CPlatform.class).stream()
        .filter(v -> v.namePattern.matcher(StringUtils.defaultString(input).toLowerCase()).find())
        .findFirst()
        .orElse(null);
  }

  /**
   * True if linux else false.
   *
   * @return the boolean
   */
  public boolean isLinux() {
    return this.equals(LINUX);
  }

  /**
   * True if mac else false.
   *
   * @return the boolean
   */
  public boolean isMac() {
    return this.equals(MAC);
  }

  /**
   * True if windows else false.
   *
   * @return the boolean
   */
  public boolean isWindows() {
    return this.equals(WINDOWS);
  }
}
