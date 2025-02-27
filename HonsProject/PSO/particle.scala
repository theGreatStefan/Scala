import java.lang.Math
import scala.util.Random

class particle(ipos:Array[Double], ivelocity_size:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, ilambda:Double) {
    var init_investableAmount:Double = 1000000
    var investableAmount:Double = 1000000
    var prev_investableAmount:Double = 1000000
    var stocks:Double = 0.0
    var capitalGains:Double = 0.0
    var capitalLosses:Double = 0.0
    var transactionCost:Double = 0.0
    // 1570 to change to the amount of data points
    var returnsRatios:Array[Double] = Array.fill(1570){0.0}

    var lb:Double = ilb
    var ub:Double = iub
    var lambda:Double = ilambda
    var pos:Array[Double] = ipos.clone()
    var velocity_size:Int = ivelocity_size
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var r = scala.util.Random
    //r.setSeed(321)
    var pbest_pos:Array[Double] = pos.clone()
    var pbest_score:Double = 0.0
    var velocity:Array[Double] = Array.fill(velocity_size){0.0}
    var prev_neighbour:Int = 0
    var next_neighbour:Int = 0

    // Evangelos: hidden layer 4
    var nn = new NN(4, 6, 3, 4)
    //              ^        ^

    var pbest_net_profit:Double = Double.MinValue
    var pbest_sharpe_ratio:Double = Double.MinValue
    var numBought:Double = 0.0
    var numSold:Double = 0.0
    var numHeld:Double = 0.0

    //var hiddenOutputs = scala.collection.mutable.Map[Double, Int]()
    //initMap(1, 0, 100)

    /*****************Functions******************/
    

    /*****************END_Functions******************/

