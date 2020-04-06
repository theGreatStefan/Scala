import java.lang.Math
import scala.util.Random

class particle(ipos:Array[Array[Double]], ivelocity_size_x:Int, ivelocity_size_y:Int, ic1:Double, ic2:Double, iw:Double, imaxNx:Array[String], iinput:Array[String]) {
    var pos:Array[Array[Double]] = ipos.transpose.transpose
    var pos_score:Double = 0.0
    var velocity_size_x:Int = ivelocity_size_x
    var velocity_size_y:Int = ivelocity_size_y
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var r = scala.util.Random
    var pbest_pos:Array[Array[Double]] = pos.transpose.transpose
    var pbest_score:Double = 0.0
    var velocity:Array[Array[Double]] = Array.fill(velocity_size_y){Array.fill(velocity_size_x){0.0}}
    var prob:Array[Array[Double]] = Array.fill(velocity_size_y){Array.fill(velocity_size_x){0.0}}
    var maxNx:Array[Double] = imaxNx.clone()
    var input:Array[Double] = iinput.clone()

    var representation:Array[Array[Char]] = Array.fill(velocity_size_y){Array.fill(velocity_size_x){'-'}}

    /*****************Functions******************/
    
    /*****************END_Functions******************/

    def updateVelocity(gbest_pos:Array[Array[Double]]):Unit = {
        var r1:Double = 0.0
        var r2:Double = 0.0
        var cognitive:Double = 0.0
        var social:Double = 0.0
        for (i <- 0 to velocity_size_y-1) {
            for (j <- 0 to velocity_size_x-1) {
                r1 = r.nextDouble()
                r2 = r.nextDouble()

                cognitive = c1*r1*(pbest_pos(i)(j) - pos(i)(j))
                social = c2*r2*(gbest_pos(i)(j) - pos(i)(j))
                velocity(i)(j) = w*velocity(i)(j)+cognitive+social
                prob(i)(j) = 1.0 / (1.0 + Math.exp(-velocity(i)(j)))
            }
        }


    }

    def updatePos():Unit = {
        var check:Double = r.nextDouble()
        for (i <- 0 to velocity_size_y-1) {
            for (i <- 0 to velocity_size_y-1) {
                pos(i)(j) = if (check < prob(i)(j)) { 1.0 } else { 0.0 }
            }
        }

    }

    def evaluateFitness():Unit = {

        var str:String = "asd"
        var str_length:Int = 0.0
        var str_init_length:Int = 0.0
        var leadingSpaces:Int = 0.0
        var totalLeadingSpaces:Int = 0.0
        for (i <- 0 to velocity_size_y-1) {
            str_length = input(i).length()
            str_init_length = str_length
            leadingSpaces = 0.0
            for (j <- 0 to velocity_size_x-1) {
                if (pos(i)(j) == 1) {
                    if (str_length > 0) {
                        if (maxNx > leadingSpaces) {
                            leadingSpaces+=1
                            representation(i)(j) = '-'
                        } else {
                            str_length-=1
                            representation(i)(j) = str.charAt(str_init_length-str_length)
                        }
                    } else {
                        representation(i)(j) = '-'
                    }
                } else {
                    str_length-=1
                    representation(i)(j) = str.charAt(str_init_length-str_length)
                }
            }
            totalLeadingSpaces += leadingSpaces
        }
        // TODO aligned chars
    }

    def updatePBestPos():Unit = {
        if (pos_score < pbest_score) {
            pbest_score = pos_score
            pbest_pos = pos
        }
    }

    override def toString(): String = {
        "String representation\n"+
        (for (i <- 0 to velocity_size_y-1) yield (
            for (j <- 0 to velocity_size_x-1) yield (
                representation(i)(j).toString()+" "
            ) + "\n"
        ))
    }

}