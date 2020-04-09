import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object binPSO extends App {
    val filename = "data/testBin.csv"
    val pw = new PrintWriter(new File(filename))

    var input:Array[String] = Array("abcdef",
                                    "bbdhg",
                                    "cabf")

    for (i <- 0 to 0) { 
        var swarm1 = new binSwarm(30, 10, 3, 2.0, 2.0, 1.0, input)
        swarm1.runSwarm(5000)
    }

}