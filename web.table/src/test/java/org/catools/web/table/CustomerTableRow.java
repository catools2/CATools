package org.catools.web.table;

import org.catools.common.collections.CHashMap;
import org.catools.common.collections.interfaces.CMap;
import org.catools.web.drivers.CDriver;

public class CustomerTableRow extends CWebTableRow<CDriver, CustomerTable> {
  public CustomerTableRow(String name, CDriver driver, int idx, CustomerTable parentTable) {
    super(name, driver, idx, parentTable);
  }

  public CustomerTableRow(String name, CDriver driver, int idx, CustomerTable parentTable, int waitSec) {
    super(name, driver, idx, parentTable, waitSec);
  }

  public CMap<String, String> getRecord() {
    CMap<String, String> values = new CHashMap<>();
    CMap<Integer, String> headersMap = parentTable.getHeadersMap();
    for (Integer hIdx : headersMap.keySet()) {
      if (parentTable.getHeader(hIdx).isVisible()) {
        String header = headersMap.get(hIdx);
        values.put(header, createControl(header).getText());
      }
    }
    return values;
  }

  @Override
  public String toString() {
    return String.valueOf(getRecord());
  }
}
