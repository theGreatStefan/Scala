
object main extends App {
    var swarm1 = new swarm(30, 30, 0.7, 0.7, 0.9)
    swarm1.runSwarm(5000)
    println(swarm1.toString())
}