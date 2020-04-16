import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object AMPSO extends App {
    val filename = "data/testAM.csv"
    val pw = new PrintWriter(new File(filename))

    var avg_velocity_mag:Array[Double] = Array.fill(5000){0.0}
    var avg_diversity_mag:Array[Double] = Array.fill(5000){0.0}

    var input:Array[String] = Array("abcdef",
                                    "bbdhg",
                                    "cabf")

    for (i <- 0 to 0) { 
        var swarm1 = new AMSwarm(30, 10, 3, 1.496180, 1.496180, 0.729844, input) //1.496180, 1.496180, 0.729844
        swarm1.runSwarm(5000)

        avg_velocity_mag = swarm1.velocityMag_overTime.clone()
        avg_diversity_mag = swarm1.euclidean_distance_overTime.clone()
    }

    for (i <- 0 to 4999) {
        pw.write(avg_velocity_mag(i)+","+avg_diversity_mag(i)+"\n")
    }

    pw.close()
}