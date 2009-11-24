package org.cauldron.einstein.ri.tests.providers;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class CounterTest extends EinsteinTestRunner {

        public CounterTest() {
            super("CounterTest");

        }

    public void testForwardStep() {
        InstructionGroup group=   einstein.asm :: {

                     resource "counter:1:10:3" count;
                     
                     while count {
                        read count >> "console:Count";
                        browse count;
                     };
        };
        assertResultEquals(group, "10");
    }

    public void testForwardNoStep() {
        InstructionGroup group=   einstein.asm :: {

                     resource "counter:1:10" count;

                     while count {
                        read count >> "console:Count";
                        browse count;
                     };
        };
        assertResultEquals(group, "10");
    }

    public void testReverseStep() {
        InstructionGroup group=   einstein.asm :: {

                     resource "counter:10:1:3" count;

                     while count {
                        read count >> "console:Count";
                        browse count;
                     };
        };
        assertResultEquals(group, "1");
    }

    public void testReverseNoStep() {
        InstructionGroup group=   einstein.asm :: {

                     resource "counter:10:1" count;

                     while count {
                        read count >> "console:Count";
                        browse count;
                     };
        };
        assertResultEquals(group, "1");
    }

}