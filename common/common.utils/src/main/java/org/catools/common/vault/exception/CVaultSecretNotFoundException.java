package org.catools.common.vault.exception;

import org.catools.common.vault.configs.CVaultConfigs;

public class CVaultSecretNotFoundException extends RuntimeException {
  public CVaultSecretNotFoundException(String secretName, String path) {
    super(
        String.format(
            "Secret %s not found. Host: %s, Path: %s.", secretName, CVaultConfigs.getUrl(), path));
  }
}
