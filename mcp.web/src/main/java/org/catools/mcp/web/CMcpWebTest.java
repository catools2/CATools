package org.catools.mcp.web;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.catools.web.tests.CWebTest;

@Slf4j
public class CMcpWebTest extends CWebTest<CDriver> {

  @Getter
  private CWebElement<CDriver> webElement;

  @CMcpTool(
      groups = "web",
      name = "driver_find_element_by_id",
      title = "Find Element by ID",
      description = "Finds a web element by its ID attribute with custom name and timeout"
  )
  public void byId(
      @CMcpToolParam(name = "name", description = "Descriptive name for the element") String name,
      @CMcpToolParam(name = "id", description = "The ID attribute value") String id,
      @CMcpToolParam(name = "waitSec", description = "Timeout in seconds") int waitSec) {
    webElement = getDriver().byId(name, id, waitSec);
  }

  @CMcpTool(
      groups = "web",
      name = "driver_find_element_by_name",
      title = "Find Element by Name",
      description = "Finds a web element by its name attribute with custom name and timeout"
  )
  public void byName(
      @CMcpToolParam(name = "name", description = "Descriptive name for the element") String name,
      @CMcpToolParam(name = "elementName", description = "The name attribute value") String elementName,
      @CMcpToolParam(name = "waitSec", description = "Timeout in seconds") int waitSec) {
    webElement = getDriver().byName(name, elementName, waitSec);
  }

  @CMcpTool(
      groups = "web",
      name = "driver_find_element_by_class_name",
      title = "Find Element by Class Name",
      description = "Finds a web element by its CSS class name with custom name and timeout"
  )
  public void byClassName(
      @CMcpToolParam(name = "name", description = "Descriptive name for the element") String name,
      @CMcpToolParam(name = "className", description = "The CSS class name") String className,
      @CMcpToolParam(name = "waitSec", description = "Timeout in seconds") int waitSec) {
    webElement = getDriver().byClassName(name, className, waitSec);
  }

  @CMcpTool(
      groups = "web",
      name = "driver_find_element_by_tag_name",
      title = "Find Element by Tag Name",
      description = "Finds a web element by its HTML tag name with custom name and timeout"
  )
  public void byTagName(
      @CMcpToolParam(name = "name", description = "Descriptive name for the element") String name,
      @CMcpToolParam(name = "tagName", description = "The HTML tag name") String tagName,
      @CMcpToolParam(name = "waitSec", description = "Timeout in seconds") int waitSec) {
    webElement = getDriver().byTagName(name, tagName, waitSec);
  }

  @CMcpTool(
      groups = "web",
      name = "driver_find_element_by_link_text",
      title = "Find Element by Link Text",
      description = "Finds a web element by its exact link text with custom name and timeout"
  )
  public void byLinkText(
      @CMcpToolParam(name = "name", description = "Descriptive name for the element") String name,
      @CMcpToolParam(name = "linkText", description = "The exact link text") String linkText,
      @CMcpToolParam(name = "waitSec", description = "Timeout in seconds") int waitSec) {
    webElement = getDriver().byLinkText(name, linkText, waitSec);
  }

  @CMcpTool(
      groups = "web",
      name = "driver_find_element_by_partial_link_text",
      title = "Find Element by Partial Link Text",
      description = "Finds a web element by partial link text with custom name and timeout"
  )
  public void byPartialLinkText(
      @CMcpToolParam(name = "name", description = "Descriptive name for the element") String name,
      @CMcpToolParam(name = "partialLinkText", description = "The partial link text") String partialLinkText,
      @CMcpToolParam(name = "waitSec", description = "Timeout in seconds") int waitSec) {
    webElement = getDriver().byPartialLinkText(name, partialLinkText, waitSec);
  }

  @CMcpTool(
      groups = "web",
      name = "driver_find_element_by_xpath",
      title = "Find Element by XPath",
      description = "Finds a web element by XPath expression with custom name and timeout"
  )
  public void byXPath(
      @CMcpToolParam(name = "name", description = "Descriptive name for the element") String name,
      @CMcpToolParam(name = "xpath", description = "The XPath expression") String xpath,
      @CMcpToolParam(name = "waitSec", description = "Timeout in seconds") int waitSec) {
    webElement = getDriver().byXPath(name, xpath, waitSec);
  }

  @CMcpTool(
      groups = "web",
      name = "driver_find_element_by_css_selector",
      title = "Find Element by CSS Selector",
      description = "Finds a web element by CSS selector with custom name and timeout"
  )
  public void byCssSelector(
      @CMcpToolParam(name = "name", description = "Descriptive name for the element") String name,
      @CMcpToolParam(name = "cssSelector", description = "The CSS selector expression") String cssSelector,
      @CMcpToolParam(name = "waitSec", description = "Timeout in seconds") int waitSec) {
    webElement = getDriver().byCssSelector(name, cssSelector, waitSec);
  }

}

