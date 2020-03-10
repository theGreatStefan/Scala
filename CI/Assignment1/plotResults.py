import numpy as np
import matplotlib.pyplot as plt
import csv

data = list(csv.reader(open("data/f24/f24_ck4_6.csv")))

avgEucl = []
avgGbest = []
avgVelMag = []
time = list(range(0, 5000))

for i in time:
    avgEucl.append(float(data[i][0]))
    avgGbest.append(float(data[i][1]))
    avgVelMag.append(float(data[i][2]))

plt.subplot(3,1,1)
plt.plot(time, avgEucl)
plt.title("Diversity over time")
plt.ylabel("Average Euclidean distance")

plt.subplot(3,1,2)
plt.plot(time, avgGbest, markersize=0.5)
plt.title("Qualtity of global best over time")
plt.ylabel("Average Global best")

plt.subplot(3,1,3)
plt.plot(time, avgVelMag, markersize=0.5)
plt.title("Velocity magnitude over time")
plt.ylabel("Velocity magnitude")
plt.show()
