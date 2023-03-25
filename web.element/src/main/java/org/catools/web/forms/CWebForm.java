package org.catools.web.forms;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.extensions.verify.CVerify;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.drivers.CDriver;
import org.slf4j.Logger;

@Slf4j
public abstract class CWebForm<DR extends CDriver> {
  protected static final int DEFAULT_TIMEOUT = CDriverConfigs.getTimeout();
  protected static final Logger logger = log;
  protected final CVerify verify = new CVerify();
  protected final DR driver;

  public CWebForm(DR driver) {
    this.driver = driver;
  }
}
