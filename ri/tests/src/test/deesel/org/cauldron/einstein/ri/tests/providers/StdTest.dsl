package org.cauldron.einstein.ri.tests.providers;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class StdTest extends EinsteinTestRunner {

        public StdTest() {
            super("StdTest");

        }

    public void test() {
        InstructionGroup group=   einstein.asm :: {

                     resource "console:Test" stdout;
                     read "text:A";
                     write stdout;
        };
                assertResultEquals(group, "A");
        
    }

   

}