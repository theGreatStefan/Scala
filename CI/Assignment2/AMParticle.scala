import java.lang.Math
import scala.util.Random

class AMParticle(ipos:Array[Double], ibin_vec_x:Int, ibin_vec_y:Int, ic1:Double, ic2:Double, iw:Double, imaxNx:Array[Int], iinput:Array[String]) {
    var pos:Array[Double] = ipos.clone()
    var pos_score:Double = 0.0
    var bin_vec_x:Int = ibin_vec_x
    var bin_vec_y:Int = ibin_vec_y
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var r = scala.util.Random
    var pbest_pos:Array[Double] = pos.clone()
    var pbest_score:Double = 0.0
    var velocity:Array[Double] = Array.fill(4){0.0}
    var bin_vec:Array[Array[Double]] = Array.fill(bin_vec_y){Array.fill(bin_vec_x){0.0}}
    var maxNx:Array[Int] = imaxNx.clone()
    var input:Array[String] = iinput.clone()

    var representation:Array[Array[Char]] = Array.fill(bin_vec_y){Array.fill(bin_vec_x){'-'}}

    updateBinVec()
    evaluateFitness(0)
    pbest_score = pos_score

    /*****************Functions******************/
    def g(x:Double):Double = {
        Math.sin( 2*Math.PI*(x-pos(0))*pos(1)*Math.cos(2*Math.PI*(x-pos(0))*pos(2)) ) + pos(3)
    }
    
    /*****************END_Functions******************/

    def updateVelocity(gbest_pos:Array[Double]):Unit = {
        var r1:Double = 0.0
        var r2:Double = 0.0
        var cognitive:Double = 0.0
        var social:Double = 0.0
        for (i <- 0 to 3) {
            r1 = r.nextDouble()
            r2 = r.nextDouble()

            cognitive = c1*r1*(pbest_pos(i) - pos(i))
            social = c2*r2*(gbest_pos(i) - pos(i))
            velocity(i) = w*velocity(i)+cognitive+social
        }


    }

    def updatePos():Unit = {
        for (i <- 0 to 3) {
            pos(i) = pos(i)+velocity(i)
        }
        //printPos()
    }

    def updateBinVec():Unit = {
        for (i <- 0 to bin_vec_y-1) {
            for (j <- 0 to bin_vec_x-1) {
                bin_vec(i)(j) = if (g(i*(bin_vec_x)+j) > 0) {1.0} else {0.0}
            }
        }
    }

    def evaluateFitness(runNum:Int):Unit = {
        var str:String = ""
        var str_length:Int = 0
        var str_init_length:Int = 0
        var leadingSpaces:Int = 0
        var totalLeadingSpaces:Int = 0
        //var w1:Double = Math.abs((Math.sin(2*Math.PI*runNum / (2500))))
        //var w2:Double = 1-w1
        var w1:Double = 0.65
        var w2:Double = 1.0-w1
        var pos_penalty:Double = 0.0
        for (i <- 0 to bin_vec_y-1) {
            str = input(i)
            str_length = input(i).length()
            str_init_length = str_length
            leadingSpaces = 0
            for (j <- 0 to bin_vec_x-1) {
                if (bin_vec(i)(j) == 1) {
                    if (str_length > 0) {
                        if (maxNx(i) > leadingSpaces) {
                            leadingSpaces+=1
                            representation(i)(j) = '-'
                        } else {
                            representation(i)(j) = str.charAt(str_init_length-str_length)
                            str_length-=1
                        }
                    } else {
                        representation(i)(j) = '-'
                    }
                } else {
                    if (str_length > 0) {
                        representation(i)(j) = str.charAt(str_init_length-str_length)
                        str_length-=1
                    } else {
                        representation(i)(j) = '-'
                    }
                }
            }
            totalLeadingSpaces += leadingSpaces
        }

        var currCh:Char = '0'
        var tempCh:Char = '0'
        var totalAllignments:Int = 0

        for (j <- 0 to bin_vec_x-1) {
            for (i <- 0 to bin_vec_y-1) {
                currCh = representation(i)(j)
                if (currCh != '-') {
                    for (k <- (i+1) to bin_vec_y-1) {
                        tempCh = representation(k)(j)
                        if (currCh == tempCh) {
                            totalAllignments += 1
                        } else if (tempCh != '-') {
                            totalAllignments -= 1
                            totalLeadingSpaces += 1
                            //pos_penalty += 3
                        }
                    }
                }
                
            }

        }
        
        pos_score = w1*totalAllignments + w2*(maxNx.sum - totalLeadingSpaces) - pos_penalty
    }

    def updatePBestPos():Unit = {
        if (pos_score > pbest_score) {
            pbest_score = pos_score
            pbest_pos = pos
        }
    }

    def getVelocityMagnetude():Double = {
        var totalVM:Double = 0.0
        totalVM = Math.pow(velocity.sum, 2)
        Math.sqrt(totalVM)
    }

    def printPos():Unit = {
        println("Printing position vector")
        for (i <- 0 to 3) {
            print(pos(i)+" ")
        }
        println("")
    }

    override def toString(): String = {
        println("String representation")
        for (i <- 0 to bin_vec_y-1) {
            for (j <- 0 to bin_vec_x-1) {
                print(representation(i)(j)+" ") 
            }
            println("")
        }
        ""

    }

}