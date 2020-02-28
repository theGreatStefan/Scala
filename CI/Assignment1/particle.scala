import java.lang.Math
import scala.util.Random

class particle(ipos:Array[Double], ivelocity_size:Int, ic1:Double, ic2:Double, iw:Double) {
    var pos:Array[Double] = ipos
    var velocity_size:Int = ivelocity_size
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var r = scala.util.Random
    var pbest_pos:Array[Double] = Array.fill(velocity_size){-100 + r.nextDouble()*200}
    var pbest_score:Double = function(pbest_pos)
    var velocity:Array[Double] = Array.fill(velocity_size){0.0}

    def function(x:Array[Double]):Double = {
        x.map(el => Math.abs(el)).sum
    }

    def updateVelocity(gbest_pos:Array[Double]):Unit = {
        
        var r1j:Array[Double] = Array.fill(velocity_size){r.nextDouble()}
        var r2j:Array[Double] = Array.fill(velocity_size){r.nextDouble()}

        // Cognitive part of the velocity update
        var cognitive_pbest:Array[Double] = r1j.zip(pbest_pos).map{ case (a,b) => c1*a*b}
        var cognitive_pos:Array[Double] = r1j.zip(pos).map{ case (a,b) => c1*a*b}
        var cognitive:Array[Double] = cognitive_pbest.zip(cognitive_pos).map{case (a,b) => a-b}

        // Social part of the velocity update
        var social_gbest:Array[Double] = r2j.zip(gbest_pos).map{ case (a,b) => c2*a*b}
        var social_pos:Array[Double] = r2j.zip(pos).map{ case (a,b) => c2*a*b}
        var social:Array[Double] = social_gbest.zip(social_pos).map{case (a,b) => a-b}

        // Add cognitive and social parts to the velocity * w
        velocity = velocity.map(el => w*el)
        velocity = velocity.zip(cognitive).map{case (a,b) => a+b}
        velocity = velocity.zip(social).map{case (a,b) => a+b}

        updatePos()

    }

    def updatePos():Unit = {
        pos = pos.zip(velocity).map{case (a,b) => a+b}

        checkBestPos()
    }

    def checkBestPos():Unit = {
        var pos_score:Double = function(pos)
        if (pos_score < pbest_score) {
            pbest_score = pos_score
            pbest_pos = pos
        }
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