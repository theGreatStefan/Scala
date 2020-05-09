import numpy as np
import matplotlib.pyplot as plt
import csv

data1 = list(csv.reader(open("data/BinIn1DynTest.csv")))
data2 = list(csv.reader(open("data/AMIn1DynTest.csv"))) 
data4 = list(csv.reader(open("data/BinIn4DynTest.csv")))
data5 = list(csv.reader(open("data/AMIn4DynTest.csv")))
data6 = list(csv.reader(open("data/BinIn5DynTest.csv")))
data7 = list(csv.reader(open("data/AMIn5DynTest.csv")))
data8 = list(csv.reader(open("data/BinIn6DynTest.csv")))
data9 = list(csv.reader(open("data/AMIn6DynTest.csv")))
data10 = list(csv.reader(open("data/BinIn7DynTest.csv")))
data11 = list(csv.reader(open("data/AMIn7DynTest.csv")))
data12 = list(csv.reader(open("data/BinIn2DynTest.csv")))
data13 = list(csv.reader(open("data/AMIn2DynTest.csv")))
data14 = list(csv.reader(open("data/BinIn3DynTest.csv")))
data15 = list(csv.reader(open("data/AMIn3DynTest.csv")))
data3 = list(csv.reader(open("data/percentages.csv")))

avgVelMag1 = []
avgEuclDist1 = []
avgGbest1 = []

avgVelMag2 = []
avgEuclDist2 = []
avgGbest2 = []

percentagesBin = []
percentagesAM = []

gbest1 = []
gbest2 = []
gbest3 = []
gbest4 = []
gbest5 = []
gbest6 = []
gbest7 = []
gbest8 = []
gbest9 = []
gbest10 = []
gbest11 = []
gbest12 = []
gbest13 = []
gbest14 = []

for i in range(0, 5000):
    gbest1.append(float(data1[i][2]))
    gbest2.append(float(data2[i][2]))
    gbest3.append(float(data4[i][2]))
    gbest4.append(float(data5[i][2]))
    gbest5.append(float(data6[i][2]))
    gbest6.append(float(data7[i][2]))
    gbest7.append(float(data8[i][2]))
    gbest8.append(float(data9[i][2]))
    gbest9.append(float(data10[i][2]))
    gbest10.append(float(data11[i][2]))
    gbest11.append(float(data12[i][2]))
    gbest12.append(float(data13[i][2]))
    gbest13.append(float(data14[i][2]))
    gbest14.append(float(data15[i][2]))

for i in range(0, 5000):
    avgVelMag1.append(float(data1[i][0]))
    avgEuclDist1.append(float(data1[i][1]))
    avgGbest1.append(float(data1[i][2]))

    avgVelMag2.append(float(data2[i][0]))
    avgEuclDist2.append(float(data2[i][1]))
    avgGbest2.append(float(data2[i][2]))

for i in np.arange(0, 14, 2):
    percentagesBin.append(float(data3[i][1]))
    percentagesAM.append(float(data3[i+1][1]))


# All gbests BinPSO
plt.plot(gbest1, label = "B1")
plt.plot(gbest3, label = "B2")
plt.plot(gbest5, label = "B3")
plt.plot(gbest7, label = "B4")
plt.plot(gbest9, label = "B5")
plt.plot(gbest11, label = "B6")
plt.plot(gbest13, label = "B7")
plt.title("All global best positions over time for BinPSO")
plt.ylabel("Global best score")
plt.xlabel("Time")
plt.legend()
plt.show()

# All gbests AMPSO
plt.plot(gbest2, label = "B1")
plt.plot(gbest4, label = "B2")
plt.plot(gbest6, label = "B3")
plt.plot(gbest8, label = "B4")
plt.plot(gbest10, label = "B5")
plt.plot(gbest12, label = "B6")
plt.plot(gbest14, label = "B7")
plt.title("All global best positions over time for AMPSO")
plt.ylabel("Global best score")
plt.xlabel("Time")
plt.legend()
plt.show()


# percentages
ind = np.arange(1, 8)
width = 0.4                # the width of the bars

p1 = plt.bar(ind, percentagesBin, width)
p2 = plt.bar(ind+width, percentagesAM, width)
plt.xticks(ind)
plt.title("Percentage of particles that leave the search space")
plt.xlabel("Benchmark")
plt.ylabel("Infeasible (%)")
plt.legend((p1[0], p2[0]), ('BinPSO', 'AMPSO'))
plt.show()

# Velocity Magnitudes
plt.plot(avgVelMag1, label = "BinPSO")
#plt.plot(avgVelMag2, label = "AMPSO")
plt.title("Velocity magnitude over time")
plt.ylabel("Velocity magnitude")
plt.xlabel("Time")
plt.legend()
plt.show()

plt.plot(avgVelMag2, 'C1', label = "AMPSO")
plt.title("Velocity magnitude over time")
plt.ylabel("Velocity magnitude")
plt.xlabel("Time")
plt.legend()
plt.show()

# Diversity
plt.plot(avgEuclDist1, label = "BinPSO")
plt.plot(avgEuclDist2, label = "AMPSO")
plt.title("Diversity over time")
plt.ylabel("Average Euclidean distance")
plt.xlabel("Time")
plt.legend()
plt.show()

# Gbest
plt.plot(avgGbest1, label = "BinPSO")
plt.plot(avgGbest2, label = "AMPSO")
plt.title("Quality of global best position over time")
plt.ylabel("Global best position score")
plt.xlabel("Time")
plt.legend()
plt.show()
