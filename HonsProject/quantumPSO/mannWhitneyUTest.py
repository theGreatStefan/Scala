import numpy as np
import csv
import statistics as stats
from scipy.stats import mannwhitneyu

#stock = "SOL"
stocks = ['AGL','GFI','IMP','NED','REM','SBK','SNH','SOL']

for stock in stocks:
    print("[+] Stock: ",stock)
    CEQPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_Sigmoid_WeightDecay005_Vmax40_quantum10_"+stock+"_netProfit_per_simulation.csv")))
    #CEQPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_Sigmoid_quantum_"+stock+"_netProfit_per_simulation.csv")))
    CEPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_"+stock+"_netProfit_per_simulation.csv")))

    CEQPSO_in = []
    CEQPSO_out = []
    CEPSO_in = []
    CEPSO_out = []

    #print("CEPSO_in \t\t CEQPSO_in \t\t CEPSO_out \t\t CEQPSO_out")
    for i in range(30):
        #print(CEPSO_data[i][0],"\t",CEQPSO_data[i][0],"\t",CEPSO_data[i][1],"\t",CEQPSO_data[i][1])
        CEPSO_in.append(float(CEPSO_data[i][0]))
        CEPSO_out.append(float(CEPSO_data[i][1]))
        CEQPSO_in.append(float(CEQPSO_data[i][0]))
        CEQPSO_out.append(float(CEQPSO_data[i][1]))

    mean_CEPSO_in = stats.median(CEPSO_in)
    mean_CEPSO_out = stats.median(CEPSO_out)
    mean_CEQPSO_in = stats.median(CEQPSO_in)
    mean_CEQPSO_out = stats.median(CEQPSO_out)
    '''print("[-] in_data:  ",mannwhitneyu(CEPSO_in, CEQPSO_in, alternative='less'))
    print("[-] in_data:  ",mannwhitneyu(CEQPSO_in, CEPSO_in, alternative='less'))
    print(" > Mean in data for CEPSO:",mean_CEPSO_in)
    print(" > Mean in data for CEQPSO:",mean_CEQPSO_in)'''
    print("[-] out_data: ",mannwhitneyu(CEPSO_out, CEQPSO_out))
    print("[-] out_data: ",mannwhitneyu(CEPSO_out, CEQPSO_out, alternative='less'))
    print("[-] out_data: ",mannwhitneyu(CEQPSO_out, CEPSO_out, alternative='less'))
    print(" > Mean out data for CEPSO:",mean_CEPSO_out)
    print(" > Mean out data for CEQPSO:",mean_CEQPSO_out)
    print("")