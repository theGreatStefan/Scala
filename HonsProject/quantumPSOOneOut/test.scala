import java.lang.Math
import scala.util.Random

object test extends App {
    var r = scala.util.Random
    /*val file_path:String = "../Data/"
    val data:readData = new readData(file_path, "BP.csv")

    var stockData:Array[Double] = data.getOpenTimeSeries()

    // Sharpe ratio
    var a:Int = 2
    var b:Int = 1
    var c:Double = 5
    println(c/(a+b))*/
    
    /*var x:Array[Double] = Array.fill(100){r.nextGaussian()*0.5}
    for (i <- 0 to 99) {
        println(x(i))
    }*/
    var num:Double = 1.7976931348623157E308+1.7976931348623157E308
    println(num)
    if (num == Double.NaN) {
        println("Found not a number")
    }
}