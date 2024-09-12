package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MaskedStringFieldToStringBuilderTest {
    @Test
    void testMasking() {
        SomeValues someValues = new SomeValues("", "");
        String blank = someValues.toString();
        assertEquals("{\"shouldBeHidden\":\"\",\"shouldShowUp\":\"\"}", blank);

        SomeValues someValues2 = new SomeValues("hello!", null);
        String onlyOne = someValues2.toString();
        assertEquals("{\"shouldBeHidden\":null,\"shouldShowUp\":\"hello!\"}", onlyOne);

        SomeValues someValues3 = new SomeValues("hello!", "goodbye!");
        String filled = someValues3.toString();
        assertEquals("{\"shouldBeHidden\":\"" + MaskedStringFieldToStringBuilder.MASKED_VALUE + "\",\"shouldShowUp\":\"hello!\"}", filled);
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