    def updateVelocity(nbest_pos:Array[Double], vMax:Double):Unit = {
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
            // Element wise velocity clamping
            /*if (velocity(i) > vMax) {
                velocity(i) = vMax
            } else if (velocity(i) < (-vMax)) {
                velocity(i) = -vMax
            }*/
        }

    }

    def updatePos():Unit = {
        for (i <- 0 to velocity_size-1) {
            pos(i) += velocity(i)
        }
    }

    def runNN(TMIs:Array[Double], price:Double, runNum:Int, hof:Boolean):Unit = {
        var results:Array[Double] = Array()
        var maxIndex:Int = 0
        var maxValue:Double = 0

        // Run NN 
        nn.updateWeights(pos)
        results = nn.runNN(TMIs)

        //addToMap(nn.getHiddenLayerOutput())

        // Make decision
        for (i <- 0 to 2) {
            if (results(i) > maxValue) {
                maxValue = results(i)
                maxIndex = i
            }
        }
        
        /*if (hof && runNum == 2133-1571) {
            println("Previous investible ammount: "+prev_investableAmount)
            println("Current investible ammount: "+investableAmount)
        }*/
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
            stocks = investableAmount / price
            transactionCost += (investableAmount*0.0005)
            prev_investableAmount = investableAmount
            investableAmount = 0.0
            numBought += 1
        }
    }

    def sell(price:Double):Unit = {
        if (stocks != 0.0) {
            
            investableAmount = stocks*price
            if (investableAmount - prev_investableAmount > 0) {
                capitalGains += (investableAmount - prev_investableAmount)
            } else {
                capitalLosses += (prev_investableAmount - investableAmount)
            }
            transactionCost += (prev_investableAmount*0.0005)
            stocks = 0.0
            numSold += 1
        }
    }

    def hold():Unit = {
        numHeld += 1
    }

    def getNetProfit():Double = {
        (capitalGains - capitalLosses - transactionCost)
    }

    // 3% for USA, 6% for EU
    def getSharpRatio():Double = {
        var mean:Double = (returnsRatios.sum / (returnsRatios.length).toDouble)
        var stddev:Double = 0.0
        var returnValue:Double = 0.0
        for (i <- 0 to returnsRatios.length-1){
            stddev += Math.pow(returnsRatios(i)-mean, 2)
        }
        stddev = Math.sqrt(stddev/(returnsRatios.length-1).toDouble)*Math.sqrt(1569.0)
        
        if (stddev != 0) {
            returnValue = ( ( (Math.pow(1+returnsRatio(init_investableAmount), (252.0/1569.0))-1 ) - 0.03) / stddev )
        } else {
            returnValue = 0.0//(Double.MinValue) // TODO: is this correct (?)
        }

        if (returnValue.isNaN()) {
            (0.0)
        } else {
            (returnValue)
        }
    }

    // Checks if the realative fitness is better now than it was
    def checkBestPos():Unit = {
        var currFit:Double = relativeFitnessCurr()
        var pbestFit:Double = relativeFitnessPBest()
        
        if (currFit > pbestFit) {
            pbest_pos = pos.clone()
            pbest_net_profit = getNetProfit()
            pbest_sharpe_ratio = getSharpRatio()
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

    def getPrevNeighbour():Int = {
        (prev_neighbour)
    }
    def getNextNeighbour():Int = {
        (next_neighbour)
    }

    def getPos():Array[Double] = {
        pos
    }

    def reset():Unit = {
        capitalGains = 0.0
        capitalLosses = 0.0
        transactionCost = 0.0
        init_investableAmount = 1000000
        investableAmount = 1000000
        prev_investableAmount = 1000000
        stocks = 0.0
        returnsRatios = Array.fill(1569){0.0}
        numBought = 0.0
        numSold = 0.0
        numHeld = 0.0
    }

    def relativeFitnessCurr():Double = {
        var netProfits:Array[Double] = Array.fill(2){0.0}
        var SRs:Array[Double] = Array.fill(2){0.0}
        var maxNetProfit:Double = 0.0
        var minNetProfit:Double = 0.0
        var maxSR:Double = 0.0
        var minSR:Double = 0.0
        var fitness:Double = 0.0

        netProfits(0) = pbest_net_profit
        netProfits(1) = getNetProfit()
        SRs(0) = pbest_sharpe_ratio
        SRs(1) = getSharpRatio()
        
        maxNetProfit = netProfits.max
        minNetProfit = netProfits.min
        maxSR = SRs.max
        minSR = SRs.min

        if (maxNetProfit-minNetProfit == 0 && maxSR-minSR == 0) {
            fitness = 0.0
        } else if (maxNetProfit-minNetProfit == 0) {
            fitness = ( (SRs(1)-minSR) / (maxSR-minSR) )
        } else if (maxSR - minSR == 0) {
            fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) )
        } else {
            fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) ) + ( (SRs(1)-minSR) / (maxSR-minSR) )
        }

        //(fitness-lambda*penaltyFunction())
        fitness
    }

    def relativeFitnessPBest():Double = {
        var netProfits:Array[Double] = Array.fill(2){0.0}
        var SRs:Array[Double] = Array.fill(2){0.0}
        var maxNetProfit:Double = 0.0
        var minNetProfit:Double = 0.0
        var maxSR:Double = 0.0
        var minSR:Double = 0.0
        var fitness:Double = 0.0

        netProfits(0) = getNetProfit()
        netProfits(1) = pbest_net_profit
        SRs(0) = getSharpRatio()
        SRs(1) = pbest_sharpe_ratio
        
        maxNetProfit = netProfits.max
        minNetProfit = netProfits.min
        maxSR = SRs.max
        minSR = SRs.min

        if (maxNetProfit-minNetProfit == 0 && maxSR-minSR == 0) {
            fitness = 0.0
        } else if (maxNetProfit-minNetProfit == 0) {
            fitness = ( (SRs(1)-minSR) / (maxSR-minSR) )
        } else if (maxSR - minSR == 0) {
            fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) )
        } else {
            fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) ) + ( (SRs(1)-minSR) / (maxSR-minSR) )
        }

        //(fitness-lambda*penaltyFunctionPBest())
        fitness
    }

    def penaltyFunction():Double = {
        var total:Double = 0.0
        for (i <- 0 to velocity_size-1) {
            total += Math.pow(pos(i), 2)
        }
        0.5*total
    }

    def penaltyFunctionPBest():Double = {
        var total:Double = 0.0
        for (i <- 0 to velocity_size-1) {
            total += Math.pow(pbest_pos(i), 2)
        }
        0.5*total
    }

    def getVelocityMagnitude(): Double = {
        var total:Double = 0.0
        for (i <- 0 to velocity_size-1) {
            total += Math.pow(velocity(i), 2)
        }
        Math.sqrt(total)
    }

    /*def initMap(ubound:Double, lbound:Double, numBins:Int):Unit = {
        var binWidth = (ubound - lbound)/numBins.toDouble
        var num:Double = 0
        for (i <- 0 to numBins) {
            num = BigDecimal(lbound+(binWidth*i)).setScale(2, BigDecimal.RoundingMode.DOWN).toDouble
            hiddenOutputs += (num -> 0)
            //println(num)
        }
    }

    def addToMap(x:Array[Double]):Unit = {
        var num:Double = 0
        for (i <- 0 to x.length-1) {
            num = BigDecimal(x(i)).setScale(2, BigDecimal.RoundingMode.DOWN).toDouble
            hiddenOutputs(num) = hiddenOutputs(num)+1
        }
    }*/
    
    override def toString(): String = {
        "---------------------"+
        "\nCurrent velocity:  "+ (for (i <- 0 to velocity_size-1) yield (velocity(i))) +
        "\nCurrent position:  "+ (for (i <- 0 to velocity_size-1) yield (pos(i)))+
        "\nPersonal best pos: "+ (for (i <- 0 to velocity_size-1) yield (pbest_pos(i)))+
        "\nPersonal best score: "+pbest_score+
        "\n---------------------"
    }

}