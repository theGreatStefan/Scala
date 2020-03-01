
object main extends App {
    var allEuclDist:Array[Array[Double]] = Array(Array())
    var allGBest:Array[Array[Double]] = Array(Array())
    var avgEuclDist:Array[Double] = Array()
    var avgGBest:Array[Double] = Array()
    //var particle1_pos:Array[Double] = Array()
    for (i <- 0 to 30) {
        var swarm1 = new swarm(30, 30, 0.7, 0.7, 0.9)
        swarm1.runSwarm(5000)
        allEuclDist = allEuclDist :+ swarm1.getAvgEuclDist()
        allGBest = allGBest :+ swarm1.getAvgGBest()
        //particle1_pos = swarm1.particle1Pos()
    }

    avgEuclDist = allEuclDist(1)
    for (i <- 2 to 30) {
        avgEuclDist = avgEuclDist.zip(allEuclDist(i)).map{case (a,b) => a+b}
    }
    avgEuclDist = avgEuclDist.map(el => el/30)

    avgGBest = allGBest(1)
    for (i <- 2 to 30) {
        avgGBest = avgGBest.zip(allGBest(i)).map{case (a,b) => a+b}
    }
    avgGBest = avgGBest.map(el => el/30)

    println("Average Euclidian distance over time:")
    (for (i <- 0 to avgEuclDist.length-1) yield (println(avgEuclDist(i))))
    //(for (i <- 0 to particle1_pos.length-1) yield (println(particle1_pos(i))))
    println("\nAverage global best score over time:")
    //println("Average global best score over time:")
    (for (i <- 0 to avgGBest.length-1) yield (println(avgGBest(i))))
    println()

}