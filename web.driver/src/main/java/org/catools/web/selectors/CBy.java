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

package org.catools.web.selectors;

import org.jspecify.annotations.NullMarked;

/**
 * Mechanism used to locate elements within a document. Factory methods return
 * lightweight `CBy` implementations located in separate files.
 */
@NullMarked
public abstract class CBy {

  public abstract String getSelector();

  public static CBy id(String id) {
    return new ById(id);
  }

  public static CBy name(String name) {
    return new ByName(name);
  }

  public static CBy linkText(String linkText) {
    return new ByLinkText(linkText);
  }

  public static CBy partialLinkText(String partialLinkText) {
    return new ByPartialLinkText(partialLinkText);
  }

  public static CBy tagName(String tagName) {
    return new ByTagName(tagName);
  }

  public static CBy xpath(String xpathExpression) {
    return new ByXPath(xpathExpression);
  }

  public static CBy className(String className) {
    return new ByClassName(className);
  }

  public static CBy cssSelector(String cssSelector) {
    return new ByCssSelector(cssSelector);
  }
}
