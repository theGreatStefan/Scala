all: 	compile run py

compile:
	scalac -d classes *.scala

clean:
	rm classes/*

run:
	scala -classpath classes main

py:
	python3 plotResults.py
