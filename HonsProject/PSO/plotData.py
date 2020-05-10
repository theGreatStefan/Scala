import numpy as np
import matplotlib.pyplot as plt
import csv

#data1 = list(csv.reader(open("../testOutput/velocity.csv")))
#data2 = list(csv.reader(open("../testOutput/HOF.csv")))
#data3 = list(csv.reader(open("../testOutput/bestNetProfit.csv")))
#data4 = list(csv.reader(open("../testOutput/BH_BP.csv")))
#data5 = list(csv.reader(open("../testOutput/BH_GE.csv")))
#data6 = list(csv.reader(open("../testOutput/BH_HSBC.csv")))
#data7 = list(csv.reader(open("../testOutput/BH_MSFT.csv")))
#data8 = list(csv.reader(open("../testOutput/BH_RIO.csv")))
#data9 = list(csv.reader(open("../testOutput/BH_T.csv")))
#data10 = list(csv.reader(open("../testOutput/BH_VOD.csv")))
#data11 = list(csv.reader(open("../testOutput/BH_XOM.csv")))
#data12 = list(csv.reader(open("../testOutput/CEPSO_BP.csv")))
#data13 = list(csv.reader(open("../testOutput/CEPSO_GE.csv")))
#data14 = list(csv.reader(open("../testOutput/CEPSO_HSBC.csv")))
#data15 = list(csv.reader(open("../testOutput/CEPSO_MSFT.csv")))
#data16 = list(csv.reader(open("../testOutput/CEPSO_RIO.csv")))
#data17 = list(csv.reader(open("../testOutput/CEPSO_T.csv")))
#data18 = list(csv.reader(open("../testOutput/CEPSO_VOD.csv")))
#data19 = list(csv.reader(open("../testOutput/CEPSO_XOM.csv")))
#data1 = list(csv.reader(open("../testOutput/SALatest/velocity.csv")))
#data2 = list(csv.reader(open("../testOutput/SALatest/HOF.csv")))
#data4 = list(csv.reader(open("../testOutput/SALatest/BH_AGL.csv")))
#data5 = list(csv.reader(open("../testOutput/SALatest/BH_GFI.csv")))
#data6 = list(csv.reader(open("../testOutput/SALatest/BH_IMP.csv")))
#data7 = list(csv.reader(open("../testOutput/SALatest/BH_NED.csv")))
#data8 = list(csv.reader(open("../testOutput/SALatest/BH_REM.csv")))
#data9 = list(csv.reader(open("../testOutput/SALatest/BH_SBK.csv")))
#data10 = list(csv.reader(open("../testOutput/SALatest/BH_SOL.csv")))
#data11 = list(csv.reader(open("../testOutput/SALatest/BH_SNH.csv")))
#data12 = list(csv.reader(open("../testOutput/SALatest/CEPSO_AGL.csv")))
#data13 = list(csv.reader(open("../testOutput/SALatest/CEPSO_GFI.csv")))
#data14 = list(csv.reader(open("../testOutput/SALatest/CEPSO_IMP.csv")))
#data15 = list(csv.reader(open("../testOutput/SALatest/CEPSO_NED.csv")))
#data16 = list(csv.reader(open("../testOutput/SALatest/CEPSO_REM.csv")))
#data17 = list(csv.reader(open("../testOutput/SALatest/CEPSO_SBK.csv")))
#data18 = list(csv.reader(open("../testOutput/SALatest/CEPSO_SOL.csv")))
#data19 = list(csv.reader(open("../testOutput/SALatest/CEPSO_SNH.csv")))
data1 = list(csv.reader(open("../testOutput/SA/velocity.csv")))
data2 = list(csv.reader(open("../testOutput/SA/HOF.csv")))
data4 = list(csv.reader(open("../testOutput/SA/BH_AGL.csv")))
data5 = list(csv.reader(open("../testOutput/SA/BH_GFI.csv")))
data6 = list(csv.reader(open("../testOutput/SA/BH_IMP.csv")))
data7 = list(csv.reader(open("../testOutput/SA/BH_NED.csv")))
data8 = list(csv.reader(open("../testOutput/SA/BH_REM.csv")))
data9 = list(csv.reader(open("../testOutput/SA/BH_SBK.csv")))
data10 = list(csv.reader(open("../testOutput/SA/BH_SOL.csv")))
data11 = list(csv.reader(open("../testOutput/SA/BH_SNH.csv")))
data12 = list(csv.reader(open("../testOutput/SA/CEPSO_Softplus_AGL.csv")))
data13 = list(csv.reader(open("../testOutput/SA/CEPSO_Softplus_GFI.csv")))
data14 = list(csv.reader(open("../testOutput/SA/CEPSO_Softplus_IMP.csv")))
data15 = list(csv.reader(open("../testOutput/SA/CEPSO_Softplus_NED.csv")))
data16 = list(csv.reader(open("../testOutput/SA/CEPSO_Softplus_REM.csv")))
data17 = list(csv.reader(open("../testOutput/SA/CEPSO_Softplus_SBK.csv")))
data18 = list(csv.reader(open("../testOutput/SA/CEPSO_Softplus_SOL.csv")))
data19 = list(csv.reader(open("../testOutput/SA/CEPSO_Softplus_SNH.csv")))
data20 = list(csv.reader(open("../testOutput/SA/Rule_AGL.csv")))
data21 = list(csv.reader(open("../testOutput/SA/Rule_GFI.csv")))
data22 = list(csv.reader(open("../testOutput/SA/Rule_IMP.csv")))
data23 = list(csv.reader(open("../testOutput/SA/Rule_NED.csv")))
data24 = list(csv.reader(open("../testOutput/SA/Rule_REM.csv")))
data25 = list(csv.reader(open("../testOutput/SA/Rule_SBK.csv")))
data26 = list(csv.reader(open("../testOutput/SA/Rule_SOL.csv")))
data27 = list(csv.reader(open("../testOutput/SA/Rule_SNH.csv")))

