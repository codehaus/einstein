package org.cauldron.einstein.ri.tests.providers;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class RegexTest extends EinsteinTestRunner {

        public RegexTest() {
            super("RegexTest");

        }


    public void testSplit() {
        InstructionGroup group=   einstein.asm :: {

                     resource "text:Anyone who has never made a mistake has never tried anything new." quote;
                     read from quote;
                     split "regex:(\\w+)";
        };
        assertResultEquals(group, "[Anyone, who, has, never, made, a, mistake, has, never, tried, anything, new]");
    }


    public void testCombined() {
        InstructionGroup group=   einstein.asm :: {

                     resource "text:Anyone who has never made a mistake has never tried anything new." quote;
                     resource "stack:stack" stack;

                     read from quote;
                     if "regex:Anyone.*" {
                        split "regex:(\\w+)";
                        write each to stack;
                     };
                     browse all from stack;
        };
        assertResultEquals(group, "[Anyone, who, has, never, made, a, mistake, has, never, tried, anything, new]");
    }


     public void testWithPositions() {
            InstructionGroup group=   einstein.asm :: {

                         resource "text:Anyone who has never made a mistake has never tried anything new." quote;

                         read from quote;
                         split "regex:(\\w+)";

                         [2, 5, 6, << "text:been", 4];
            };
            assertResultEquals(group, "[has, a, mistake, been, made]");
        }


   

}