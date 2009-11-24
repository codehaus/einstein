package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;


//TODO: This needs to be tested against a resource that really supports dispatch.
public class DispatchTest extends EinsteinTestRunner {

        public DispatchTest() {
            super("DispatchTest");

        }


    public void testDispatchInstruction1() {
        InstructionGroup group=   einstein.asm :: {

                    read "text:A";
                    dispatch "esper:";


        };
        assertResultEquals(group, "A");
    }

    public void testDispatchAll1() {
        InstructionGroup group=   einstein.asm :: {

                    [read "text:A", read "text:B"];
                    dispatch each "esper:";



        };
        assertResultEquals(group, "[A, B]");
    }

    public void testDispatchAllMessage() {
        InstructionGroup group=   einstein.asm :: {

                    [read "text:A", read "text:B"];
                    dispatch each payload "esper:";



        };
        assertResultEquals(group, "[A, B]");
    }

    public void testDispatchOperatorDirect1() {
        InstructionGroup group=   einstein.asm :: {

                    read "text:A" &> "esper:";


        };
        assertResultEquals(group, "A");
    }


    public void testDispatchOperatorFlow1() {
        InstructionGroup group=   einstein.asm :: {

                    << "text:A";
                    &> "esper:";

        };
        assertResultEquals(group, "A");
    }

   

}