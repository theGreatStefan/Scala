import java.lang.Math

object test extends App {
    var stockData:Array[Double] = Array(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    //for (i <- 0 to data.length-1) yield (println(data(i)))

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

    println(RSI(20, 14))

}