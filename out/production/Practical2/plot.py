def read_file(filename):
    f = open(filename, "r")
    for line in f.readlines():
        print(line)

read_file('../eval.txt')