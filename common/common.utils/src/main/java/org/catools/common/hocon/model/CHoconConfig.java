package org.catools.common.hocon.model;

import static org.catools.common.hocon.utils.CHoconUtils.SENSITIVE_PATH;
import static org.catools.common.hocon.utils.CHoconUtils.VALUE_PATH;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigRenderOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.configs.CVaultConfigs;
import org.catools.common.hocon.utils.CHoconUtils;
import org.catools.common.utils.CJsonUtil;
import org.catools.common.utils.CStringUtil;
import org.catools.common.vault.CVault;

@Slf4j
@NoArgsConstructor
public class CHoconConfig implements CConfig {
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
    return asT(defaultValue, Config::getString);
  }

  @Override
  public String asString() {
    return asString(CStringUtil.EMPTY);
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
    // In the second scenario we need to read and parse the string value and process
    // it.
    // 3- If the value is not defined in configuration then try to read value
    // from Environmental Variables or System Properties, considering that value
    // should parse as yaml
    // property so we try to read value as is and if conversion failed, then try
    // quoted value
    String vaultValue = getFromVault(path);
    if (StringUtils.isNotBlank(vaultValue)) {
      return parseAndApply(vaultValue, defaultValue, fuc);
    }

    if (isDefined() || isDefinedAsProperty()) {
      return getDefinedValue(fuc);
    }

    String value = readPropertyOrEnv(valuePath);
    if (StringUtils.isNotBlank(value)) {
      return parseAndApply(value, defaultValue, fuc);
    }

    return defaultValue;
  }

  private <T> T parseAndApply(String value, T defaultValue, BiFunction<Config, String, T> fuc) {
    try {
      return Optional.of(parseString(value)).map(c -> fuc.apply(c, VALUE)).orElse(defaultValue);
    } catch (ConfigException ignored) {
      return Optional.of(parseString(String.format("\"%s\"", value)))
          .map(c -> fuc.apply(c, VALUE))
          .orElse(defaultValue);
    }
  }

  public String getFromVault(String path) {
    if (ignoreVaultLookup(path)) {
      return null;
    }

    return CVault.getValue(valuePath);
  }

  private boolean ignoreVaultLookup(String key) {
    Config cfg = getConfig();

    // If vault configuration is not part of the loaded configuration then no need
    // to check for ignored keys patterns
    if (!cfg.hasPath("catools.vault.enabled") || !cfg.getBoolean("catools.vault.enabled")) {
      return true;
    }

    if (cfg.hasPath("ignored.conf.keys")) {
      List<String> ignoredKeys = cfg.getStringList("ignored.conf.keys");
      return ignoredKeys.stream().anyMatch(pattern -> key.matches(patternToRegex(pattern)));
    }

    return false;
  }

  private static String patternToRegex(String pattern) {
    return pattern.replace(".", "\\.").replace("*", ".*");
  }

  private <T> T getDefinedValue(BiFunction<Config, String, T> fuc) {
    try {
      return fuc.apply(config, valuePath);
    } catch (ConfigException ex) {
      return fuc.apply(parseString(), VALUE);
    }
  }

  private static <T> T getModelFromConfig(Class<T> clazz, Config val) {
    try {
      String jsonFormatString = val.resolve().root().render(ConfigRenderOptions.concise());
      T model = CJsonUtil.read(jsonFormatString, clazz);
      Objects.requireNonNull(model);
      return model;
    } catch (Exception ex) {
      return ConfigBeanFactory.create(val, clazz);
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
    return ConfigFactory.parseString("%s=%s".formatted(VALUE, input));
  }

  private static String readPropertyOrEnv(String property) {
    String key = convertToEnvVariable(property);
    return CHoconUtils.getProperty(key);
  }

  private static String convertToEnvVariable(final String property) {
    return property.toUpperCase().replaceAll("[^a-zA-Z0-9]+", "_");
  }
}
