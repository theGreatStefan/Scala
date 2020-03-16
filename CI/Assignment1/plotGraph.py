import numpy as np
import matplotlib.pyplot as plt
import csv

data1 = list(csv.reader(open("data/f5/f5_nc_4.csv")))
data2 = list(csv.reader(open("data/f5/f5_cd_4.csv")))
data3 = list(csv.reader(open("data/f5/f5_cn1_4.csv")))
data4 = list(csv.reader(open("data/f5/f5_cn4_4.csv")))
data5 = list(csv.reader(open("data/f5/f5_ck1_4.csv")))
data6 = list(csv.reader(open("data/f5/f5_ck4_4.csv")))
data7 = list(csv.reader(open("data/f5/f5_ce_4.csv")))

avgEucl1 = []
avgGbest1 = []
avgVelMag1 = []
avgEucl2 = []
avgGbest2 = []
avgVelMag2 = []
avgEucl3 = []
avgGbest3 = []
avgVelMag3 = []
avgEucl4 = []
avgGbest4 = []
avgVelMag4 = []
avgEucl5 = []
avgGbest5 = []
avgVelMag5 = []
avgEucl6 = []
avgGbest6 = []
avgVelMag6 = []
avgEucl7 = []
avgGbest7 = []
avgVelMag7 = []
time = list(range(0, 5000))

for i in time:
    avgEucl1.append(float(data1[i][0]))
    avgGbest1.append(float(data1[i][1]))
    avgVelMag1.append(float(data1[i][2]))
    avgEucl2.append(float(data2[i][0]))
    avgGbest2.append(float(data2[i][1]))
    avgVelMag2.append(float(data2[i][2]))
    avgEucl3.append(float(data3[i][0]))
    avgGbest3.append(float(data3[i][1]))
    avgVelMag3.append(float(data3[i][2]))
    avgEucl4.append(float(data4[i][0]))
    avgGbest4.append(float(data4[i][1]))
    avgVelMag4.append(float(data4[i][2]))
    avgEucl5.append(float(data5[i][0]))
    avgGbest5.append(float(data5[i][1]))
    avgVelMag5.append(float(data5[i][2]))
    avgEucl6.append(float(data6[i][0]))
    avgGbest6.append(float(data6[i][1]))
    avgVelMag6.append(float(data6[i][2]))
    avgEucl7.append(float(data7[i][0]))
    avgGbest7.append(float(data7[i][1]))
    avgVelMag7.append(float(data7[i][2]))


#plt.subplot(3,1,1)
#plt.ylim(top=10000)
plt.plot(time, avgEucl1, label="no clamping")
plt.plot(time, avgEucl2, label="dynamic clamping")
plt.plot(time, avgEucl3, label="normal clamping 0.1")
plt.plot(time, avgEucl4, label="normal clamping 0.9")
plt.plot(time, avgEucl5, label="basic clamping 0.1")
plt.plot(time, avgEucl6, label="basic clamping 0.9")
plt.plot(time, avgEucl7, label="exponential clamping 1.5")
plt.title("Diversity over time")
plt.ylabel("Average Euclidean distance")
plt.xlabel("Time")
plt.legend()
plt.show()

#plt.subplot(3,1,2)
plt.plot(time, avgGbest1, label="no clamping")
plt.plot(time, avgGbest2, label="dynamic clamping")
plt.plot(time, avgGbest3, label="normal clamping 0.1")
plt.plot(time, avgGbest4, label="normal clamping 0.9")
plt.plot(time, avgGbest5, label="basic clamping 0.1")
plt.plot(time, avgGbest6, label="basic clamping 0.9")
plt.plot(time, avgGbest7, label="exponential clamping 1.5")
plt.title("Qualtity of global best over time")
plt.ylabel("Average Global best")
plt.xlabel("Time")
plt.legend()
plt.show()

#plt.subplot(3,1,3)
plt.plot(time, avgVelMag1, label="no clamping")
plt.plot(time, avgVelMag2, label="dynamic clamping")
plt.plot(time, avgVelMag3, label="normal clamping 0.1")
plt.plot(time, avgVelMag4, label="normal clamping 0.9")
plt.plot(time, avgVelMag5, label="basic clamping 0.1")
plt.plot(time, avgVelMag6, label="basic clamping 0.9")
plt.plot(time, avgVelMag7, label="exponential clamping 1.5")
plt.title("Velocity magnitude over time")
plt.ylabel("Velocity magnitude")
plt.xlabel("Time")
plt.legend()
plt.show()
