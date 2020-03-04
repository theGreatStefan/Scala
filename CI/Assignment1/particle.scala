import java.lang.Math
import scala.util.Random

class particle(ipos:Array[Double], ivelocity_size:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, function:String) {
    var lb:Double = ilb
    var ub:Double = iub
    var pos:Array[Double] = ipos.clone()
    var velocity_size:Int = ivelocity_size
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var r = scala.util.Random
    var pbest_pos:Array[Double] = Array.fill(velocity_size){lb + r.nextDouble()*(ub-lb)}
    var pbest_score:Double = function match{
            case "f1" => f1(pbest_pos)
            case "f2" => f2(pbest_pos)
            case "f3" => f3(pbest_pos)
            case "f24" => f24(pbest_pos)
            case "f25" => f25(pbest_pos)
        }
    var velocity:Array[Double] = Array.fill(velocity_size){0.0}

    /*****************Functions******************/
    // [-100, 100]
    def f1(x:Array[Double]):Double = {
        var my_total:Double = 0.0
        for (i <- 0 to x.length-1) {
            my_total = my_total + Math.abs(x(i))
        }
        my_total
    }

    // [−32.768, 32.768]
    def f2(x:Array[Double]):Double = {
        var part1:Double = x.map(el => Math.pow(el, 2)).sum
        var part2:Double = x.map(el => Math.cos(2*Math.PI*el)).sum
        (-20*Math.exp(-0.2*Math.sqrt((1/velocity_size)*part1)) - Math.exp((1/velocity_size)*part2) + 20 + Math.exp(1))
    }

    // [-10, 10]
    def f3(x:Array[Double]):Double = {
        var part1:Double = x.map(el => Math.sin(el)).product
        var part2:Double = x.product
        (part1 * Math.sqrt(part2))
    }

    // [0.25, 10]
    def f24(x:Array[Double]):Double = {
        var part1:Double = x.map(el => Math.sin(10*Math.sqrt(el))).sum
        (-1*(1+part1))
    }

    // [-0.5, 0.5]
    def f25(x:Array[Double]):Double = {
        var step1:Array[Double] = Array()
        var j:Int = 0
        while (j < velocity_size-1) {
            step1 = step1 :+  (for (i <- 1 to 20) yield (Math.pow(0.5,i)*Math.cos(2*Math.PI*Math.pow(3,i)*(x(j)+0.5)))).sum
            j+=1
        }
        var step2:Double = (for (i <- 1 to 20) yield (Math.pow(0.5,i)*Math.cos(Math.PI*Math.pow(3,i)))).sum
        (step1.sum - (velocity_size*step2))
        
    }
    /*****************END_Functions******************/

    def updateVelocity(gbest_pos:Array[Double]):Unit = {
        var r1:Double = 0.0
        var r2:Double = 0.0
        var cognitive:Double = 0.0
        var social:Double = 0.0
        for (i <- 0 to velocity_size-1) {
            r1 = r.nextDouble()
            r2 = r.nextDouble()

            cognitive = c1*r1*(pbest_pos(i) - pos(i))
            social = c2*r2*(gbest_pos(i) - pos(i))
            velocity(i) = w*velocity(i)+cognitive+social
        }

        updatePos()

    }

    def updateVelocityClamp1(gbest_pos:Array[Double], v_max:Array[Double]):Unit = {
        var r1:Double = 0.0
        var r2:Double = 0.0
        var cognitive:Double = 0.0
        var social:Double = 0.0
        var check_val:Double = 0.0
        for (i <- 0 to velocity_size-1) {
            r1 = r.nextDouble()
            r2 = r.nextDouble()

            cognitive = c1*r1*(pbest_pos(i) - pos(i))
            social = c2*r2*(gbest_pos(i) - pos(i))
            check_val = w*velocity(i)+cognitive+social
            velocity(i) = if (check_val < v_max(i)) {check_val} else {v_max(i)}
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
        var pos_score:Double = function match{
            case "f1" => f1(pos)
            case "f2" => f2(pos)
            case "f3" => f3(pos)
            case "f24" => f24(pos)
            case "f25" => f25(pos)
        }
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