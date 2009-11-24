    new EinsteinScriptRunner(einstein.asm :: {

    resource "console:>" stdin;
    resource "console(format=printf):Didn't understand '%s', you could try: news, time, say, count, name, password or history.%n" error;
    resource "stack:Passwords" passwords;
    resource "stack:History" history;
    resource "counter:1" counter;
    resource "stack:name" name;
    resource "jgroups:chat" multicast;

    (<< "text:Unknown") >> name;

    listen multicast { >> "console(format=printf):%s said '%s'%n" };

    while not "regex:.*exit.*" {

             resource "stack:CurrentCommand" currentCommand;
             resource "stack:SplitCommand" splitCommand;

             read stdin;

             //Make sure something was entered
             if  "regex:.+" {
             
                 write to currentCommand;
                 split with "regex:(\\w+)";
                 (0 to end) >> splitCommand;
                 element 0;


                 choose from [ news : {     clear;
                                       using ( DataModel : XMLDOM ) {
                                            read payload from "camel:http://rss.news.yahoo.com/rss/topstories";
                                            split with "xpath://item/title/text()" { write payload to "console:Headline: "};
                                       };

                                  },


                          time : {
                                      read from "time:HH:mm:ss";
                                      write payload to "console:The time is ";
                                 },

                          password : {
                                      read from "console(mask=X):Enter your password: " >> passwords;
                                 },

                          name : {
                                      read "console:Your name:" >> name;
                                 },

                          history : {
                                      browse all history;
                                      each { (~ [0,1] ) >> "console:>>"};
                                 },

                          count : {
                                     resource "counter:1:10" counter;
                                     read all counter;
                                     write payload to "console: Counting ";
                                 },
                          say : {
                                     read from splitCommand;
                                     //To make the next line work properly the String Data Model must be implemented
                                     //Then we can flatten and merge to create a single text output.
                                     [browse name, 1 to end] >> multicast;
                                 },
                          exit : {
                                  (<< "text:Goodbye!") >> "console:";
                                 },

                          default : {
                                       write  to error;
                                  }
                        ];

                [id: read from counter, command : read from currentCommand, time : read from "time:HH:mm:ss"] >> history;

            };

    };

  

}).call();
