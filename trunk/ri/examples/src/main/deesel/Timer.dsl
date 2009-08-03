new EinsteinScriptRunner(einstein.asm :: {

    resource "time(schedule=cron):0/10 * * * * ?" everyTenSeconds;
    resource "time(schedule=cron):0/1 * * * * ?" everySecond;
    resource "stack:TimeStack" stack;

    (<< "time:yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
    write payload "console:The time according to Einstein is ";

    listen everyTenSeconds "bool:true" {read all from stack; write payload to "console:";};
    
    listen everySecond "bool:true" {write to stack};

    execute "java:org.cauldron.einstein.ri.examples.SleepHack";
  

}).call();
