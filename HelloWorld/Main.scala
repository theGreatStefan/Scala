/*object Hello {    // Requires a main for an entry point
    def main(args: Array[String]): Unit = {
        printString("Hello", " Wolrd")
    }

    def printString(a:String, b:String): Unit = {   // Unit is equavalent to void (its the return type)
        println(a+b)
    }
}*/

object Hello extends App {  // extends App results in all the code in this object being executed
    val first : String = "Hello"
    val last : String = ", World!"

    printString(first, last)

    var num1 : Int = 1
    var num2 : Int = 2
    var num3 : Int = addition(num1, num2)

    printStringInt("The answer is: ", num3)

    // Object classes
    var student1 = Student(name = "Daniel")
    println(student1.name)

    var student2 = Student("Stefan", 22, 75)
    var student3 = Student("Daniel", 21, 90)
    println(student2 > student3)                                // Acctually saying student2.>(student3)

    // Lists and for loops
    var nums = List(1,2,3,4)
    for (num <- nums) println(num)                                // Can also use { } as with java

    println()
    nums.foreach{n:Int => println(n)}

    println(nums(0))

    // Function definitions
    def printString(a:String, b:String) : Unit = {   // Unit is equavalent to void (its the return type)
        println(a+b)
    }

    def printStringInt(a:String, b:Int) : Unit = {
        println(a+b)
    }
    
    def addition(a:Int, b:Int) : Int = {
        var c : Int = a + b                                     // Everything is an object ora function. Thus a + b == a.+(b)
        /*return */c                                            // Specifying 'return' is optional.
    }

    // Object class
    case class Student(var name:String = "Stefan", var age:Int = 22, var mark:Int = 75)
    {
        def >(s2 : Student) : Boolean = mark > s2.mark          // Using '>' as a function
    }
}

