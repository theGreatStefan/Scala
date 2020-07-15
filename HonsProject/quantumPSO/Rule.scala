import java.io.PrintWriter
import java.io.File

object Rule extends App {
    val file_path:String = "../Data/USAandEURO/"
    val data:readData = new readData(file_path, "XOM.csv")
    var stockData:Array[Double] = data.getOpenTimeSeries()

    var init_investableAmount:Double = 1000000.0
    var investableAmount:Double = 1000000.0
    var prev_investableAmount:Double = 0.0
    var stocks:Double = 0.0
    var capitalGains:Double = 0.0
    var capitalLosses:Double = 0.0
    var transactionCost:Double = 0.0

    var filename = "../testOutput/USAandEURO/Rule_XOM.csv"
    val pw1 = new PrintWriter(new File(filename))

    /****************************Functions**********************************/
    var priceMomentum:Array[Double] = Array.fill(stockData.length){0.0}
    var percentageBBands:Array[Double] = Array.fill(stockData.length){0.0}
    var rSIs:Array[Double] = Array.fill(stockData.length){0.0}
    for (i <- 0 to stockData.length-1) {
        percentageBBands(i) = PercentageBBand(i, 20, 2.0)
        rSIs(i) = RSI(i, 14)
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

    /**************************Functions END********************************/
    var L_lower:Double = 30
    var L_mid:Double = 50
    var p_lower:Double = 0
    var p_mid:Double = 50
    println("*****************Rule based trading*******************")
    for (i <- 1 to 1568) {
        if (rSIs(i-1) < L_lower && rSIs(i) >= L_lower) {
            if (percentageBBands(i-1) < p_lower && percentageBBands(i) >= p_lower) {
                if (investableAmount != 0) {
                    buy(stockData(i))
                }
            }
        } 
        else if (rSIs(i-1) < L_mid && rSIs(i) >= L_mid) {
            if (percentageBBands(i-1) < p_mid && percentageBBands(i) >= p_mid) {
                if (stocks != 0) {
                    sell(stockData(i))
                }
            }
        }
    }

    var netprofit_in:Double = (Math.pow(1+(capitalGains - capitalLosses - transactionCost)/init_investableAmount , (252.0/1569.0)) -1)*100
    println("\nAnnual returns (%) for in data: "+ netprofit_in + "\n")
    pw1.write(netprofit_in+",")


    // Out data
    investableAmount = init_investableAmount
    prev_investableAmount = 0.0
    stocks = 0.0
    capitalGains = 0.0
    capitalLosses = 0.0
    transactionCost = 0.0

    for (i <- 1570 to 2133) {
        if (rSIs(i-1) < L_lower && rSIs(i) >= L_lower) {
            if (percentageBBands(i-1) < p_lower && percentageBBands(i) >= p_lower) {
                if (investableAmount != 0) {
                    buy(stockData(i))
                }
            }
        } 
        else if (rSIs(i-1) < L_mid && rSIs(i) >= L_mid) {
            if (percentageBBands(i-1) < p_mid && percentageBBands(i) >= p_mid) {
                if (stocks != 0) {
                    sell(stockData(i))
                }
            }
        }
    }

    var netprofit_out:Double = (Math.pow(1+(capitalGains - capitalLosses - transactionCost)/init_investableAmount , (252.0/563.0)) -1)*100
    println("\nAnnual returns (%) for out data: "+ netprofit_out + "\n")
    pw1.write(netprofit_out+",")
    pw1.close()

    def buy(price:Double):Unit = {
        stocks = investableAmount/price
        prev_investableAmount = investableAmount
        investableAmount = 0.0
        //println("Amount of stocks baught: "+stocks)
        //println("At price: "+price)
    }

    def sell(price:Double):Unit = {
        investableAmount = stocks * price
        stocks = 0
        if (investableAmount - prev_investableAmount > 0) {
            capitalGains += (investableAmount - prev_investableAmount)
        } else {
            capitalLosses += (prev_investableAmount - investableAmount)
        }
        transactionCost += (prev_investableAmount*0.0005)
        
        //println("Stocks sold for: "+investableAmount)
        //println("At price: "+price)
    }


}