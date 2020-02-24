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

    def sigmoid(x:List[Double]): List[Double] = {
        var new_x:List[Double] = (for (i <- 0 to x.size-1) yield (1.0 / (1.0 + Math.exp(-x(i))))).toList
        return new_x
    }

    def sigmoidDeriv(x:List[Double]): List[Double] = {
        var new_x:List[Double] = (for (i <- 0 to x.size-1) yield (x(i) * (1.0 - x(i)))).toList
        return new_x
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

    var output_layer:List[Double] = List(0)

    var errors:List[Double] = List(0)

    var adjustments:List[Double] = List(0)

    var hidden_adjustments:List[Double] = List(0)

//Go from here at 28:01
    def main(args: Array[String]): Unit = {
        for (i <- 0 to 100000) {
            input_layer = training_in

            hidden_layer = List(List(-100))
            for (j <- 0 to 1) {
                var hidden = sigmoid(dotProduct(weights.slice(j*3, (j+1)*3), input_layer.transpose))
                hidden = hidden.map(_ * hidden_weights(j))
                if (hidden_layer(0)(0) == -100) {
                    hidden_layer = List(hidden)
                } else {
                    hidden_layer = hidden_layer ::: List(hidden)
                }
                
            }
            output_layer = sigmoid(dotProduct(hidden_weights, hidden_layer))
            
            //output_layer = sigmoid(dotProduct(weights, input_layer.transpose))
            
            errors = (for (j <- 0 to training_out.size-1) yield (training_out(j) - output_layer(j))).toList

            var sigDeriv:List[Double] = sigmoidDeriv(output_layer)

            hidden_adjustments = (for (j <- 0 to errors.size-1) yield (errors(j) * sigDeriv(j))).toList
            var new_hidden_weights = dotProduct(hidden_adjustments, hidden_layer)
            hidden_weights = (for (j <- 0 to hidden_weights.size-1) yield (hidden_weights(j) + new_hidden_weights(j))).toList

            var sigDeriv_2:List[Double] = sigmoidDeriv(hidden_layer)
            //adjustments = (for (j <- 0 to errors.size-1) yield (errors(j) * sigDeriv(j))).toList
            adjustments = (for (j <- 0 to new_hidden_weights.size-1) yield (new_hidden_weights(j) * sigDeriv(j))).toList
            //var new_weights = dotProduct(adjustments, input_layer)
            weights = (for (j <- 0 to weights.size-1) yield (
                if (j<3) {
                    weights(j) + new_weights(j)
                } else {
                    weights(j) + new_weights(j-3)
                }
                )).toList
        }

        println(output_layer)

        
    }
    
}