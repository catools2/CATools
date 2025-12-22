package org.catools.web.enums;

import lombok.Getter;

/**
 * Unified key mapping enum that provides cross-browser compatibility between Selenium and
 * Playwright. Each key constant maps to both Selenium Keys and Playwright key strings.
 *
 * <p>This enum allows writing browser-agnostic automation code that works with both Selenium
 * WebDriver and Playwright engines.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Use with CDriver or CWebElement
 * element.sendKeys(CKey.CONTROL, "a", CKey.DELETE);
 * element.sendKeys("text", CKey.ENTER);
 * element.sendKeys(CKey.ESCAPE);
 * }</pre>
 *
 * @author CATools Team
 * @since 2.0.0
 */
@Getter
public enum CKeys {
  // Control Keys
  ENTER('\n', '\uE007', "Enter"),
  TAB('\t', '\uE004', "Tab"),
  ESCAPE('\u001B', '\uE00C', "Escape"),
  BACKSPACE('\u0008', '\uE003', "Backspace"),
  DELETE('\uE017', '\uE017', "Delete"),
  SPACE(' ', '\uE00D', "Space"),

  // Navigation Keys
  PAGE_UP('\uE00E', '\uE00E', "PageUp"),
  PAGE_DOWN('\uE00F', '\uE00F', "PageDown"),
  END('\uE010', '\uE010', "End"),
  HOME('\uE011', '\uE011', "Home"),

  // Arrow Keys
  ARROW_LEFT('\uE012', '\uE012', "ArrowLeft"),
  ARROW_UP('\uE013', '\uE013', "ArrowUp"),
  ARROW_RIGHT('\uE014', '\uE014', "ArrowRight"),
  ARROW_DOWN('\uE015', '\uE015', "ArrowDown"),

  // Modifier Keys
  SHIFT('\uE008', '\uE008', "Shift"),
  CONTROL('\uE009', '\uE009', "Control"),
  ALT('\uE00A', '\uE00A', "Alt"),
  META('\uE03D', '\uE03D', "Meta"),
  COMMAND('\uE03D', '\uE03D', "Meta"), // Same as META on Mac

  // Function Keys
  F1('\uE031', '\uE031', "F1"),
  F2('\uE032', '\uE032', "F2"),
  F3('\uE033', '\uE033', "F3"),
  F4('\uE034', '\uE034', "F4"),
  F5('\uE035', '\uE035', "F5"),
  F6('\uE036', '\uE036', "F6"),
  F7('\uE037', '\uE037', "F7"),
  F8('\uE038', '\uE038', "F8"),
  F9('\uE039', '\uE039', "F9"),
  F10('\uE03A', '\uE03A', "F10"),
  F11('\uE03B', '\uE03B', "F11"),
  F12('\uE03C', '\uE03C', "F12"),

  // Special Keys
  INSERT('\uE016', '\uE016', "Insert"),
  HELP('\uE002', '\uE002', "Help"),
  CANCEL('\uE001', '\uE001', "Cancel"),
  CLEAR('\uE005', '\uE005', "Clear"),
  PAUSE('\uE00B', '\uE00B', "Pause"),

  // Numpad Keys
  NUMPAD0('\uE01A', '\uE01A', "Numpad0"),
  NUMPAD1('\uE01B', '\uE01B', "Numpad1"),
  NUMPAD2('\uE01C', '\uE01C', "Numpad2"),
  NUMPAD3('\uE01D', '\uE01D', "Numpad3"),
  NUMPAD4('\uE01E', '\uE01E', "Numpad4"),
  NUMPAD5('\uE01F', '\uE01F', "Numpad5"),
  NUMPAD6('\uE020', '\uE020', "Numpad6"),
  NUMPAD7('\uE021', '\uE021', "Numpad7"),
  NUMPAD8('\uE022', '\uE022', "Numpad8"),
  NUMPAD9('\uE023', '\uE023', "Numpad9"),
  MULTIPLY('\uE024', '\uE024', "NumpadMultiply"),
  ADD('\uE025', '\uE025', "NumpadAdd"),
  SEPARATOR('\uE026', '\uE026', "NumpadDecimal"),
  SUBTRACT('\uE027', '\uE027', "NumpadSubtract"),
  DECIMAL('\uE028', '\uE028', "NumpadDecimal"),
  DIVIDE('\uE029', '\uE029', "NumpadDivide"),

