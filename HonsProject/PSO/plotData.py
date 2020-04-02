import numpy as np
import matplotlib.pyplot as plt
import csv

data1 = list(csv.reader(open("../testOutput/velocity.csv")))

avgVelMag = []
avgEuclDist = []

for i in range(0, 350):
    avgVelMag.append(float(data1[i][0]))
    avgEuclDist.append(float(data1[i][1]))

plt.plot(avgVelMag)
plt.title("Velocity magnitude over time")
plt.ylabel("Velocity magnitude")
plt.xlabel("Time")
plt.show()

plt.plot(avgEuclDist)
plt.title("Diversity over time")
plt.ylabel("Average Euclidean distance")
plt.xlabel("Time")
plt.show()