
object main extends App {
    //var allEuclDist:Array[Array[Double]] = Array(Array())
    var avgEuclDist:Array[Double] = Array.fill(5000){0}
    var avgGBest:Array[Double] = Array.fill(5000){0}
    var avgPersentageLeft:Double = 0
    var avgVelocityMagnitude:Array[Double] = Array.fill(5000){0}

    //var particle1_pos:Array[Double] = Array()
    for (i <- 0 to 30) {
        var swarm1 = new swarm(30, 30, 0.7, 0.7, 0.9, -100, 100, "f1")
        swarm1.runSwarm(5000)
        //allEuclDist = allEuclDist :+ swarm1.getAvgEuclDist()
        avgEuclDist = avgEuclDist.zip(swarm1.getAvgEuclDist()).map{case (a,b) => a+b}
        avgGBest = avgGBest.zip(swarm1.getAvgGBest()).map{case (a,b) => a+b}
        avgPersentageLeft += swarm1.percentageOutside()
        avgVelocityMagnitude = avgVelocityMagnitude.zip(swarm1.getAvgVelocityMagnitude()).map{case (a,b) => a+b}
        //particle1_pos = swarm1.particle1Pos()
    }

    /*avgEuclDist = allEuclDist(1)
    for (i <- 2 to 30) {
        avgEuclDist = avgEuclDist.zip(allEuclDist(i)).map{case (a,b) => a+b}
    }*/
    avgEuclDist = avgEuclDist.map(el => el/30)
 
    avgGBest = avgGBest.map(el => el/30)

    avgPersentageLeft = (avgPersentageLeft)/30

    avgVelocityMagnitude = avgVelocityMagnitude.map(el => el/30)

    println("Average Euclidian distance over time:")
    (for (i <- 0 to avgEuclDist.length-1) yield (println(avgEuclDist(i))))
    //(for (i <- 0 to particle1_pos.length-1) yield (println(particle1_pos(i))))
    println("\nAverage global best score over time:")
    (for (i <- 0 to avgGBest.length-1) yield (println(avgGBest(i))))
    println("\nPercentage of particles that left the search area:")
    println(avgPersentageLeft)    
    println("\nAverage velocity magnitude over time:")
    (for (i <- 0 to avgVelocityMagnitude.length-1) yield (println(avgVelocityMagnitude(i))))
    println()

}