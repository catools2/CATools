package org.catools.common.tests;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.extensions.verify.CVerify;

@Slf4j
public class CBaseUnitTest {

  protected CVerify verify = new CVerify();

  protected static Object[] getParams() {
    StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
    return new String[]{
        stackTrace
            .getClassName()
            .replace("org.catools.common.tests.verify.noretry.interfaces", "")
            .replace("org.catools.common.tests.wait.noretry.interfaces", ""),
        stackTrace.getMethodName()
    };
  }
}
