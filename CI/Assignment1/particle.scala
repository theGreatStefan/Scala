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
            case "f5" => f5(pbest_pos)
            case "f12" => f12(pbest_pos)
            case "f24" => f24(pbest_pos)
        }
    var velocity:Array[Double] = Array.fill(velocity_size){0.0}

    /*****************Functions******************/
    // [-100, 100]
    def f1(x:Array[Double]):Double = {
        var my_total:Double = 0.0
        for (i <- 0 to x.length-1) {
            my_total += Math.abs(x(i))
        }
        my_total
    }

    // [−32.768, 32.768]
    def f2(x:Array[Double]):Double = {
        var part1:Double = 0.0
        var part2:Double = 0.0
        for (i <- 0 to x.length-1) {
            part1 += Math.pow(x(i), 2)
            part2 += Math.cos(2*Math.PI*x(i))
        }
        (-20*Math.exp(-0.2*Math.sqrt((1/x.length)*part1)) - Math.exp((1/x.length)*part2) + 20 + Math.exp(1))
    }

    // [-100, 100]
    def f5(x:Array[Double]):Double = {
        var part1:Double = 0.0
        for (i <- 0 to x.length-1) {
            part1 += Math.pow(Math.pow(10, 6), (i-1)/(x.length-1))*Math.pow(x(i), 2)
        }
        part1
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

    // regular clamping
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
            velocity(i) = if (Math.abs(check_val) < v_max(i)) {check_val} else { if (check_val<0) {-1*v_max(i)} else {v_max(i)} }
        }

        updatePos()
    }

    // normalised clamping
    def updateVelocityClamp2(gbest_pos:Array[Double], v_max:Array[Double]):Unit = {
        var r1:Double = 0.0
        var r2:Double = 0.0
        var cognitive:Double = 0.0
        var social:Double = 0.0
        var check_val:Double = 0.0
        var new_vel:Array[Double] = velocity.clone()
        for (i <- 0 to velocity_size-1) {
            r1 = r.nextDouble()
            r2 = r.nextDouble()

            cognitive = c1*r1*(pbest_pos(i) - pos(i))
            social = c2*r2*(gbest_pos(i) - pos(i))
            new_vel(i) = w*velocity(i)+cognitive+social

        }

        for (i <- 0 to velocity_size-1) {
            check_val += Math.pow(new_vel(i), 2)
        }
        check_val = Math.sqrt(check_val)

        if (check_val <= v_max(0)) {
            velocity = new_vel.clone()
        } else {
            for (i <- 0 to velocity_size-1) {
                velocity(i) = (v_max(i)/check_val)*new_vel(i)
            }
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
            case "f5" => f5(pos)
            case "f12" => f12(pos)
            case "f24" => f24(pos)
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