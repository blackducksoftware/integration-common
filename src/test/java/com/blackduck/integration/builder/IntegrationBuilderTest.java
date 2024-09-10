package com.blackduck.integration.builder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IntegrationBuilderTest {
    public static final String POSITIVE_TEST = "positive";
    public static final String NEGATIVE_TEST = "negative";

    @Test
    public void testSuccess() {
        TestIntegrationBuilderBldr testBuilder = new TestIntegrationBuilderBldr();
        testBuilder.setTestString(POSITIVE_TEST);

        BuilderStatus testBuilderStatus = testBuilder.validateAndGetBuilderStatus();
        assertTrue(testBuilderStatus.isValid());

        testBuilder.build();
        assertTrue(testBuilder.isValid());
    }

    @Test
    public void testFailure() {
        TestIntegrationBuilderBldr testBuilder = new TestIntegrationBuilderBldr();
        testBuilder.setTestString(NEGATIVE_TEST);

        BuilderStatus testBuilderStatus = testBuilder.validateAndGetBuilderStatus();
        assertFalse(testBuilderStatus.isValid());

        assertThrows(IllegalArgumentException.class, testBuilder::build);
        assertFalse(testBuilder.isValid());
    }

    private static class TestIntegrationBuilder implements Buildable {
        public final String testStringOne;

        private TestIntegrationBuilder(String testStringOne) {
            this.testStringOne = testStringOne;
        }
    }

    private static class TestIntegrationBuilderBldr extends IntegrationBuilder<TestIntegrationBuilder> {
        private String testString;

        @Override
        protected TestIntegrationBuilder buildWithoutValidation() {
            return new TestIntegrationBuilder(testString);
        }

        @Override
        protected void validate(BuilderStatus builderStatus) {
            if (testString.equals(NEGATIVE_TEST)) {
                builderStatus.addErrorMessage(NEGATIVE_TEST);

            }
        }

        public void setTestString(String stringOne) {
            this.testString = stringOne;
        }
    }
}
