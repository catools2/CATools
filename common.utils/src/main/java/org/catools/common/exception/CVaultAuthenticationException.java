package org.catools.common.exception;


import com.bettercloud.vault.VaultException;
import org.catools.common.configs.CVaultConfigs;

public class CVaultAuthenticationException extends RuntimeException {
  public CVaultAuthenticationException(String authType) {
    super(String.format("Authentication type %s has not been implemented on client side. Host: %s.",
        authType,
        CVaultConfigs.getUrl()));
  }

  public CVaultAuthenticationException(String authType, VaultException e) {
    super(String.format("Failed to perform %s authentication against vault. Host: %s.",
        authType,
        CVaultConfigs.getUrl()), e);
  }
}
