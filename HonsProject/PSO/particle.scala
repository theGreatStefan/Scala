import java.lang.Math
import scala.util.Random

class particle(ipos:Array[Double], ivelocity_size:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, function:String) {
    var init_investableAmount:Double = 1000000
    var investableAmount:Double = 1000000
    var prev_investableAmount:Double = 1000000
    var stocks:Double = 0.0
    var capitalGains:Double = 0.0;
    var capitalLosses:Double = 0.0;
    var transactionCost:Double = 0.0;
    // 100 to change to the amount of data points
    var returnsRatios:Array[Double] = Array.fill(100){0.0}

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
    var prev_neighbour:Int = 0
    var next_neighbour:Int = 0

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

    }

    def updatePos():Unit = {
        for (i <- 0 to velocity_size-1) {
            pos(i) += velocity(i)
        }
    }

    def runNN(TMIs:Array[Double], price:Double, runNum:Int):Unit = {
        var results:Array[Double] = Array()
        var maxIndex:Int = 0
        var maxValue:Int = 0
        // Run NN here
        // Make decision
        for (i <- 0 to 2) {
            if (results(i) > maxValue) {
                maxValue = results(i)
                maxIndex = i
            }
        }
        // make transaction
        maxIndex match {
            case 0 => buy(price)
            case 1 => sell(price)
            case 2 => hold()
            case _ => hold()
        }

        returnsRatios(runNum) = returnsRatio(init_investableAmount)
        
    }

    def buy(price:Double):Unit = {
        if (investableAmount != 0.0) {
            stocks = investableAmount / amount
            transactionCost += investableAmount*0.0005
            prev_investableAmount = investableAmount
            investableAmount = 0.0
        }
    }

    def sell(price:Double):Unit = {
        if (stocks != 0.0) {
            investableAmount = stocks*price
            if (investableAmount - prev_investableAmount > 0) {
                capitalGains += investableAmount-prev_investableAmount
            } else {
                capitalLosses += prev_investableAmount - investableAmount
            }
            transactionCost += prev_investableAmount*0.0005
        }
    }

    def hold():Unit = {

    }

    def getNetProfit():Double = {
        (capitalGains - capitalLosses - transactionCost)
    }

    // 3% for USA, 6% for EU
    def getSharpRatio():Double = {
        var mean:Double = (returnsRatios.sum / returnsRatios.length)
        var stddev:Double = 0.0
        for (i <- 0 to returnsRatios.length-1){
            stddev += Math.pow(returnsRatios(i)-mean, 2)
        }
        stddev = Math.sqrt(stddev/(returnsRatios.length-1))

        ( (returnsRatio(init_investableAmount)-0.03) / stddev )
    }

    // Checks if the realative fitness is better now than it was
    // TODO: Still unsure if this is the optimal way to do it since we are comparing fitnesses and not actually performance
    def checkBestPos(fitness:Double):Unit = {
        if (fitness > pbest_score) {
            pbest_score = fitness
            pbest_pos = pos
        }
    }

    def returnsRatio(IA:Double):Double = {
        ((capitalGains-capitalLosses)/IA)
    }

    def setPrevNeighbour(index:Int):Unit = {
        prev_neighbour = index
    }
    def setNextNeighbour(index:Int):Unit = {
        next_neighbour = index
    }

    def getPrevNeighbour():Unit = {
        prev_neighbour
    }
    def getNextNeighbour():Unit = {
        next_neighbour
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