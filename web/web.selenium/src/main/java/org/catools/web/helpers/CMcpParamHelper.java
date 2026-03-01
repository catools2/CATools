// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.catools.web.helpers;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

/** Helper class for web selectors. */
@UtilityClass
public class CMcpParamHelper {
  /**
   * Converts a locator string to a By object. If the locator contains "//", it is treated as an
   * XPath expression; otherwise, it is treated as a CSS selector.
   *
   * @param locator the locator string
   * @return the corresponding By object
   */
  public By toBy(String locator) {
    if (locator == null) return By.cssSelector("");

    return locator.contains("//") ? By.xpath(locator) : By.cssSelector(locator);
  }
}
