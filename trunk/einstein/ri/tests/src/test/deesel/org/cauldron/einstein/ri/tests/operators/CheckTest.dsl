package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class CheckTest extends EinsteinTestRunner {

        public CheckTest() {
            super("CheckTest");

        }


    public void testCheckInstruction1() {
        InstructionGroup group=   einstein.asm :: {

                    check "text:A";


        };
        assertResultEquals(group, "A");
    }

    public void testCheckInstruction2() {
        InstructionGroup group=   einstein.asm :: {

                    [check "text:A", check "text:B"];


        };
        assertResultEquals(group, "[A, B]");
    }

    public void testCheckOperatorDirect1() {
        InstructionGroup group=   einstein.asm :: {

                    <& "text:A";

        };
        assertResultEquals(group, "A");
    }


    public void testCheckOperatorFlow1() {
        InstructionGroup group=   einstein.asm :: {

                    <& "text:A";
                    >> "console:";

        };
        assertResultEquals(group, "A");
        
    }

   

}