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
    var pbest_score:Double = f1(pbest_pos)
    var velocity:Array[Double] = Array.fill(velocity_size){0.0}

    // [-100, 100]
    def f1(x:Array[Double]):Double = {
        x.map(el => Math.abs(el)).sum
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
        var pos_score:Double = f1(pos)
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