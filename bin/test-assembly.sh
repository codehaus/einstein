mvn assembly:assembly
rm -rf tmp
mkdir tmp
cd tmp
unzip ../target/*.zip
cd einstein
cd examples
EXAMPLES_JAR=`ls einstein-reference-implementation-examples-*.jar`
../bin/ec -cp .:$EXAMPLES_JAR -sp .
../bin/einstein -cp .:$EXAMPLES_JAR Newsreader
