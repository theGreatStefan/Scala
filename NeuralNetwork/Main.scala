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
        var new_x:List[Double] = (for (i <- 0 to x.size) yield (1.0 / (1.0 + Math.exp(-x(i))))).toList
        return new_x
    }

    def sigmoidDeriv(x:List[Double]): List[Double] = {
        var new_x:List[Double] = (for (i <- 0 to x.size) yield x(i) * (1.0 - x(i))).toList
        return new_x
    }

    var training_in: List[List[Double]] = List(List(0, 0, 1),
                                            List(1, 1, 1),
                                            List(1, 0, 1),
                                            List(0, 1, 1))

    var training_out: List[Double] = List(0, 1, 1, 0)

    var r = scala.util.Random

    var weights:List[Double] = (List(2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1, 2 * r.nextDouble() - 1))

    var input_layer:List[List[Double]] = List(List(0))

    var output_layer:List[Double] = List(0)

    var errors:List[Double] = List(0)

    var adjustments:List[Double] = List(0)


    def main(args: Array[String]): Unit = {
        for (i <- 0 to 100) {
            println("Iteration nr"+i)
            input_layer = training_in

            output_layer = sigmoid(dotProduct(weights, input_layer))
            
            errors = (for (j <- 0 to training_out.size) yield (training_out(j) - output_layer(j))).toList

            var sigDeriv:List[Double] = sigmoidDeriv(output_layer)
            adjustments = (for (j <- 0 to errors.size) yield (errors(j) * sigDeriv(j))).toList

            weights = dotProduct(adjustments, input_layer)

        }

        println("Printing out the first iteration: ")
        println(output_layer)
        }
    
}