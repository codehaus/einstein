package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class ReadTest extends EinsteinTestRunner {

        public ReadTest() {
            super("ReadTest");

        }


    public void testReadInstruction1() {
        InstructionGroup group=   einstein.asm :: {

                    read "text:A";


        };
        assertResultEquals(group, "A");
    }

    public void testReadInstruction2() {
        InstructionGroup group=   einstein.asm :: {

                    [read "text:A", read "text:B"];


        };
        assertResultEquals(group, "[A, B]");
    }

    public void testReadOperatorDirect1() {
        InstructionGroup group=   einstein.asm :: {

                    << "text:A";

        };
        assertResultEquals(group, "A");
    }

    public void testReadOperatorWithTimeout1() {
        InstructionGroup group=   einstein.asm :: {

                   (>> "console:") << "text:A" : 1000;

        };
        assertResultEquals(group, "A");
    }

    public void testReadOperatorFlow1() {
        InstructionGroup group=   einstein.asm :: {

                    << "text:A";
                    >> "console:";

        };
                assertResultEquals(group, "A");

    }

   

}