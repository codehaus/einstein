package org.cauldron.einstein.ri.tests.models;

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.ri.parser.EinsteinTestRunner;

public class UsingTest extends EinsteinTestRunner {

        public UsingTest() {
            super("UsingTest");

        }


    public void testAlternatingModels() {
        InstructionGroup group=   einstein.asm :: {
           using ( DataModel : POJO ) {
               using ( DataModel : XMLDOM ) {
                    read payload from "camel:http://rss.news.yahoo.com/rss/topstories";
                    split with"xpath://item/title/text()" { write "console:Headline: "};
                    [0];
               };
               [current];
            };
           using ( DataModel : XMLDOM ) {
               [current];
           };


        };
        assertSuccess(group);

    }



   

}