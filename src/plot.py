import matplotlib.pyplot as plt
from numpy import *
from mpl_toolkits import mplot3d

def read_file(filename):
    x = []
    time = []
    nodes = []
    revisions = []
    with open(filename) as fp:
        for line in fp:
            parts = line.strip().split()
            x.append(parts[0])
            time.append(int(parts[2]))
            nodes.append(int(parts[3]))
            revisions.append(int(parts[4]))
    length = int(len(x)/2)

    x = x[:length]
    fc_time = time[:length]
    fc_nodes = nodes[:length]
    fc_revisions = revisions[:length]
    mac_time = time[length:]
    mac_nodes = nodes[length:]
    mac_revisions = revisions[length:]

    # plt.xlabel('n')
    # plt.ylabel('Time(ms)')
    # plt.title('Time')
    # plt.plot(x, fc_time, 'r', label="FC")
    # plt.plot(x, mac_time, 'b', label='MAC')
    # plt.legend(loc="upper left")
    plt.xlabel('n')
    plt.ylabel('Revisions')
    plt.title('Revisions')
    plt.plot(x, fc_revisions, 'r', label="FC")
    plt.plot(x, mac_revisions, 'b', label='MAC')
    plt.legend(loc="upper left")

    plt.show()



read_file('../langfords.txt')