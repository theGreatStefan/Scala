import numpy as np
import matplotlib.pyplot as plt
import csv

data1 = list(csv.reader(open("SA/IMP.csv")))

timeseries = []
timeseriesDates = []

for i in range(1, len(data1)):
    timeseries.append(float(data1[i][1]))
    timeseriesDates.append((data1[i][0])[0 : 4])

#plt.plot(timeseriesDates, timeseries)
plt.plot(timeseries)
plt.title("Time series")
plt.ylabel("Price")
plt.xlabel("Time")
plt.show()

