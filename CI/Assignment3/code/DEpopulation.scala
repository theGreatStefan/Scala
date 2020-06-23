
class DEpopulation(ipop_size:Int, ivec_size:Int, ibeta:Double, ipr:Double, ilb:Double, iub:Double, ifunction:String, imu:Double, itheta:Double, ia:Double, boltzmann:Boolean) {
    var pop_size:Int = ipop_size
    var vec_size:Int = ivec_size
    var beta:Double = ibeta
    var init_beta:Double = ibeta
    var pr:Double = ipr
    var init_pr:Double = ipr
    var lb:Double = ilb
    var ub:Double = iub
    var function:String = ifunction
    var mu:Double = imu
    var theta:Double = itheta
    var a:Double = ia

    var pop:Array[Individual] = Array()

    var trial_vec:Array[Double] = Array.fill(vec_size){0.0}
    var offspring_vec:Array[Double] = Array.fill(vec_size){0.0}

    var wBeta:Array[Double] = Array.fill(pop_size){0.0}
    var wPr:Array[Double] = Array.fill(pop_size){0.0}

    var t:Double = 0.0
    var alpha:Double = 0.0

    var bestSol:Double = Double.MaxValue
    var bestSolIndex:Int = 0
    var currSol:Double = 0

    var avgBestSol:Double = Double.MaxValue

    var avg_pos:Array[Double] = Array()
    var avg_eucl_d_time:Array[Double] = Array.fill(5000){0}
    var avg_mag_diff_vecs_time:Array[Double] = Array.fill(5000){0}
    var violations_time:Array[Double] = Array.fill(5000){0}
    var best_sol_time:Array[Double] = Array.fill(5000){0}
    var avg_num_replacements:Array[Double] = Array.fill(5000){0}

    var r = scala.util.Random

    for (i <- 0 to pop_size-1) {
        var individual_pos:Array[Double] = Array.fill(vec_size){lb + r.nextDouble()*(ub - lb)}
        pop = pop :+ new Individual(individual_pos, vec_size, beta, pr, lb, ub, function)
    }

    /**
      * Main method called for running the swarm for a specified number of epochs 
      *
      * @param epochs
      */
    def runPop(epochs:Int):Unit = {
        var eval:Int = 0
        var iteration_best:Double = Double.MaxValue
        var archiveEmpty:Boolean = true
        t = 5000.0
        alpha = 0.5
        for (i <- 0 to epochs-1) {
            for (j <- 0 to pop_size-1) {
                iteration_best = pop(j).evaluateFitness()    //?
                if (iteration_best < avgBestSol) {
                    best_sol_time(i) = iteration_best
                }
                createTrialVector(j, i)
                createOffspring(j)
                
                t = alpha*t
                eval = pop(j).evaluateOffspring(offspring_vec, boltzmann, t)
                if (eval != -1) {
                    if (eval == 1) {
                        pop(j).changePos(offspring_vec)
                        avg_num_replacements(i) += 1
                        /*Self adaptive*/
                        /*wBeta(j) = beta
                        wPr(j) = pr
                        archiveEmpty = false*/
                    }
                } else {
                    violations_time(i) += 1
                }

            }
            /*if (!archiveEmpty) {
                selfAdapt()
            }*/
            changeBeta(i)
            changePr(i)
            avg_eucl_d_time(i) = avgEuclidianDistance()

            archiveEmpty = true
            
        }
        var currSol:Double = 0

        for (i <- 0 to pop_size-1) {
            currSol = pop(i).evaluateFitness()
            if (currSol < bestSol) {
                bestSol = currSol
                bestSolIndex = i
            }
        }

        printSol()
        
    }

    /**
      * Calculate the trial vector by obtaining a random target individual and 
      * 2 unique random individuals for the difference vector
      *
      * @param curr_individual
      */
    def createTrialVector(curr_individual:Int, curr_iteration:Int):Unit = {
        var target_individual:Int = r.nextInt(pop_size)
        var indiv1:Int = r.nextInt(pop_size)
        var indiv2:Int = r.nextInt(pop_size)


        /*beta = normal(r.nextDouble(), mu, 0.1)
        pr = cauchy(r.nextDouble(), theta, 0.1)*/

        while (target_individual == curr_individual) {  // Cannot be the same as the parent individual
            target_individual = r.nextInt(pop_size)
        }
        while (indiv1 == curr_individual || indiv1 == target_individual) {
            indiv1 = r.nextInt(pop_size)
        }
        while (indiv2 == curr_individual || indiv2 == target_individual || indiv2 == indiv1) {
            indiv2 = r.nextInt(pop_size)
        }

        // Calculate the trial vector
        var diff_vec:Array[Double] = Array.fill(vec_size){0.0}
        for (i <- 0 to vec_size-1) {
            diff_vec(i) = pop(indiv1).pos(i) - pop(indiv2).pos(i)
            trial_vec(i) = pop(target_individual).pos(i) + beta*( diff_vec(i) )
        }

        for (i <- 0 to vec_size-1) {
            avg_mag_diff_vecs_time(curr_iteration) += Math.pow(diff_vec(i), 2)
        }
        avg_mag_diff_vecs_time(curr_iteration) = Math.sqrt(avg_mag_diff_vecs_time(curr_iteration))


    }

