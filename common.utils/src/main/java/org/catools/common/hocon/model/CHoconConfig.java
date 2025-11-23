package org.catools.common.hocon.model;

import com.typesafe.config.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.utils.CHoconUtils;
import org.catools.common.utils.CJsonUtil;
import org.catools.common.vault.CVault;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.catools.common.hocon.utils.CHoconUtils.SENSITIVE_PATH;
import static org.catools.common.hocon.utils.CHoconUtils.VALUE_PATH;

@Slf4j
@NoArgsConstructor
public class CHoconConfig implements CConfig {
  private static final String PRINT_PATH_VALUE = "PRINT_PATH_VALUE";
  private static final String VALUE = "value";
  private Config config;

  @Getter
  private String name;
  private String valuePath;
  private String path;

  public CHoconConfig(Config config, String path) {
    this(config, path, path.toUpperCase().replaceAll("\\W", "_"));
  }

  public CHoconConfig(Config config, String path, String name) {
    this.config = config;
    this.path = path;
    this.name = name;

    this.valuePath = config.hasPath(path + VALUE_PATH) ? path + VALUE_PATH : path;
  }

  @Override
  public boolean isSensitive() {
    return config.hasPath(path + SENSITIVE_PATH) && config.getBoolean(path + SENSITIVE_PATH);
  }

  @Override
  public boolean isDefined() {
    try {
      return !getConfig().getIsNull(valuePath);
    } catch (ConfigException ex) {
      return false;
    }
  }

  @Override
  public boolean isDefinedAsProperty() {
    try {
      return !getConfig().getIsNull(convertToEnvVariable(valuePath));
    } catch (ConfigException ex) {
      return false;
    }
  }

  @Override
  public String asString(String defaultValue) {
    return asT(defaultValue, (c, path) -> c.getString(valuePath));
  }

  @Override
  public String asString() {
    return asString("");
  }

  @Override
  public List<String> asStrings(List<String> defaultValue) {
    return asT(defaultValue, Config::getStringList);
  }

  @Override
  public List<String> asStrings() {
    return asStrings(List.of());
  }

  @Override
  public Boolean asBoolean(Boolean defaultValue) {
    return asT(defaultValue, Config::getBoolean);
  }

  @Override
  public Boolean asBoolean() {
    return asBoolean(false);
  }

  @Override
  public List<Boolean> asBooleans(List<Boolean> defaultValue) {
    return asT(defaultValue, Config::getBooleanList);
  }

  @Override
  public List<Boolean> asBooleans() {
    return asBooleans(List.of());
  }

  @Override
  public Number asNumber(Number defaultValue) {
    return asT(defaultValue, Config::getNumber);
  }

  @Override
  public Number asNumber() {
    return asNumber(0);
  }

  @Override
  public List<Number> asNumbers(List<Number> defaultValue) {
    return asT(defaultValue, Config::getNumberList);
  }

  @Override
  public List<Number> asNumbers() {
    return asNumbers(List.of());
  }

  @Override
  public Integer asInteger(Integer defaultValue) {
    return asT(defaultValue, Config::getInt);
  }

  @Override
  public Integer asInteger() {
    return asInteger(0);
  }

  @Override
  public List<Integer> asIntegers(List<Integer> defaultValue) {
    return asT(defaultValue, Config::getIntList);
  }

  @Override
  public List<Integer> asIntegers() {
    return asIntegers(List.of());
  }

  @Override
  public Long asLong(Long defaultValue) {
    return asT(defaultValue, Config::getLong);
  }

  @Override
  public Long asLong() {
    return asLong(0L);
  }

  @Override
  public List<Long> asLongs(List<Long> defaultValue) {
    return asT(defaultValue, Config::getLongList);
  }

  @Override
  public List<Long> asLongs() {
    return asLongs(List.of());
  }

  @Override
  public Double asDouble(Double defaultValue) {
    return asT(defaultValue, Config::getDouble);
  }

  @Override
  public Double asDouble() {
    return asDouble(0D);
  }

  @Override
  public List<Double> asDoubles(List<Double> defaultValue) {
    return asT(defaultValue, Config::getDoubleList);
  }

  @Override
  public List<Double> asDoubles() {
    return asDoubles(List.of());
  }

  @Override
  public <T extends Enum<T>> T asEnum(Class<T> aClass) {
    return asEnum(aClass, null);
  }

  @Override
  public <T extends Enum<T>> T asEnum(Class<T> aClass, T defaultValue) {
    return asT(defaultValue, (c, path) -> c.getEnum(aClass, path));
  }

  @Override
  public <T extends Enum<T>> List<T> asEnums(Class<T> aClass) {
    return asEnums(aClass, List.of());
  }

  @Override
  public <T extends Enum<T>> List<T> asEnums(Class<T> aClass, List<T> defaultValue) {
    return asT(defaultValue, (c, path) -> c.getEnumList(aClass, path));
  }

  @Override
  public Object asObject() {
    return asObject(null);
  }

  @Override
  public Object asObject(Object defaultValue) {
    return asT(defaultValue, Config::getAnyRef);
  }

  @Override
  public List<?> asObjects() {
    return asObjects(List.of());
  }

