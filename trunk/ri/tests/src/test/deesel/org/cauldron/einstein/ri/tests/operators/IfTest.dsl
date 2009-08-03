package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class IfTest extends EinsteinTestRunner {

        public IfTest() {
            super("IfTest");

        }

    public void testInstruction1() {
        InstructionGroup group=   einstein.asm :: {
             << "text:A";
             if "regex:(\\w+)" {
                << "text:B";
             };
        };
        assertResultEquals(group, "B");
    }

    public void testInstruction1() {
        InstructionGroup group=   einstein.asm :: {
             << "text:A";
             if "regex:(\\d+)" {
                << "text:B";
             } else {
                << "text:C";
            };
        };
        assertResultEquals(group, "C");
    }

    public void testInstructionNot1() {
        InstructionGroup group=   einstein.asm :: {
             << "text:A";
             if not "regex:(\\d+)" {
                << "text:B";
             } else {
                << "text:C";
            };
        };
        assertResultEquals(group, "B");
    }

       public void testInstruction1() {
           InstructionGroup group=   einstein.asm :: {
                << "text:A";
                ? "regex:(\\w+)";
           };
           assertResultEquals(group, "A");
       }


       //Need to assert void return types but that needs changes in ETR first.


}