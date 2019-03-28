import commands
import time
import sys
 
def combine_timestamp(geral, prec):
    lista = geral.split(' ')
    return lista[0] + " " + lista[1] + " " + lista[2] + " " + lista[3] + "." + str(prec).split('.')[1] + " "+lista[4]
 
cpus = []
mems = []
sleeptime=0.1 #precision
if len(sys.argv) <= 1:
    print "Need some application e.g. (" +sys.argv[0]+" APP)"
    exit(0)

while 1:
    try:
        #use the command to get cpu and memory usage information, collecting a timestamp as well
        timestamp = time.ctime()
        prectime = time.time()

        saida = commands.getoutput("top -bn1 | grep "+sys.argv[1].split('\n')[0]).split('\n')[0]
        if(saida == ""):
            time.sleep(sleeptime)
            print "wait..."
            continue
 
        #parse result and store it
        lista = saida.split('\n')[0].split(' ')
         
        timestamp = combine_timestamp(timestamp, prectime)

        mems.append((timestamp, float(lista[len(lista)-5].replace(',','.'))))
        cpus.append(float(lista[len(lista)-7].replace(',','.')))
 
        prectime2 = time.time()
        diff = prectime2 - prectime
        if diff < sleeptime:
            time.sleep(sleeptime - diff)
    except KeyboardInterrupt:
        print "will write file"
        arq = open("cpu_mem_usage.csv", "w")
        arq.write("timestamp;cpu;mem\n")
        for i in range(len(mems)):
            arq.write(mems[i][0]+";"+str(cpus[i])+";"+str(mems[i][1])+"\n")
        arq.close()
        print "done"
        quit()