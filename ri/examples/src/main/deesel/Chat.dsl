new EinsteinScriptRunner(einstein.asm :: {
    
    resource "jgroups:chat" multicast;
    resource "stack:name" name;

    read "console:Your name:" >> name;

    listen multicast { >> "console(format=printf):%s said '%s'%n" };
    poll "console:>" { [browse name, current] >> multicast };

}).call();
