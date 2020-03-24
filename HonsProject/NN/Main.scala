import java.lang.Math

object Main extends App{
    var inputs:Array[Double] = Array(1,2,3,4,5,6)
    var nn = new NN(10, 6, 3, 10)
    nn.runNN(inputs)

    /*println("\n*********Updating Weights*********\n")
    nn.updateWeights(Array(0.3, 0.1, 0.5, 0.25, 0.90, 0.35, 0.7, 0.1, 0.4, 0.2, 0.15, 0.35, 0.45, 0.6, 0.7))
    var arr:Array[Double] = nn.runNN(inputs)

    println("\n**********Printing Array**********")
    for (i <- 0 to arr.length-1) {
        println(arr(i))
    }*/

}
