package org.catools.mcp.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.catools.common.utils.CStringUtil;
import org.testng.annotations.Test;

public class TypeConverterTest {

  @Test
  public void testConvertTargetType_shouldReturnDefaultValueWhenValueIsNull() {
    assertEquals(CTypeConverter.convert(null, String.class), CStringUtil.EMPTY);
    assertEquals(CTypeConverter.convert(null, int.class), 0);
    assertEquals(CTypeConverter.convert(null, Integer.class), 0);
    assertEquals(CTypeConverter.convert(null, long.class), 0L);
    assertEquals(CTypeConverter.convert(null, Long.class), 0L);
    assertEquals(CTypeConverter.convert(null, float.class), 0.0F);
    assertEquals(CTypeConverter.convert(null, Float.class), 0.0F);
    assertEquals(CTypeConverter.convert(null, double.class), 0.0);
    assertEquals(CTypeConverter.convert(null, Double.class), 0.0);
    assertEquals(CTypeConverter.convert(null, Number.class), 0.0);
    assertEquals(CTypeConverter.convert(null, boolean.class), false);
    assertEquals(CTypeConverter.convert(null, Boolean.class), false);
    assertNull(CTypeConverter.convert(null, Object.class));
  }

  @Test
  public void testConvertTargetType_shouldReturnStrWhenTargetTypeIsStr() {
    assertEquals(CTypeConverter.convert("test", String.class), "test");
  }

  @Test
  public void testConvertTargetType_shouldReturnIntWhenTargetTypeIsInt() {
    assertEquals(CTypeConverter.convert("1", int.class), 1);
    assertEquals(CTypeConverter.convert("1", Integer.class), 1);
  }

  @Test
  public void testConvertTargetType_shouldReturnLongWhenTargetTypeIsLong() {
    assertEquals(CTypeConverter.convert("1", long.class), 1L);
    assertEquals(CTypeConverter.convert("1", Long.class), 1L);
  }

  @Test
  public void testConvertTargetType_shouldReturnFloatWhenTargetTypeIsFloat() {
    assertEquals(CTypeConverter.convert("1", float.class), 1.0F);
    assertEquals(CTypeConverter.convert("1", Float.class), 1.0F);
  }

  @Test
  public void testConvertTargetType_shouldReturnDoubleWhenTargetTypeIsDouble() {
    assertEquals(CTypeConverter.convert("1", double.class), 1.0);
    assertEquals(CTypeConverter.convert("1", Double.class), 1.0);
  }

  @Test
  public void testConvertTargetType_shouldReturnNumberWhenTargetTypeIsNumber() {
    assertEquals(CTypeConverter.convert(Integer.MAX_VALUE, Number.class), 2147483647);
    assertEquals(CTypeConverter.convert(Long.MAX_VALUE, Number.class), 9223372036854775807L);
    assertEquals(CTypeConverter.convert("1.0", Number.class), 1.0);
  }

  @Test
  public void testConvertTargetType_shouldReturnBooleanWhenTargetTypeIsBoolean() {
    assertEquals(CTypeConverter.convert("true", boolean.class), true);
    assertEquals(CTypeConverter.convert("true", Boolean.class), true);
  }

  @Test
  public void testConvertTargetType_shouldReturnValueAsStringWhenTargetTypeIsNotSupported() {
    assertEquals(CTypeConverter.convert("test", Object.class), "test");
  }
}
