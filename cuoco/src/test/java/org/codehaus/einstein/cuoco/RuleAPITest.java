package org.codehaus.einstein.cuoco;

import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.einstein.cuoco.Rule;


public class RuleAPITest extends TestCase {


    public void testSimple() throws InvocationTargetException {
        assertTrue(new Rule(new RuleID("A", "B")).is(1 == 1).execute().passed());
    }

    public void testOr() {

        assertTrue(new Rule(new RuleID("A", "B")).eq(1, 1).or().eq(1, 2).execute().passed());
    }

    public void testOrAnd() {
        assertFalse(new Rule(new RuleID("A", "B")).and(true).or().is(false).is(false).execute().passed());
        assertTrue(new Rule(new RuleID("A", "B")).is(true).or().is(false).is(true).execute().passed());
    }


}
