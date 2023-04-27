package org.catools.common.tests;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.extensions.verify.CVerify;
import org.slf4j.Logger;

@Slf4j
public class CBaseUnitTest {
  protected CVerify verify = new CVerify();

  public Logger getLogger() {
    return log;
  }
}
