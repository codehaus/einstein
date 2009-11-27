package org.codehaus.einstein.cuoco;

import junit.framework.TestCase;
import org.codehaus.einstein.cuoco.Weebl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Arrays;

import org.codehaus.einstein.cuoco.RuleResult;
import org.codehaus.einstein.cuoco.RuleResultCollection;
import org.codehaus.einstein.cuoco.SimpleRuleBuilder;


public class RuleCombinedTest extends TestCase {

    private List<String> values= Arrays.asList("1","2","3");

    public void testSimple() throws InvocationTargetException {

        final SimpleRuleBuilder<Weebl> builder = new SimpleRuleBuilder<Weebl>(new Weebl(), false) {

            public void internal(Weebl domain) {
                createRule(new RuleID("RULE1", "A"), propertyIsNotNull("bobs.pie") && (propertyIsNull("bobs.empty")) && (isNotNull("")));
                createRule(new RuleID("RULE2", "B"), isNotNull(getProperty("bobs.pie")) && (eq(getProperty("bobs.pie"), "pie")));
                createRule(new RuleID("RULE3", "C"), propertyIsNull("bobs.pie") && (propertyIsNull("bobs.empty")) && (isNotNull("")));
                createRule(new RuleID("RULE4", "D"), is(2 > 1) && eq(getProperty("bobs.pie"), "pie"));
                createRule(new RuleID("RULE5", "E"), propertyIn("bobs.pie", "eat", "some", "pie") && (eq(getProperty("bobs.pie"), "pie")));
                createRule(new RuleID("RULE6", "F"), propertyEquals("bobs.pie", "pie"));
                createRule(new RuleID("RULE7", "G"), isNonNumeric("pie"));
                createRule(new RuleID("RULE8", "H"), isNumeric("1"));
                createRule(new RuleID("RULE9", "I"), isAlphanumeric("A1B"));
                createRule(new RuleID("RULE9", "J"), isAlpha("A1B"));
                createRule(new RuleID("RULE10", "K"), isAlpha("AB"));
                createRule(new RuleID("RULE11", "L"), in("A", "A", "B", "C"));
                createRule(new RuleID("RULE12", "M"), in("A", "X", "Y", "Z"));
                createRule(new RuleID("RULE13", "N"), in("1", values));
                createRule(new RuleID("RULE14", "O"), blank("  ") && (blank("")) && (blank(null)));
                createRule(new RuleID("RULE14", "P"), not(blank("X")) && (blank("")) && (blank(null)));
                createRule(new RuleID("RULE15", "Q"), anyPropertyExists("dave", "bobs.pie", "pie"));
                createRule(new RuleID("RULE16", "R"), blank("dave") || (blank("")));

                String nullVal= null;
                createRule(new RuleID("RULE17", "S"), isNotNull(nullVal) || (isNumeric(nullVal)));
            }
        };
        
        final RuleResultCollection results = builder.build().execute();
        RuleResult rule1 = results.getResult("RULE1");
        RuleResult rule2 = results.getResult("RULE2");

        assertTrue(rule1.passed());
        assertTrue(rule2.passed());

        assertFalse(results.getResult("RULE3").passed());
        assertTrue(results.getResult("RULE4").passed());
        assertTrue(results.getResult("RULE5").passed());
        assertTrue(results.getResult("RULE6").passed());
        assertTrue(results.getResult("RULE7").passed());
        assertTrue(results.getResult("RULE8").passed());
        assertFalse(results.getResult("RULE9").passed());
        assertTrue(results.getResult("RULE10").passed());
        assertTrue(results.getResult("RULE11").passed());
        assertFalse(results.getResult("RULE12").passed());
        assertTrue(results.getResult("RULE13").passed());
        assertTrue(results.getResult("RULE14").passed());
        assertTrue(results.getResult("RULE15").passed());
        assertTrue(results.getResult("RULE16").passed());
        assertFalse(results.getResult("RULE17").passed());

        assertEquals("RULE1", rule1.getRuleId());
        assertEquals("RULE2", rule2.getRuleId());

        assertEquals("H", results.getResult("RULE8").getErrorCode());

        final List<RuleResult> resultList = results.getAll();
        assertFalse(results.hasAllPassed());

        for (RuleResult ruleResult : resultList) {
            System.err.println("Result: " + ruleResult.getRuleId() + " = " + ruleResult.passed());
        }

        builder.dump();

    }

    public void testAllPassed() throws InvocationTargetException {

          assertTrue(new SimpleRuleBuilder<Weebl>(new Weebl(), false) {

              public void internal(Weebl domain) {

                  createRule(new RuleID("RULE1", "A"), propertyIsNotNull("bobs.pie") && (propertyIsNull("bobs.empty")) && (isNotNull("")));
                  createRule(new RuleID("RULE2", "B"), isNotNull(getProperty("bobs.pie")) && (eq(getProperty("bobs.pie"), "pie")));
                  createRule(new RuleID("RULE3", "C"), propertyIsNotNull("bobs.pie") && (propertyIsNull("bobs.empty")) && (isNotNull("")));
                  createRule(new RuleID("RULE4", "D"), is(2 > 1) && (eq(getProperty("bobs.pie"), "pie")));

              }
          }.build().execute().hasAllPassed());

      }


}