package org.catools.web.table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.catools.common.utils.CStringUtil;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.function.Function;

@Getter
@Accessors(chain = true)
public abstract class CWebTableRow<DR extends CDriver, P extends CWebTable>
    extends CWebElement<DR> {
  protected final P parentTable;

  @Setter
  private String cellXpath;

  public CWebTableRow(String name, DR driver, int idx, P parentTable) {
    this(name, driver, idx, parentTable, DEFAULT_TIMEOUT);
  }

  public CWebTableRow(String name, DR driver, int idx, P parentTable, int waitSec) {
    super(name, driver, By.xpath(parentTable.getRowXpath(idx)), waitSec);
    this.parentTable = parentTable;
    this.cellXpath = parentTable.getCellXpath() + "[%d]";
  }

  protected <C extends CWebElement<DR>> C createControl(
      String header, Function<By, C> controlBuilder) {
    return controlBuilder.apply(getLocator(header, ""));
  }

  protected <C extends CWebElement<DR>> C createControl(
      String header, String childLocator, Function<By, C> controlBuilder) {
    return controlBuilder.apply(getLocator(header, childLocator));
  }

  private By getLocator(String header, String childLocator) {
    String cellLocator =
        String.format(cellXpath + childLocator, parentTable.getHeadersMap().get(header));
    cellLocator = CStringUtil.removeStart(cellLocator, ".");
    return new ByChained(getLocator(), By.xpath("." + cellLocator));
  }
}
