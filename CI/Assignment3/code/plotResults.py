import numpy as np
import matplotlib.pyplot as plt
import csv
import pandas

data1 = pandas.read_csv("tests/f1_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
data2 = pandas.read_csv("tests/f5_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
data3 = pandas.read_csv("tests/f12_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
data4 = pandas.read_csv("tests/f24_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
data9 = pandas.read_csv("tests/f8_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])

#data1 = pandas.read_csv("tests/f1_standard_beta05_pr01_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
#data2 = pandas.read_csv("tests/f1_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
#data3 = pandas.read_csv("tests/f1_selfAdapt_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
#data4 = pandas.read_csv("tests/f24_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
#data9 = pandas.read_csv("tests/f8_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])

#data1 = pandas.read_csv("tests/f1_dynamicB_dynamicP_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
#data2 = pandas.read_csv("tests/f1_dynamicB_dynamicP_boltzmann.csv", sep=",", names=['1', '2', '3', '4', '5'])
#data3 = pandas.read_csv("tests/f1_selfAdapt_standard.csv", sep=",", names=['1', '2', '3', '4', '5'])
#data4 = pandas.read_csv("tests/f1_selfAdapt_boltzmann.csv", sep=",", names=['1', '2', '3', '4', '5'])

data5 = pandas.read_csv("tests/f1_dynamicB_dynamicP_boltzmann.csv", sep=",", names=['1', '2', '3', '4', '5'])
data6 = pandas.read_csv("tests/f5_dynamicB_dynamicP_boltzmann.csv", sep=",", names=['1', '2', '3', '4', '5'])
data7 = pandas.read_csv("tests/f12_dynamicB_dynamicP_boltzmann.csv", sep=",", names=['1', '2', '3', '4', '5'])
data8 = pandas.read_csv("tests/f24_dynamicB_dynamicP_boltzmann.csv", sep=",", names=['1', '2', '3', '4', '5'])
data10 = pandas.read_csv("tests/f8_dynamicB_dynamicP_boltzmann.csv", sep=",", names=['1', '2', '3', '4', '5'])

#labels = ['f1','f5','f8','f12','f24']
#labels = ['\u03B2=1.9, p\u1D63=0.5','\u03B2=1.5, p\u1D63=0.5','\u03B2=0.5, p\u1D63=0.5','\u03B2=0.1, p\u1D63=0.5','\u03B2=0.5, p\u1D63=0.1']
labels = ['f1 elitism','f1 boltzmann','f5 elitism','f5 boltzmann','f8 elitism','f8 boltzmann','f12 elitism','f12 boltzmann','f24 elitism','f24 boltzmann',]
xmin = 1000000
xmax = -1000000

avgEucl1 = np.array(data1['1'])
avgDiffVecMag1 = np.array(data1['2'])
avgViolations1 = np.array(data1['3'])
avgBestSol1 = np.array(data1['4'])
avgNumReplace1 = np.array(data1['5'])

avgEucl2 = np.array(data2['1'])
avgDiffVecMag2 = np.array(data2['2'])
avgViolations2 = np.array(data2['3'])
avgBestSol2 = np.array(data2['4'])
avgNumReplace2 = np.array(data2['5'])

avgEucl3 = np.array(data3['1'])
avgDiffVecMag3 = np.array(data3['2'])
avgViolations3 = np.array(data3['3'])
avgBestSol3 = np.array(data3['4'])
avgNumReplace3 = np.array(data3['5'])

avgEucl4 = np.array(data4['1'])
avgDiffVecMag4 = np.array(data4['2'])
avgViolations4 = np.array(data4['3'])
avgBestSol4 = np.array(data4['4'])
avgNumReplace4 = np.array(data4['5'])

avgEucl5 = np.array(data5['1'])
avgDiffVecMag5 = np.array(data5['2'])
avgViolations5 = np.array(data5['3'])
avgBestSol5 = np.array(data5['4'])
avgNumReplace5 = np.array(data5['5'])

avgEucl6 = np.array(data6['1'])
avgDiffVecMag6 = np.array(data6['2'])
avgViolations6 = np.array(data6['3'])
avgBestSol6 = np.array(data6['4'])
avgNumReplace6 = np.array(data6['5'])

avgEucl7 = np.array(data7['1'])
avgDiffVecMag7 = np.array(data7['2'])
avgViolations7 = np.array(data7['3'])
avgBestSol7 = np.array(data7['4'])
avgNumReplace7 = np.array(data7['5'])

avgEucl8 = np.array(data8['1'])
avgDiffVecMag8 = np.array(data8['2'])
avgViolations8 = np.array(data8['3'])
avgBestSol8 = np.array(data8['4'])
avgNumReplace8 = np.array(data8['5'])

avgEucl9 = np.array(data9['1'])
avgDiffVecMag9 = np.array(data9['2'])
avgViolations9 = np.array(data9['3'])
avgBestSol9 = np.array(data9['4'])
avgNumReplace9 = np.array(data9['5'])

avgEucl10 = np.array(data10['1'])
avgDiffVecMag10 = np.array(data10['2'])
avgViolations10 = np.array(data10['3'])
avgBestSol10 = np.array(data10['4'])
avgNumReplace10 = np.array(data10['5'])

time = list(range(0, 5000))

plt.plot(time, avgEucl1)
plt.title("Diversity over time")
plt.ylabel("Average Euclidean distance")
plt.legend(['f1'])
plt.show()

plt.plot(time, avgDiffVecMag1)
plt.title("Difference vector magnitude over time")
plt.ylabel("Difference vector magnitude")
plt.legend(['f1'])
plt.show()

plt.plot(time, avgViolations1)
plt.title("Violations over time")
plt.ylabel("Violations")
plt.legend(['f1'])
plt.show()

plt.plot(time, avgBestSol1)
plt.title("Quality of the best solution over time")
plt.ylabel("Quality of the best solution")
plt.legend(['f1'])
plt.show()

plt.plot(time, avgNumReplace1)
plt.title("Number of times the offspring replaces the parent over time")
plt.ylabel("Number of replacements")
plt.legend(['f1'])
plt.show()

# Normalise data

avgEucl1 = avgEucl1/np.amax(avgEucl1)*100
avgEucl2 = avgEucl2/np.amax(avgEucl2)*100
avgEucl3 = avgEucl3/np.amax(avgEucl3)*100
avgEucl4 = avgEucl4/np.amax(avgEucl4)*100
avgEucl5 = avgEucl5/np.amax(avgEucl5)*100
avgEucl6 = avgEucl6/np.amax(avgEucl6)*100
avgEucl7 = avgEucl7/np.amax(avgEucl7)*100
avgEucl8 = avgEucl8/np.amax(avgEucl8)*100
avgEucl9 = (avgEucl9-np.amin(avgEucl9))/(np.amax(avgEucl9)-np.amin(avgEucl9))*100
avgEucl10 = (avgEucl10-np.amin(avgEucl10))/(np.amax(avgEucl10)-np.amin(avgEucl10))*100


avgDiffVecMag1 = avgDiffVecMag1/np.amax(avgDiffVecMag1)*100
avgDiffVecMag2 = avgDiffVecMag2/np.amax(avgDiffVecMag2)*100
avgDiffVecMag3 = avgDiffVecMag3/np.amax(avgDiffVecMag3)*100
avgDiffVecMag4 = avgDiffVecMag4/np.amax(avgDiffVecMag4)*100
avgDiffVecMag5 = avgDiffVecMag5/np.amax(avgDiffVecMag5)*100
avgDiffVecMag6 = avgDiffVecMag6/np.amax(avgDiffVecMag6)*100
avgDiffVecMag7 = avgDiffVecMag7/np.amax(avgDiffVecMag7)*100
avgDiffVecMag8 = avgDiffVecMag8/np.amax(avgDiffVecMag8)*100
avgDiffVecMag9 = (avgDiffVecMag9-np.amin(avgDiffVecMag9))/(np.amax(avgDiffVecMag9)-np.amin(avgDiffVecMag9))*100
avgDiffVecMag10 = (avgDiffVecMag10-np.amin(avgDiffVecMag10))/(np.amax(avgDiffVecMag10)-np.amin(avgDiffVecMag10))*100

avgViolations1 = avgViolations1/np.amax(avgViolations1)*100
avgViolations2 = avgViolations2/np.amax(avgViolations2)*100
avgViolations3 = avgViolations3/np.amax(avgViolations3)*100
avgViolations4 = avgViolations4/np.amax(avgViolations4)*100
avgViolations5 = avgViolations5/np.amax(avgViolations5)*100
avgViolations6 = avgViolations6/np.amax(avgViolations6)*100
avgViolations7 = avgViolations7/np.amax(avgViolations7)*100
avgViolations8 = avgViolations8/np.amax(avgViolations8)*100
avgViolations9 = (avgViolations9-np.amin(avgViolations9))/(np.amax(avgViolations9)-np.amin(avgViolations9))*100
avgViolations10 = (avgViolations10-np.amin(avgViolations10))/(np.amax(avgViolations10)-np.amin(avgViolations10))*100

avgBestSol1 = avgBestSol1/np.amax(avgBestSol1)*100
avgBestSol2 = avgBestSol2/np.amax(avgBestSol2)*100
avgBestSol3 = avgBestSol3/np.amax(avgBestSol3)*100
#avgBestSol4 = avgBestSol4/np.amax(avgBestSol4)*100
avgBestSol4 = (avgBestSol4-np.amin(avgBestSol4))/(np.amax(avgBestSol4)-np.amin(avgBestSol4))*100
avgBestSol5 = avgBestSol5/np.amax(avgBestSol5)*100
avgBestSol6 = avgBestSol6/np.amax(avgBestSol6)*100
avgBestSol7 = avgBestSol7/np.amax(avgBestSol7)*100
#avgBestSol8 = avgBestSol8/np.amax(avgBestSol8)*100
avgBestSol8 = (avgBestSol8-np.amin(avgBestSol8))/(np.amax(avgBestSol8)-np.amin(avgBestSol8))*100
avgBestSol9 = (avgBestSol9-np.amin(avgBestSol9))/(np.amax(avgBestSol9)-np.amin(avgBestSol9))*100
avgBestSol10 = (avgBestSol10-np.amin(avgBestSol10))/(np.amax(avgBestSol10)-np.amin(avgBestSol10))*100

avgNumReplace1 = avgNumReplace1/np.amax(avgNumReplace1)*100
avgNumReplace2 = avgNumReplace2/np.amax(avgNumReplace2)*100
avgNumReplace3 = avgNumReplace3/np.amax(avgNumReplace3)*100
avgNumReplace4 = avgNumReplace4/np.amax(avgNumReplace4)*100
avgNumReplace5 = avgNumReplace5/np.amax(avgNumReplace5)*100
avgNumReplace6 = avgNumReplace6/np.amax(avgNumReplace6)*100
avgNumReplace7 = avgNumReplace7/np.amax(avgNumReplace7)*100
avgNumReplace8 = avgNumReplace8/np.amax(avgNumReplace8)*100
avgNumReplace9 = (avgNumReplace9-np.amin(avgNumReplace9))/(np.amax(avgNumReplace9)-np.amin(avgNumReplace9))*100
avgNumReplace10 = (avgNumReplace10-np.amin(avgNumReplace10))/(np.amax(avgNumReplace10)-np.amin(avgNumReplace10))*100
    

plt.plot(time, avgEucl1)
plt.plot(time, avgEucl5, '--')
plt.plot(time, avgEucl2)
plt.plot(time, avgEucl6, '--')
plt.plot(time, avgEucl9)
plt.plot(time, avgEucl10, '--')
plt.plot(time, avgEucl3)
plt.plot(time, avgEucl7, '--')
plt.plot(time, avgEucl4)
plt.plot(time, avgEucl4, '--')
plt.title("Diversity over time")
plt.ylabel("Average Euclidean distance")
plt.legend(labels)
plt.show()

plt.plot(time, avgDiffVecMag1)
plt.plot(time, avgDiffVecMag5, '--')
plt.plot(time, avgDiffVecMag2)
plt.plot(time, avgDiffVecMag6, '--')
plt.plot(time, avgDiffVecMag9)
plt.plot(time, avgDiffVecMag10, '--')
plt.plot(time, avgDiffVecMag3)
plt.plot(time, avgDiffVecMag7, '--')
plt.plot(time, avgDiffVecMag4)
plt.plot(time, avgDiffVecMag8, '--')
plt.title("Average difference vector magnitude over time")
plt.ylabel("Average difference vector magnitude")
plt.legend(labels)
plt.show()

plt.plot(time, avgViolations1)
plt.plot(time, avgViolations5, '--')
plt.plot(time, avgViolations2)
plt.plot(time, avgViolations6, '--')
plt.plot(time, avgViolations9)
plt.plot(time, avgViolations10, '--')
plt.plot(time, avgViolations3)
plt.plot(time, avgViolations7, '--')
plt.plot(time, avgViolations4)
plt.plot(time, avgViolations8, '--')
plt.title("Average boundary violations over time")
plt.ylabel("Average boundary violations")
plt.legend(labels)
plt.show()

plt.plot(time, avgBestSol1)
plt.plot(time, avgBestSol5, '--')
plt.plot(time, avgBestSol2)
plt.plot(time, avgBestSol6, '--')
plt.plot(time, avgBestSol9)
plt.plot(time, avgBestSol10, '--')
plt.plot(time, avgBestSol3)
plt.plot(time, avgBestSol7, '--')
plt.plot(time, avgBestSol4)
plt.plot(time, avgBestSol8, '--')
plt.title("Average best solution over time")
plt.ylabel("Average best solution")
plt.legend(labels)
plt.show()

plt.plot(time, avgNumReplace1)
plt.plot(time, avgNumReplace5)
#plt.plot(time, avgNumReplace2)
#plt.plot(time, avgNumReplace9)
#plt.plot(time, avgNumReplace3)
#plt.plot(time, avgNumReplace4)
plt.title("Average number of replacements over time")
plt.ylabel("Average number of replacements")
plt.legend(labels)
plt.show()

#dynamicApproach = []
#for i in time:
#    dynamicApproach.append(1.9*np.exp(-(i/1000.0))+0.1)

#plt.plot(time, dynamicApproach)
#plt.title("Dynamic approach to \u03B2")
#plt.ylabel("\u03B2")
#plt.xlabel("time")
#plt.show()

#dynamicApproach = []
#for i in time:
#    dynamicApproach.append(0.9*np.exp(-(i/1000.0))+0.1)

#plt.plot(time, dynamicApproach)
#plt.title("Dynamic approach to p\u1D63")
#plt.ylabel("p\u1D63")
#plt.xlabel("time")
#plt.show()