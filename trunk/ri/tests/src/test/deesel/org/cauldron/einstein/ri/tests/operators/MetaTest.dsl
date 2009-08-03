package org.cauldron.einstein.ri.tests.operators;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;



public class MetaTest extends EinsteinTestRunner {

        public MetaTest() {
            super("MetaTest");

        }


    public void testUsingOperator() {
        InstructionGroup group=   einstein.asm :: {

            @(DataModel:XMLDOM) : {
                read "text:Hello";
            }

        };

    }


    public void testUsingInstruction() {
        InstructionGroup group=   einstein.asm :: {

            using (DataModel:XMLDOM) {
                read "text:Hello";
            }

        };

    }

   

}