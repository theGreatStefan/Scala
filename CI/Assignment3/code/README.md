Instructions on how to compile and execute the DE:
====================================================
* **Compiling**:
    From the command line run the command:
        ```
        $ make compile
        ```

* **Executing the DE**:
    From the command line run the command:
        ```
        $ make run
        ```

* **To plot the data**:
    From the command line run the command:
        ```
        $ python3 plotData.py
        ```

* **To clean class data**:
    From the command line run the command:
        ```
        $ make clean
        ```

Instructions on how to alter the current test executed by Both PSOs:
===================================================================    
* **In DE.scala**:
    - change the filename to the file that the data is to be saved in.
    - rename the benchmark function to be considered to input instead of input{NUMBER}.
    - change the paramters from line 34 to 47.
    
* **In plotResults.py**:
    - change the file names to the csv files to be plot.
