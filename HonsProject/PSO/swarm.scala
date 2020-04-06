import java.lang.Math
import scala.util.Random
import scala.concurrent.duration.Duration.Infinite
import java.io.IOException
import java.io.PrintWriter
import java.io.File

class swarm(iswarm_size:Int, iconstraint_size:Int, ic1:Double, ic2:Double, iw:Double, ilb:Double, iub:Double, stockData:Array[Double]) {
    var lb:Double = ilb
    var ub:Double = iub
    var c1:Double = ic1
    var c2:Double = ic2
    var w:Double = iw
    var swarm_size:Int = iswarm_size
    var constraint_size:Int = iconstraint_size
    var r = scala.util.Random
    //r.setSeed(456)
    var pswarm:Array[particle] = Array()
    var nbest_pos:Array[Array[Double]] = Array.fill(swarm_size){Array.fill(constraint_size){0.0}}
    var nbest_score:Array[Double] = Array.fill(swarm_size){0.0}
    var fitness_score:Array[Double] = Array.fill(swarm_size){0.0}
    var priceMomentum:Array[Double] = Array.fill(stockData.length){0.0}

    var HallOfFame:Array[HOFParticle] = Array.fill(11){new HOFParticle()}

    var avg_velocity_magnitude:Array[Double] = Array.fill(350){0}
    var avg_euclidean_distance:Array[Double] = Array.fill(350){0}
    val filename = "../testOutput/velocity.csv"
    val pw = new PrintWriter(new File(filename))

    var globalFitnesses:Array[Double] = Array.fill(swarm_size){0.0}
    
    for (i <- 0 to swarm_size-1) {
        var particle_pos:Array[Double] = Array.fill(constraint_size){lb + r.nextDouble()*(ub - lb)}
        pswarm = pswarm :+ new particle(particle_pos, constraint_size, c1, c2, w, lb, ub)

        if (i==0) {
            pswarm(i).setPrevNeighbour(swarm_size-1)
            pswarm(i).setNextNeighbour(i+1)
        } else if (i==(swarm_size-1)) {
            pswarm(i).setPrevNeighbour(i-1)
            pswarm(i).setNextNeighbour(0)
        } else {
            pswarm(i).setPrevNeighbour(i-1)
            pswarm(i).setNextNeighbour(i+1)
        }
    }

    //************ TMI time series
    var aroonUps:Array[Double] = Array.fill(stockData.length){0.0}
    var aroonDowns:Array[Double] = Array.fill(stockData.length){0.0}
    var percentageBBands:Array[Double] = Array.fill(stockData.length){0.0}
    var mACDs1:Array[Double] = Array.fill(stockData.length){0.0}
    var mACDs2:Array[Double] = Array.fill(stockData.length){0.0}
    var rSIs:Array[Double] = Array.fill(stockData.length){0.0}

    for (i <- 0 to stockData.length-1) {
        aroonUps(i) = AroonUp(i, 14)
        aroonDowns(i) = AroonDown(i, 14)
        percentageBBands(i) = PercentageBBand(i, 20, 2.0)
        var macdTuple = MACD(i, 12, 26, 9)
        mACDs1(i) = macdTuple._1
        mACDs2(i) = macdTuple._2
        rSIs(i) = RSI(i, 14)
    }

    // Normalise TMIs
    var aUpMax:Double = aroonUps.max
    var aUpMin:Double = aroonUps.min
    var aDownMax:Double = aroonDowns.max
    var aDownMin:Double = aroonDowns.min
    var pBMax:Double = percentageBBands.max
    var pBMin:Double = percentageBBands.min
    var mAC1Max:Double = mACDs1.max
    var mAC1Min:Double = mACDs1.min
    var mAC2Max:Double = mACDs2.max
    var mAC2Min:Double = mACDs2.min
    var rSIMax:Double = rSIs.max
    var rSIMin:Double = rSIs.min

    for (i <- 0 to stockData.length-1) {
        aroonUps(i) = 2*((aroonUps(i) - aUpMin) / (aUpMax - aUpMin))-1
        aroonDowns(i) = 2*((aroonDowns(i) - aDownMin) / (aDownMax - aDownMin))-1
        percentageBBands(i) = 2*((percentageBBands(i) - pBMin) / (pBMax - pBMin))-1
        mACDs1(i) = 2*((mACDs1(i) - mAC1Min) / (mAC1Max - mAC1Min))-1
        mACDs2(i) = 2*((mACDs2(i) - mAC2Min) / (mAC2Max - mAC2Min))-1
        rSIs(i) = 2*((rSIs(i) - rSIMin) / (rSIMax - rSIMin))-1
    }
    //*********** TMI time series END

