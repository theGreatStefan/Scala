#Instructions on how to compile and execute the PSO:

* Compiling:
    From the command line run the command:
        ```
        $ make compile
        ```

* Executing the PSO:
    From the command line run the command:
        ```
        $ make run
        ```

* To plot the data:
    From the command line run the command:
        ```
        $ make py
        ```

* To clean class data:
    From the command line run the command:
        ```
        $ make clean
        ```

#Instructions on how to alter the current test executed by the PSO:
    
* In main.scala:
    - in line 11 change the filename to the file that the data is to be saved in.
    - in line 16 change the parameters to suite the new test. (Parameters are described in line 15).
    
* In swarm.scala:
    - in line 88 set the k value for velocity clamping. If no k value is needed, set to k=1.
    - in lines 94-95 comment/uncomment the required clamping method.
    - in lines 97-99 comment/uncomment the required velocity update method.
        - For no-clamping: uncomment 'updateVelocity'.
        - For normalised clamping: uncomment 'updateVelocityClamp2'.
        - For all other clamping: uncomment 'updateVelocityClamp1'.

* In plotResults.py:
    - in line 5 change the file name to the csv file to be plot.
