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

import java.util.Arrays;

/**
 * Mechanism used to locate elements within a document. Factory methods return lightweight `CBy`
 * implementations located in separate files.
 */
@NullMarked
public abstract class CBy {

  public abstract String getSelector();

  public static CById id(String id) {
    return new CById(id);
  }

  public static CByName name(String name) {
    return new CByName(name);
  }

  public static CByLinkText linkText(String linkText) {
    return new CByLinkText(linkText);
  }

  public static CByPartialLinkText partialLinkText(String partialLinkText) {
    return new CByPartialLinkText(partialLinkText);
  }

  public static CByTagName tagName(String tagName) {
    return new CByTagName(tagName);
  }

  public static CByXPath xpath(String xpathExpression) {
    return new CByXPath(xpathExpression);
  }

  public static CByClassName className(String className) {
    return new CByClassName(className);
  }

  public static CByCssSelector cssSelector(String cssSelector) {
    return new CByCssSelector(cssSelector);
  }

  @SuppressWarnings("unchecked")
  public static <B extends CBy> B chain(CBy... paths) {
    boolean hasCssSelector = Arrays.stream(paths).anyMatch(path -> path instanceof CByCssSelector);
    boolean hasNoneCssSelector =
        Arrays.stream(paths).anyMatch(path -> !(path instanceof CByCssSelector));

    if (hasCssSelector && hasNoneCssSelector) {
      throw new IllegalArgumentException("Cannot chain CSS selectors with non-CSS selectors.");
    }

    if (hasCssSelector) {
      CByCssSelector[] cssPaths =
          Arrays.stream(paths).map(path -> (CByCssSelector) path).toArray(CByCssSelector[]::new);
      return (B) chainCssSelectors(cssPaths);
    } else {
      CByXPath[] xpathPaths =
          Arrays.stream(paths).map(path -> (CByXPath) path).toArray(CByXPath[]::new);
      return (B) chainXPaths(xpathPaths);
    }
  }

  private static CByXPath chainXPaths(CByXPath... paths) {
    StringBuilder chainedXPath = new StringBuilder();
    for (CByXPath path : paths) {
      if (chainedXPath.isEmpty()) {
        chainedXPath.append(path.getSelector());
      } else {
        String xpath = path.getSelector();
        if (xpath.startsWith(".")) {
          xpath = xpath.substring(1);
        }
        chainedXPath.append(xpath);
      }
    }
    return new CByXPath(chainedXPath.toString());
  }

  private static CByCssSelector chainCssSelectors(CByCssSelector... paths) {
    StringBuilder chainedXPath = new StringBuilder();
    for (CByCssSelector path : paths) {
      if (chainedXPath.isEmpty()) {
        chainedXPath.append(path.getSelector());
      } else {
        chainedXPath.append(" >> ").append(path.getSelector());
      }
    }
    return new CByCssSelector(chainedXPath.toString());
  }
}
