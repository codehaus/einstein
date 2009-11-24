package org.cauldron.einstein.ri.tests;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class UnSequencedGroupTest extends EinsteinTestRunner {

        public UnSequencedGroupTest() {
            super("UnSequencedGroupTest");

        }


    public void testAppendOperator1() {
        InstructionGroup group=   einstein.asm :: {
                    (<< "text:A") & ( << "text:B") & (<<"text:C");

        };
        assertResultEquals(group, "[A, B, C]");
    }


    public void testAppendOperator2() {
        InstructionGroup group=   einstein.asm :: {
                     (<< "text:A");
                     += (<< "text:B", << "text:C");
        };
        assertResultEquals(group, "[A, B]");
    }

    public void testIterateOperator1() {
        InstructionGroup group1=   einstein.asm :: {
                     [<<"text:A", << "text:B"] ... (+=(<< "text:1"), +=(<< "text:2"));
        };
        InstructionGroup group2=   einstein.asm :: {
                     [<< "text:A", << "text:B"] ... {+=(<< "text:1")};
        };
        assertResultEquals(group1, group2);
    }

   


    //FIXME

    /*public void testIterateOperator2() {
        InstructionGroup group=   einstein.asm :: {
                     |- ([<< "text:A", << "text:B"] ... (&(<< "text:1"), &(<< "text:2")));
        };
        assertResultEquals(group, "[A, 1]");
    } */

   

}