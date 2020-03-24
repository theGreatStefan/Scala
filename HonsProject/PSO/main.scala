
object main extends App {
    
    for (i <- 0 to 30) {
        var stockData:Array[Double] = Array()
        var swarm1 = new swarm(30, 30, 0.7, 0.7, 0.9, -100, 100, stockData) 
        swarm1.runSwarm(5000)   
    }


}