import java.io.PrintWriter
import java.io.File

object BH extends App {
    val file_path:String = "../Data/SALatest/"
    val data:readData = new readData(file_path, "SBK.csv")
    var stockData:Array[Double] = data.getOpenTimeSeries()

    var init_investibleAmount:Double = 1000000.0
    var investibleAmount:Double = 1000000.0
    var stocks:Double = 0.0

    var filename = "../testOutput/SALatest/BH_SBK.csv"
    val pw1 = new PrintWriter(new File(filename))

    // Buy stocks
    println("*****************BUY AND HOLD strat*******************")
    stocks = investibleAmount/stockData(0)
    investibleAmount = 0.0
    println("Amount of stocks baught: "+stocks)
    println("At price: "+stockData(0))

    println("At end of in data.")
    investibleAmount = stocks * stockData(1569)
    stocks = 0
    println("Stocks sold for: "+investibleAmount)
    println("At price: "+stockData(1569))

    var netprofit_in:Double = (Math.pow(1+(investibleAmount-init_investibleAmount)/init_investibleAmount , (252.0/1569.0)) -1)*100
    println("Annual returns (%) for in data: "+ netprofit_in )
    pw1.write(netprofit_in+",")

    investibleAmount = init_investibleAmount
    stocks = 0.0
    stocks = investibleAmount/stockData(1570)
    investibleAmount = 0.0
    println("Amount of stocks baught: "+stocks)
    println("At price: "+stockData(1570))

    println("At end of in data.")
    investibleAmount = stocks * stockData(2133)
    stocks = 0
    println("Stocks sold for: "+investibleAmount)
    println("At price: "+stockData(2133))

    var netprofit_out:Double = (Math.pow(1+(investibleAmount-init_investibleAmount)/init_investibleAmount , (252.0/563.0)) -1)*100
    println("Annual returns (%) for out data: "+ netprofit_out )
    pw1.write(netprofit_out+"")
    pw1.close()


}