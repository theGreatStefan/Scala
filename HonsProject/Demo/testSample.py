import numpy as np
import matplotlib.pyplot as plt

def sampleNonUniform(center, radi):
    for i in range(10000):
        rcloud = radi
        x = np.random.normal(0,rcloud)
        y = np.random.normal(0,rcloud)
        dist = np.sqrt((x*x)+(y*y))
        u = np.random.normal(0,1/3)
        x = rcloud*x*(u/dist)+center
        y = rcloud*y*(u/dist)+center
        numbers1.append(x)
        numbers2.append(y)


numbers1 = []
numbers2 = []
sampleNonUniform(0,0.5)
plt.scatter(numbers1, numbers2, s=1, c='k')
plt.title("Non-uniform distribution")
plt.show()