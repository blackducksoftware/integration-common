package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class IntegrationEscapeUtilTest {
    @Test
    public void testEscapingForUri() {
        final IntegrationEscapeUtil integrationEscapeUtil = new IntegrationEscapeUtil();
        assertEquals(null, integrationEscapeUtil.replaceWithUnderscore((String)null));
        assertEquals("", integrationEscapeUtil.replaceWithUnderscore(""));
        assertEquals("_a_b_c_1_2___3", integrationEscapeUtil.replaceWithUnderscore("!a$b:c@1 2-- 3"));

        final List<String> messyStrings = Arrays.asList(new String[] { "#A(B)C++=", "def", "~\tgh1<>23*i.." });
        final List<String> cleanStrings = Arrays.asList(new String[] { "_A_B_C___", "def", "__gh1__23_i__" });
        assertEquals(cleanStrings, integrationEscapeUtil.replaceWithUnderscore(messyStrings));
    }
    
    @Test
    public void testNonEnglishEscaping() {
    	IntegrationEscapeUtil integrationEscapeUtil = new IntegrationEscapeUtil();
    	
    	List<String> beforeEscaping = Arrays.asList(new String[] { "Black@^%&%(du*&)&ck123ブラックダック黑鸭子" });
    	List<String> afterEscaping = Arrays.asList(new String[] { "Black______du____ck123ブラックダック黑鸭子" });
    
    	assertEquals(afterEscaping, integrationEscapeUtil.replaceWithUnderscore(beforeEscaping));
    }
}
