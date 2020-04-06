import java.lang.Math

class HOFParticle (){
    var pos:Array[Double] = Array()
    var net_profit:Double = Double.MinValue
    var sharpe_ratio:Double = Double.MinValue

    def setPos(x:Array[Double]):Unit = {
        pos = x.clone()
    }

    def getPos():Array[Double] = {
        pos
    }

    def setNetProfit(x:Double):Unit = {
        net_profit = x
    }

    def getNetProfit():Double = {
        net_profit
    }

    def setSharpeRatio(x:Double):Unit = {
        sharpe_ratio = x
    }

    def getSharpeRatio():Double = {
        sharpe_ratio
    }

    def isEqualTo(x:Array[Double]):Boolean = {
        (pos sameElements x)
    }

}