import numpy as np
import matplotlib.pyplot as plt
import csv

data1 = list(csv.reader(open("../testOutput/velocity.csv")))

avgVelMag1 = []

for i in range(0, 100):
    avgVelMag1.append(float(data1[i][0]))

plt.plot(avgVelMag1)
plt.title("Velocity magnitude over time")
plt.ylabel("Velocity magnitude")
plt.xlabel("Time")
plt.show()