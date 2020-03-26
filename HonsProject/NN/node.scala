import java.lang.Math

class node(numIncomming:Int) {
    var totalArray:Array[Double] = Array().fill(numIncomming){0.0}
    var activatedNum:Double = 0.0

    def addSynopsValue(value:Double, index:Int) {
        totalArray(index) = value
    }

    def sum():Double = {
        totalArray.sum
    }

    def getactivatedNum(): Double = {
        activatedNum
    }

    def setActivatedNum(x:Double) {
        activatedNum = x
    }

    override def toString(): String = {
        "----------------------------------------------"+
        "\nNumber of incomming synopses: "+numIncomming+
        "\nSum of all weighted inputs: "+sum()+
        "\nValue after activation: "+activatedNum+
        "\n----------------------------------------------"
    }

    def *(x:Double): Double = {
        sum()*x
    }

}