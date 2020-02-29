
object main extends App {
    for (i <- 0 to 0) {
        var swarm1 = new swarm(30, 30, 0.7, 0.7, 0.9)
        swarm1.runSwarm(5000)
        swarm1.printTests()
    }
}