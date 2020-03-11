import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object main extends App {
    var avgEuclDist:Array[Double] = Array.fill(5000){0}
    var avgGBest:Array[Double] = Array.fill(5000){0}
    var avgPersentageLeft:Double = 0
    var avgVelocityMagnitude:Array[Double] = Array.fill(5000){0}
    
    val filename = "data/f24/f24_cd_6.csv"
    val pw = new PrintWriter(new File(filename))

    for (i <- 0 to 30) {
        var swarm1 = new swarm(30, 30, 0.5, 0.5, 1.0, 0.25, 10, "f24")
        swarm1.runSwarm(5000)
        
        var avgEuc:Array[Double] = swarm1.getAvgEuclDist().clone()
        for (j <- 0 to avgEuclDist.length-1) {
            avgEuclDist(j) += avgEuc(j)
        }

        var avgGb:Array[Double] = swarm1.getAvgGBest().clone()
        for (j <- 0 to avgGb.length-1) {
            avgGBest(j) += avgGb(j)
        }

        avgPersentageLeft += swarm1.percentageOutside()

        var avgVm:Array[Double] = swarm1.getAvgVelocityMagnitude().clone()
        for (j <- 0 to avgVelocityMagnitude.length-1) {
            avgVelocityMagnitude(j) += avgVm(j)
        }
    }

    for (i <- 0 to avgEuclDist.length-1) {
        avgEuclDist(i) = avgEuclDist(i)/30
    }
 
    for (i <- 0 to avgGBest.length-1) {
        avgGBest(i) = avgGBest(i)/30
    }

    avgPersentageLeft = (avgPersentageLeft)/30

    for (i <- 0 to avgVelocityMagnitude.length-1) {
        avgVelocityMagnitude(i) = avgVelocityMagnitude(i)/30
    }

    //println("Average Euclidian distance over time:")
    //(for (i <- 0 to avgEuclDist.length-1) yield (println(avgEuclDist(i)+" , "+avgGBest(i)+" , "+avgVelocityMagnitude(i))))
    (for (i <- 0 to avgEuclDist.length-1) yield (pw.write(avgEuclDist(i)+" , "+avgGBest(i)+" , "+avgVelocityMagnitude(i)+"\n")))
    pw.close()
    println("Percentage of particles that left the search area:")
    println(avgPersentageLeft)

}