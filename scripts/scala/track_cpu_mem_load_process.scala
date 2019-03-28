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

	  	val sleeptime=1000 // precision miliseconds
	  	var timestamp = new ListBuffer[String]()
	  	var cpus = new ListBuffer[String]()
	  	var mems = new ListBuffer[String]()

	  	if (args.size <= 0) {
	  		println("Need some application e.g. (./track_cpu_mem_load_process.scala APP)")
	  		sys.exit
	  	}

		Signal.handle(new Signal("INT"), new SignalHandler() {
			def handle(sig: Signal) {
			println(f"\nwill write file\n")

			
			val file = new File("cpu_mem_usage.csv")
			val bw = new BufferedWriter(new FileWriter(file))

    		bw.write("timestamp;cpu;mem\n")

    		for( i <- 0 to timestamp.size-1){
    			var string = timestamp(i)+";"+cpus(i)+";"+mems(i)+"\n"
    			bw.write(string)
      		}
			bw.close()	 
			sys.exit(0)
			}
		})

		while (true) {
			// continue
			breakable {
				val top = "top -bn1"  
				val grep = "grep " + args(0).toString
				
				// if the APP not run yet than wait 
				if ((top #| grep ).! == 1){
					println("wait...")
					Thread.sleep(sleeptime)
					break
				}else{
					// exec a extern command 
					val output = (top #| grep).!!

					//get first line and split the values in a list
					val line = output.split("\n")(0).split(" +") 
					// -4 is cpu
					// -3 is mem
					val ts = new java.util.Date()

					timestamp+=ts.toString
					cpus+=line(line.size-4).replace(",",".")
					mems+=line(line.size-3).replace(",",".")

					Thread.sleep(sleeptime)					
				}	
			}
		}
	}
}

