package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.blackduck.integration.util.EnumUtils;

public class EnumUtilsTest {
    @Test
    public void testPrettyPrint() {
        assertEquals("Value One", EnumUtils.prettyPrint(TestEnum.VALUE_ONE));
        assertEquals("Value Two", EnumUtils.prettyPrint(TestEnum.Value_Two));
        assertEquals("Value Three", EnumUtils.prettyPrint(TestEnum.value_three));
    }

    @Test
    public void testCommaDelimittedParsing() {
        String testing = "VALUE_ONE,value_three";
        List<TestEnum> enumValues = EnumUtils.parseCommaDelimitted(testing, TestEnum.class);
        assertEquals(2, enumValues.size());
        assertEquals(TestEnum.VALUE_ONE, enumValues.get(0));
        assertEquals(TestEnum.value_three, enumValues.get(1));
    }

    @Test
    public void testParsingEmptyString() {
        String testing = "";
        List<TestEnum> enumValues = EnumUtils.parseCommaDelimitted(testing, TestEnum.class);
        assertTrue(enumValues.isEmpty());
    }

    @Test
    public void testParsingBadValues() {
        String testing = "notAValidValue,anotherBadValue";
        try {
            List<TestEnum> enumValues = EnumUtils.parseCommaDelimitted(testing, TestEnum.class);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testParsingOnlyCommas() {
        String testing = ",,,,";
        List<TestEnum> enumValues = EnumUtils.parseCommaDelimitted(testing, TestEnum.class);
        assertTrue(enumValues.isEmpty());
    }

    private enum TestEnum {
        VALUE_ONE,
        Value_Two,
        value_three
    }

}
