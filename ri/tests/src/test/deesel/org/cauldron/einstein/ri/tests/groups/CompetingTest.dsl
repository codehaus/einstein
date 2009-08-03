package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;



public class CompetingTest extends EinsteinTestRunner {

        public CompetingTest() {
            super("CompetingTest");

        }

    //This one should succeed as one instruction will succeed
    public void testCompetingInstruction1() {
        InstructionGroup group=   einstein.asm :: {
             resource "stack:Stack" stack;

             //create a void message
             if "regex:AAA" {
                current ;
             };

            (read from stack, split, read from stack);

        };
        assertSuccess(group);

    }

    
 //TODO: Create a proper exception scenario.
//This one should fail
  public void testCompetingInstruction2() {
        InstructionGroup group=   einstein.asm :: {

             resource "stack:Stack" stack;

             //create a void message
             if "regex:AAA" {
                current ;
             };

            (split, split, split);

        };
    //    assertFailure(group);

    }
   
    //This one should succeed as all will succeed
    public void testCompetingInstruction1() {
        InstructionGroup group=   einstein.asm :: {
             resource "stack:Stack" stack;
             (<< "text:A") >> stack;
             (<< "text:B") >> stack;
             (<< "text:C") >> stack;

            (read from stack, read from stack, read from stack);

        };
        assertSuccess(group);

    }

}