data28 = list(csv.reader(open("../testOutput/SA/CEPSO_Stagnated_AGL.csv")))
data29 = list(csv.reader(open("../testOutput/SA/CEPSO_Stagnated_GFI.csv")))
data30 = list(csv.reader(open("../testOutput/SA/CEPSO_Stagnated_IMP.csv")))
data31 = list(csv.reader(open("../testOutput/SA/CEPSO_Stagnated_NED.csv")))
data32 = list(csv.reader(open("../testOutput/SA/CEPSO_Stagnated_REM.csv")))
data33 = list(csv.reader(open("../testOutput/SA/CEPSO_Stagnated_SBK.csv")))
data34 = list(csv.reader(open("../testOutput/SA/CEPSO_Stagnated_SOL.csv")))
data35 = list(csv.reader(open("../testOutput/SA/CEPSO_Stagnated_SNH.csv")))

avgVelMag = []
avgEuclDist = []
iterationBest = []
HOFin = []
HOFout = []

BH_in = []
CEPSO_in = []
Rule_in = []
BH_out = []
CEPSO_out = []
Rule_out = []

CEPSO_Stag_in = []
CEPSO_Stag_out = []

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
Rule_in.append(float(data20[0][0]))
BH_in.append(float(data5[0][0]))
CEPSO_in.append(float(data13[0][0]))
Rule_in.append(float(data21[0][0]))
BH_in.append(float(data6[0][0]))
CEPSO_in.append(float(data14[0][0]))
Rule_in.append(float(data22[0][0]))
BH_in.append(float(data7[0][0]))
CEPSO_in.append(float(data15[0][0]))
Rule_in.append(float(data23[0][0]))
BH_in.append(float(data8[0][0]))
CEPSO_in.append(float(data16[0][0]))
Rule_in.append(float(data24[0][0]))
BH_in.append(float(data9[0][0]))
CEPSO_in.append(float(data17[0][0]))
Rule_in.append(float(data25[0][0]))
BH_in.append(float(data10[0][0]))
CEPSO_in.append(float(data18[0][0]))
Rule_in.append(float(data26[0][0]))
BH_in.append(float(data11[0][0]))
CEPSO_in.append(float(data19[0][0]))
Rule_in.append(float(data27[0][0]))

#bestNetProfit_out.append(float(data3[0][1]))
BH_out.append(float(data4[0][1]))
CEPSO_out.append(float(data12[0][1]))
Rule_out.append(float(data20[0][1]))
BH_out.append(float(data5[0][1]))
CEPSO_out.append(float(data13[0][1]))
Rule_out.append(float(data21[0][1]))
BH_out.append(float(data6[0][1]))
CEPSO_out.append(float(data14[0][1]))
Rule_out.append(float(data22[0][1]))
BH_out.append(float(data7[0][1]))
CEPSO_out.append(float(data15[0][1]))
Rule_out.append(float(data23[0][1]))
BH_out.append(float(data8[0][1]))
CEPSO_out.append(float(data16[0][1]))
Rule_out.append(float(data24[0][1]))
BH_out.append(float(data9[0][1]))
CEPSO_out.append(float(data17[0][1]))
Rule_out.append(float(data25[0][1]))
BH_out.append(float(data10[0][1]))
CEPSO_out.append(float(data18[0][1]))
Rule_out.append(float(data26[0][1]))
BH_out.append(float(data11[0][1]))
CEPSO_out.append(float(data19[0][1]))
Rule_out.append(float(data27[0][1]))

