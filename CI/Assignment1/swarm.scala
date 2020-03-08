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
    var gbest_pos:Array[Double] = Array.fill(constraint_size){lb + r.nextDouble()*(ub - lb)}
    var pswarm:Array[particle] = Array()
    var avg_pos:Array[Double] = Array()
    var avg_eucl_d_time:Array[Double] = Array.fill(5000){0}
    var gbest_score_time:Array[Double] = Array.fill(5000){0}
    var gbest_score:Double = function match{
        case "f1" => f1(gbest_pos)
        case "f2" => f2(gbest_pos)
        case "f5" => f5(gbest_pos)
        case "f12" => f12(gbest_pos)
        case "f24" => f24(gbest_pos)
        }
    var num_particles_outside:Double = 0
    var velocity_magnitude:Double = 0.0
    var avg_velocity_magnitude:Array[Double] = Array.fill(5000){0}
    var gbest_score_hist:Array[Double] = Array.fill(5){Double.MaxValue}
    
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Double] = Array.fill(constraint_size){lb + r.nextDouble()*(ub - lb)}
        pswarm = pswarm :+ new particle(particle_pos, constraint_size, c1, c2, w, lb, ub, function)
    }

    /*****************Functions******************/
    // [-100, 100]
    def f1(x:Array[Double]):Double = {
        var my_total:Double = 0.0
        for (i <- 0 to x.length-1) {
            my_total += Math.abs(x(i))
        }
        my_total
    }

    // [−32.768, 32.768]
    def f2(x:Array[Double]):Double = {
        var part1:Double = 0.0
        var part2:Double = 0.0
        for (i <- 0 to x.length-1) {
            part1 += Math.pow(x(i), 2)
            part2 += Math.cos(2*Math.PI*x(i))
        }
        (-20*Math.exp(-0.2*Math.sqrt((1/x.length)*part1)) - Math.exp((1/x.length)*part2) + 20 + Math.exp(1))
    }

    // [-100, 100]
    def f5(x:Array[Double]):Double = {
        var part1:Double = 0.0
        for (i <- 0 to x.length-1) {
            part1 += Math.pow(Math.pow(10, 6), (i-1)/(x.length-1))*Math.pow(x(i), 2)
        }
        part1
    }

    // [-5.12, 5.12]
    def f12(x:Array[Double]):Double = {
        var part1:Double = 0.0
        for (i <- 0 to x.length-1) {
            part1 += (Math.pow(x(i), 2) - 10*Math.cos(2*Math.PI*x(i)))
        }
        (10*x.length+part1)
    }

    // [0.25, 10]
    def f24(x:Array[Double]):Double = {
        var part1:Double = 0.0
        for (i <- 0 to x.length-1) {
            part1 += Math.sin(10*Math.sqrt(x(i)))
        }
        (-1*(1+part1))
    }

    /*****************END_Functions******************/

    // Main method called for running the swarm for a specified number of epochs
    def runSwarm(epochs:Int):Unit = {
        //var v_max:Array[Double] = Array.fill(constraint_size){ub}
        var k:Double = 1.0
        var v_max:Array[Double] = Array.fill(constraint_size){k*(ub-lb)}

        var beta:Double = 1.0
        num_particles_outside = 0.0
        for (i <- 0 to epochs-1) {
            //v_max = velocityClampingNorm(0.1)
            //v_max = velocityClampingDynamic(beta, 5, v_max)
            //v_max = velocityClampingExponential(2, i, v_max)
            for (j <- 0 to swarm_size-1) {
                pswarm(j).updateVelocity(gbest_pos)
                //pswarm(j).updateVelocityClamp1(gbest_pos, v_max)
                //pswarm(j).updateVelocityClamp2(gbest_pos, v_max)
                var pbest_score:Double = pswarm(j).pbest_score
                if (pbest_score < gbest_score) {
                    gbest_score = pbest_score
                    gbest_pos = pswarm(j).pbest_pos.clone()
                }
                if (pswarm(j).checkSearchSpace()) {
                    num_particles_outside += 1.0
                }
                velocity_magnitude += pswarm(j).velocityMagnitude()
                
            }

            updateGbestHist(gbest_score)
            gbest_score_time(i) = gbest_score
            avg_eucl_d_time(i) = avgEuclidianDistance()
            avg_velocity_magnitude(i) = (velocity_magnitude/swarm_size)
            velocity_magnitude = 0.0
            beta = 1-(i * (0.99/5000))
            
        }
        
    }

    def globalBest():Double = {
        gbest_score
    }

    def percentageOutside():Double = {
        (num_particles_outside/(swarm_size*5000.0))
    }

    def avgParticle(): Array[Double] = {
        var avg_particle_pos:Array[Double] = pswarm(0).pos.clone()
        for (i <- 1 to swarm_size-1) {
            //avg_particle_pos = avg_particle_pos.zip(pswarm(i).pos).map{case (a,b) => a+b}
            for (j <- 0 to constraint_size-1) {
                avg_particle_pos(j) += pswarm(i).pos(j)
            }
        }
        //avg_particle_pos = avg_particle_pos.map(el => el/swarm_size)
        for (i <- 0 to constraint_size-1) {
            avg_particle_pos(i) = avg_particle_pos(i)/swarm_size
        }
        avg_particle_pos
    }

    def avgEuclidianDistance():Double = {
        avg_pos = avgParticle()
        var avg_eucl_d:Double = 0.0
        var avg_eucl_d_curr:Double = 0.0
        for (i <- 0 to swarm_size-1) {
            for (j <- 0 to constraint_size-1) {
                avg_eucl_d_curr += Math.pow(pswarm(i).pos(j)-avg_pos(j), 2)
            }
            avg_eucl_d += Math.sqrt(avg_eucl_d_curr)
            avg_eucl_d_curr = 0.0
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

    def velocityClampingNorm(k:Double):Array[Double] = {
        var arr:Array[Double] = Array.fill(constraint_size){0.0}
        var max_val:Double = Double.MinValue
        var min_val:Double = Double.MaxValue
        var curr_val:Double = 0.0
        for (i <- 0 to constraint_size-1) {
            for (j <- 0 to swarm_size-1) {
                curr_val = pswarm(j).pos(i)
                if (curr_val > max_val) {
                    max_val = curr_val
                } else if (curr_val < min_val) {
                    min_val = curr_val
                }
            }
            arr(i) = k*(max_val-min_val)
            max_val = Double.MinValue
            min_val = Double.MaxValue
        }
        arr
    }

    def velocityClampingDynamic(beta:Double, tao:Int, v_max:Array[Double]):Array[Double] = {
        var arr:Array[Double] = v_max.clone()
        var max_val:Double = Double.MinValue
        var min_val:Double = Double.MaxValue
        var curr_val:Double = 0.0
        var t:Int = 0
        var found:Boolean = false
        while (!found && t < tao) {
            if (gbest_score > gbest_score_hist(t)) {
                found=true
            }
            t+=1
        }

        if (found) {
            for (i <- 0 to constraint_size-1) {
                arr(i) = beta*v_max(i)
            }
        }
        arr
    }

    def velocityClampingExponential(alpha:Double, t:Double, v_max:Array[Double]):Array[Double] = {
        var arr:Array[Double] = Array.fill(constraint_size){0.0}

        for (i <- 0 to constraint_size-1) {
            arr(i) = (1-Math.pow((t/5000), alpha))*v_max(i)
        }
        arr
    }

    def updateGbestHist(new_gbest:Double):Unit = {
        var prev:Double = gbest_score_hist(0)
        var curr:Double = 0.0
        for (i <- 1 to 3) {
            curr = gbest_score_hist(i)
            gbest_score_hist(i) = prev
            prev = curr
        }
        gbest_score_hist(4) = new_gbest
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