  // Special Characters (for consistency)
  SEMICOLON(';', '\uE018', "Semicolon"),
  EQUALS('=', '\uE019', "Equal");

  /** Unicode character representation of the key. Used for conversion to string when needed. */
  private final char unicode;

  /**
   * Selenium key string representation (WebDriver wire protocol format). Used when interacting with
   * Selenium WebDriver.
   */
  private final char seleniumKey;

  /** Playwright key string representation. Used when interacting with Playwright API. */
  private final String playwrightKey;

  /**
   * Constructor for CKey enum.
   *
   * @param unicode Unicode character representation
   * @param seleniumKey Selenium key string (WebDriver wire protocol format)
   * @param playwrightKey Playwright key string
   */
  CKeys(char unicode, char seleniumKey, String playwrightKey) {
    this.unicode = unicode;
    this.seleniumKey = seleniumKey;
    this.playwrightKey = playwrightKey;
  }

  /**
   * Converts CKey to Selenium key format.
   *
   * @return Selenium key string
   */
  public char toSeleniumKey() {
    return seleniumKey;
  }

  /**
   * Converts CKey to Playwright key format.
   *
   * @return Playwright key string
   */
  public String toPlaywrightKey() {
    return playwrightKey;
  }

  /**
   * Returns the string representation of this key.
   *
   * @return Unicode string representation
   */
  @Override
  public String toString() {
    return String.valueOf(unicode);
  }

  /**
   * Checks if this key is a modifier key (Shift, Control, Alt, Meta, Command).
   *
   * @return true if this is a modifier key
   */
  public boolean isModifier() {
    return this == SHIFT || this == CONTROL || this == ALT || this == META || this == COMMAND;
  }

  /**
   * Checks if this key is a function key (F1-F12).
   *
   * @return true if this is a function key
   */
  public boolean isFunctionKey() {
    return this.name().startsWith("F") && this.name().length() <= 3;
  }

  /**
   * Checks if this key is an arrow key.
   *
   * @return true if this is an arrow key
   */
  public boolean isArrowKey() {
    return this == ARROW_LEFT || this == ARROW_UP || this == ARROW_RIGHT || this == ARROW_DOWN;
  }

  /**
   * Checks if this key is a navigation key (Page Up/Down, Home, End).
   *
   * @return true if this is a navigation key
   */
  public boolean isNavigationKey() {
    return this == PAGE_UP || this == PAGE_DOWN || this == HOME || this == END;
  }

  /**
   * Converts a Selenium key string to CKey.
   *
   * @param seleniumKey Selenium key string (WebDriver wire protocol format)
   * @return corresponding CKey, or null if not found
   */
  public static CKeys fromSeleniumKey(CharSequence seleniumKey) {
    if (seleniumKey == null || seleniumKey.length() != 1) return null;

    char keyChar = seleniumKey.charAt(0);
    for (CKeys cKeys : values()) {
      if (keyChar == cKeys.seleniumKey) {
        return cKeys;
      }
    }
    return null;
  }

  /**
   * Converts a Playwright key string to CKey.
   *
   * @param playwrightKey Playwright key string
   * @return corresponding CKey, or null if not found
   */
  public static CKeys fromPlaywrightKey(String playwrightKey) {
    if (playwrightKey == null) return null;

    for (CKeys cKeys : values()) {
      if (cKeys.playwrightKey.equalsIgnoreCase(playwrightKey)) {
        return cKeys;
      }
    }
    return null;
  }
}
