package org.catools.mcp.util;

import org.apache.logging.log4j.util.Strings;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class TypeConverterTest {

    @Test
    public void testConvertTargetType_shouldReturnDefaultValueWhenValueIsNull() {
        assertEquals(TypeConverter.convert(null, String.class), Strings.EMPTY);
        assertEquals(TypeConverter.convert(null, int.class), 0);
        assertEquals(TypeConverter.convert(null, Integer.class), 0);
        assertEquals(TypeConverter.convert(null, long.class), 0L);
        assertEquals(TypeConverter.convert(null, Long.class), 0L);
        assertEquals(TypeConverter.convert(null, float.class), 0.0F);
        assertEquals(TypeConverter.convert(null, Float.class), 0.0F);
        assertEquals(TypeConverter.convert(null, double.class), 0.0);
        assertEquals(TypeConverter.convert(null, Double.class), 0.0);
        assertEquals(TypeConverter.convert(null, Number.class), 0.0);
        assertEquals(TypeConverter.convert(null, boolean.class), false);
        assertEquals(TypeConverter.convert(null, Boolean.class), false);
        assertNull(TypeConverter.convert(null, Object.class));
    }

    @Test
    public void testConvertTargetType_shouldReturnStrWhenTargetTypeIsStr() {
        assertEquals(TypeConverter.convert("test", String.class), "test");
    }

    @Test
    public void testConvertTargetType_shouldReturnIntWhenTargetTypeIsInt() {
        assertEquals(TypeConverter.convert("1", int.class), 1);
        assertEquals(TypeConverter.convert("1", Integer.class), 1);
    }

    @Test
    public void testConvertTargetType_shouldReturnLongWhenTargetTypeIsLong() {
        assertEquals(TypeConverter.convert("1", long.class), 1L);
        assertEquals(TypeConverter.convert("1", Long.class), 1L);
    }

    @Test
    public void testConvertTargetType_shouldReturnFloatWhenTargetTypeIsFloat() {
        assertEquals(TypeConverter.convert("1", float.class), 1.0F);
        assertEquals(TypeConverter.convert("1", Float.class), 1.0F);
    }

    @Test
    public void testConvertTargetType_shouldReturnDoubleWhenTargetTypeIsDouble() {
        assertEquals(TypeConverter.convert("1", double.class), 1.0);
        assertEquals(TypeConverter.convert("1", Double.class), 1.0);
    }

    @Test
    public void testConvertTargetType_shouldReturnNumberWhenTargetTypeIsNumber() {
        assertEquals(TypeConverter.convert(Integer.MAX_VALUE, Number.class), 2147483647);
        assertEquals(TypeConverter.convert(Long.MAX_VALUE, Number.class), 9223372036854775807L);
        assertEquals(TypeConverter.convert("1.0", Number.class), 1.0);
    }

    @Test
    public void testConvertTargetType_shouldReturnBooleanWhenTargetTypeIsBoolean() {
        assertEquals(TypeConverter.convert("true", boolean.class), true);
        assertEquals(TypeConverter.convert("true", Boolean.class), true);
    }

    @Test
    public void testConvertTargetType_shouldReturnValueAsStringWhenTargetTypeIsNotSupported() {
        assertEquals(TypeConverter.convert("test", Object.class), "test");
    }
}
