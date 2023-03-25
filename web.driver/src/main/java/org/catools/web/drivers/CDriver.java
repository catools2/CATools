package org.catools.web.drivers;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.concurrent.CTimeBoxRunner;
import org.catools.common.date.CDate;
import org.catools.common.extensions.types.CDynamicStringExtension;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.utils.CRetry;
import org.catools.media.model.CScreenShot;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.enums.CBrowser;
import org.slf4j.Logger;

import java.awt.image.BufferedImage;
import java.util.function.Predicate;

@Slf4j
public class CDriver implements CDriverActions, CDriverNavigation {
  public static final int DEFAULT_TIMEOUT = CDriverConfigs.getTimeout();

  @Getter
  private final CDriverSession driverSession;

  @Getter
  private final Logger logger = log;

  @Getter
  private final CVerify verify = new CVerify();

  @Getter
  private final boolean useJs = CDriverConfigs.useJS();

  public CDriver(CDriver driver) {
    this(driver.driverSession);
  }

  public CDriver(CDriverSession driverSession) {
    this.driverSession = driverSession;
  }

  public final CDynamicStringExtension Title =
      new CDynamicStringExtension() {
        @Override
        public String get() {
          return getTitle();
        }

        @Override
        public int getDefaultWaitIntervalInMilliSeconds() {
          return 50;
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return DEFAULT_TIMEOUT;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return " Page Title";
        }
      };

  public final CDynamicStringExtension Url =
      new CDynamicStringExtension() {
        @Override
        public String get() {
          return getUrl();
        }

        @Override
        public int getDefaultWaitIntervalInMilliSeconds() {
          return 50;
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return DEFAULT_TIMEOUT;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return " Page Url";
        }
      };

  public final CScreenShot ScreenShot =
      new CScreenShot() {
        @Override
        public boolean _useWaiter() {
          return true;
        }

        @Override
        public BufferedImage get() {
          return getScreenShot();
        }

        @Override
        public int getDefaultWaitIntervalInMilliSeconds() {
          return 50;
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return DEFAULT_TIMEOUT;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return " Page Screenshot";
        }
      };

  public <DR extends CDriver, A extends CWebAlert<DR>> A getAlert() {
    return (A) new CWebAlert<>(this);
  }

  public CBrowser getBrowser() {
    return driverSession == null ? null : driverSession.getDriverProvider().getBrowser();
  }

  public CDriver startSession() {
    quit();
    driverSession.startSession();
    return this;
  }

  public void quit() {
    performActionOnDriver(
        "Quit",
        webDriver -> {
          if (webDriver != null) {
            try {
              CTimeBoxRunner.get(
                  () -> {
                    try {
                      getAlert().closeIfPresent(true, 1);
                    } catch (Throwable t) {
                      logger.trace("Failed to close alert");
                    }
                    try {
                      webDriver.close();
                    } catch (Throwable t) {
                      logger.trace("Failed to close webdriver");
                    }
                    try {
                      webDriver.quit();
                    } catch (Throwable t) {
                      logger.trace("Failed to quit webdriver");
                    }
                    return true;
                  },
                  10);
            } catch (Throwable ex) {
              logger.trace("Failed to quit driver");
            }
            driverSession.reset();
          }
          return true;
        });
  }

  public final CDriver refresh() {
    return performActionOnDriver(
        "Refresh",
        webDriver -> {
          webDriver.navigate().refresh();
          return this;
        });
  }

  public final CDriver refresh(Predicate<CDriver> postCondition) {
    return refresh(postCondition, 3, 1000);
  }

  public final CDriver refresh(
      Predicate<CDriver> postCondition, int retryTimes, int intervalInSeconds) {
    CRetry.retryIfNot(integer -> refresh(), postCondition, retryTimes, intervalInSeconds);
    return this;
  }

  public BufferedImage getScreenShot() {
    return performActionOnDriver(
        "Get Screenshot",
        webDriver -> {
          if (webDriver == null) {
            return null;
          }
          setCaretColorForAllInputs("transparent");
          try {
            return webDriver == null
                ? null
                : Shutterbug.shootPage(webDriver)
                .withTitle(getTitle())
                .withName(getTitle() + CDate.now().toTimeStampForFileName())
                .getImage();
          } catch (Throwable t) {
            return null;
          }
        });
  }

  public String getTitle() {
    return performActionOnDriver(
        "Get Title",
        webDriver -> {
          return webDriver != null ? webDriver.getTitle() : "";
        });
  }

  public String getUrl() {
    return performActionOnDriver(
        "Get URL",
        webDriver -> {
          return webDriver != null ? webDriver.getCurrentUrl() : "";
        });
  }

  public boolean isActive() {
    try {
      return !getTitle().isBlank();
    } catch (Throwable t) {
      return false;
    }
  }
}
