import java.lang.Math
import scala.util.Random

class swarm(iswarm_size:Int, iconstraint_size:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, stockData:Array[Double]) {
    var lb:Double = ilb
    var ub:Double = iub
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var swarm_size:Int = iswarm_size
    var constraint_size:Int = iconstraint_size
    var r = scala.util.Random
    var pswarm:Array[particle] = Array()
    var nbest_pos:Array[Array[Double]] = Array.fill(swarm_size){Array.fill(constraint_size){lb + r.nextDouble()*(ub - lb)}}
    var nbest_score:Array[Double] = Array.fill(swarm_size){0.0}
    var priceMomentum:Array[Double] = Array.fill(stockData.length){0.0}
    
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Double] = Array.fill(constraint_size){lb + r.nextDouble()*(ub - lb)}
        pswarm = pswarm :+ new particle(particle_pos, constraint_size, c1, c2, w, lb, ub)

        if (i==0) {
            pswarm(i).setPrevNeighbour(swarm_size-1)
            pswarm(i).setNextNeighbour(i+1)
        } else if (i==(swarm_size-1)) {
            pswarm(i).setPrevNeighbour(i-1)
            pswarm(i).setNextNeighbour(0)
        } else {
            pswarm(i).setPrevNeighbour(i-1)
            pswarm(i).setNextNeighbour(i+1)
        }
    }

    //************ TMI time series
    var aroonUps:Array[Double] = Array.fill(stockData.length){0.0}
    var aroonDowns:Array[Double] = Array.fill(stockData.length){0.0}
    var percentageBBands:Array[Double] = Array.fill(stockData.length){0.0}
    var mACDs1:Array[Double] = Array.fill(stockData.length){0.0}
    var mACDs2:Array[Double] = Array.fill(stockData.length){0.0}
    var rSIs:Array[Double] = Array.fill(stockData.length){0.0}

    for (i <- 0 to stockData.length-1) {
        aroonUps(i) = AroonUp(i, 14)
        aroonDowns(i) = AroonDown(i, 14)
        percentageBBands(i) = PercentageBBand(i, 20, 2.0)
        var macdTuple = MACD(i, 9, 12, 26)
        mACDs1(i) = macdTuple._1
        mACDs2(i) = macdTuple._2
        rSIs(i) = RSI(i, 14)
    }

    // Normalise TMIs
    var aUpMax:Double = aroonUps.max
    var aUpMin:Double = aroonUps.min
    var aDownMax:Double = aroonDowns.max
    var aDownMin:Double = aroonDowns.min
    var pBMax:Double = percentageBBands.max
    var pBMin:Double = percentageBBands.min
    var mAC1Max:Double = mACDs1.max
    var mAC1Min:Double = mACDs1.min
    var mAC2Max:Double = mACDs2.max
    var mAC2Min:Double = mACDs2.min
    var rSIMax:Double = rSIs.max
    var rSIMin:Double = rSIs.min

    for (i <- 0 to stockData.length-1) {
        aroonUps(i) = (aroonUps(i) - aUpMin) / (aUpMax - aUpMin)
        aroonDowns(i) = (aroonDowns(i) - aDownMin) / (aDownMax - aDownMin)
        percentageBBands(i) = (percentageBBands(i) - pBMin) / (pBMax - pBMin)
        mACDs1(i) = (mACDs1(i) - mAC1Min) / (mAC1Max - mAC1Min)
        mACDs2(i) = (mACDs2(i) - mAC2Min) / (mAC2Max - mAC2Min)
        rSIs(i) = (rSIs(i) - rSIMin) / (rSIMax - rSIMin)
        
    }
    //*********** TMI time series END

    /*****************Technical Market Indicators******************/
    
    def AroonUp(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        var highIndex:Double = (stockData.indexOf(prevPrices.max)+1) - (t+1) + p
        var aroonUp:Double = 100*(highIndex/p)
        aroonUp
    }

    def AroonDown(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        var lowIndex:Double = (stockData.indexOf(prevPrices.min)+1) - (t+1) + p
        var aroonDown:Double = 100*(lowIndex/p)
        aroonDown
    }

    def PercentageBBand(t:Int, p:Int, D:Double):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p+1, (t+1))
        var midBand:Double = (prevPrices.sum)/p
        var lowInside:Double = (for (i <- 0 to prevPrices.length-1) yield (Math.pow(prevPrices(i)-midBand, 2))).sum
        var lowBand:Double = midBand - (D * Math.sqrt(lowInside/p))
        var upInside:Double = (for (i <- 0 to prevPrices.length-1) yield (Math.pow(prevPrices(i)-midBand, 2))).sum
        var upBand:Double = midBand + (D * Math.sqrt(lowInside/p))
        var pBand:Double = 100*( (stockData(t)-lowBand) / (upBand-lowBand) )
        pBand
    }

    // TODO Figure out if this is right
    def MACD(t:Int, a:Int, b:Int, c:Int):(Double,Double) = {
        var prevPrices_a:Array[Double] = stockData.slice(t-a+1, (t+1))
        var prevPrices_b:Array[Double] = stockData.slice(t-b+1, (t+1))
        var prevPricesMomentum_c:Array[Double] = priceMomentum.slice(t-c+1, (t+1))
        var ema_a = stockData(t) * (2/(a+1)) + (prevPrices_a.sum/a) * (100-(2/(a+1)))
        var ema_b = stockData(t) * (2/(b+1)) + (prevPrices_b.sum/b) * (100-(2/(b+1)))

        priceMomentum(t) = ema_a - ema_b
        var ema_c = priceMomentum(t) * (2/(c+1)) + (prevPricesMomentum_c.sum/c) * (100-(2/(c+1)))

        var momentumTrigger:Double = ema_c
        (priceMomentum(t), momentumTrigger)
    }

    def RSI(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        
        var totalGain:Double = 0.0
        for (i <- 1 to prevPrices.length-1) {
            var gain:Double = (prevPrices(i)-prevPrices(i-1))                            
            if (gain > 0) {
                totalGain += gain
            }
        }

        var totalLoss:Double = 0.0
        for (i <- 1 to prevPrices.length-1) {
            var loss:Double = (prevPrices(i)-prevPrices(i-1))                            
            if (loss < 0) {
                totalLoss += loss
            }
        }
        totalLoss = Math.abs(totalLoss)

        var rs:Double = totalGain/totalLoss
        var rsi:Double = 100 * (1-(1/(1+rs)))

        rsi
    }

    /***************END Technical Market Indicators****************/

    // Main method called for running the swarm for a specified number of epochs
    // Synchronous lbest PSO
    def runSwarm(epochs:Int):Unit = {
        var TMIs:Array[Double] = Array.fill(6){0.0}

        for (i <- 0 to epochs-1) {

            // Run through the entire TMI time series until 31st March 2006
            for (j <- 0 to 1570) {
                TMIs(0) = aroonUps(j)
                TMIs(1) = aroonDowns(j)
                TMIs(2) = percentageBBands(j)
                TMIs(3) = mACDs1(j)
                TMIs(4) = mACDs2(j)
                TMIs(5) = rSIs(j)

                // TODO: takes very long
                for (k <- 0 to swarm_size-1) {
                    //println("******** particle "+k+" *************")
                    pswarm(k).runNN(TMIs, stockData(j), j)
                }
                
            }

            calc_nbest_scores()

            for (j <- 0 to swarm_size-1) {
                pswarm(j).checkBestPos(nbest_score(j));
            }
            for (j <- 0 to swarm_size-1) {
                checkNBest(j, pswarm(j).pbest_score, pswarm(j).pbest_pos)
            }
            for (j <- 0 to swarm_size-1) {
                pswarm(j).updateVelocity(nbest_pos(j))
                pswarm(j).updatePos()
            }
            println("Run number: "+i)
            println(toString())

            // Reset particles
            for (j <- 0 to swarm_size-1) {
                pswarm(j).reset()
            }
        }
        
    }

    // TODO: not sure if this is correct. Should it be nbest(i) of pswarm(i).pbest_score (?)
    def checkNBest(j:Int, j_score:Double, j_pos:Array[Double]):Unit = {
        var prev_j:Int = pswarm(j).getPrevNeighbour()
        var next_j:Int = pswarm(j).getNextNeighbour()

        if (nbest_score(prev_j) > j_score && nbest_score(prev_j) > nbest_score(next_j)) {
            nbest_score(j) = nbest_score(prev_j)
            nbest_pos(j) = nbest_pos(prev_j)

        } else if (j_score > nbest_score(prev_j) && j_score > nbest_score(next_j)) {
            nbest_score(j) = j_score
            nbest_pos(j) = j_pos

        } else if (nbest_score(next_j) > nbest_score(prev_j) && nbest_score(next_j) > j_score) {
            nbest_score(j) = nbest_score(next_j)
            nbest_pos(j) = nbest_pos(next_j)
        }

    }

    def calc_nbest_scores():Unit = {
        for (i <- 0 to swarm_size-1) {
            nbest_score(i) = relativeFitness(i)
        }
    }

    // Score between 0 and 2; 0 being the worst and 2 being the best
    def relativeFitness(index:Int):Double = {
        var netProfits:Array[Double] = Array.fill(3){0.0}
        var SRs:Array[Double] = Array.fill(3){0.0}
        var maxNetProfit:Double = 0.0
        var minNetProfit:Double = 0.0
        var maxSR:Double = 0.0
        var minSR:Double = 0.0
        var fitness:Double = 0.0

        netProfits(0) = pswarm(pswarm(index).getPrevNeighbour()).getNetProfit
        println("net profit prev N: "+netProfits(0))
        netProfits(1) = pswarm(index).getNetProfit()
        println("net profit curr N: "+netProfits(1))
        netProfits(2) = pswarm(pswarm(index).getNextNeighbour()).getNetProfit
        println("net profit next N: "+netProfits(2))
        SRs(0) = pswarm(pswarm(index).getPrevNeighbour()).getSharpRatio
        println("SharpeRatio prev N: "+SRs(0))
        SRs(1) = pswarm(index).getSharpRatio()
        println("SharpeRatio curr N: "+SRs(1))
        SRs(2) = pswarm(pswarm(index).getNextNeighbour()).getSharpRatio
        println("SharpeRatio next N: "+SRs(2))
        
        maxNetProfit = netProfits.max
        minNetProfit = netProfits.min
        maxSR = SRs.max
        minSR = SRs.min

        fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) ) + ( (SRs(1)-minSR) / (maxSR-minSR) )

        println(fitness)
        fitness
    }
    
    override def toString(): String = {
        "Net profit: "+pswarm(5).getNetProfit()+
        "\nCapital gains: "+pswarm(5).capitalGains+
        "\nCapital losses: "+pswarm(5).capitalLosses+
        "\nStocks: "+pswarm(5).stocks+
        "\nPrice: "+stockData(1570)
    }

}