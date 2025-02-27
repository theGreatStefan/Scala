import java.lang.Math
import scala.util.Random

class binParticle(ipos:Array[Array[Double]], ivelocity_size_x:Int, ivelocity_size_y:Int, ic1:Double, ic2:Double, iw:Double, imaxNx:Array[Int], iinput:Array[String]) {
    var pos:Array[Array[Double]] = ipos.transpose.transpose
    var pos_score:Double = 0.0
    var velocity_size_x:Int = ivelocity_size_x
    var velocity_size_y:Int = ivelocity_size_y
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var r = scala.util.Random
    //r.setSeed(5678)
    var pbest_pos:Array[Array[Double]] = pos.transpose.transpose
    var pbest_score:Double = 0.0
    var velocity:Array[Array[Double]] = Array.fill(velocity_size_y){Array.fill(velocity_size_x){0.0}}
    var prob:Array[Array[Double]] = Array.fill(velocity_size_y){Array.fill(velocity_size_x){0.0}}
    var maxNx:Array[Int] = imaxNx.clone()
    var input:Array[String] = iinput.clone()

    var currPosValid:Boolean = true
    var infeasibleCount:Double = 0

    var representation:Array[Array[Char]] = Array.fill(velocity_size_y){Array.fill(velocity_size_x){'-'}}

    for (i <- 0 to velocity_size_y-1) {
        for (j <- 0 to velocity_size_x-1) {
            prob(i)(j) = 1.0 / (1.0 + 1.0)
        }
    }
    evaluateFitness(0)
    pbest_score = pos_score

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
        var check:Double = 0.0
        for (i <- 0 to velocity_size_y-1) {
            for (j <- 0 to velocity_size_x-1) {
                check = r.nextDouble()
                pos(i)(j) = if (check < prob(i)(j)) { 1.0 } else { 0.0 }
            }
        }
    }

    def evaluateFitness(runNum:Int):Unit = {
        var str:String = ""
        var str_length:Int = 0
        var str_init_length:Int = 0
        var leadingSpaces:Int = 0
        var totalLeadingSpaces:Int = 0
        //var w1:Double = Math.abs((Math.sin(2*Math.PI*runNum / (5))))  //5
        //var w2:Double = 1.0-w1
        var w1:Double = 0.5*Math.abs((Math.sin(2*Math.PI*(runNum+1.25) / (5))))+0.5
        var w2:Double = 1.0-w1
        //var w1:Double = Math.abs((Math.cos(0.0001*runNum+0.5)))
        //var w2:Double = 1.0-w1
        //var w1:Double = 0.75 //0.65
        //var w2:Double = 1.0-w1
        var pos_penalty:Double = 0.0

        currPosValid = checkPosValidity()
        if (!currPosValid) {
            infeasibleCount+=1
            return
        }

        for (i <- 0 to velocity_size_y-1) {
            str = input(i)
            str_length = input(i).length()
            str_init_length = str_length
            leadingSpaces = 0
            for (j <- 0 to velocity_size_x-1) {
                if (pos(i)(j) == 1) {
                    // Differentiates between leading and trailing spaces
                    if (str_length > 0) {
                        leadingSpaces+=1
                        representation(i)(j) = '-'
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
        var foundStr:String = ""

        for (j <- 0 to velocity_size_x-1) {
            foundStr = ""
            for (i <- 0 to velocity_size_y-1) {
                currCh = representation(i)(j)
                if (currCh != '-' && !foundStr.contains(currCh)) {
                    for (k <- (i+1) to velocity_size_y-1) {
                        tempCh = representation(k)(j)
                        if (currCh == tempCh) {
                            totalAllignments += 2
                        } else if (tempCh != '-') {
                            /*totalAllignments -= 1
                            totalLeadingSpaces += 1*/
                            //pos_penalty += 1
                        }
                    }
                    foundStr = foundStr+currCh
                }
                
            }

        }
        
        pos_score = w1*totalAllignments + w2*(maxNx.sum - totalLeadingSpaces)
    }

    def checkPosValidity():Boolean = {
        var count:Int = 0
        for (i <- 0 to velocity_size_y-1) {
            for (j <- 0 to velocity_size_x-1) {
                if (pos(i)(j) == 0) {
                    count+=1
                }
            }
            
            if (input(i).length() > count) {
                return false
            }
            count = 0
        }
        return true
    }

    def updatePBestPos():Unit = {
        if (!currPosValid) {
            return
        }
        if (pos_score > pbest_score) {
            pbest_score = pos_score
            pbest_pos = pos
        }
    }

    def getVelocityMagnetude():Double = {
        var totalVM:Double = 0.0
        for (i <- 0 to velocity_size_y-1) {
            totalVM += Math.pow(velocity(i).sum, 2)
        }
        Math.sqrt(totalVM)
    }

    def printPos(): Unit = {
        println("Position matrix "+ currPosValid)
        for (i <- 0 to velocity_size_y-1) {
            for (j <- 0 to velocity_size_x-1) {
                print(pos(i)(j)+" ") 
            }
            println("")
        }

    }

    override def toString(): String = {
        println("String representation")
        for (i <- 0 to velocity_size_y-1) {
            for (j <- 0 to velocity_size_x-1) {
                print(representation(i)(j)+" ") 
            }
            println("")
        }
        ""

    }

}