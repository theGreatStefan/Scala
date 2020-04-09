all: 	compile

compile:
	scalac -d classes *.scala

clean:
	rm classes/*

runBin:
	scala -classpath classes binPSO

runAM:
	scala -classpath classes AMPSO

py:
	python3 plotResults.py
