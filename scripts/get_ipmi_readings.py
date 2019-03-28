import os
import commands
import time
import signal
 
medicoes = []
 
while 1:
    try:
    timestamp = time.ctime()
        saida = commands.getoutput("sudo ipmitool sdr elist full | grep 77h")
        lista = saida.split('|')
        power = float(lista[len(lista)-1].split(' ')[1])
        medicoes.append([timestamp,power])
    except KeyboardInterrupt:
    print "writing file"
    arq = open("ipmi_measurements.csv", "w")
    arq.write("timestamp;power\n")
    for meas in medicoes:
        arq.write(meas[0]+";"+str(meas[1])+"\n")
    arq.close()
    print "done"
    quit()