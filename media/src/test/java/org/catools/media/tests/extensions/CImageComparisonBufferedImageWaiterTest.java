package org.catools.media.tests.extensions;

import org.catools.media.extensions.wait.interfaces.CImageComparisonWaiter;

public class CImageComparisonBufferedImageWaiterTest extends CBaseImageComparisonWaiterTest {

  @Override
  protected CImageComparisonWaiter toWaiter() {
    return () -> FROG_BUFFERED_IMAGE;
  }
}
