import java.lang.Math

object NN {    
    var myList: List[Int] = List(0, 1, 2, 3, 4, 5, 6)
    var myMatrix: List[List[Int]] = List(List(0,1,2),
                                         List(3,4,5),
                                         List(6,7,8))

    var matrix_1: List[List[Double]] = List(List(1, 0, 1),
                                            List(1, 1, 0),
                                            List(0, 0, 1))

    var matrix_2: List[List[Double]] = List(List(0, 1, 1),
                                            List(1, 0, 1),
                                            List(1, 1, 1))

    var matrix_11: Array[Array[Double]] = Array(Array(1, 0, 1),
                                            Array(1, 1, 0),
                                            Array(0, 0, 1),
                                            Array(0, 1, 0))

    var matrix_22: Array[Array[Double]] = Array(Array(0, 1, 1, 0),
                                            Array(1, 0, 1, 1),
                                            Array(1, 1, 1, 0))

    
    def main(args: Array[String]): Unit = {
        println("My list:  "+myList)
        var newList:List[Int] = myList.map(el => el+1)
        println("New List: "+newList)
        println("------------------------------------------------------")

        println("My matrix:  "+myMatrix)
        var newMatrix:List[List[Int]] = myMatrix.map(matEl => matEl.map(el => el+1))
        println("New Matrix: "+newMatrix)
        println("------------------------------------------------------")

        var mydotMatrix:List[List[Double]] = dotProductMatrices(matrix_1, matrix_2)
        println("My dot matrix: "+mydotMatrix)
        println("------------------------------------------------------")

        var mydotMatrix_2:Array[Array[Double]] = dotProductMatrices_2(matrix_11, matrix_22)
        println("My dot matrix 2: ")
        for (j <- 0 to mydotMatrix_2.length-1) {
            for (k <- 0 to mydotMatrix_2(j).length-1) {
                print(mydotMatrix_2(j)(k)+" ")
            }
            println()
        }
        println("------------------------------------------------------")

        // Zip
        var a: Array[Double] = Array(1,2,3)
        var b: Array[Double] = Array(4,5,6)
        //var a_zip_b: Array[(Double, Double)] = a.zip(b)
        var a_zip_b: Array[Double] = a.zip(b).map{case (a,b) => a+b}
        println("a_zip_b.map((a,b) => a+b):")
        for (i <- 0 to a_zip_b.length-1) print(a_zip_b(i)+" ")
        println()
        println("------------------------------------------------------")

        var c: Array[Double] = Array(1,2,3)
        var d: Array[Double] = Array(4,5,6)
        var c_zip_d: Array[(Double, Double)] = (c).zip(d)
        println("c_zip_d:")
        for (i <- 0 to c_zip_d.length-1) print(c_zip_d(i)+" ")
        println()
        println("------------------------------------------------------")

        // 1D array to 2D array
        var arr:Array[Double] = Array(1,2,3)
        var twoDArr:Array[Array[Double]] = arr.map(el => Array(el))
        for (i <- 0 to twoDArr.length-1) {
            for (j <- 0 to twoDArr(0).length-1) {
                println(twoDArr(i)(j))
            }
        }
        println("--------------------------------------------------------")
    }

    // dot product using lists
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

    // dot product using arrays
    def dotProductMatrices_2(matrix1: Array[Array[Double]], matrix2: Array[Array[Double]]): Array[Array[Double]] = {
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

    // dot product using zip
    def dotProductMatrices_3(matrix1: Array[Array[Double]], matrix2: Array[Array[Double]]): Array[Array[Double]] = {
        var returnMat:Array[Array[Double]] = Array.ofDim[Double](matrix1(0).length, matrix2.length)
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
    
}