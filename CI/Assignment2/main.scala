import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object main extends App {
    val filename = "data/test1.csv"
    val pw = new PrintWriter(new File(filename))

    var input:Array[String] = Array("abcdef",
                                    "bbdhg",
                                    "cabf")

    for (i <- 0 to 0) { 
        var swarm1 = new swarm(30, 10, 3, 0.7, 0.7, 0.9, -100, 100, input)
        swarm1.runSwarm(10)
    }

}