avgBH_in = sum(BH_in)/8
BH_in.append(avgBH_in)
avgBH_out = sum(BH_out)/8
BH_out.append(avgBH_out)
avgCEPSO_in = sum(CEPSO_in)/8
CEPSO_in.append(avgCEPSO_in)
avgCEPSO_out = sum(CEPSO_out)/8
CEPSO_out.append(avgCEPSO_out)
avgRule_in = sum(Rule_in)/8
Rule_in.append(avgRule_in)
avgRule_out = sum(Rule_out)/8
Rule_out.append(avgRule_out)

CEPSO_Stag_in.append(float(data28[0][0]))
CEPSO_Stag_in.append(float(data29[0][0]))
CEPSO_Stag_in.append(float(data30[0][0]))
CEPSO_Stag_in.append(float(data31[0][0]))
CEPSO_Stag_in.append(float(data32[0][0]))
CEPSO_Stag_in.append(float(data33[0][0]))
CEPSO_Stag_in.append(float(data34[0][0]))
CEPSO_Stag_in.append(float(data35[0][0]))
avgCEPSO_Stag_in = sum(CEPSO_Stag_in)/8
CEPSO_Stag_in.append(avgCEPSO_Stag_in)

CEPSO_Stag_out.append(float(data28[0][1]))
CEPSO_Stag_out.append(float(data29[0][1]))
CEPSO_Stag_out.append(float(data30[0][1]))
CEPSO_Stag_out.append(float(data31[0][1]))
CEPSO_Stag_out.append(float(data32[0][1]))
CEPSO_Stag_out.append(float(data33[0][1]))
CEPSO_Stag_out.append(float(data34[0][1]))
CEPSO_Stag_out.append(float(data35[0][1]))
avgCEPSO_Stag_out = sum(CEPSO_Stag_out)/8
CEPSO_Stag_out.append(avgCEPSO_Stag_out)

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
ind = np.arange(1, 10)      # the x locations for the groups
width = 0.25                # the width of the bars
labels = ['AGL','GFI','IMP','NED','REM','SBK','SOL','SNH', 'AVG']

p1 = plt.bar(ind, BH_in, width)
p2 = plt.bar(ind+width, CEPSO_in, width)
p3 = plt.bar(ind+width*2, Rule_in, width)
plt.xticks(ind+(width), labels)
plt.legend((p1[0], p2[0], p3[0]), ('BH', 'CEPSO', 'Rule'))
plt.title("Annualised returns (%) of in sample data")
plt.xlabel("Stock")
plt.ylabel("Returns (%)")
plt.show()

# Best Net Profit for in and out data samples
ind = np.arange(1, 10)      # the x locations for the groups
width = 0.25                # the width of the bars
labels = ['AGL','GFI','IMP','NED','REM','SBK','SOL','SNH', 'AVG']

p1 = plt.bar(ind, BH_out, width)
p2 = plt.bar(ind+width, CEPSO_out, width)
p3 = plt.bar(ind+width*2, Rule_out, width)
plt.xticks(ind+(width), labels)
plt.legend((p1[0], p2[0], p3[0]), ('BH', 'CEPSO', 'Rule'))
plt.title("Annualised returns (%) of the out sample data")
plt.xlabel("Stock")
plt.ylabel("Returns (%)")
plt.show()

# Best Net Profit for in and out data samples
ind = np.arange(1, 10)      # the x locations for the groups
width = 0.35                # the width of the bars
labels = ['AGL','GFI','IMP','NED','REM','SBK','SOL','SNH', 'AVG']

p1 = plt.bar(ind, CEPSO_in, width)
p2 = plt.bar(ind+width, CEPSO_Stag_in, width)
plt.xticks(ind+(width), labels)
plt.legend((p1[0], p2[0]), ('CEPSO', 'CEPSO stagnated'))
plt.title("Annualised returns (%) of the in sample data (leakyReLU vs sigmoid)")
plt.xlabel("Stock")
plt.ylabel("Returns (%)")
plt.show()

# Best Net Profit for in and out data samples
ind = np.arange(1, 10)      # the x locations for the groups
width = 0.35                # the width of the bars
labels = ['AGL','GFI','IMP','NED','REM','SBK','SOL','SNH', 'AVG']

p1 = plt.bar(ind, CEPSO_out, width)
p2 = plt.bar(ind+width, CEPSO_Stag_out, width)
plt.xticks(ind+(width), labels)
plt.legend((p1[0], p2[0]), ('CEPSO', 'CEPSO stagnated'))
plt.title("Annualised returns (%) of the out sample data (leakyReLU vs sigmoid)")
plt.xlabel("Stock")
plt.ylabel("Returns (%)")
plt.show()

# Best Net Profit of the swarm per iteration
plt.plot(iterationBest)
plt.title("Best Net Profit over time")
plt.ylabel("Net Profit")
plt.xlabel("Time")
plt.show()