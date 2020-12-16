Files:
=======
 - Data: Contains the historic price data.
 - DataManager: Contains the readData.scala to extract the historic price data for use later on.
 - PSO: Contains the standard PSO implementation a.k.a. CEPSO.
 - quantumPSO: Contains the quantum CEQPSO implemention.
 - quantumPSOOneOut: Contains the regression implementation.
 - testOutput: Contains the output files of the captured data.

QuantumPSO:
===========
To run:
- Compile: ``
   $ make
   ``
 - Run CEQPSO: ``
   $ make run
   ``
 - Run Buy and Hold strategy: ``
   $ make runBH
   ``
 - Run Rule based strategy: ``
   $ make runRule
   ``
- Clean class files: ``
   $ make clean
   ``

To view the results:
- Run the appropriate python script such as 'plotData.py' or 'table.py'.

*The same applies to all other implementations*.
