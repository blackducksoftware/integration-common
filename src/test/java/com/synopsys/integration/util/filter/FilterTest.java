package com.synopsys.integration.util.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

import org.junit.Test;

public class FilterTest {

    @Test
    public void testAndWithTrueCondition() {
        final FilterBuilder builder = new AndFieldFilterBuilder(DefaultFilterBuilders.ALWAYS_TRUE, DefaultFilterBuilders.ALWAYS_TRUE);

        final Predicate example = builder.buildPredicate();
        assertTrue(example.test(null));
    }

    @Test
    public void testAndWithFalseConditions() {
        final FilterBuilder builder1 = new AndFieldFilterBuilder(DefaultFilterBuilders.ALWAYS_TRUE, DefaultFilterBuilders.ALWAYS_FALSE);
        final Predicate example1 = builder1.buildPredicate();
        assertFalse(example1.test(null));

        final FilterBuilder builder2 = new AndFieldFilterBuilder(DefaultFilterBuilders.ALWAYS_FALSE, DefaultFilterBuilders.ALWAYS_TRUE);
        final Predicate example2 = builder2.buildPredicate();
        assertFalse(example2.test(null));

        final FilterBuilder builder3 = new AndFieldFilterBuilder(DefaultFilterBuilders.ALWAYS_FALSE, DefaultFilterBuilders.ALWAYS_FALSE);
        final Predicate example3 = builder3.buildPredicate();
        assertFalse(example3.test(null));
    }

    @Test
    public void testOrWithTrueConditions() {
        final FilterBuilder builder1 = new OrFieldFilterBuilder(DefaultFilterBuilders.ALWAYS_TRUE, DefaultFilterBuilders.ALWAYS_TRUE);
        final Predicate example1 = builder1.buildPredicate();
        assertTrue(example1.test(null));

        final FilterBuilder builder2 = new OrFieldFilterBuilder(DefaultFilterBuilders.ALWAYS_TRUE, DefaultFilterBuilders.ALWAYS_FALSE);
        final Predicate example2 = builder2.buildPredicate();
        assertTrue(example2.test(null));

        final FilterBuilder builder3 = new OrFieldFilterBuilder(DefaultFilterBuilders.ALWAYS_FALSE, DefaultFilterBuilders.ALWAYS_TRUE);
        final Predicate example3 = builder3.buildPredicate();
        assertTrue(example3.test(null));
    }

    @Test
    public void testOrWithFalseCondition() {
        final FilterBuilder builder = new OrFieldFilterBuilder(DefaultFilterBuilders.ALWAYS_FALSE, DefaultFilterBuilders.ALWAYS_FALSE);

        final Predicate example = builder.buildPredicate();
        assertFalse(example.test(null));
    }
}
