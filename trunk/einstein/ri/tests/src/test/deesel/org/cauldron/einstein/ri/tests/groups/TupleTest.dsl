package org.cauldron.einstein.ri.tests;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class TupleTest extends EinsteinTestRunner {

        public TupleTests() {
            super("TupleTest");

        }


    public void testAppendInstruction1() {
        InstructionGroup group=   einstein.asm :: {

                    read "text:A";
                    append { << "text:B" };
                    append { << "text:C" };

        };
        assertResultEquals(group, "[A, B, C]");
    }

    public void testAppendInstruction2() {
        InstructionGroup group=   einstein.asm :: {

                    read "text:A";
                    append [<< "text:B", << "text:C"];
                    
        };
        assertResultEquals(group, "[A, B, C]");
    }

    public void testAppendOperator1() {
        InstructionGroup group=   einstein.asm :: {
                     (<< "text:A");
                     += (<< "text:B");
                     += (<< "text:C");
        };
        assertResultEquals(group, "[A, B, C]");
    }


      public void testFlatten() {
        InstructionGroup group=   einstein.asm :: {
                     resource "stack:FlattenStack" stack;
                     (<< "text:A") >> stack;
                     (<< "text:B") >> stack;
                     (<< "text:C") >> stack;
                     [browse all stack, << "text:D"] & [browse all stack];
                     flatten;

        };
        assertResultEquals(group, "[A, B, C, D, A, B, C]");
    }

      public void testCurrent() {
        InstructionGroup group=   einstein.asm :: {
                    read "text:A";
                    [current, << "text:B"];

        };
        assertResultEquals(group, "[A, B]");
    }

     public void testPositionOperator1() {
        InstructionGroup group=   einstein.asm :: {
                     [<< "text:A", << "text:B"];
                     0;

        };
        assertResultEquals(group, "A");
    }

       public void testPositionOperator2() {
        InstructionGroup group=   einstein.asm :: {
                     [<< "text:A", << "text:B", << "text:C"];
                     1 to end;

        };
        assertResultEquals(group, "[B, C]");
    }
   
       public void testPositionOperator3() {
        InstructionGroup group=   einstein.asm :: {
                     [<< "text:A", << "text:B", << "text:C"];
                     0 to 1;

        };
        assertResultEquals(group, "[A, B]");
    }

       public void testPositionOperator4() {
        InstructionGroup group=   einstein.asm :: {
                     [<< "text:A", << "text:B", << "text:C"];
                     0 to 0;

        };
        assertResultEquals(group, "[A]");
    }

       public void testPositionOperator5() {
        InstructionGroup group=   einstein.asm :: {
                     [[<< "text:A", << "text:B", << "text:C"], [<< "text:D", << "text:E", << "text:F"], [<< "text:G", << "text:H", << "text:I"]];
                     each {0};

        };
        assertResultEquals(group, "[A, D, G]");
    }



}