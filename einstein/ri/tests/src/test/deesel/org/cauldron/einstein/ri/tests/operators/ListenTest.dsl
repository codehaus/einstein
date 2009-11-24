package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;


//TODO: Figure out how to test this.
public class ListenTest extends EinsteinTestRunner {

        public ListenTest() {
            super("ListenTest");

        }


    public void testListenInstruction1() {
        InstructionGroup group=   einstein.asm :: {

            resource "time(schedule=cron):0/10 * * * * ?" everyTenSeconds;

            listen everyTenSeconds "bool:true" {
                   >> "console:The time is: ";
            };


            (<< "text:dummy") <! everyTenSeconds : {
                   >> "console:The time is: ";
            };


        };

    }



   

}