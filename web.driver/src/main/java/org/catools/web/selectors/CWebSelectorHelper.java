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

import java.util.Objects;

/**
 * Helper class for web selectors.
 */
public abstract class CWebSelectorHelper {
  /**
   * Escapes the given string value to be used as a string literal in an XPath expression.
   * <p>
   * This method handles cases where the string contains single quotes, double quotes, or both.
   * It constructs an appropriate XPath string literal using the `concat` function if necessary.
   * </p>
   *
   * @param value the string value to escape
   * @return the escaped XPath string literal
   * @throws NullPointerException if the value is null
   */
  public static String escape(String value) {
    Objects.requireNonNull(value, "value must not be null");
    if (!value.contains("'")) {
      return "'" + value + "'";
    }
    if (!value.contains("\"")) {
      return '"' + value + '"';
    }

    boolean quoteIsLast = value.lastIndexOf("\"") == value.length() - 1;
    String[] substringsWithoutQuotes = value.split("\"");
    StringBuilder quoted = new StringBuilder("concat(");

    for (int i = 0; i < substringsWithoutQuotes.length; ++i) {
      quoted.append("\"").append(substringsWithoutQuotes[i]).append("\"");
      quoted.append(i == substringsWithoutQuotes.length - 1 ? (quoteIsLast ? ", '\"')" : ")") : ", '\"', ");
    }

    return quoted.toString();
  }
}