  @Override
  public List<?> asObjects(List<Object> defaultValue) {
    return asT(defaultValue, Config::getAnyRefList);
  }

  /**
   * Read model using Type Safe Configuration implementation or Jackson
   *
   * @param clazz model class type
   * @param <T>   class Type
   * @return the model
   */
  public <T> List<T> asList(Class<T> clazz) {
    List<T> output = new ArrayList<>();
    List<? extends Config> configs = getConfig().getConfigList(this.valuePath);
    for (Config val : configs) {
      output.add(getModelFromConfig(clazz, val));
    }
    return output;
  }

  /**
   * Read model using Type Safe Configuration implementation or Jackson
   *
   * @param clazz model class type
   * @param <T>   class Type
   * @return the model
   */
  @Override
  public <T> T asModel(Class<T> clazz) {
    return asModel(clazz, null);
  }

  @Override
  public <T> T asModel(Class<T> clazz, T defaultValue) {
    try {
      return asT(defaultValue, (c, p) -> getModelFromConfig(clazz, c.getConfig(p)));
    } catch (ConfigException.WrongType ignored) {
      return asT(defaultValue, (c, p) -> getModelFromConfigs(clazz, c.getList(p)));
    }
  }

  private <T> T asT(T defaultValue, BiFunction<Config, String, T> fuc) {
    // If configuration defined then we might have 2 scenarios.
    // 1- Case when value setup directly in configuration.
    // 2- Case when value setup value using environmental variables.
    // In the second scenario we need to read and parse the string value and process it.
    // 3- If the value is not defined in configuration then try to read value
    // from Environmental Variables or System Properties, considering that value should parse as yaml
    // property so we try to read value as is and if conversion failed, then try quoted value
    if (isDefined()) {
      return getDefinedValue(fuc);
    }

    if (isDefinedAsProperty()) {
      return getDefinedPropertyValue(fuc);
    }

    // Try to read from environment variable or system property
    String value = readPropertyOrEnv(valuePath);
    if (StringUtils.isNotBlank(value)) {
      try {
        // Try to parse value as is in a case of complex structure like list or object
        return printPathValue(path, Optional.of(parseString(value)).map(c -> fuc.apply(c, VALUE)).orElse(defaultValue));
      } catch (ConfigException ignored) {
        // build valid json format for string value to read it properly
        return printPathValue(path, Optional.of(parseString(String.format("\"%s\"", value))).map(c -> fuc.apply(c, VALUE)).orElse(defaultValue));
      }
    }

    // If value from property is blank try to read from vault using path as is
    // If not found try to read using converted to env variable format
    // This is to support both key formats in vault
    String vaultValueByPath = CVault.getValue(valuePath);
    if (StringUtils.isNotBlank(vaultValueByPath)) {
      value = vaultValueByPath;
    } else {
      String vaultValueByProperty = CVault.getValue(convertToEnvVariable(valuePath));
      if (StringUtils.isNotBlank(vaultValueByProperty)) {
        value = vaultValueByProperty;
      }


      // If value is still blank return default value
      if (StringUtils.isBlank(value)) {
        return defaultValue;
      }
    }

    // set the value in config to avoid re-reading from env or vault next time
    config = config.withValue(valuePath, ConfigValueFactory.fromAnyRef(value));
    return getDefinedValue(fuc);
  }

  private <T> T getDefinedValue(BiFunction<Config, String, T> fuc) {
    try {
      return printPathValue(path, fuc.apply(config, valuePath));
    } catch (ConfigException ex) {
      return printPathValue(path, fuc.apply(parseString(), VALUE));
    }
  }

  private <T> T getDefinedPropertyValue(BiFunction<Config, String, T> fuc) {
    String property = convertToEnvVariable(path);
    try {
      return printPathValue(property, fuc.apply(config, valuePath));
    } catch (ConfigException ex) {
      return printPathValue(property, fuc.apply(parseString(), VALUE));
    }
  }

  private <T> T printPathValue(String path, T value) {
    String printPathValue = CHoconUtils.getProperty(PRINT_PATH_VALUE);
    if (StringUtils.isNoneBlank(printPathValue)) {
      log.debug("{} value is set to {}", path, value);
    }
    return value;
  }

  private static <T> T getModelFromConfig(Class<T> clazz, Config val) {
    try {
      return ConfigBeanFactory.create(val, clazz);
    } catch (Exception ex) {
      String jsonFormatString = val.resolve().root().render(ConfigRenderOptions.concise());
      return CJsonUtil.read(jsonFormatString, clazz);
    }
  }

  private static <T> T getModelFromConfigs(Class<T> clazz, ConfigList config) {
    String jsonFormatString = config.render(ConfigRenderOptions.concise());
    return CJsonUtil.read(jsonFormatString, clazz);
  }

  private Config getConfig() {
    return config;
  }

  private Config parseString() {
    return parseString(config.getString(valuePath));
  }

  private static Config parseString(String input) {
    return ConfigFactory.parseString(VALUE + " = " + input);
  }

  private static String readPropertyOrEnv(String property) {
    String key = convertToEnvVariable(property);
    return CHoconUtils.getProperty(key);
  }

  private static String convertToEnvVariable(final String property) {
    return property.toUpperCase().replaceAll("[^a-zA-Z0-9]+", "_");
  }
}
