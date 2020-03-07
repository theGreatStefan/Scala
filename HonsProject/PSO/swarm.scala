import java.lang.Math
import scala.util.Random

class swarm(iswarm_size:Int, iconstraint_size:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, function:String, stockData:Array[Double]) {
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
    var gbest_score:Double = 0.0
    var num_particles_outside:Double = 0.0
    var velocity_magnitude:Double = 0.0
    var avg_velocity_magnitude:Array[Double] = Array.fill(5000){0}
    var nbest_pos:Array[Array[Double]] = Array.fill(swarm_size){Array.fill(constraint_size){lb + r.nextDouble()*(ub - lb)}}
    var nbest_score:Array[Double] = Array.fill(swarm_size){0.0}
    
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Double] = Array.fill(constraint_size){lb + r.nextDouble()*(ub - lb)}
        pswarm = pswarm :+ new particle(particle_pos, constraint_size, c1, c2, w, lb, ub, function)
    }

    /*****************Technical Market Indicators******************/
    
    def AroonUp(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        var highIndex:Double = (stockData.indexOf(prevPrices.max)+1) - (t+1) + p
        var aroonUp:Double = 100*(highIndex/p)
        aroonUp
    }

    def AroonDown(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        var lowIndex:Double = (stockData.indexOf(prevPrices.min)+1) - (t+1) + p
        var aroonDown:Double = 100*(lowIndex/p)
        aroonDown
    }

    def percentageBBand(t:Int, p:Int, D:Double):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p+1, (t+1))
        var midBand:Double = (prevPrices.sum)/p
        var lowInside:Double = (for (i <- 0 to prevPrices.length-1) yield (Math.pow(prevPrices(i)-midBand, 2))).sum
        var lowBand:Double = midBand - (D * Math.sqrt(lowInside/p))
        var upInside:Double = (for (i <- 0 to prevPrices.length-1) yield (Math.pow(prevPrices(i)-midBand, 2))).sum
        var upBand:Double = midBand + (D * Math.sqrt(lowInside/p))
        var pBand:Double = 100*( (stockData(t)-lowBand) / (upBand-lowBand) )
        pBand
    }

    // TODO Figure out whats going on here
    def MACD(t:Int, p:Int, a:Int, b:Int, c:Int):(Double,Double) = {
        var prevPrices_a:Array[Double] = stockData.slice(t-a+1, (t+1))
        var prevPrices_b:Array[Double] = stockData.slice(t-b+1, (t+1))
        var prevPrices_c:Array[Double] = stockData.slice(t-c+1, (t+1))
        var ema_a = stockData(t) * (2/(a+1)) + (prevPrices_a.sum/a) * (100-(2/(a+1)))
        var ema_b = stockData(t) * (2/(b+1)) + (prevPrices_b.sum/b) * (100-(2/(b+1)))

        var priceMomentum:Double = ema_a - ema_b
        var ema_c = stockData(t) * (2/(c+1)) + (prevPrices_c.sum/c) * (100-(2/(c+1)))

        var momentumTrigger:Double = ema_c
        (priceMomentum, momentumTrigger)
    }

    def RSI(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        
        var totalGain:Double = 0.0
        for (i <- 1 to prevPrices.length-1) {
            var gain:Double = (prevPrices(i)-prevPrices(i-1))                            
            if (gain > 0) {
                totalGain += gain
            }
        }

        var totalLoss:Double = 0.0
        for (i <- 1 to prevPrices.length-1) {
            var loss:Double = (prevPrices(i)-prevPrices(i-1))                            
            if (loss < 0) {
                totalLoss += loss
            }
        }
        totalLoss = Math.abs(totalLoss)

        var rs:Double = totalGain/totalLoss
        var rsi:Double = 100 * (1-(1/(1+rs)))

        rsi
    }

    /***************END Technical Market Indicators****************/

    // Main method called for running the swarm for a specified number of epochs
    def runSwarm(epochs:Int):Unit = {
        init_nbest_score()
        //num_particles_outside = 0.0
        for (i <- 0 to epochs-1) {
            for (j <- 0 to swarm_size-1) {
                pswarm(j).updateVelocity(nbest_pos(j))
                checkNBest(j, pswarm(j).pbest_score, pswarm(j).pbest_pos)
                /*if (pswarm(j).checkSearchSpace()) {
                    num_particles_outside += 1.0
                }
                velocity_magnitude += pswarm(j).velocityMagnitude()
                */
            }

            /*gbest_score_time(i) = gbest_score
            avg_eucl_d_time(i) = avgEuclidianDistance()
            avg_velocity_magnitude(i) = (velocity_magnitude/swarm_size)
            velocity_magnitude = 0.0*/
            
        }
        
    }

    def checkNBest(j:Int, j_score:Double, j_pos:Array[Double]):Unit = {
        var prev_j:Int = 0
        var next_j:Int = 0

        if (j==0) {
            prev_j = swarm_size
            next_j = j+1
        } else if (j==swarm_size) {
            prev_j = j-1
            next_j = 0
        } else {
            prev_j = j-1
            next_j = j+1
        }

        if (nbest_score(prev_j) < j_score && nbest_score(prev_j) < nbest_score(next_j)) {
            nbest_score(j) = nbest_score(prev_j)
            nbest_pos(j) = nbest_pos(prev_j)

        } else if (j_score < nbest_score(prev_j) && j_score < nbest_score(next_j)) {
            nbest_score(j) = j_score
            nbest_pos(j) = j_pos

        } else if (nbest_score(next_j) < nbest_score(prev_j) && nbest_score(next_j) < j_score) {
            nbest_score(j) = nbest_score(next_j)
            nbest_pos(j) = nbest_pos(next_j)
        }

    }

    def init_nbest_score():Unit = {
        for (i <- 0 to swarm_size-1) {
            nbest_score(i) = relativeFitness(i)
        }
    }

    def relativeFitness(index:Int):Double = {
        0.0
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
            for (j <- 0 to constraint_size-1) {
                avg_particle_pos(j) += pswarm(i).pos(j)
            }
        }
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

    override def toString(): String = {
        "Average Euclidian distance over time:\n"+
        (for (i <- 0 to avg_eucl_d_time.length-1) yield ((avg_eucl_d_time(i)+"\n")))+
        "\nAverage global best score over time:\n"+
        (for (i <- 0 to gbest_score_time.length-1) yield ((gbest_score_time(i)+"\n")))
    }
}