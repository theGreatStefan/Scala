import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object AMPSO extends App {
    val filename = "data/testAM.csv"
    val pw = new PrintWriter(new File(filename))

    var input:Array[String] = Array("abcdef",
                                    "bbdhg",
                                    "cabf")

    for (i <- 0 to 0) { 
        var swarm1 = new AMSwarm(30, 10, 3, 1.496180, 1.496180, 0.729844, input)
        swarm1.runSwarm(5000)
    }

}