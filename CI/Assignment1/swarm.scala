import java.lang.Math
import scala.util.Random

class swarm(iswarm_size:Int, iconstraint_size:Int, ic1:Double, ic2:Double, iw:Double) {
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var swarm_size:Int = iswarm_size
    var constraint_size:Int = iconstraint_size
    var r = scala.util.Random
    var gbest_pos:Array[Double] = Array.fill(constraint_size){-100 + r.nextDouble()*200}
    var gbest_score:Double = function(gbest_pos)
    var pswarm:Array[particle] = Array()
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Double] = Array.fill(constraint_size){-100 + r.nextDouble()*200}
        pswarm = pswarm :+ new particle(particle_pos, constraint_size, c1, c2, w)
    }

    def function(x:Array[Double]):Double = {
        x.map(el => Math.abs(el)).sum
    }

    def runSwarm(epochs:Int):Unit = {
        for (i <- 0 to epochs-1) {
            for (j <- 0 to swarm_size-1) {
                pswarm(j).updateVelocity(gbest_pos)
                var pbest_score:Double = pswarm(j).pbest_score
                var pbest_pos:Array[Double] = pswarm(j).pbest_pos
                if (pbest_score < gbest_score) {
                    gbest_score = pbest_score
                    gbest_pos = pbest_pos
                }
            }
        }
    }

    override def toString(): String = {
        "---------------------"+
        "\nGlobal best position: "+ (for (i <- 0 to constraint_size-1) yield (gbest_pos(i))) +
        "\nGlobal best score: "+ gbest_score+
        "\n---------------------"
    }
}