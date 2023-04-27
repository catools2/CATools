package org.catools.common.tests.verify.wait;

import org.catools.common.extensions.verify.CBooleanVerification;

import java.util.function.Consumer;

public class CNumberWaitTest extends CNumberBaseWaitTest {

  @Override
  public void verify(Consumer<CBooleanVerification> action) {
    action.accept(verify.Bool);
  }
}
