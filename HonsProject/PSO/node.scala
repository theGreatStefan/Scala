import java.lang.Math

class node(numIncomming:Int) {
    var total:Double = 0.0
    var activatedNum:Double = 0.0

    def addSynopsValue(value:Double) {
        total += value
    }

    def sum():Double = {
        total
    }

    def getactivatedNum(): Double = {
        activatedNum
    }

    def setActivatedNum(x:Double) {
        activatedNum = x
        total = 0
    }

    override def toString(): String = {
        "----------------------------------------------"+
        "\nNumber of incomming synopses: "+numIncomming+
        "\nSum of all weighted inputs: "+sum()+
        "\nValue after activation: "+activatedNum+
        "\n----------------------------------------------"
    }

}