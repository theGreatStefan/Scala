import numpy as np
import csv
import statistics as stats
from scipy.stats import mannwhitneyu

stocks = ['AGL','GFI','IMP','NED','REM','SBK','SNH','SOL']

CEPSO_in = []
CEPSO_out = []

for stock in stocks:
    CEPSO_data = list(csv.reader(open("../testOutput/SA/CEPSO_"+stock+".csv")))
    CEPSO_in.append(float(CEPSO_data[0][0]))
    CEPSO_out.append(float(CEPSO_data[0][1]))

index = 0
print("\t CEPSO_in \t CEPSO_out")
for stock in stocks:
    print(stock,"\t",round(CEPSO_in[index],4),"\t",round(CEPSO_out[index],4))
    index += 1

