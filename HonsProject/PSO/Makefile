all: compile

compile:
	scalac -d classes *.scala
	clear

clean:
	rm classes/*

run:
	scala -classpath classes main

runBH:
	scala -classpath classes BH

runRule:
	scala -classpath classes Rule
