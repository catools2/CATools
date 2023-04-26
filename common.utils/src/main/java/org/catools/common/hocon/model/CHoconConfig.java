package org.catools.common.hocon.model;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigRenderOptions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.catools.common.utils.CJsonUtil;

import java.util.Collections;
import java.util.List;

import static org.catools.common.hocon.utils.CHoconUtils.SENSITIVE_PATH;
import static org.catools.common.hocon.utils.CHoconUtils.VALUE_PATH;

@NoArgsConstructor
public class CHoconConfig implements CConfig {
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

  public boolean isSensitive() {
    return config.hasPath(path + SENSITIVE_PATH) && config.getBoolean(path + SENSITIVE_PATH);
  }

  public boolean isDefined() {
    return !isNotDefined();
  }

  public boolean isNotDefined() {
    try {
      return getConfig().getIsNull(valuePath);
    }catch (ConfigException ex) {
      return true;
    }
  }

  public String asString() {
    return isNotDefined() ? "" : getConfig().getString(valuePath);
  }

  public Boolean asBoolean() {
    return isDefined() && getConfig().getBoolean(valuePath);
  }

  public Number asNumber() {
    return isNotDefined() ? null : getConfig().getNumber(valuePath);
  }

  public Integer asInteger() {
    return isNotDefined() ? 0 : getConfig().getInt(valuePath);
  }

  public Long asLong() {
    return isNotDefined() ? 0 : getConfig().getLong(valuePath);
  }

  public Double asDouble() {
    return isNotDefined() ? 0 : getConfig().getDouble(valuePath);
  }

  public <T extends Enum<T>> T asEnum(Class<T> aClass) {
    return isNotDefined() ? null : getConfig().getEnum(aClass, valuePath);
  }

  public Object asObject() {
    return isNotDefined() ? null : getConfig().getAnyRef(valuePath);
  }

  public List<Boolean> asBooleans() {
    return isNotDefined() ? Collections.EMPTY_LIST : getConfig().getBooleanList(valuePath);
  }

  public List<Number> asNumbers() {
    return isNotDefined() ? Collections.EMPTY_LIST : getConfig().getNumberList(valuePath);
  }

  public List<Integer> asIntegers() {
    return isNotDefined() ? Collections.EMPTY_LIST : getConfig().getIntList(valuePath);
  }

  public List<Long> asLongs() {
    return isNotDefined() ? Collections.EMPTY_LIST : getConfig().getLongList(valuePath);
  }

  public List<Double> asDoubles() {
    return isNotDefined() ? Collections.EMPTY_LIST : getConfig().getDoubleList(valuePath);
  }

  public List<String> asStrings() {
    return isNotDefined() ? Collections.EMPTY_LIST : getConfig().getStringList(valuePath);
  }

  public <T extends Enum<T>> List<T> asEnumList(Class<T> aClass) {
    return isNotDefined() ? Collections.EMPTY_LIST : getConfig().getEnumList(aClass, valuePath);
  }

  public List<? extends Object> asObjects() {
    return isNotDefined() ? Collections.EMPTY_LIST : getConfig().getAnyRefList(valuePath);
  }

  /**
   * Read model using Type Safe Configuration implementation or Jackson
   *
   * @param clazz model class type
   * @param <T>   class Type
   * @return the model
   */
  public <T> T asModel(Class<T> clazz) {
    try {
      return ConfigBeanFactory.create(getConfig(), clazz);
    } catch (Exception ex) {
      String jsonFormatString = getConfig().resolve().root().render(ConfigRenderOptions.concise());
      return CJsonUtil.read(jsonFormatString, clazz);
    }
  }

  private Config getConfig() {
    return config;
  }
}