    /**
      * Creates the offspring of the parent and the trial vector 
      * using binomial crossover
      *
      * @param curr_individual
      */
    def createOffspring(curr_individual:Int):Unit = {
        // Using a binary mask for the crossover points
        var crossover_points:Array[Int] = Array.fill(vec_size){0}
        var rand_num:Double = 0.0
        var rand_init_pos:Int = r.nextInt(vec_size)
        crossover_points(rand_init_pos) = 1

        for (i <- 0 to vec_size-1) {
            if (i != rand_init_pos) {
                rand_num = r.nextDouble()
                if (rand_num < pr) {
                    crossover_points(i) = 1
                }
            }
        }

        // Apply mask
        for (i <- 0 to vec_size-1) {
            if (crossover_points(i) == 1) {
                offspring_vec(i) = trial_vec(i)
            } else {
                offspring_vec(i) = pop(curr_individual).pos(i)
            }
        }
    }

    def changeBeta(x:Int):Unit = {
        //beta = init_beta*Math.exp(-(x/1000.0))+0.1
        beta = 1.0/240.0 + /*0.11375*/0.5/(Math.pow(2.0, x))
    }

    def changePr(x:Int):Unit = {
        //pr = init_pr*Math.exp(-(x/1000.0))+0.1
        pr = 1.0/240.0 + /*0.11375*/0.5/(Math.pow(2.0, x))
    }

    /**
      * Dynamically adapts the values of mu and theta depending on the success of 
      * their respective variables in the entire population.
      */
    def selfAdapt():Unit = {
        mu = a*mu + (1-a)*(wBeta.sum/pop_size.toDouble)
        theta = a*theta + (1-a)*(wPr.sum/pop_size.toDouble)

        wBeta = Array.fill(pop_size){0.0}
        wPr = Array.fill(pop_size){0.0}
    }

    def cauchy(x:Double, x_0:Double, dev:Double):Double = {
        ( (1/(Math.PI*dev))*((dev*dev)/(Math.pow(x-x_0, 2)+(dev*dev))) )
    }

    def normal(x:Double, mean:Double, std:Double):Double = {
        ( (1/(std*Math.sqrt(2*Math.PI))) * Math.exp(-(1/2)*Math.pow((x-mean)/std, 2)) )
    }

    def avgIndividual(): Array[Double] = {
        var avg_indiv_pos:Array[Double] = pop(0).pos.clone()
        for (i <- 1 to pop_size-1) {
            //avg_particle_pos = avg_particle_pos.zip(pswarm(i).pos).map{case (a,b) => a+b}
            for (j <- 0 to vec_size-1) {
                avg_indiv_pos(j) += pop(i).pos(j)
            }
        }
        //avg_particle_pos = avg_particle_pos.map(el => el/swarm_size)
        for (i <- 0 to vec_size-1) {
            avg_indiv_pos(i) = avg_indiv_pos(i)/pop_size
        }
        avg_indiv_pos
    }

    def avgEuclidianDistance():Double = {
        avg_pos = avgIndividual()
        var avg_eucl_d:Double = 0.0
        var avg_eucl_d_curr:Double = 0.0
        for (i <- 0 to pop_size-1) {
            for (j <- 0 to vec_size-1) {
                avg_eucl_d_curr += Math.pow(pop(i).pos(j)-avg_pos(j), 2)
            }
            avg_eucl_d += Math.sqrt(avg_eucl_d_curr)
            avg_eucl_d_curr = 0.0
        }
        (avg_eucl_d/pop_size)
    }

    def getAvgEuclDist(): Array[Double] = {
        avg_eucl_d_time
    }

    def printSol():Unit = {
        var sol:Array[Double] = pop(bestSolIndex).pos.clone()
        println("Best solution: "+bestSol)
        /*for (i <- 0 to vec_size-1) {
            print(sol(i)+", ")
        }
        print("\n")*/
    }


}