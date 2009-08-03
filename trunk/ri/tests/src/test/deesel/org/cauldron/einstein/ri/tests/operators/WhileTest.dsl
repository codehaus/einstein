package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class WhileTest extends EinsteinTestRunner {

        public WhileTest() {
            super("WhileTest");

        }

    public void test() {
        InstructionGroup group=   einstein.asm :: {
                     << "text:A";
                     while "xpath:.[text()='A']" {
                        >> "console:";
                     };
        };
        assertResultEquals(group, "A");
    }

   
    public void testNot() {
        InstructionGroup group=   einstein.asm :: {
                     resource "counter:1:5" counter;
                     read counter;
                     while not "regex:3" {
                         read counter;
                     };
        };
        assertResultEquals(group, "3");
    }

}