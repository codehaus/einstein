project:Einstein 
The Language of the Enteprise*

Version: ${project.version}


Licensing
=========

project:Einstein APIs and core runtime classes are licensed under Apache v2.0 
(permissive). Tools are released under AGPL v3.0 (copyleft). Please make sure you 
understand the two licenses before attempting to redistribute the compiler/tools. There
is no intention to place restrictions on the code you have compiled with Einstein; rest
assured code you have compiled yourself will not come under any restrictions and you may
redistribute the core runtime under the very permissive Apache license.

Disclaimer
==========

This release is made avaialble so that interested parties can decide if they want to get
involved in the project it's release name (Uber Experimental) should give you a clue of
what to expect. Things might work, they might not. With any luck the examples will 
compile and run for you; after that you're on your own.

NOTE: There is no support for windows yet, I'm just way to busy at this stage to add
this to my concerns. If you have the volition to produce a wrapper bat file then FAB!
Send me it and I'll add it to the distribution.

Getting Started
===============

The compiler is in the bin directory and is called 'ec' to get started I recommend 
compiling the examples,

cd examples
../bin/ec -cp .:einstein-reference-implementation-examples-${project.version}.jar -sp .

And then run the examples using:

../bin/einstein -cp .:einstein-reference-implementation-examples-${project.version}.jar -sp  <Example-Name>

Swapping <Example-Name> with either Newsreader, Console, Esper etc.

Good luck! Any problems, just e-mail me: neil.ellis@mangala.co.uk.


________________________________________________________________________________________
*"The Language of the Enterprise" s a trademark of Mangala Solutions Ltd.
