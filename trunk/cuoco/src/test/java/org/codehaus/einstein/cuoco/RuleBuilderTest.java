package org.codehaus.einstein.cuoco;

import junit.framework.TestCase;
import org.codehaus.einstein.cuoco.Weebl;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.einstein.cuoco.SimpleRuleBuilder;


public class RuleBuilderTest extends TestCase {

    public void testSimple() throws InvocationTargetException {
        final SimpleRuleBuilder<Weebl> builder = new SimpleRuleBuilder<Weebl>(new Weebl(), false) {
            public void internal(Weebl domain) {
                
                assertTrue(propertyIsNotNull("bobs.pie"));
                assertFalse(propertyIsNotNull("bobs.empty"));
                assertTrue(propertyIsNull("bobs.empty"));
            }
        };
        builder.build().execute();
        builder.dump();
    }


}