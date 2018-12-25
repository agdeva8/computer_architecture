
import pandas as pd
import numpy as np
import numpy as np
import scipy.interpolate as interp
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

print("reading data from csv file... ")
result =pd.read_csv('result.csv')


X = result['width'].values.tolist()
Y = result['prob'].values.tolist()
Z = result['time'].values.tolist()

print("creating mash grid for x,y and z list...")
plotx,ploty, = np.meshgrid(np.linspace(np.min(X),np.max(X),10),\
                           np.linspace(np.min(Y),np.max(Y),10))
plotz = interp.griddata((X,Y),Z,(plotx,ploty),method='linear')

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.plot_surface(plotx,ploty,plotz,cstride=1,rstride=1,cmap='viridis')  # or 'hot'

print("giving labels and titles to graph ...")
ax.set_xlabel("width")
ax.set_ylabel("probablity")
ax.set_zlabel("time taken")

ax.set_title("3d plot of time taken by infiltrator vs probablity and width")

plt.show()
#plt.savefig("graph_timeTaken.png")
#plt.close()

