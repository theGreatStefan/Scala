import java.lang.Math

object test extends App {
    val file_path:String = "../Data/"
    val data:readData = new readData(file_path, "BP.csv")

    var stockData:Array[Double] = data.getOpenTimeSeries()

    // Sharpe ratio
    var a:Int = 2
    var b:Int = 1
    var c:Double = 5
    println(c/(a+b))
    

}