#!/usr/bin/env scala

import java.time.LocalDateTime
import sun.misc.Signal
import sun.misc.SignalHandler
import sys.process._
import java.io._
import util.control.Breaks._
import scala.collection.mutable.ListBuffer
import java.io.StringWriter
import java.io.FileWriter
import java.io.BufferedWriter
import scala.collection.JavaConverters._
import scala.util._

object Main {
    def main(args: Array[String]) {

        var timestamp = new ListBuffer[String]()
        var measurements = new ListBuffer[String]()

        Signal.handle(new Signal("INT"), new SignalHandler() {
            def handle(sig: Signal) {
            println(f"\nwill write file\n")

            val file = new File("ipmi_measurements.csv")
            val bw = new BufferedWriter(new FileWriter(file))

            bw.write("timestamp;power\n")

            for( i <- 0 to timestamp.size-1){
                var string = timestamp(i)+";"+measurements(i)+"\n"
                bw.write(string)
            }
            bw.close()   
            sys.exit(0)
            }
        })

        while (true) {

            val command = "sudo ipmitool sdr elist full | grep 77"  
            // exec a extern command 
            val output = (command).!!
            //get first line and split the values in a list
            val line = output.split("|") 
            val ts = new java.util.Date()
            timestamp+=ts.toString
            measurements+=line(line.size-1).split(" ")(1)        
        }
    }
}

