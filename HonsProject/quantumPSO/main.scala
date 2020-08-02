import java.io.PrintWriter
import java.io.File

object main extends App {
    val file_path:String = "../Data/SA/"
    val data:readData = new readData(file_path, "IMP.csv")
    var stockData:Array[Double] = data.getOpenTimeSeries()

    //************ TMI time series
    var priceMomentum:Array[Double] = Array.fill(stockData.length){0.0}
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
        var macdTuple = MACD(i, 12, 26, 9)
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
        aroonUps(i) = 2*((aroonUps(i) - aUpMin) / (aUpMax - aUpMin))-1
        aroonDowns(i) = 2*((aroonDowns(i) - aDownMin) / (aDownMax - aDownMin))-1
        percentageBBands(i) = 2*((percentageBBands(i) - pBMin) / (pBMax - pBMin))-1
        mACDs1(i) = 2*((mACDs1(i) - mAC1Min) / (mAC1Max - mAC1Min))-1
        mACDs2(i) = 2*((mACDs2(i) - mAC2Min) / (mAC2Max - mAC2Min))-1
        rSIs(i) = 2*((rSIs(i) - rSIMin) / (rSIMax - rSIMin))-1
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

    def MACD(t:Int, a:Int, b:Int, c:Int):(Double,Double) = {
        var prevPrices_a:Array[Double] = stockData.slice(t-a+1, (t+1))
        var prevPrices_b:Array[Double] = stockData.slice(t-b+1, (t+1))
        var ema_a = stockData(t) * (2/(a+1).toDouble) + (prevPrices_a.sum/a) * (100-(2/(a+1).toDouble))
        var ema_b = stockData(t) * (2/(b+1).toDouble) + (prevPrices_b.sum/b) * (100-(2/(b+1).toDouble))

        priceMomentum(t) = ema_a - ema_b
        var prevPricesMomentum_c:Array[Double] = priceMomentum.slice(t-c+1, (t+1))
        var ema_c = priceMomentum(t) * (2/(c+1).toDouble) + (prevPricesMomentum_c.sum/c) * (100-(2/(c+1).toDouble))

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

        var rsi:Double = 0.0
        if (totalLoss == 0) {
            rsi = 100
        } else {
            var rs:Double = totalGain/totalLoss
            rsi = 100 * (1-(1/(1+rs)))
        }
        
        rsi
    }

    /***************END Technical Market Indicators****************/
    
    var filename = "../testOutput/SA/velocity.csv"
    val pw1 = new PrintWriter(new File(filename))

    filename = "../testOutput/SA/HOF.csv"
    val pw2 = new PrintWriter(new File(filename))

    //filename = "../testOutput/SA/CEPSO_Sigmoid_WeightDecay005_Vmax40_quantum10_SNH.csv"
    filename = "../testOutput/SA/test_IMP.csv"
    val pw3 = new PrintWriter(new File(filename))

    //filename = "../testOutput/SA/CEPSO_Sigmoid_WeightDecay005_Vmax40_quantum10_SNH_avgPos.csv"
    filename = "../testOutput/SA/test_IMP_avgPos.csv"
    val pw4 = new PrintWriter(new File(filename))

    filename = "../testOutput/SA/test_IMP_hist.csv"
    val pw5 = new PrintWriter(new File(filename))

    var epocs:Int = 350

    var avgVelocityMag:Array[Double] = Array.fill(epocs){0.0}
    var avgEuclDist:Array[Double] = Array.fill(epocs){0.0}
    var iterationBest:Array[Double] = Array.fill(epocs){0.0}
    var HOFnetProfit_in:Array[Double] = Array.fill(epocs){0.0}
    var HOFnetProfit_out:Array[Double] = Array.fill(epocs){0.0}
    var avgBestNetProfit_in:Double = 0.0
    var avgBestNetProfit_out:Double = 0.0
    var avgPosVec:Array[Double] = Array.fill(36){0.0}
    var hiddenOutputsArr:Array[Double] = Array.fill(200){0.0}

    var tempVelocityMag:Array[Double] = Array()
    var tempEuclDist:Array[Double] = Array()
    var tempiterationBest:Array[Double] = Array()
    var tempHOFnetProfit_in:Array[Double] = Array()
    var tempHOFnetProfit_out:Array[Double] = Array()
    var tempavgPosVec:Array[Double] = Array.fill(36){0.0}
    var temphiddenOutputsArr:Array[Double] = Array()

