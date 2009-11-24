new EinsteinScriptRunner(einstein.asm :: {


    using (DataModel:XMLDOM)  {

        resource "time(schedule=cron):0/1 * * * * ?" everySecond;
        
        listen everySecond {

             read payload "camel:http://rss.news.yahoo.com/rss/topstories";
             split "xpath://*[local-name()='content']/@url" {
                using (DataModel:POJO) {
                        execute "java:org.cauldron.einstein.ri.examples.ImageMaker";
                        execute "java:org.cauldron.einstein.ri.examples.MontageMaker";
                        clear;
                };

             };

        };
        
        //We put the sleep hack here because when a resource goes out of scope all it's facades will
        //be stopped, i.e. listeners will no longer listen. This is a gotcha until systems come in
        //in 0.2 really.
        execute "java:org.cauldron.einstein.ri.examples.SleepHack";
    };


}).call();

