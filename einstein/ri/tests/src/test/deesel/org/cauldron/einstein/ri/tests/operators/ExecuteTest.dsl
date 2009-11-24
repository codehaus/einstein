package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class ExecuteTest extends EinsteinTestRunner {

        public ExecuteTest() {
            super("ExecuteTest");

        }

   //TODO: add support for references to Deesel from Einstein.
    public void test() {
        InstructionGroup group=   einstein.asm :: {
                     read from "text:B";
             //        execute :::({"A" + it });
        };
        assertResultEquals(group, "B");
    }

   

}