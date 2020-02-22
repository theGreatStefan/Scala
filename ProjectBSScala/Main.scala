import java.util.Scanner
import java.io.IOException
import java.lang.Math

object Main extends App{

    private var in = new Scanner(System.in)
    var N:Int = 0
    var E:Int = 0
    var area:Double = 0.0
    var points = List[Point]()
    
    parseInput()

    def parseInput(): Unit = {    
        N = in.nextInt()

        for (i <- 1 to N) {
            print(i+": ")
            points = List[Point]()
            E = in.nextInt()
            populatePoints()
            getArea()
            //points.foreach{p:Point => p.printPoint()}
        }

        
        //points.foreach{p:Point => p.printPoint()}
    }

    def getArea(): Unit = {
        var totalX:Long = 0
        var totalY:Long = 0
        var numLatticePoints:Long = 0

        for (i <- 0 to E-1) {
            totalX += (points(i).get_x() * points(i).get_next_y())
            totalY += (points(i).get_y() * points(i).get_next_x())
        }

        area = Math.abs(totalX - totalY)

        for (i <- 0 to E-1) {
            numLatticePoints += points(i).getNumLatticePoints()
        }

        println(Math.round((area + 2 - numLatticePoints)/2))

    }

    def populatePoints(): Unit = {
        var x:Long = 0
        var y:Long = 0

        for (i <- 0 to E-1) {
            x = in.nextLong()
            y = in.nextLong()

            var point:Point = Point(x, y)
            points = points :+ point

            if (i>0) points(i-1).setNextPoint(x, y)

            if (i == E-1) points(i).setNextPoint(points(0).get_x, points(0).get_y)
        }
    }

    case class Point(private var x:Long, private var y:Long) 
    {
        var next_x:Long = 0
        var next_y:Long = 0

        def setNextPoint(next_x:Long, next_y:Long) : Unit = {
            this.next_x = next_x
            this.next_y = next_y
        }

        def printPoint() : Unit = println("X: "+x+"; Y: "+y)
        
        def get_x() : Long = this.x 

        def get_y() : Long = this.y 
        
        def get_next_x() : Long = this.next_x 

        def get_next_y() : Long = this.next_y

        def getNumLatticePoints() : Long = {
            var change_y:Long = Math.abs(next_y - y)
            var change_x:Long = Math.abs(next_x - x)
            myGCD(change_x, change_y)
        }

        def myGCD(change_x:Long, change_y:Long) : Long = {
            if (change_y == 0) return change_x
            myGCD(change_y, change_x % change_y)
        }
    }

}