
object main extends App {
    //var allEuclDist:Array[Array[Double]] = Array(Array())
    var avgEuclDist:Array[Double] = Array.fill(5000){0}
    var avgGBest:Array[Double] = Array.fill(5000){0}
    var avgPersentageLeft:Double = 0
    var avgVelocityMagnitude:Array[Double] = Array.fill(5000){0}

    for (i <- 0 to 30) {
        var stockData:Array[Double] = Array()
        var swarm1 = new swarm(30, 30, 0.7, 0.7, 0.9, -100, 100, "f1", stockData) 
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

    println("Average Euclidian distance over time:")
    (for (i <- 0 to avgEuclDist.length-1) yield (println(avgEuclDist(i))))
    println("\nAverage global best score over time:")
    (for (i <- 0 to avgGBest.length-1) yield (println(avgGBest(i))))
    println("\nPercentage of particles that left the search area:")
    println(avgPersentageLeft)    
    println("\nAverage velocity magnitude over time:")
    (for (i <- 0 to avgVelocityMagnitude.length-1) yield (println(avgVelocityMagnitude(i))))
    println()

}