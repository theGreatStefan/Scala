import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object DE extends App {
    val filename = "tests/f1_dynamicReal_beta05_pr01_standard.csv"
    val pw = new PrintWriter(new File(filename))

    var avg_eucl_d_time:Array[Double] = Array.fill(5000){0}
    var avg_mag_diff_vecs_time:Array[Double] = Array.fill(5000){0}
    var avg_violations_time:Array[Double] = Array.fill(5000){0}
    var avg_best_sol_time:Array[Double] = Array.fill(5000){0}
    var avg_num_replacements:Array[Double] = Array.fill(5000){0}

    var tempavg_eucl_d_time:Array[Double] = Array.fill(5000){0}
    var tempavg_mag_diff_vecs_time:Array[Double] = Array.fill(5000){0}
    var tempavg_violations_time:Array[Double] = Array.fill(5000){0}
    var tempavg_best_sol_time:Array[Double] = Array.fill(5000){0}
    var tempavg_num_replacements:Array[Double] = Array.fill(5000){0}

    /*****************Functions******************/
    // f1: [-100, 100]

    // f5: [-100, 100]

    // f6: [-600, 600]

    // f12: [-5.12, 5.12]

    // f24: [0.25, 10]
    
    /*****************END_Functions******************/

    var runs:Int = 30
    var epochs:Int = 5000

    var pop_size:Int = 100
    var vec_size:Int = 30
    var beta:Double = 0.5       // (0, inf)
    var pr:Double = 0.5
    var lb:Double = 0.0
    var ub:Double = Math.PI
    var function:String = "f1"
    var mu:Double = 0.5
    var theta:Double = 0.5
    var a:Double = 0.9
    var boltzmann:Boolean = false

    for (i <- 0 to runs-1) {
        var DEpop = new DEpopulation(pop_size, vec_size, beta, pr, lb, ub, function, mu, theta, a, boltzmann)
        DEpop.runPop(epochs)
        
        tempavg_eucl_d_time = DEpop.avg_eucl_d_time.clone()
        tempavg_mag_diff_vecs_time = DEpop.avg_mag_diff_vecs_time.clone()
        tempavg_violations_time = DEpop.violations_time.clone()
        tempavg_best_sol_time = DEpop.best_sol_time.clone()
        tempavg_num_replacements = DEpop.avg_num_replacements.clone()
        for (j <- 0 to epochs-1) {
            avg_eucl_d_time(j) += tempavg_eucl_d_time(j)
            avg_mag_diff_vecs_time(j) += tempavg_mag_diff_vecs_time(j)
            avg_violations_time(j) += tempavg_violations_time(j)
            avg_best_sol_time(j) += tempavg_best_sol_time(j)
            avg_num_replacements(j) += tempavg_num_replacements(j)
        }

    }


    for (i <- 0 to epochs-1) {
        pw.write(avg_eucl_d_time(i)/runs+","+avg_mag_diff_vecs_time(i)/runs+","+avg_violations_time(i)/runs+","+avg_best_sol_time(i)/runs+","+avg_num_replacements(i)/runs+"\n")
    }
    pw.close()

}