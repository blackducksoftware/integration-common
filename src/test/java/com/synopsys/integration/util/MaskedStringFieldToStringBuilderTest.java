package com.synopsys.integration.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaskedStringFieldToStringBuilderTest {
    @Test
    void testMasking() {
        SomeValues someValues = new SomeValues("", "");
        String blank = someValues.toString();
        assertEquals("{\"shouldShowUp\":\"\",\"shouldBeHidden\":\"\"}", blank);

        SomeValues someValues2 = new SomeValues("hello!", null);
        String onlyOne = someValues2.toString();
        assertEquals("{\"shouldShowUp\":\"hello!\",\"shouldBeHidden\":null}", onlyOne);

        SomeValues someValues3 = new SomeValues("hello!", "goodbye!");
        String filled = someValues3.toString();
        assertEquals("{\"shouldShowUp\":\"hello!\",\"shouldBeHidden\":\"" + MaskedStringFieldToStringBuilder.MASKED_VALUE + "\"}", filled);
    }

    private class SomeValues {
        public String shouldShowUp;
        public String shouldBeHidden;

        public SomeValues(String shouldShowUp, String shouldBeHidden) {
            this.shouldShowUp = shouldShowUp;
            this.shouldBeHidden = shouldBeHidden;
        }

        @Override
        public String toString() {
            return new MaskedStringFieldToStringBuilder(this, "shouldBeHidden").toString();
        }
    }

}
