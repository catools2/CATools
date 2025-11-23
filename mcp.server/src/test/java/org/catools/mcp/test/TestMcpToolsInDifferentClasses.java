package org.catools.mcp.test;

import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;

@Slf4j
public class TestMcpToolsInDifferentClasses {

  @CMcpTool
  public void toolWithVoidReturn() {
    log.debug("calling toolWithVoidReturn");
  }

  @CMcpTool
  public String toolWithReturnNull() {
    log.debug("calling toolWithReturnNull");
    return null;
  }

  @CMcpTool
  public String toolWithIntParam(@CMcpToolParam(name = "param") int param) {
    log.debug("calling toolWithIntParam with param: {}", param);
    return "toolWithIntParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithIntegerParam(@CMcpToolParam(name = "param") Integer param) {
    log.debug("calling toolWithIntegerParam with param: {}", param);
    return "toolWithIntegerParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithLongParam(@CMcpToolParam(name = "param") long param) {
    log.debug("calling toolWithLongParam with param: {}", param);
    return "toolWithLongParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithLongClassParam(@CMcpToolParam(name = "param") Long param) {
    log.debug("calling toolWithLongClassParam with param: {}", param);
    return "toolWithLongClassParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithFloatParam(@CMcpToolParam(name = "param") float param) {
    log.debug("calling toolWithFloatParam with param: {}", param);
    return "toolWithFloatParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithFloatClassParam(@CMcpToolParam(name = "param") Float param) {
    log.debug("calling toolWithFloatClassParam with param: {}", param);
    return "toolWithFloatClassParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithDoubleParam(@CMcpToolParam(name = "param") double param) {
    log.debug("calling toolWithDoubleParam with param: {}", param);
    return "toolWithDoubleParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithDoubleClassParam(@CMcpToolParam(name = "param") Double param) {
    log.debug("calling toolWithDoubleClassParam with param: {}", param);
    return "toolWithDoubleClassParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithNumberParam(@CMcpToolParam(name = "param") Number param) {
    log.debug("calling toolWithNumberParam with param: {}", param);
    return "toolWithNumberParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithBooleanParam(@CMcpToolParam(name = "param") boolean param) {
    log.debug("calling toolWithBooleanParam with param: {}", param);
    return "toolWithBooleanParam is called with param: " + param;
  }

  @CMcpTool
  public String toolWithBooleanClassParam(@CMcpToolParam(name = "param") Boolean param) {
    log.debug("calling toolWithBooleanClassParam with param: {}", param);
    return "toolWithBooleanClassParam is called with param: " + param;
  }
}
