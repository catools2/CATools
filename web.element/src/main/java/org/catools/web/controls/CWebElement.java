package org.catools.web.controls;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.extensions.types.CDynamicBooleanExtension;
import org.catools.common.extensions.types.CDynamicNumberExtension;
import org.catools.common.extensions.types.CDynamicStringExtension;
import org.catools.common.extensions.verify.CVerify;
import org.catools.media.model.CScreenShot;
import org.catools.web.drivers.CDriver;
import org.openqa.selenium.By;
import org.slf4j.Logger;

import java.awt.image.BufferedImage;

@Slf4j
public class CWebElement<DR extends CDriver> implements CWebElementActions<DR> {
  @Getter
  protected final Logger logger = log;

  @Getter
  protected final CVerify verify;

  @Getter
  protected final DR driver;

  @Getter
  protected final int waitSec;

  @Getter
  protected final String name;

  @Getter
  protected final By locator;

  @Getter
  protected final boolean useJS;

  public CWebElement(String name, DR driver, By locator) {
    this(name, driver, locator, CDriver.DEFAULT_TIMEOUT);
  }

  public CWebElement(String name, DR driver, By locator, boolean useJS) {
    this(name, driver, locator, useJS, CDriver.DEFAULT_TIMEOUT);
  }

  public CWebElement(String name, DR driver, By locator, int waitSec) {
    this(name, driver, locator, false, waitSec);
  }

  public CWebElement(String name, DR driver, By locator, boolean useJS, int waitSec) {
    super();
    this.name = name;
    this.driver = driver;
    this.locator = locator;
    this.waitSec = waitSec;
    this.useJS = useJS;
    this.verify = this.driver.getVerify();
  }

  // Control Extension
  public final CDynamicNumberExtension<Integer> Offset =
      new CDynamicNumberExtension<>() {
        @Override
        public Integer get() {
          return getOffset(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Offset";
        }
      };

  public final CDynamicBooleanExtension Staleness =
      new CDynamicBooleanExtension() {
        @Override
        public Boolean get() {
          return isStaleness(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Staleness";
        }
      };

  public final CDynamicBooleanExtension Present =
      new CDynamicBooleanExtension() {
        @Override
        public Boolean get() {
          return isPresent(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Presence";
        }
      };

  public final CDynamicBooleanExtension Visible =
      new CDynamicBooleanExtension() {
        @Override
        public Boolean get() {
          return isVisible(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Visibility";
        }
      };

  public final CDynamicBooleanExtension Enabled =
      new CDynamicBooleanExtension() {
        @Override
        public Boolean get() {
          return isEnabled(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Enable State";
        }
      };

  public final CDynamicStringExtension Text =
      new CDynamicStringExtension() {
        @Override
        public String get() {
          return getText(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Text";
        }
      };

  public final CDynamicStringExtension Value =
      new CDynamicStringExtension() {
        @Override
        public String get() {
          return getValue(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Value";
        }
      };

  public final CDynamicStringExtension InnerHTML =
      new CDynamicStringExtension() {
        @Override
        public String get() {
          return getInnerHTML(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Inner HTML";
        }
      };

  public final CDynamicStringExtension TagName =
      new CDynamicStringExtension() {
        @Override
        public String get() {
          return getTagName(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Tag Name";
        }
      };

  public final CDynamicStringExtension Css(final String cssKey) {
    return new CDynamicStringExtension() {
      @Override
      public String get() {
        return getCss(cssKey, 0);
      }

      @Override
      public int getDefaultWaitInSeconds() {
        return waitSec;
      }

      @Override
      public String getVerifyMessagePrefix() {
        return name + " " + cssKey + " CSS value";
      }
    };
  }

  public final CDynamicStringExtension Attribute(final String attribute) {
    return new CDynamicStringExtension() {
      @Override
      public String get() {
        return getAttribute(attribute, 0);
      }

      @Override
      public int getDefaultWaitInSeconds() {
        return waitSec;
      }

      @Override
      public String getVerifyMessagePrefix() {
        return name + " " + attribute + " Attribute value";
      }
    };
  }

  public final CScreenShot ScreenShot =
      new CScreenShot() {
        @Override
        public BufferedImage get() {
          return getScreenShot(0);
        }

        @Override
        public int getDefaultWaitIntervalInMilliSeconds() {
          return 50;
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return CDriver.DEFAULT_TIMEOUT;
        }

        @Override
        public boolean _useWaiter() {
          return true;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Screen Shot";
        }
      };

  public final CDynamicBooleanExtension Selected =
      new CDynamicBooleanExtension() {
        @Override
        public Boolean get() {
          return isSelected();
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Selected State";
        }
      };

  public final CDynamicBooleanExtension Clickable =
      new CDynamicBooleanExtension() {
        @Override
        public Boolean get() {
          return isClickable();
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return waitSec;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return name + " Clickable";
        }
      };
}
