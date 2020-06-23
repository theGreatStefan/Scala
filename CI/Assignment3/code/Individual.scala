
class Individual(iindividual_pos:Array[Double], ivec_size:Int, ibeta:Double, ipr:Double, ilb:Double, iub:Double, ifunction:String) {
    var pos:Array[Double] = iindividual_pos.clone()
    var vec_size:Int = ivec_size
    var beta:Double = ibeta
    var pr:Double = ipr
    var ub:Double = iub
    var lb:Double = ilb
    var function:String = ifunction
    var fitness:Double = 0

    var r = scala.util.Random
    
    /*****************Functions******************/
    // [-100, 100]
    def f1(x:Array[Double]):Double = {
        var my_total:Double = 0.0
        for (i <- 0 to x.length-1) {
            my_total += Math.abs(x(i))
        }
        my_total
    }

    // [-100, 100]
    def f5(x:Array[Double]):Double = {
        var part1:Double = 0.0
        for (i <- 0 to x.length-1) {
            part1 += Math.pow(Math.pow(10, 6), (i-1)/(x.length-1))*Math.pow(x(i), 2)
        }
        part1
    }

    // [-600, 600]
    def f6(x:Array[Double]):Double = {
        var part1:Double = 0.0
        var part2:Double = 1.0
        for (i <- 0 to x.length-1) {
            part1 += Math.pow(x(i), 2)
            part2 *= Math.cos(x(i)/Math.sqrt(i))
        }
        (1+(1/4000)*part1-part2)
    }

    // [0, PI]
    def f8(x:Array[Double]):Double = {
        var part1:Double = 1.0
        var part2:Double = 1.0
        var total:Double = 0.0
        for (i <- 0 to x.length-1) {
            part1 = Math.sin(x(i))
            part2 = Math.pow( Math.sin( (i*Math.pow(x(i),2))/Math.PI ) , 20 )
            total += part1*part2
        }
        (-total)
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

    def evaluateFitness():Double = {
        fitness = function match{
            case "f1" => f1(pos)
            case "f5" => f5(pos)
            case "f6" => f6(pos)
            case "f8" => f8(pos)
            case "f12" => f12(pos)
            case "f24" => f24(pos)
        }
        (fitness)
    }

    /**
      * evaluates if the offspring vector is more fit than the parent vector
      * If so return 1
      * else return 0
      * If the boundaries are violated return -1
      *
      * @param offspring
      * @return
      */
    def evaluateOffspring(offspring:Array[Double], boltzmann:Boolean, t:Double):Int = {
        if (checkBoundaries(offspring)) {
            return -1
        }
        var offspring_fitness = function match{
            case "f1" => f1(offspring)
            case "f5" => f5(offspring)
            case "f6" => f6(offspring)
            case "f8" => f8(offspring)
            case "f12" => f12(offspring)
            case "f24" => f24(offspring)
        }

        if (boltzmann) {
            var rand_num:Double = r.nextDouble()

            if (rand_num > 1.0/( 1+Math.exp( (evaluateFitness() - offspring_fitness ) / t.toDouble ))) {
                1
            } else {
                0
            }
        } else {
            if (offspring_fitness < evaluateFitness()) {
                1
            } else {
                0
            }
        }
        
    }

    /**
      * Checks if an element violates a boundary constraint
      * If so return true
      * else return false
      *
      * @param x
      * @return
      */
    def checkBoundaries(x:Array[Double]):Boolean = {
        for (i <- 0 to vec_size-1) {
            if (x(i)>ub || x(i)<lb) {
                return true
            }
        }
        return false
    }

    def changePos(x:Array[Double]):Unit = {
        pos = x.clone()
    }
}