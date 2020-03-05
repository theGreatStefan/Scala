import numpy as np
import matplotlib.pyplot as plt

fileIn = input()
fileIn = input()
avgEucl = []
avgGbest = []
avgVelMag = []
time = list(range(0, 5000))

while (not fileIn == "" or not fileIn == ''):
    avgEucl.append(float(fileIn))
    fileIn = input()

plt.subplot(3,1,1)
plt.plot(time, avgEucl, markersize=0.5)
plt.title("Diversity over time")
plt.ylabel("Average Euclidean distance")

fileIn = input()
fileIn = input()

while (not fileIn == "" or not fileIn == ''):
    avgGbest.append(float(fileIn))
    fileIn = input()

plt.subplot(3,1,2)
plt.plot(time, avgGbest, markersize=0.5)
plt.title("Qualtity of global best over time")
plt.ylabel("Average Global best")

fileIn = input()
fileIn = input()
print("Percentage that left the search space: ", fileIn)
fileIn = input()
fileIn = input()
fileIn = input()

while (not fileIn == "" or not fileIn == ''):
    avgVelMag.append(float(fileIn))
    fileIn = input()

plt.subplot(3,1,3)
plt.plot(time, avgVelMag, markersize=0.5)
plt.title("Velocity magnitude over time")
plt.ylabel("Velocity magnitude")
plt.show()
