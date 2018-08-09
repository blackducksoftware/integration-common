package com.synopsys.integration.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EnumUtilsTest {
    @Test
    public void testPrettyPrint() {
        assertEquals("Value One", EnumUtils.prettyPrint(TestEnum.VALUE_ONE));
        assertEquals("Value Two", EnumUtils.prettyPrint(TestEnum.Value_Two));
        assertEquals("Value Three", EnumUtils.prettyPrint(TestEnum.value_three));
    }

    private static enum TestEnum {
        VALUE_ONE,
        Value_Two,
        value_three;
    }

}
