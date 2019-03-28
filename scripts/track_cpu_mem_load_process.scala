#!/usr/bin/env scala
import java.time.LocalDateTime
import sun.misc.Signal
import sun.misc.SignalHandler
import sys.process._
import java.io.File
import util.control.Breaks._
import scala.collection.mutable.ListBuffer

object Main {
	def main(args: Array[String]) {

	  	val sleeptime=10000 // precision miliseconds
	  	var timestamp = new ListBuffer[String]()
	  	var cpus = new ListBuffer[Float]()
	  	var mems = new ListBuffer[Float]()

	  	if (args.size <= 0) {
	  		println("Need some application e.g. (./track_cpu_mem_load_process.scala APP)")
	  		sys.exit
	  	}


		Signal.handle(new Signal("INT"), new SignalHandler() {
			def handle(sig: Signal) {
			println(f"\nwill write file\n")
			sys.exit(0)
			}
		})

		while (true) {
			// continue
			breakable {
				val ts = new java.util.Date
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

					println("gabage")

					//get first line and split the values in a list

					// TODO the split by space is ustable, sometimes it make a list of a size,
					// sometimes with another size! 
					// IDK how I will solve this bug! 
					val line = output.split("\n")(0).split(" ") 

					line.foreach(println)

					println("gabage")
					// -5 is cpu
					// -7 is mem
					println(line(line.size-5).replace(",","."))
					println(line(line.size-7).replace(",","."))

					//cpus+=line(13).replace(",",".").toFloat


					Thread.sleep(sleeptime)
					
				}	
			}
		}
	}

}

