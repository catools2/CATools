package org.catools.vault.model;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.api.pki.Credential;
import com.bettercloud.vault.api.pki.CredentialFormat;
import com.bettercloud.vault.response.LogicalResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.security.CSensitiveDataMaskingManager;
import org.catools.vault.configs.CVaultConfigs;
import org.catools.vault.exception.CVaultAuthenticationException;
import org.catools.vault.exception.CVaultOperationException;
import org.catools.vault.exception.CVaultSecretNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.catools.vault.configs.CVaultConfigs.getVaultBaseConfig;

@Slf4j
public class CVault {
  private Map<String, String> kVData;

  private final Vault vault;
  private final String path;

  public CVault() {
    this(new Vault(getVaultBaseConfig().token(getAuthClientToken())), CVaultConfigs.getPath());
  }

  public CVault(String path) {
    this(new Vault(getVaultBaseConfig().token(getAuthClientToken())), path);
  }

  public CVault(Vault vault, String path) {
    this.vault = vault;
    this.path = path;
  }

  public String getValue(String key) {
    String value = getValue(key, null);
    if (value == null) {
      throw new CVaultSecretNotFoundException(getFullKey(key));
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

  public Credential issuePKI(String pkiMountPath,
                             String roleName,
                             String commonName,
                             List<String> altNames,
                             List<String> ipSans,
                             String ttl,
                             CredentialFormat format) {
    try {
      return vault.pki(pkiMountPath).issue(roleName, commonName, altNames, ipSans, ttl, format).getCredential();
    }
    catch (VaultException e) {
      throw new CVaultOperationException("Read", e);
    }
  }

  public LogicalResponse deleteKey(String path) {
    try {
      return vault.logical().delete(path);
    }
    catch (VaultException e) {
      throw new CVaultOperationException("Delete Key", e);
    }
  }

  private static String getAuthClientToken() {
    CVaultAuthType authType = CVaultConfigs.getAuthType();
    Objects.requireNonNull(authType);

    try {
      return switch (authType) {
        case TOKEN -> CVaultConfigs.getAuthToken();
        case LDAP -> new Vault(getVaultBaseConfig()).auth()
            .loginByLDAP(CVaultConfigs.getAuthLdapUsername(), CVaultConfigs.getAuthLdapPassword())
            .getAuthClientToken();
        case APP_ROLE -> new Vault(getVaultBaseConfig()).auth()
            .loginByAppRole(CVaultConfigs.getAuthAppRoleRoleId(), CVaultConfigs.getAuthAppRoleSecretId())
            .getAuthClientToken();
        default -> throw new CVaultAuthenticationException(authType.name());
      };
    }
    catch (VaultException e) {
      throw new CVaultOperationException("Failed to generate vault client authorization token.", e);
    }
  }

  /**
   * User CATOOLS_VAULT_KEY_PREFIX to differentiate group of keys on the same path
   */
  private static String getFullKey(String key) {
    return StringUtils.isBlank(key) ? "" : CVaultConfigs.getKeyPrefix() + key;
  }
}
