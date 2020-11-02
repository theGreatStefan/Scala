import numpy as np
import csv
import statistics as stats
from scipy.stats import mannwhitneyu
import matplotlib.pyplot as plt


stocks = ['AGL','GFI','IMP','NED','REM','SBK','SNH','SOL']

# -------------------- TABLE --------------------
CEQPSO_in = []
CEQPSO_out = []
BH_in = []
BH_out = []
avg_CEQPSO_in = 0
avg_CEQPSO_out = 0
avg_BH_in = 0
avg_BH_out = 0

for stock in stocks:
    #CEQPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_Sigmoid_quantum_"+stock+".csv")))
    CEQPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_"+stock+".csv")))
    BH_data = list(csv.reader(open("../testOutput/SA/BH_"+stock+".csv")))
    CEQPSO_in.append(float(CEQPSO_data[0][0]))
    CEQPSO_out.append(float(CEQPSO_data[0][1]))
    BH_in.append(float(BH_data[0][0]))
    BH_out.append(float(BH_data[0][1]))
    avg_CEQPSO_in += float(CEQPSO_data[0][0])
    avg_CEQPSO_out += float(CEQPSO_data[0][1])
    avg_BH_in += float(BH_data[0][0])
    avg_BH_out += float(BH_data[0][1])

avg_CEQPSO_in = avg_CEQPSO_in/8.0
avg_CEQPSO_out = avg_CEQPSO_out/8.0
avg_BH_in = avg_BH_in/8.0
avg_BH_out = avg_BH_out/8.0
index = 0
print("\t CEQPSO_in \t B&H_in \t CEQPSO_out \t B&H_out")
for stock in stocks:
    print(stock,"\t",round(CEQPSO_in[index],4),"\t",round(BH_in[index],4),"\t",round(CEQPSO_out[index],4),"\t",round(BH_out[index],4))
    index += 1

print("AVG","\t",round(avg_CEQPSO_in,4),"\t",round(avg_BH_in,4),"\t",round(avg_CEQPSO_out,4),"\t",round(avg_BH_out,4))
# ------------------ TABLE END ------------------

CEQPSO_in = []
CEQPSO_out = []
CEPSO_in = []
CEPSO_out = []
BH_in = []
BH_out = []
RULE_in = []
RULE_out = []
CEQPSO_avg_in = 0
CEQPSO_avg_out = 0
CEPSO_avg_in = 0
CEPSO_avg_out = 0
BH_avg_in = 0
BH_avg_out = 0
RULE_avg_in = 0
RULE_avg_out = 0

for stock in stocks:
    CEQPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_Sigmoid_WeightDecay005_Vmax40_quantum_oneOut_"+stock+".csv")))
    CEPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_Sigmoid_WeightDecay005_Vmax40_quantum10_"+stock+".csv")))
    #CEPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_"+stock+".csv")))
    BH_data = list(csv.reader(open("../testOutput/SA/BH_"+stock+".csv")))
    RULE_data = list(csv.reader(open("../testOutput/SA/Rule_"+stock+".csv")))
    CEQPSO_in.append(float(CEQPSO_data[0][0]))
    CEQPSO_out.append(float(CEQPSO_data[0][1]))
    CEPSO_in.append(float(CEPSO_data[0][0]))
    CEPSO_out.append(float(CEPSO_data[0][1]))
    BH_in.append(float(BH_data[0][0]))
    BH_out.append(float(BH_data[0][1]))
    RULE_in.append(float(RULE_data[0][0]))
    RULE_out.append(float(RULE_data[0][1]))

    CEQPSO_avg_in += float(CEQPSO_data[0][0])/8.0
    CEQPSO_avg_out += float(CEQPSO_data[0][1])/8.0
    CEPSO_avg_in += float(CEPSO_data[0][0])/8.0
    CEPSO_avg_out += float(CEPSO_data[0][1])/8.0
    BH_avg_in += float(BH_data[0][0])/8.0
    BH_avg_out += float(BH_data[0][1])/8.0
    RULE_avg_in += float(RULE_data[0][0])/8.0
    RULE_avg_out += float(RULE_data[0][1])/8.0

CEQPSO_in.append(CEQPSO_avg_in)
CEQPSO_out.append(CEQPSO_avg_out)
CEPSO_in.append(CEPSO_avg_in)
CEPSO_out.append(CEPSO_avg_out)
BH_in.append(BH_avg_in)
BH_out.append(BH_avg_out)
RULE_in.append(RULE_avg_in)
RULE_out.append(RULE_avg_out)

# Best Net Profit for in data samples
ind = np.arange(1, 10)      # the x locations for the groups
width = 0.20                # the width of the bars
labels = ['AGL','GFI','IMP','NED','REM','SBK','SOL','SNH', 'AVG']

p1 = plt.bar(ind, CEQPSO_in, width)
p2 = plt.bar(ind+width, CEPSO_in, width)
p3 = plt.bar(ind+width*2, BH_in, width)
p4 = plt.bar(ind+width*3, RULE_in, width)
plt.xticks(ind+(width), labels)
plt.legend((p1[0], p2[0], p3[0], p4[0]), ('CEQPSO', 'CEPSO', 'BH', 'Rule'))
plt.title("Annualised returns (%) of in sample data")
plt.xlabel("Stock")
plt.ylabel("Returns (%)")
plt.show()

# Best Net Profit for out data samples
ind = np.arange(1, 10)      # the x locations for the groups
width = 0.20                # the width of the bars
labels = ['AGL','GFI','IMP','NED','REM','SBK','SOL','SNH', 'AVG']

p1 = plt.bar(ind, CEQPSO_out, width)
p2 = plt.bar(ind+width, CEPSO_out, width)
p3 = plt.bar(ind+width*2, BH_out, width)
p4 = plt.bar(ind+width*3, RULE_out, width)
plt.xticks(ind+(width), labels)
plt.legend((p1[0], p2[0], p3[0], p4[0]), ('CEQPSO', 'CEPSO', 'BH', 'Rule'))
plt.title("Annualised returns (%) of out sample data")
plt.xlabel("Stock")
plt.ylabel("Returns (%)")
plt.show()