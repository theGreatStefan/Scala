//This is a very broken attempt at a Neural network

import java.lang.Math

object NN {    // Requires a main for an entry point
    
    def dotProduct(vector: List[Double], matrix: List[List[Double]]): List[Double] = { 
        // ignore dimensionality checks for simplicity of example 
        (0 to (matrix(0).size - 1)).toList.map( colIdx => { 
            val colVec: List[Double] = matrix.map( rowVec => rowVec(colIdx) ) 
            val elemWiseProd: List[Double] = (vector zip colVec).map( entryTuple => entryTuple._1 * entryTuple._2 ) 
            elemWiseProd.sum 
        } )
    }

    def dotProductMatrices(matrix1: List[List[Double]], matrix2: List[List[Double]]): List[List[Double]] = {
        var returnMat:List[List[Double]] = List(List(0,0,0))
        for (i <- 0 to matrix1.length-1) {
            var matEl:List[Double] = matrix1(i)
            var vector:List[Double] = (
            (0 to (matrix2(0).size - 1)).toList.map( colIdx => {
                val colVec: List[Double] = matrix2.map( rowVec => rowVec(colIdx) ) 
                val elemWiseProd: List[Double] = (matEl zip colVec).map( entryTuple => entryTuple._1 * entryTuple._2 ) 
                elemWiseProd.sum 
            } ) )
            if (i == 0) {
                returnMat = returnMat :+ vector
                returnMat = returnMat.drop(1)
            } else {
                returnMat = returnMat :+ vector
            }
        }
        return returnMat
    }

    /*def sigmoid(x:List[Double]): List[Double] = {
        var new_x:List[Double] = (for (i <- 0 to x.size-1) yield (1.0 / (1.0 + Math.exp(-x(i))))).toList
        return new_x
    }*/

    def sigmoid(x:List[List[Double]]): List[List[Double]] = {
        var returnX = x
        for (i <- 0 to x.length-1) {
            var new_x:List[Double] = (for (j <- 0 to x(i).size-1) yield (1.0 / (1.0 + Math.exp(-x(i)(j))))).toList
            returnX = returnX :+ new_x
            returnX = returnX.drop(1)
        }

        return returnX
    }

    /*def sigmoidDeriv(x:List[Double]): List[Double] = {
        var new_x:List[Double] = (for (i <- 0 to x.size-1) yield (x(i) * (1.0 - x(i)))).toList
        return new_x
    }*/

    def sigmoidDeriv(x:List[List[Double]]): List[List[Double]] = {
        var returnX = x
        for (i <- 0 to x.length-1) {
            var new_x:List[Double] = (for (j <- 0 to x(i).size-1) yield (x(i)(j) * (1.0 - x(i)(j)))).toList
            returnX = returnX :+ new_x
            returnX = returnX.drop(1)
        }
        return returnX
    }

    var training_in: List[List[Double]] = List(List(0, 0, 1),
                                               List(1, 1, 1),
                                               List(1, 0, 1),
                                               List(0, 1, 1))

    var training_out: List[List[Double]] = List(List(0),
                                                List(1),
                                                List(1),
                                                List(0))

    var r = scala.util.Random

    var weights:List[List[Double]] = List(List(2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1),
                                          List(2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1),
                                          List(2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1))

    var input_layer:List[List[Double]] = List(List(0))

    var hidden_layer:List[List[Double]] = List(List(-100))

    var hidden_weights:List[List[Double]] = List(List(2 * r.nextDouble() - 1),
                                                 List(2 * r.nextDouble() - 1),
                                                 List(2 * r.nextDouble() - 1),
                                                 List(2 * r.nextDouble() - 1))

    var output_layer:List[List[Double]] = List(List(0))

    var errors:List[List[Double]] = List(List(0))

    var adjustments:List[List[Double]] = List(List(0))

    var hidden_adjustments:List[List[Double]] = List(List(0))

//Go from here at 28:01
    def main(args: Array[String]): Unit = {
        for (i <- 0 to 100000) {
            input_layer = training_in

            hidden_layer = List(List(-100))
            
            //var hidden = sigmoid(dotProductMatrices(input_layer, weights))
            hidden_layer = sigmoid(dotProductMatrices(input_layer, weights))
            output_layer = sigmoid(dotProductMatrices(hidden_layer, hidden_weights))

            var output_errors = (for (k <- 0 to training_out.length-1) yield 
                (for (j <- 0 to training_out(k).length-1) yield (training_out(k)(j) - output_layer(k)(j))).toList).toList
            var output_delta = (for (k <- 0 to output_errors.length-1) yield
                (for (j <- 0 to output_errors(k).size-1) yield (output_errors(k)(j)*sigmoidDeriv(output_layer))).toList).toList
            var hidden_errors = dotProductMatrices(output_delta, hidden_weights.transpose)
            var hidden_delta = (for (k <- 0 to hidden_errors.length-1) yield
                (for (j <- 0 to hidden_errors(k).size-1) yield (hidden_errors(k)(j)*sigmoidDeriv(hidden_layer)).toList)).toList

            var adjustHW = dotProductMatrices(hidden_layer, output_delta)
            var countHW = hidden_weights.length
            hidden_weights = (for (j <- 0 to countHW-1) yield (
                hidden_weights +: hidden_weights(j).map(el => el+(adjustments(hidden_weights(j).indexOf(el))))
                hidden_weights = hidden_weights.drop(1)
            ))

            var adjustW = dotProductMatrices(input_layer, hidden_delta)
            var countW = weights.length
            weights = (for (j <- 0 to countW-1) yield (
                weights +: weights(j).map(el => el+(adjustments(weights(j).indexOf(el))))
                weights = weights.drop(1)
            ))

        }

        println(output_layer)

        
    }
    
}