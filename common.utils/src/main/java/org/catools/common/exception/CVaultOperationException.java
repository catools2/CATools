package org.catools.common.exception;

import com.bettercloud.vault.VaultException;
import org.catools.common.configs.CVaultConfigs;

public class CVaultOperationException extends RuntimeException {
  public CVaultOperationException(String message) {
    super(message);
  }

  public CVaultOperationException(String operation, VaultException e) {
    super(String.format("Failed to perform %s operation against vault. Host: %s.",
        operation,
        CVaultConfigs.getUrl()), e);
  }

  public CVaultOperationException(String operation, String path, VaultException e) {
    super(String.format("Failed to perform %s operation against vault. Host: %s, Path: %s.",
        operation,
        CVaultConfigs.getUrl(),
        path), e);
  }
}
