import java.lang.Math
import scala.util.Random

class swarm(iswarm_size:Int, iconstraint_size:Int, ic1:Double, ic2:Double, iw:Double) {
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var swarm_size:Int = iswarm_size
    var constraint_size:Int = iconstraint_size
    var r = scala.util.Random
    var gbest_pos:Array[Double] = Array.fill(constraint_size){-0.5 + r.nextDouble()*1}
    var gbest_score:Double = f25(gbest_pos)
    var pswarm:Array[particle] = Array()
    
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Double] = Array.fill(constraint_size){-0.5 + r.nextDouble()*1}
        pswarm = pswarm :+ new particle(particle_pos, constraint_size, c1, c2, w)
    }

    def function(x:Array[Double]):Double = {
        x.map(el => Math.abs(el)).sum
    }

    def f25(x:Array[Double]):Double = {
        var step1:Array[Double] = Array()
        var j:Int = 0
        while (j < constraint_size-1) {
            step1 = step1 :+  (for (i <- 1 to 20) yield (Math.pow(0.5,i)*Math.cos(2*Math.PI*Math.pow(3,i)*(x(j)+0.5)))).sum
            j+=1
        }
        var step2:Double = (for (i <- 1 to 20) yield (Math.pow(0.5,i)*Math.cos(Math.PI*Math.pow(3,i)))).sum
        (step1.sum - (constraint_size*step2))
        
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

    def globalBest():Double = {
        gbest_score
    }

    override def toString(): String = {
        "---------------------"+
        "\nGlobal best position: "+ (for (i <- 0 to constraint_size-1) yield (gbest_pos(i))) +
        "\nGlobal best score: "+ gbest_score+
        "\n---------------------"
    }
}