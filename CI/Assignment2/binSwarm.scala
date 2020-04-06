class binSwarm(iswarm_size:Int, iconstraint_size_x:Int, iconstraint_size_y:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, input:Array[String]) {
    var lb:Double = ilb
    var ub:Double = iub
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var swarm_size:Int = iswarm_size
    var constraint_size_x:Int = iconstraint_size_x
    var constraint_size_y:Int = iconstraint_size_y
    var r = scala.util.Random
    var gbest_pos:Array[Array[Double]] = Array.fill(constraint_size_y){Array.fill(constraint_size_x){lb + r.nextDouble()*(ub - lb)}}
    var pswarm:Array[binParticle] = Array()
    var gbest_score:Double = 0.0 // TODO fitness function

    // Calculate Array[ max leading spaces ]
    var maxNx:Array[Int] = Array.fill(constraint_size_y)
    for (i <- 0 to input.length-1) {
        maxNx(i) = constraint_size_x - input(i).length()
    }
    
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Array[Double]] = Array.fill(constraint_size_y){Array.fill(constraint_size_x){lb + r.nextDouble()*(ub - lb)}}
        pswarm = pswarm :+ new binParticle(particle_pos, constraint_size_x, constraint_size_y, c1, c2, w, maxNx, input)
    }

    /*****************Functions******************/

    /*****************END_Functions******************/

    // Main method called for running the swarm for a specified number of epochs
    def runSwarm(epochs:Int):Unit = {
        for (i <- 0 to epochs-1) {
            for (j <- 0 to swarm_size-1) {
                // Evaluate particle fitness
                pswarm(j).evaluateFitness()
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
            }

            
        }
        
    }
    
}