    var vMax:Double = 0.40

    var runs:Int = 30
    
    for (i <- 0 to runs-1) {
        //var stockData:Array[Double] = data.getOpenTimeSeries()
            // fanin = 6; input nodes for the NN (?) 
            // constraint size for 4 hidden nodes = 36
            // constraint size for 6 hidden nodes = 54
            // constraint size for 1 output node = 28
        var swarm1 = new swarm(150, 36, 1.496180, 1.496180, 0.729844, -1/(Math.sqrt(6)), 1/(Math.sqrt(6)), stockData, aroonUps,
                                                                                                                    aroonDowns,
                                                                                                                    percentageBBands,
                                                                                                                    mACDs1,
                                                                                                                    mACDs2,
                                                                                                                    rSIs, vMax) 
        swarm1.runSwarm(epocs)

        avgBestNetProfit_in += swarm1.getBestHofNetProfit_in()
        avgBestNetProfit_out += swarm1.getBestHofNetProfit_out()
        System.out.println("Average best: "+avgBestNetProfit_out)

        tempVelocityMag = swarm1.getAvgVelocityMagnitude
        tempEuclDist = swarm1.getAvgEuclidianDistance
        tempiterationBest = swarm1.getIterationBest()
        tempHOFnetProfit_in = swarm1.getHOFnetProfits_in()
        tempHOFnetProfit_out = swarm1.getHOFnetProfits_out()
        for (j <- 0 to epocs-1) {
            avgVelocityMag(j) += tempVelocityMag(j)
            avgEuclDist(j) += tempEuclDist(j)
            iterationBest(j) += tempiterationBest(j)
        }
        for (j <- 0 to tempHOFnetProfit_in.length-1) {
            HOFnetProfit_in(j) += tempHOFnetProfit_in(j)
            HOFnetProfit_out(j) += tempHOFnetProfit_out(j)
        }

        avgPosVec = swarm1.avgPosVector.clone()
        //tempavgPosVec = swarm1.avgPosVector.clone()
        //for (j <- 0 to 36-1) {
        //    avgPosVec(j) += swarm1.avgPosVector(j)
        //}
        temphiddenOutputsArr = swarm1.hiddenOutputsArr.clone()
        for (j <- 0 to 100-1) {
            hiddenOutputsArr(j) += temphiddenOutputsArr(j)
        }

        println("Run "+(i+1)+" complete!")
        
    }

    avgBestNetProfit_in = avgBestNetProfit_in/runs
    avgBestNetProfit_out = avgBestNetProfit_out/runs

    for (j <- 0 to epocs-1) {
        avgVelocityMag(j) = avgVelocityMag(j)/runs.toDouble
        avgEuclDist(j) = avgEuclDist(j)/runs.toDouble
        iterationBest(j) = iterationBest(j)/runs.toDouble
    }

    for (j <- 0 to tempHOFnetProfit_in.length-1) {
        HOFnetProfit_in(j) = HOFnetProfit_in(j)/runs.toDouble
        HOFnetProfit_out(j) = HOFnetProfit_out(j)/runs.toDouble
    }

    for (j <- 0 to 100-1) {
        hiddenOutputsArr(j) = temphiddenOutputsArr(j)/runs.toDouble
    }

    for (i <- 0 to epocs-1) {
        pw1.write(avgVelocityMag(i)+","+avgEuclDist(i)+","+iterationBest(i)+","+HOFnetProfit_in+","+HOFnetProfit_out+"\n")
    }
    for (i <- 0 to HOFnetProfit_in.length-1) {
        pw2.write(HOFnetProfit_in(i)+","+HOFnetProfit_out(i)+"\n")
    }

    pw3.write(avgBestNetProfit_in+","+avgBestNetProfit_out+"\n")

    for (i <- 0 to 36-1) {
        //avgPosVec(i) = avgPosVec(i)/runs.toDouble
        pw4.write(avgPosVec(i)+"\n")
    }

    for (i <- 0 to 100-1) {
        //avgPosVec(i) = avgPosVec(i)/runs.toDouble
        pw5.write(hiddenOutputsArr(i)+"\n")
        println(hiddenOutputsArr(i))
    }

    pw1.close()
    pw2.close()
    pw3.close()
    pw4.close()
    pw5.close()
}