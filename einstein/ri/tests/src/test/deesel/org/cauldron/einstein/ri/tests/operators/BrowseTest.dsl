package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;


//TODO: This needs to be tested against a resource that really supports browsing.
public class BrowseTest extends EinsteinTestRunner {

        public BrowseTest() {
            super("BrowseTest");

        }


    public void testBrowseInstruction1() {
        InstructionGroup group=   einstein.asm :: {
                    resource "stack:1" stack;
                    read "text:A";
                    write stack;
                    read "text:B";
                    write stack;
                    browse stack;
        };
        assertResultEquals(group, "B");
    }

    public void testBrowseInstructionMessage1() {
        InstructionGroup group=   einstein.asm :: {
                    resource "stack:2" stack;
                    read "text:A";
                    write stack;
                    read "text:B";
                    write stack;
                    browse  stack;

        };
        assertResultEquals(group, "B");
    }

    public void testBrowseInstructionAllMessage1() {
        InstructionGroup group=   einstein.asm :: {

                    resource "stack:3" stack;
                    read "text:A";
                    write  stack;
                    read "text:B";
                    write  stack;
                    browse all  stack;


        };
        assertResultEquals(group, "[A, B]");
    }

    public void testBrowseInstructionAll1() {
        InstructionGroup group=   einstein.asm :: {

                    resource "stack:4" stack;
                    read "text:A" >> stack;
                    read "text:B" >> stack;

                    browse all stack;


        };
        assertResultEquals(group, "[A, B]");
    }



    public void testBrowseOperator1() {
        InstructionGroup group=   einstein.asm :: {

                    resource "stack:5" stack;
                    read "text:A" >> stack;
                    read "text:B" >> stack;
                    <? stack;

        };
        assertResultEquals(group, "B");
    }



   

}