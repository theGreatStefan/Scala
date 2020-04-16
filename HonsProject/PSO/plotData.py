import numpy as np
import matplotlib.pyplot as plt
import csv

data1 = list(csv.reader(open("../testOutput/velocity.csv")))
data2 = list(csv.reader(open("../testOutput/HOF.csv")))
data3 = list(csv.reader(open("../testOutput/bestNetProfit.csv")))
data4 = list(csv.reader(open("../testOutput/BH_BP.csv")))
data5 = list(csv.reader(open("../testOutput/BH_GE.csv")))
data6 = list(csv.reader(open("../testOutput/BH_HSBC.csv")))
data7 = list(csv.reader(open("../testOutput/BH_MSFT.csv")))
data8 = list(csv.reader(open("../testOutput/BH_RIO.csv")))
data9 = list(csv.reader(open("../testOutput/BH_T.csv")))
data10 = list(csv.reader(open("../testOutput/BH_VOD.csv")))
data11 = list(csv.reader(open("../testOutput/BH_XOM.csv")))
data12 = list(csv.reader(open("../testOutput/CEPSO_BP.csv")))
data13 = list(csv.reader(open("../testOutput/CEPSO_GE.csv")))
data14 = list(csv.reader(open("../testOutput/CEPSO_HSBC.csv")))
data15 = list(csv.reader(open("../testOutput/CEPSO_MSFT.csv")))
data16 = list(csv.reader(open("../testOutput/CEPSO_RIO.csv")))
data17 = list(csv.reader(open("../testOutput/CEPSO_T.csv")))
data18 = list(csv.reader(open("../testOutput/CEPSO_VOD.csv")))
data19 = list(csv.reader(open("../testOutput/CEPSO_XOM.csv")))

avgVelMag = []
avgEuclDist = []
iterationBest = []
HOFin = []
HOFout = []

BH_in = []
CEPSO_in = []
BH_out = []
CEPSO_out = []

for i in range(0, 350):
    avgVelMag.append(float(data1[i][0]))
    avgEuclDist.append(float(data1[i][1]))
    iterationBest.append(float(data1[i][2]))

for i in range(0, 10):
    HOFin.append(float(data2[i][0]))
    HOFout.append(float(data2[i][1]))

#bestNetProfit_in.append(float(data3[0][0]))
BH_in.append(float(data4[0][0]))
CEPSO_in.append(float(data12[0][0]))
BH_in.append(float(data5[0][0]))
CEPSO_in.append(float(data13[0][0]))
BH_in.append(float(data6[0][0]))
CEPSO_in.append(float(data14[0][0]))
BH_in.append(float(data7[0][0]))
CEPSO_in.append(float(data15[0][0]))
BH_in.append(float(data8[0][0]))
CEPSO_in.append(float(data16[0][0]))
BH_in.append(float(data9[0][0]))
CEPSO_in.append(float(data17[0][0]))
BH_in.append(float(data10[0][0]))
CEPSO_in.append(float(data18[0][0]))
BH_in.append(float(data11[0][0]))
CEPSO_in.append(float(data19[0][0]))

#bestNetProfit_out.append(float(data3[0][1]))
BH_out.append(float(data4[0][1]))
CEPSO_out.append(float(data12[0][1]))
BH_out.append(float(data5[0][1]))
CEPSO_out.append(float(data13[0][1]))
BH_out.append(float(data6[0][1]))
CEPSO_out.append(float(data14[0][1]))
BH_out.append(float(data7[0][1]))
CEPSO_out.append(float(data15[0][1]))
BH_out.append(float(data8[0][1]))
CEPSO_out.append(float(data16[0][1]))
BH_out.append(float(data9[0][1]))
CEPSO_out.append(float(data17[0][1]))
BH_out.append(float(data10[0][1]))
CEPSO_out.append(float(data18[0][1]))
BH_out.append(float(data11[0][1]))
CEPSO_out.append(float(data19[0][1]))

# Velocity Magnitudes
plt.plot(avgVelMag)
plt.title("Velocity magnitude over time")
plt.ylabel("Velocity magnitude")
plt.xlabel("Time")
plt.show()

# Diversity
plt.plot(avgEuclDist)
plt.title("Diversity over time")
plt.ylabel("Average Euclidean distance")
plt.xlabel("Time")
plt.show()

# HOF Net Profits
# Worth noting that the in data is larger than the out data
ind = np.arange(1, 11)      # the x locations for the groups
width = 0.35                # the width of the bars

p1 = plt.bar(ind, HOFin, width)
p2 = plt.bar(ind+width, HOFout, width)
plt.xticks(ind)
plt.legend((p1[0], p2[0]), ('in data', 'out data'))
plt.title("Returns (%) of the Hall Of Fame particles for the in and out data")
plt.xlabel("Particles")
plt.ylabel("Returns (%)")
plt.show()

# Best Net Profit for in and out data samples
ind = np.arange(1, 9)      # the x locations for the groups
width = 0.35                # the width of the bars
labels = ['BP','GE','HSBC','MSFT','RIO','T','VOD','XOM']

p1 = plt.bar(ind, BH_in, width)
p2 = plt.bar(ind+width, CEPSO_in, width)
plt.xticks(ind+(width/2), labels)
plt.legend((p1[0], p2[0]), ('BH', 'CEPSO'))
plt.title("Annualised returns (%) of in sample data")
plt.xlabel("Particles")
plt.ylabel("Returns (%)")
plt.show()

# Best Net Profit for in and out data samples
ind = np.arange(1, 9)      # the x locations for the groups
width = 0.35                # the width of the bars
labels = ['BP','GE','HSBC','MSFT','RIO','T','VOD','XOM']

p1 = plt.bar(ind, BH_out, width)
p2 = plt.bar(ind+width, CEPSO_out, width)
plt.xticks(ind+(width/2), labels)
plt.legend((p1[0], p2[0]), ('BH', 'CEPSO'))
plt.title("Annualised returns (%) of the out sample data")
plt.xlabel("Particles")
plt.ylabel("Returns (%)")
plt.show()

# Best Net Profit of the swarm per iteration
#plt.plot(iterationBest)
#plt.title("Best Net Profit over time")
#plt.ylabel("Net Profit")
#plt.xlabel("Time")
#plt.show()