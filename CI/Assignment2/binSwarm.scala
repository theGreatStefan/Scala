class binSwarm(iswarm_size:Int, iconstraint_size_x:Int, iconstraint_size_y:Int, ic1:Double, ic2:Double, iw:Double, input:Array[String]) {
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var swarm_size:Int = iswarm_size
    var constraint_size_x:Int = iconstraint_size_x
    var constraint_size_y:Int = iconstraint_size_y
    var r = scala.util.Random
    //r.setSeed(1234)
    var gbest_pos:Array[Array[Double]] = Array.fill(constraint_size_y){Array.fill(constraint_size_x){0.0}}
    var pswarm:Array[binParticle] = Array()
    var gbest_score:Double = 0.0

    var velocityMag_overTime:Array[Double] = Array.fill(5000){0.0}
    var euclidean_distance_overTime:Array[Double] = Array.fill(5000){0.0}
    var gbest_overTime:Array[Double] = Array.fill(5000){0.0}
    var infeasibleCount:Double = 0

    // Calculate Array[ max leading spaces ]
    var maxNx:Array[Int] = Array.fill(constraint_size_y){0}
    for (i <- 0 to input.length-1) {
        maxNx(i) = constraint_size_x - input(i).length()
    }
    
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Array[Double]] = Array.fill(constraint_size_y){Array.fill(constraint_size_x){if (r.nextDouble() > 0.5) {1.0} else {0.0}}}
        pswarm = pswarm :+ new binParticle(particle_pos, constraint_size_x, constraint_size_y, c1, c2, w, maxNx, input)
    }

    /*****************Functions******************/

    /*****************END_Functions******************/

    // Main method called for running the swarm for a specified number of epochs
    def runSwarm(epochs:Int):Unit = {
        var avgVelMag:Double = 0.0
        for (i <- 0 to epochs-1) {
            for (j <- 0 to swarm_size-1) {
                // Evaluate particle fitness
                pswarm(j).evaluateFitness(i)
                // Update personal best pos
                pswarm(j).updatePBestPos()
                // Update global best pos
                if (pswarm(j).pbest_score > gbest_score) {
                    gbest_pos = pswarm(j).pbest_pos.transpose.transpose
                    gbest_score = pswarm(j).pbest_score
                }
            }
            for (j <- 0 to swarm_size-1) {
                // Update particle velocity
                pswarm(j).updateVelocity(gbest_pos)
                // Update particle pos
                pswarm(j).updatePos()
                avgVelMag += pswarm(j).getVelocityMagnetude()
            }
            //println(avgVelMag/swarm_size)
            //println("Gbest: "+gbest_score+", velMag: "+(avgVelMag/swarm_size))
            velocityMag_overTime(i) = (avgVelMag/swarm_size)
            euclidean_distance_overTime(i) = avgEuclidianDistance()
            gbest_overTime(i) = gbest_score
            avgVelMag = 0.0
            
        }
        for (i <- 0 to swarm_size-1) {
            infeasibleCount += pswarm(i).infeasibleCount
            //println("******************************")
            //println(pswarm(i).toString())
        }
        infeasibleCount = infeasibleCount/swarm_size
        /*println("Average number of infeasible solutions: "+infeasibleCount)
        println("Average percentage of infeasible solutions: "+(infeasibleCount/epochs.toDouble)*100+"%")*/
        
    }

    def avgParticle(): Array[Array[Double]] = {
        var avg_particle_pos:Array[Array[Double]] = pswarm(0).pos.transpose.transpose
        for (i <- 1 to swarm_size-1) {
            for (j <- 0 to constraint_size_y-1) {
                for (k <- 0 to constraint_size_x-1) {
                    avg_particle_pos(j)(k) += pswarm(i).pos(j)(k)
                }
            }
        }
        for (i <- 0 to constraint_size_y-1) {
            for (j <- 0 to constraint_size_x-1) {
                avg_particle_pos(i)(j) = avg_particle_pos(i)(j)/swarm_size
            }
        }
        avg_particle_pos
    }
 
    def avgEuclidianDistance():Double = {
        var avg_pos:Array[Array[Double]] = avgParticle()
        var avg_eucl_d:Double = 0.0
        var avg_eucl_d_curr:Double = 0.0
        for (i <- 0 to swarm_size-1) {
            for (j <- 0 to constraint_size_y-1) {
                for (k <- 0 to constraint_size_x-1) {
                    avg_eucl_d_curr += Math.pow(pswarm(i).pos(j)(k)-avg_pos(j)(k), 2)
                }
            }
            avg_eucl_d += Math.sqrt(avg_eucl_d_curr)
            avg_eucl_d_curr = 0.0
        }
        (avg_eucl_d/swarm_size)
    }
    
}