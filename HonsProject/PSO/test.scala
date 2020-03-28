import java.lang.Math

object test extends App {
    var stockData:Array[Double] = Array(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    //for (i <- 0 to data.length-1) yield (println(data(i)))

    stockData(0) = stockData(1)
    println("StockData(0)"+stockData(0)+" StockData(1)"+stockData(1))

    stockData(0) = 5
    println("StockData(0)"+stockData(0)+" StockData(1)"+stockData(1))

}