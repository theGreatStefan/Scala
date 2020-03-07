import java.lang.Math
import scala.util.Random

class particle(ipos:Array[Double], ivelocity_size:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, function:String) {
    var bank:Double = 1000000
    var stocks:Double = 0
    var lb:Double = ilb
    var ub:Double = iub
    var pos:Array[Double] = ipos.clone()
    var velocity_size:Int = ivelocity_size
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var r = scala.util.Random
    var pbest_pos:Array[Double] = Array.fill(velocity_size){lb + r.nextDouble()*(ub-lb)}
    var pbest_score:Double = 0.0
    var velocity:Array[Double] = Array.fill(velocity_size){0.0}

    /*****************Functions******************/
    
    /*****************END_Functions******************/

    def updateVelocity(nbest_pos:Array[Double]):Unit = {
        var r1:Double = 0.0
        var r2:Double = 0.0
        var cognitive:Double = 0.0
        var social:Double = 0.0
        for (i <- 0 to velocity_size-1) {
            r1 = r.nextDouble()
            r2 = r.nextDouble()

            cognitive = c1*r1*(pbest_pos(i) - pos(i))
            social = c2*r2*(nbest_pos(i) - pos(i))
            velocity(i) = w*velocity(i)+cognitive+social
        }

        updatePos()

    }

    def updatePos():Unit = {
        for (i <- 0 to velocity_size-1) {
            pos(i) += velocity(i)
        }

        checkBestPos()
    }

    def checkBestPos():Unit = {
        // Run NN here
        // make transaction
        // evaluate position
        var pos_score:Double = 0.0
        if (pos_score < pbest_score) {
            pbest_score = pos_score
            pbest_pos = pos
        }
    }

    def checkSearchSpace():Boolean = {
        var outside:Boolean = false
        var i:Int = 0
        while (!outside && i < velocity_size) {
            if (pos(i) > ub || pos(i) < lb) {
                outside = true
            }
            i+=1
        }
        outside
    }

    def velocityMagnitude():Double = {
        var my_total:Double = 0.0
        for (i <- 0 to velocity_size-1) {
            my_total += Math.pow(velocity(i), 2)
        }
        Math.sqrt(my_total)
    }

    override def toString(): String = {
        "---------------------"+
        "\nCurrent velocity:  "+ (for (i <- 0 to velocity_size-1) yield (velocity(i))) +
        "\nCurrent position:  "+ (for (i <- 0 to velocity_size-1) yield (pos(i)))+
        "\nPersonal best pos: "+ (for (i <- 0 to velocity_size-1) yield (pbest_pos(i)))+
        "\nPersonal best score: "+pbest_score+
        "\n---------------------"
    }

}