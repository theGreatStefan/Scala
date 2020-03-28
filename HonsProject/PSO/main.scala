
object main extends App {
    val file_path:String = "../Data/"

    val data:readData = new readData(file_path, "BP.csv")
    
    for (i <- 0 to 0) {
        var stockData:Array[Double] = data.getOpenTimeSeries()
            // fanin = 6; input nodes for the NN (?)
        var swarm1 = new swarm(150, 36, 1.496180, 1.496180, 0.729844, -1/(Math.sqrt(6)), 1/(Math.sqrt(6)), stockData) 
        swarm1.runSwarm(200)   
    }


}