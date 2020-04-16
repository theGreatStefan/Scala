import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object binPSO extends App {
    val filename = "data/testBin.csv"
    val pw = new PrintWriter(new File(filename))

    var avg_velocity_mag:Array[Double] = Array.fill(5000){0.0}
    var avg_diversity_mag:Array[Double] = Array.fill(5000){0.0}

    var input:Array[String] = Array("abcdef",
                                    "bbdhg",
                                    "cabf")

    for (i <- 0 to 0) { 
        var swarm1 = new binSwarm(30, 10, 3, 2.0, 2.0, 1.0, input)
        swarm1.runSwarm(5000)
        avg_velocity_mag = swarm1.velocityMag_overTime.clone()
        avg_diversity_mag = swarm1.euclidean_distance_overTime.clone()
    }

    for (i <- 0 to 4999) {
        pw.write(avg_velocity_mag(i)+","+avg_diversity_mag(i)+"\n")
    }

    pw.close()

}