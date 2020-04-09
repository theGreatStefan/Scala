class binSwarm(iswarm_size:Int, iconstraint_size_x:Int, iconstraint_size_y:Int, ic1:Double, ic2:Double, iw:Double, input:Array[String]) {
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var swarm_size:Int = iswarm_size
    var constraint_size_x:Int = iconstraint_size_x
    var constraint_size_y:Int = iconstraint_size_y
    var r = scala.util.Random
    var gbest_pos:Array[Array[Double]] = Array.fill(constraint_size_y){Array.fill(constraint_size_x){0.0}}
    var pswarm:Array[binParticle] = Array()
    var gbest_score:Double = 0.0

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
            avgVelMag = 0.0
            println("Gbest: "+gbest_score)

            
        }
        for (i <- 0 to swarm_size-1) {
            println("******************************")
            println(pswarm(i).toString())
        }
        
    }
    
}