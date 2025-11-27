package org.catools.common.vault;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.configs.CVaultConfigs;
import org.catools.common.exception.CVaultOperationException;
import org.catools.common.exception.CVaultSecretNotFoundException;
import org.catools.common.security.CSensitiveDataMaskingManager;
import org.catools.common.utils.CStringUtil;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class CVaultClient {
  private Map<String, String> kVData;

  private final Vault vault;
  private final String path;

  public CVaultClient(Vault vault, String path) {
    this.vault = vault;
    this.path = path;
  }

  public String getValue(String key) {
    String value = getValue(key, null);
    if (value == null) {
      throw new CVaultSecretNotFoundException(getFullKey(key), path);
    }
    CSensitiveDataMaskingManager.addMask(value);
    return value;
  }

  public String getValue(String key, String defaultTo) {
    if (StringUtils.isBlank(key)) {
      return null;
    }

    String value = getData().get(getFullKey(key));
    return value == null ? defaultTo : value;
  }

  public Map<String, String> getData() {
    if (!Objects.isNull(kVData)) {
      return kVData;
    }

    try {
      kVData = vault.logical().read(path).getData();
    } catch (VaultException e) {
      throw new CVaultOperationException("Read", e);
    }

    return kVData;
  }

  /**
   * User CATOOLS_VAULT_KEY_PREFIX to differentiate group of keys on the same path
   */
  private static String getFullKey(String key) {
    return StringUtils.isBlank(key) ? CStringUtil.EMPTY : CVaultConfigs.getKeyPrefix() + key;
  }
}
