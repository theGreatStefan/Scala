import java.io.PrintStream
import java.io.BufferedReader
import java.io.IOException
import java.io.FileReader
import java.io.FileNotFoundException

class readData(file_path:String, file_name:String) {
    var dates:Array[String] = Array.fill(2134){""}
    var openVal:Array[Double] = Array.fill(2134){0.0}
    var high:Array[Double] = Array.fill(2134){0.0}
    var low:Array[Double] = Array.fill(2134){0.0}
    var closeVal:Array[Double] = Array.fill(2134){0.0}
    var volume:Array[Double] = Array.fill(2134){0.0}
    try {
        var bf:BufferedReader = new BufferedReader(new FileReader(file_path+file_name))
        var currLine:Array[String] = bf.readLine().split(",").toArray
        for (i <- 0 to 2133) {
            currLine = bf.readLine().split(",")
            dates(i) = currLine(0)
            openVal(i) = currLine(1).toDouble
            high(i) = currLine(2).toDouble
            low(i) = currLine(3).toDouble
            closeVal(i) = currLine(4).toDouble
            volume(i) = currLine(6).toDouble
        }
        
        //for (i <- 0 to 2133) yield (println(dates(i)))

    } catch {
        case e:FileNotFoundException => println(e)
    }

    def getDate(i:Int):String = {
        dates(i)
    }
        
    def getOpen(i:Int):Double = {
        openVal(i)
    }

    def getHigh(i:Int):Double = {
        high(i)
    }

    def getLow(i:Int):Double = {
        low(i)
    }

    def getClose(i:Int):Double = {
        closeVal(i)
    }

    def getVolume(i:Int):Double = {
        volume(i)
    }

    def findDate(date:String):Int = {
        dates.indexOf(date)
    }
}