import java.time.LocalDateTime
import sun.misc.Signal
import sun.misc.SignalHandler
import sys.process._
import java.io._
import util.control.Breaks._
import scala.collection.mutable.ListBuffer
import java.io.StringWriter
import au.com.bytecode.opencsv.CSVWriter
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

			val out = new BufferedWriter(new FileWriter("test.csv"));
			val writer = new CSVWriter(out);

			val header: List[String] =
    			List("timestamp", "cpu", "mem")

    		val rows: List[List[String]] =
  			timestamp.zip(cpus.zip(mems)).foldLeft(List.empty[List[String]]){
    			case (acc, (a, (b, c))) => List(a, b, c) +: acc
  			}.reverse

    		var listOfRecords= List(header,rows)

    		println(listOfRecords)
    		// TODO write csv 
    		writer.writeAll(listOfRecords)
			out.close()
    		
    		//println(writeCsvFile("test.csv", header, rows))

  
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

					println("gabage")

					//get first line and split the values in a list
					val line = output.split("\n")(0).split(" +") 

					line.foreach(println)

					println(line.size)

					println("gabage")
					// -4 is cpu
					// -3 is mem
					// println(line(line.size-4).replace(",","."))
					// println(line(line.size-3).replace(",","."))
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

