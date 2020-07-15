import numpy as np
import matplotlib.pyplot as plt

def sample(center, radi):
    for i in range(10000):
        rcloud = 1
        x = np.random.normal(0,1)
        y = np.random.normal(0,1)
        dist = np.sqrt(x*x+y*y)
        u = np.random.uniform(0,1)
        x = rcloud*x*(np.power(u,0.5)/dist)
        y = rcloud*y*(np.power(u,0.5)/dist)
        numbers1.append(x)
        numbers2.append(y)

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
#sample(5,1)
sampleNonUniform(5,0.5)
plt.scatter(numbers1, numbers2, s=1)
plt.show()