package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class WriteTest extends EinsteinTestRunner {

        public WriteTest() {
            super("WriteTest");

        }


    public void testwriteInstruction1() {
        InstructionGroup group=   einstein.asm :: {

                    read "text:A";
                    write "console:";

        };
        assertResultEquals(group, "A");
    }

    public void testwriteInstruction2() {
        InstructionGroup group=   einstein.asm :: {

                    [<< "text:A", << "text:B"];
                    write "console:";

        };
        assertResultEquals(group, "[A, B]");
    }

    public void testwriteOperatorDirect1() {
        InstructionGroup group=   einstein.asm :: {

                    (<< "text:A") >> "console:";

        };
        assertResultEquals(group , "A");
    }

    public void testwriteOperatorFlow1() {
        InstructionGroup group=   einstein.asm :: {

                    (<< "text:A");
                    >> "console:";

        };
        assertResultEquals(group, "A");
    }

   

}