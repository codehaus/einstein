new EinsteinScriptRunner(einstein.asm :: {

    listen "time(schedule=cron):0/1 * * * * ?"  {
    
         execute "java:org.cauldron.einstein.ri.examples.esper.EventMaker";

         listen payload to "esper:select avg(value) from org.cauldron.einstein.ri.examples.esper.AbstractWidget.win:time(30 sec)" {
            extract "xpath:/underlying" >> "console:Tesseract average value is: ";
         };

         if "xpath:.[@value > 90]" {extract "xpath:./@value" >> "console:Over 90 detected "};

    };

    execute "java:org.cauldron.einstein.ri.examples.SleepHack";


    
}).call();
