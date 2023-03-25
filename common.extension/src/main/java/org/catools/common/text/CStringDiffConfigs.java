package org.catools.common.text;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.fusesource.jansi.Ansi;

public class CStringDiffConfigs {
  public static int getDiffEditCost() {
    return CHocon.get(Configs.DIFF_TEXT_EDIT_COST).asInteger(2);
  }

  public static Ansi.Color getInsertColor() {
    return Ansi.Color.valueOf(
        CHocon.get(Configs.DIFF_TEXT_INSERT_COLOR).asString("YELLOW"));
  }

  public static Ansi.Color getDeleteColor() {
    return Ansi.Color.valueOf(
        CHocon.get(Configs.DIFF_TEXT_DELETE_COLOR).asString("RED"));
  }

  public static Ansi.Color getEqualColor() {
    return Ansi.Color.valueOf(
        CHocon.get(Configs.DIFF_TEXT_EQUAL_COLOR).asString("DEFAULT"));
  }

  public static String getInsertFormat() {
    return CHocon.get(Configs.DIFF_TEXT_INSERT_FORMAT).asString("|(+)%s|");
  }

  public static String getDeleteFormat() {
    return CHocon.get(Configs.DIFF_TEXT_DELETE_FORMAT).asString("|(-)%s|");
  }

  public static String getEqualFormat() {
    return CHocon.get(Configs.DIFF_TEXT_EQUAL_FORMAT).asString("%s");
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    DIFF_TEXT_EDIT_COST("catools.diff_text.edit_cost"),
    DIFF_TEXT_INSERT_FORMAT("catools.diff_text.insert_format"),
    DIFF_TEXT_DELETE_FORMAT("catools.diff_text.delete_format"),
    DIFF_TEXT_EQUAL_FORMAT("catools.diff_text.equal_format"),
    DIFF_TEXT_INSERT_COLOR("catools.diff_text.insert_color"),
    DIFF_TEXT_DELETE_COLOR("catools.diff_text.delete_color"),
    DIFF_TEXT_EQUAL_COLOR("catools.diff_text.equal_color");

    private final String path;
  }
}
