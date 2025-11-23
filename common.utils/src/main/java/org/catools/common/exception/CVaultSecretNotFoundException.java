package org.catools.common.exception;

import org.catools.common.configs.CVaultConfigs;

public class CVaultSecretNotFoundException extends RuntimeException {
  public CVaultSecretNotFoundException(String secretName, String path) {
    super(String.format("Secret %s not found. Host: %s, Path: %s.", secretName, CVaultConfigs.getUrl(), path));
  }
}
