import java.lang.Math

object NN {

    def sigmoid(x:Array[Array[Double]], deriv:Boolean): Array[Array[Double]] = {
        if (deriv){
            for (i <- 0 to x.length-1) {
                x(i) = x(i).map(el => (el*(1-el)))
            }
        } else {
            for (i <- 0 to x.length-1) {
                x(i) = x(i).map(el => (1/(1+Math.exp(-el))))
            }
        }
        return x
    }

    def dotProductMatrices(matrix1: Array[Array[Double]], matrix2: Array[Array[Double]]): Array[Array[Double]] = {
        var returnMat:Array[Array[Double]] = Array.ofDim[Double](matrix1.length, matrix2(0).length)
        for (i <- 0 to matrix1.length-1) {
            var matEl:Array[Double] = matrix1(i)
            var vector:Array[Double] = (
            (0 to (matrix2(0).length - 1)).toArray.map( colIdx => {
                val colVec: Array[Double] = matrix2.map( rowVec => rowVec(colIdx) ) 
                val elemWiseProd: Array[Double] = (matEl zip colVec).map( entryTuple => entryTuple._1 * entryTuple._2 ) 
                elemWiseProd.sum 
            } ) )
            
            returnMat(i) = vector
        }
        return returnMat
    }

    var input_data:Array[Array[Double]] = Array(Array(0,0,1),
                                                Array(0,1,1),
                                                Array(1,0,1),
                                                Array(1,1,1) )

    var output_data:Array[Array[Double]] = Array(Array(1),
                                                 Array(0),
                                                 Array(0),
                                                 Array(1))

    var r = scala.util.Random

    var syn0:Array[Array[Double]] = Array(Array(2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1),
                                          Array(2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1),
                                          Array(2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1))

    var syn1:Array[Array[Double]] = Array(Array(2 * r.nextDouble() - 1),
                                          Array(2 * r.nextDouble() - 1),
                                          Array(2 * r.nextDouble() - 1),
                                          Array(2 * r.nextDouble() - 1))
    /*var syn0:Array[Array[Double]] = Array(Array(6.38866509, 6.32696724, -3.58124886, -3.82780347),
                                          Array(-7.03092137, -5.62771339, 1.876251, -4.09604335),
                                          Array(-3.08689442, 2.61721117, -0.24907715, 0.84759576))

    var syn1:Array[Array[Double]] = Array(Array(-13.10552019),
                                          Array(7.04077103),
                                          Array(-7.16393713),
                                          Array(4.38937447))*/
    
    var l2:Array[Array[Double]] = output_data

    def main(args: Array[String]): Unit = {

        /*var l0 = input_data
        var l1 = sigmoid(dotProductMatrices(l0, syn0), false)
        l2 = sigmoid(dotProductMatrices(l1, syn1), false)*/
        for (i <- 0 to 10000) {
            // layers
            var l0 = input_data
            var l1 = sigmoid(dotProductMatrices(l0, syn0), false)
            l2 = sigmoid(dotProductMatrices(l1, syn1), false)

            // backpropogation
            var l2_error:Array[Array[Double]] = Array.ofDim[Double](l2.length, l2(0).length)
            for (j <- 0 to l2_error.length-1) {
                var l2_error_vec:Array[Double] = output_data(j).zip(l2(j)).map{case (a,b) => a-b}
                l2_error(j) = l2_error_vec
            }

            var l2_sigDiv:Array[Array[Double]] = sigmoid(l2, deriv=true)
            var l2_delta:Array[Array[Double]] = Array.ofDim[Double](l2_sigDiv.length, l2_sigDiv(0).length)
            for (j <- 0 to l2_delta.length-1) {
                for (k <- 0 to l2_delta(0).length-1) {
                    l2_delta(j)(k) = l2_error(j)(k) * l2_sigDiv(j)(k)
                }
            }

            var l1_error:Array[Array[Double]] = dotProductMatrices(l2_delta, syn1.transpose)

            var l1_sigDiv:Array[Array[Double]] = sigmoid(l1, deriv=true)
            var l1_delta:Array[Array[Double]] = Array.ofDim[Double](l1_sigDiv.length, l1_sigDiv(0).length)
            for (j <- 0 to l1_delta.length-1) {
                for (k <- 0 to l1_delta(0).length-1) {
                    l1_delta(j)(k) = l1_error(j)(k) * l1_sigDiv(j)(k)
                }
            }

            // Update synapses
            var new_syn1:Array[Array[Double]] = dotProductMatrices(l1.transpose, l2_delta)
            for (j <- 0 to new_syn1.length-1) {
                var new_syn1_vec:Array[Double] = syn1(j).zip(new_syn1(j)).map{case (a,b) => a+b}
                syn1(j) = new_syn1_vec
            }

            var new_syn0:Array[Array[Double]] = dotProductMatrices(l0.transpose, l1_delta)
            for (j <- 0 to new_syn0.length-1) {
                var new_syn0_vec:Array[Double] = syn0(j).zip(new_syn0(j)).map{case (a,b) => a+b}
                syn0(j) = new_syn0_vec
            }

            
        }

        for (j <- 0 to l2.length-1) {
            for (k <- 0 to l2(0).length-1) {
                print(l2(j)(k)+" ")
            }
            println()
        }
        println()
        
    }
    
    
}