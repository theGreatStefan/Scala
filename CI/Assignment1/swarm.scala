import java.lang.Math
import scala.util.Random

class swarm(iswarm_size:Int, iconstraint_size:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, function:String) {
    var lb:Double = ilb
    var ub:Double = iub
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var swarm_size:Int = iswarm_size
    var constraint_size:Int = iconstraint_size
    var r = scala.util.Random
    var gbest_pos:Array[Double] = Array.fill(constraint_size){-lb + r.nextDouble()*(ub - lb)}
    var pswarm:Array[particle] = Array()
    var avg_eucl_d_time:Array[Double] = Array()
    var gbest_score_time:Array[Double] = Array()
    var gbest_score:Double = function match{
        case "f1" => f1(gbest_pos)
        case "f2" => f2(gbest_pos)
        case "f3" => f3(gbest_pos)
        case "f24" => f24(gbest_pos)
        case "f25" => f25(gbest_pos)
    }
    var num_particles_outside:Double = 0
    var velocity_magnitude:Double = 0.0
    var avg_velocity_magnitude:Array[Double] = Array()
    //var particle_1_pos:Array[Double] = Array()
    
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Double] = Array.fill(constraint_size){-lb + r.nextDouble()*(ub - lb)}
        pswarm = pswarm :+ new particle(particle_pos, constraint_size, c1, c2, w, lb, ub, function)
    }

    /*****************Functions******************/
    // [-100, 100]
    def f1(x:Array[Double]):Double = {
        x.map(el => Math.abs(el)).sum
    }

    // [−32.768, 32.768]
    def f2(x:Array[Double]):Double = {
        var part1:Double = x.map(el => Math.pow(el, 2)).sum
        var part2:Double = x.map(el => Math.cos(2*Math.PI*el)).sum
        (-20*Math.exp(-0.2*Math.sqrt((1/constraint_size)*part1)) - Math.exp((1/constraint_size)*part2) + 20 + Math.exp(1))
    }

    // [-10, 10]
    def f3(x:Array[Double]):Double = {
        var part1:Double = x.map(el => Math.sin(el)).product
        var part2:Double = x.product
        (part1 * Math.sqrt(part2))
    }

    // [0.25, 10]
    def f24(x:Array[Double]):Double = {
        var part1:Double = x.map(el => Math.sin(10*Math.sqrt(el))).sum
        (-1*(1+part1))
    }

    // [-0.5, 0.5]
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
    /*****************END_Functions******************/

    // Main method called for running the swarm for a specified number of epochs
    def runSwarm(epochs:Int):Unit = {
        num_particles_outside = 0.0
        for (i <- 0 to epochs-1) {
            for (j <- 0 to swarm_size-1) {
                pswarm(j).updateVelocity(gbest_pos)
                var pbest_score:Double = pswarm(j).pbest_score
                var pbest_pos:Array[Double] = pswarm(j).pbest_pos
                if (pbest_score < gbest_score) {
                    gbest_score = pbest_score
                    gbest_pos = pbest_pos
                }
                if (pswarm(j).checkSearchSpace()) {
                    num_particles_outside += 1.0
                }
                velocity_magnitude += pswarm(j).velocityMagnitude()
            }
            //particle_1_pos = particle_1_pos :+ f1(pswarm(0).pos)

            gbest_score_time = gbest_score_time :+ gbest_score
            avg_eucl_d_time = avg_eucl_d_time :+ avgEuclidianDistance()
            avg_velocity_magnitude = avg_velocity_magnitude :+ (velocity_magnitude/swarm_size)
        }
        
    }

    def globalBest():Double = {
        gbest_score
    }

    //def particle1Pos(): Array[Double] = {
    //    particle_1_pos
    //}

    def percentageOutside():Double = {
        (num_particles_outside/(swarm_size*5000.0))
    }

    def avgParticle(): Array[Double] = {
        var avg_pos:Array[Double] = pswarm(0).pos
        for (i <- 1 to swarm_size-1) {
            avg_pos = avg_pos.zip(pswarm(i).pos).map{case (a,b) => a+b}
        }
        avg_pos = avg_pos.map(el => el/swarm_size)
        avg_pos
    }

    def avgEuclidianDistance():Double = {
        var avg_pos:Array[Double] = avgParticle()
        var avg_eucl_d:Double = Math.sqrt((pswarm(0).pos.zip(avg_pos).map{case (a,b) => Math.pow((a-b), 2)}).sum)
        for (i <- 1 to swarm_size-1) {
            avg_eucl_d = avg_eucl_d + Math.sqrt((pswarm(i).pos.zip(avg_pos).map{case (a,b) => Math.pow((a-b), 2)}).sum)
        }
        (avg_eucl_d/swarm_size)
    }

    def getAvgEuclDist(): Array[Double] = {
        avg_eucl_d_time
    }

    def getAvgGBest(): Array[Double] = {
        gbest_score_time
    }

    def getAvgVelocityMagnitude():Array[Double] = {
        avg_velocity_magnitude
    }

    def printTests(): Unit = {
        println("Average Euclidian distance over time:")
        (for (i <- 0 to avg_eucl_d_time.length-1) yield (println(avg_eucl_d_time(i))))
        println("\nAverage global best score over time:")
        (for (i <- 0 to gbest_score_time.length-1) yield (println(gbest_score_time(i))))
    }

    override def toString(): String = {
        "Average Euclidian distance over time:\n"+
        (for (i <- 0 to avg_eucl_d_time.length-1) yield ((avg_eucl_d_time(i)+"\n")))+
        "\nAverage global best score over time:\n"+
        (for (i <- 0 to gbest_score_time.length-1) yield ((gbest_score_time(i)+"\n")))
    }
}