    /*****************Technical Market Indicators******************/
    
    def AroonUp(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        var highIndex:Double = (stockData.indexOf(prevPrices.max)+1) - (t+1) + p
        var aroonUp:Double = 100*(highIndex/p)
        aroonUp
    }

    def AroonDown(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        var lowIndex:Double = (stockData.indexOf(prevPrices.min)+1) - (t+1) + p
        var aroonDown:Double = 100*(lowIndex/p)
        aroonDown
    }

    def PercentageBBand(t:Int, p:Int, D:Double):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p+1, (t+1))
        var midBand:Double = (prevPrices.sum)/p
        var lowInside:Double = (for (i <- 0 to prevPrices.length-1) yield (Math.pow(prevPrices(i)-midBand, 2))).sum
        var lowBand:Double = midBand - (D * Math.sqrt(lowInside/p))
        var upInside:Double = (for (i <- 0 to prevPrices.length-1) yield (Math.pow(prevPrices(i)-midBand, 2))).sum
        var upBand:Double = midBand + (D * Math.sqrt(lowInside/p))
        var pBand:Double = 100*( (stockData(t)-lowBand) / (upBand-lowBand) )
        pBand
    }

    def MACD(t:Int, a:Int, b:Int, c:Int):(Double,Double) = {
        var prevPrices_a:Array[Double] = stockData.slice(t-a+1, (t+1))
        var prevPrices_b:Array[Double] = stockData.slice(t-b+1, (t+1))
        var ema_a = stockData(t) * (2/(a+1).toDouble) + (prevPrices_a.sum/a) * (100-(2/(a+1).toDouble))
        var ema_b = stockData(t) * (2/(b+1).toDouble) + (prevPrices_b.sum/b) * (100-(2/(b+1).toDouble))

        priceMomentum(t) = ema_a - ema_b
        var prevPricesMomentum_c:Array[Double] = priceMomentum.slice(t-c+1, (t+1))
        var ema_c = priceMomentum(t) * (2/(c+1).toDouble) + (prevPricesMomentum_c.sum/c) * (100-(2/(c+1).toDouble))

        var momentumTrigger:Double = ema_c
        (priceMomentum(t), momentumTrigger)
    }

    def RSI(t:Int, p:Int):Double = {
        var prevPrices:Array[Double] = stockData.slice(t-p, (t+1))
        
        var totalGain:Double = 0.0
        for (i <- 1 to prevPrices.length-1) {
            var gain:Double = (prevPrices(i)-prevPrices(i-1))                            
            if (gain > 0) {
                totalGain += gain
            }
        }

        var totalLoss:Double = 0.0
        for (i <- 1 to prevPrices.length-1) {
            var loss:Double = (prevPrices(i)-prevPrices(i-1))                            
            if (loss < 0) {
                totalLoss += loss
            }
        }
        totalLoss = Math.abs(totalLoss)

        var rsi:Double = 0.0
        if (totalLoss == 0) {
            rsi = 100
        } else {
            var rs:Double = totalGain/totalLoss
            rsi = 100 * (1-(1/(1+rs)))
        }
        
        rsi
    }

    /***************END Technical Market Indicators****************/

    // Main method called for running the swarm for a specified number of epochs
    // Synchronous lbest PSO
    def runSwarm(epochs:Int):Unit = {
        var TMIs:Array[Double] = Array.fill(6){0.0}
        var maxFitnessIndex:Int = 0
        var HOFFitnesses:Array[Double] = Array.fill(11){0.0}
        var minHOFFitnessIndex:Int = 0
        var HOF_Final:Array[particle] = Array()

        var netProfits:Array[Double] = Array.fill(swarm_size){0.0}
        var SRs:Array[Double] = Array.fill(swarm_size){0.0}
        var maxNetProfit:Double = 0.0
        var minNetProfit:Double = 0.0
        var maxSR:Double = 0.0
        var minSR:Double = 0.0

        for (i <- 0 to epochs-1) {
            // Reset particles
            for (j <- 0 to swarm_size-1) {
                pswarm(j).reset()
            }

            // Run through the entire TMI time series until 31st March 2006
            for (j <- 0 to 1570) {
                TMIs(0) = aroonUps(j)
                TMIs(1) = aroonDowns(j)
                TMIs(2) = percentageBBands(j)
                TMIs(3) = mACDs1(j)
                TMIs(4) = mACDs2(j)
                TMIs(5) = rSIs(j)

                for (k <- 0 to swarm_size-1) {
                    //println("******** particle "+k+" *************")
                    pswarm(k).runNN(TMIs.clone(), stockData(j), j, false)
                }
                
            }

            // Global fitness
            for (j <- 0 to swarm_size-1) {
                netProfits(j) = pswarm(j).getNetProfit
                SRs(j) = pswarm(j).getSharpRatio()
            }
            
            maxNetProfit = netProfits.max
            minNetProfit = netProfits.min
            maxSR = SRs.max
            minSR = SRs.min
            
            for (j <- 0 to swarm_size-1) {
                globalFitnesses(j) = relativeFitnessGroup(j, netProfits(j), maxNetProfit, minNetProfit, SRs(j), maxSR, minSR)
            }

            /**************Hall Of Fame*****************/
            var found = false
            // Find best strategy
            maxFitnessIndex = globalFitnesses.indexOf(globalFitnesses.max)
            // Add to Hall of fame
            HallOfFame(0).setPos(pswarm(maxFitnessIndex).getPos())
            HallOfFame(0).setNetProfit(pswarm(maxFitnessIndex).getNetProfit())
            HallOfFame(0).setSharpeRatio(pswarm(maxFitnessIndex).getSharpRatio())
            for (j <- 1 to 10) {
                if (HallOfFame(j).isEqualTo(HallOfFame(0).pos)) {
                    found = true
                }
            }
            if (!found) {
                for (j <- 0 to 10) {
                    HOFFitnesses(j) = relativeFitnessHOF(j)
                }
                minHOFFitnessIndex = HOFFitnesses.indexOf(HOFFitnesses.min)
                if (minHOFFitnessIndex != 0) {
                    HallOfFame(minHOFFitnessIndex).setPos(HallOfFame(0).getPos.clone())
                    HallOfFame(minHOFFitnessIndex).setNetProfit(HallOfFame(0).getNetProfit)
                    HallOfFame(minHOFFitnessIndex).setSharpeRatio(HallOfFame(0).getSharpeRatio)
                }
            }
            /**************Hall Of Fame END**************/

            for (j <- 0 to swarm_size-1) {
                pswarm(j).checkBestPos();
            }
            for (j <- 0 to swarm_size-1) {
                checkNBest(j)
            }

            var velocity_magnitudes:Double = 0.0
            for (j <- 0 to swarm_size-1) {
                pswarm(j).updateVelocity(nbest_pos(j))
                pswarm(j).updatePos()

                velocity_magnitudes += pswarm(j).getVelocityMagnitude()
            }
            //avg_velocity_magnitude(i) = velocity_magnitudes/swarm_size
            pw.write(velocity_magnitudes/swarm_size.toDouble+","+avgEuclidianDistance()+"\n")


        }
        for (i <- 0 to swarm_size-1) {
            println("Particle "+i)
            println(toString(i)+"\n")
        }

        for (i <- 1 to 10) {
            println("Hall of fame "+i+": "+HallOfFame(i).getNetProfit)
        }

        // Run the Hall of fame against new data
        // Create new particles from the Hall of fame
        for (i <- 0 to 9) {
            HOF_Final = HOF_Final :+ new particle(HallOfFame(i+1).getPos.clone(), constraint_size, c1, c2, w, lb, ub)
        }
        for (i <- 1571 to stockData.length-1) {
            TMIs(0) = aroonUps(i)
            TMIs(1) = aroonDowns(i)
            TMIs(2) = percentageBBands(i)
            TMIs(3) = mACDs1(i)
            TMIs(4) = mACDs2(i)
            TMIs(5) = rSIs(i)

            for (j <- 0 to 9) {
                if(i == stockData.length-1) {
                    println("******** particle "+j+" *************")
                }
                HOF_Final(j).runNN(TMIs, stockData(i), i-1571, true)
            }
                
        }
        // final sell
        /*for (i <- 0 to 4) {
            HOF_Final(i).sell(stockData(stockData.length-1))
        }*/
        for (i <- 0 to 9) {
            println("Hall Of Fame Particle "+(i+1))
            println("Net Profit: "+HOF_Final(i).getNetProfit())
            println("Stocks: "+HOF_Final(i).stocks)
        }

        pw.close()
    }

    // TODO: not sure if this is correct. Should it be nbest(i) of pswarm(i).pbest_score (?)
    def checkNBest(curr_j:Int):Unit = {
        var prev_j:Int = pswarm(curr_j).getPrevNeighbour()
        var next_j:Int = pswarm(curr_j).getNextNeighbour()

        var fitnesses:Array[Double] = Array.fill(3){0.0}
        fitnesses(0) = relativeFitness(next_j, prev_j, curr_j) //Relative fitness for previous particle in neighbourhood
        fitnesses(1) = relativeFitness(prev_j, curr_j, next_j) //Relative fitness for current particle in neighbourhood
        fitnesses(2) = relativeFitness(curr_j, next_j, prev_j) //Relative fitness for next particle in neighbourhood

        if (fitnesses(0) >= fitnesses(1) && fitnesses(0) >= fitnesses(2)) {
            nbest_score(curr_j) = fitnesses(0)
            nbest_pos(curr_j) = pswarm(prev_j).pbest_pos.clone()

        } else if (fitnesses(1) >= fitnesses(0) && fitnesses(1) >= fitnesses(2)) {
            nbest_score(curr_j) = fitnesses(1)
            nbest_pos(curr_j) = pswarm(curr_j).pbest_pos.clone()

        } else if (fitnesses(2) >= fitnesses(0) && fitnesses(2) >= fitnesses(1)) {
            nbest_score(curr_j) = fitnesses(2)
            nbest_pos(curr_j) = pswarm(next_j).pbest_pos.clone()
        } else {
            println("prev: "+fitnesses(0))
            println("curr: "+fitnesses(1))
            println("next: "+fitnesses(2))
            println("Oops!!")
            System.exit(0)
        }

    }

    // Score between 0 and 2; 0 being the worst and 2 being the best
    /*def relativeFitness(index:Int):Double = {
        var netProfits:Array[Double] = Array.fill(3){0.0}
        var SRs:Array[Double] = Array.fill(3){0.0}
        var maxNetProfit:Double = 0.0
        var minNetProfit:Double = 0.0
        var maxSR:Double = 0.0
        var minSR:Double = 0.0
        var fitness:Double = 0.0

        netProfits(0) = pswarm(pswarm(index).getPrevNeighbour()).getNetProfit
        netProfits(1) = pswarm(index).getNetProfit()
        netProfits(2) = pswarm(pswarm(index).getNextNeighbour()).getNetProfit
        SRs(0) = pswarm(pswarm(index).getPrevNeighbour()).getSharpRatio
        SRs(1) = pswarm(index).getSharpRatio()
        SRs(2) = pswarm(pswarm(index).getNextNeighbour()).getSharpRatio
        
        maxNetProfit = netProfits.max
        minNetProfit = netProfits.min
        maxSR = SRs.max
        minSR = SRs.min

        if (maxNetProfit-minNetProfit == 0 && maxSR-minSR == 0) {
            fitness = 0.0//Double.MinValue
        } else if (maxNetProfit-minNetProfit == 0) {
            fitness = ( (SRs(1)-minSR) / (maxSR-minSR) )
        } else if (maxSR - minSR == 0) {
            fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) )
        } else {
            fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) ) + ( (SRs(1)-minSR) / (maxSR-minSR) )
        }

        fitness
    }*/
    def relativeFitness(prev_j:Int, curr_j:Int, next_j:Int):Double = {
        var netProfits:Array[Double] = Array.fill(3){0.0}
        var SRs:Array[Double] = Array.fill(3){0.0}
        var maxNetProfit:Double = 0.0
        var minNetProfit:Double = 0.0
        var maxSR:Double = 0.0
        var minSR:Double = 0.0
        var fitness:Double = 0.0

        netProfits(0) = pswarm(prev_j).pbest_net_profit
        netProfits(1) = pswarm(curr_j).pbest_net_profit
        netProfits(2) = pswarm(next_j).pbest_net_profit
        SRs(0) = pswarm(prev_j).pbest_sharpe_ratio
        SRs(1) = pswarm(curr_j).pbest_sharpe_ratio
        SRs(2) = pswarm(next_j).pbest_sharpe_ratio
        
        maxNetProfit = netProfits.max
        minNetProfit = netProfits.min
        maxSR = SRs.max
        minSR = SRs.min

        if (maxNetProfit-minNetProfit == 0 && maxSR-minSR == 0) {
            fitness = 0.0//Double.MinValue
        } else if (maxNetProfit-minNetProfit == 0) {
            fitness = ( (SRs(1)-minSR) / (maxSR-minSR) )
        } else if (maxSR - minSR == 0) {
            fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) )
        } else {
            fitness = ( (netProfits(1)-minNetProfit) / (maxNetProfit-minNetProfit) ) + ( (SRs(1)-minSR) / (maxSR-minSR) )
        }

        fitness
    }

    def relativeFitnessGroup(index:Int, netProfit:Double, maxNetProfit:Double, minNetProfit:Double, SR:Double, maxSR:Double, minSR:Double):Double = {
        var fitness:Double = 0.0

        if (maxNetProfit-minNetProfit == 0 && maxSR-minSR == 0) {
            fitness = 0.0//Double.MinValue
        } else if (maxNetProfit-minNetProfit == 0) {
            fitness = ( (SR-minSR) / (maxSR-minSR) )
        } else if (maxSR - minSR == 0) {
            fitness = ( (netProfit-minNetProfit) / (maxNetProfit-minNetProfit) )
        } else {
            fitness = ( (netProfit-minNetProfit) / (maxNetProfit-minNetProfit) ) + ( (SR-minSR) / (maxSR-minSR) )
        }

        fitness
    }

    def relativeFitnessHOF(index:Int):Double = {
        var netProfits:Array[Double] = Array.fill(11){0.0}
        var SRs:Array[Double] = Array.fill(11){0.0}
        var maxNetProfit:Double = 0.0
        var minNetProfit:Double = 0.0
        var maxSR:Double = 0.0
        var minSR:Double = 0.0
        var fitness:Double = 0.0

        for (i <- 0 to 10) {
            netProfits(i) = HallOfFame(i).getNetProfit
            SRs(i) = HallOfFame(i).getSharpeRatio()
        }
        
        maxNetProfit = netProfits.max
        minNetProfit = netProfits.min
        maxSR = SRs.max
        minSR = SRs.min

        if (maxNetProfit-minNetProfit == 0 && maxSR-minSR == 0) {
            fitness = 0.0//Double.MinValue
        } else if (maxNetProfit-minNetProfit == 0) {
            fitness = ( (SRs(index)-minSR) / (maxSR-minSR) )
        } else if (maxSR - minSR == 0) {
            fitness = ( (netProfits(index)-minNetProfit) / (maxNetProfit-minNetProfit) )
        } else {
            fitness = ( (netProfits(index)-minNetProfit) / (maxNetProfit-minNetProfit) ) + ( (SRs(index)-minSR) / (maxSR-minSR) )
        }

        fitness
    }

    def avgParticle(): Array[Double] = {
        var avg_particle_pos:Array[Double] = pswarm(0).pos.clone()
        for (i <- 1 to swarm_size-1) {
            for (j <- 0 to constraint_size-1) {
                avg_particle_pos(j) += pswarm(i).pos(j)
            }
        }
        for (i <- 0 to constraint_size-1) {
            avg_particle_pos(i) = avg_particle_pos(i)/swarm_size
        }
        avg_particle_pos
    }

    def avgEuclidianDistance():Double = {
        var avg_pos:Array[Double] = avgParticle()
        var avg_eucl_d:Double = 0.0
        var avg_eucl_d_curr:Double = 0.0
        for (i <- 0 to swarm_size-1) {
            for (j <- 0 to constraint_size-1) {
                avg_eucl_d_curr += Math.pow(pswarm(i).pos(j)-avg_pos(j), 2)
            }
            avg_eucl_d += Math.sqrt(avg_eucl_d_curr)
            avg_eucl_d_curr = 0.0
        }
        (avg_eucl_d/swarm_size)
    }
    
    def toString(i:Int): String = {
        "Net profit: "+pswarm(i).getNetProfit()+
        "\nCapital gains: "+pswarm(i).capitalGains+
        "\nCapital losses: "+pswarm(i).capitalLosses+
        "\nStocks: "+pswarm(i).stocks+
        "\nPrice: "+stockData(1570)+
        "\nBought:"+pswarm(i).numBought+
        "\nSold:"+pswarm(i).numSold+
        "\nHeld:"+pswarm(i).numHeld
    }

}