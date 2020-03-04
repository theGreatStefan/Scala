
object main extends App {
    val file_path:String = "../Data/"

    val data:readData = new readData(file_path, "BP.csv")

    println(data.getDate(200))
    println(data.getOpen(200))
    println(data.getClose(200))
    println(data.getVolume(200))

    println(data.findDate("2007-04-23"))

}