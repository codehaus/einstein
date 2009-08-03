package org.cauldron.einstein.ri.tests;
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.*;

public class ParserTest extends EinsteinTestRunner {

        public PareserTest() {
            super("ParserTest");

        }
        

    public void test() {
        InstructionGroup group=   einstein.asm :: {
                    read "text: Hello World! ";
                    write "console:And now I say: ";
                    [read "text: Hello", read "text: World"];
                    merge;  
                    write "console:>>>>";
                    [read "text:1", read "text:2"];
                    append [read "text:3", read "text:4"];
                    append [read "text:5", read "text:6"];
                    merge;
                    append [read "text:7"];
                    write "console:Testing, testing ... ";
                    [read "text:1", read "text:2"];
                    each { append [read "text: potatoe"]; };
                    write "console:With potatoes: ";
                    [read "text:1", read "text:2", [read "text:3", read "text:4"]];
                    write "console:Nested: ";



                    read "java:org.cauldron.einstein.ri.test.MyBean";
                    split "xpath:/myOtherBean/name";


        };
        assertResultEquals(group, "[dave]");
    }




    /*
    public void testExecutionOperator() {
        InstructionGroup group=   einstein.asm :: {

                    //If you understand this, I'll give you a <insert-confectionary-item-here>.
                    //TODO: Fix this
                    //   **:::((java.util.concurrent.Callable)({new org.cauldron.einstein.ri.core.resource.StaticResourceRef("text:"+new java.util.Date())}));
                    >> "console: The date is ... ";
           
        };
        assertResultEquals(group, "fail");
    }
    */

    public void testRouteOperator() {
        InstructionGroup group=   einstein.asm :: {

                    ( << "text:red") => "java:org.cauldron.einstein.ri.test.ByPayloadRouter" : [  red : >> "console:Roses are ", blue : >> "console: Violets are "];

        };

        assertResultEquals(group, "red");
     }


    public void testStatefulRoutingUsingResourceKeyword() {
        InstructionGroup group=   einstein.asm :: {

                    resource "java:org.cauldron.einstein.ri.test.AlternatingRouter" myRouter;
                    resource "stack:result" result;
                    << "text: row";
                    route myRouter [even : >> "console:even ", odd : >> "console:odd "];
                    route myRouter [even : >> "console:even ", odd : >> "console:odd "];
                    route myRouter [even : >> "console:even ", odd : >> "console:odd "];
                    route myRouter [even : << "text:even" , odd : << "text:odd"];

        };
        assertResultEquals(group, "odd");
     }



}


