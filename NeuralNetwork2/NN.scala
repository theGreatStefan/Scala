/**
  * Class for a general neural network with 1 hidden layer
  * 
  * Initiation:
  * - hiddenLength: Length of the hidden layer
  * - hiddenIncomming: Number of incomming synopses for a hidden neuron
  * - outputLength: Length of the output layer
  * - outputIncomming: Number of incomming synopses for an output neuron
  * 
  */

import java.lang.Math
import scala.util.Random

class NN (hiddenLength:Int, hiddenIncomming:Int, outputLength:Int, outputIncomming:Int){

    var weights: Array[Double] = Array()
    var input_nodes: Array[Double] = Array()
    var hidden_nodes: Array[node] = Array()
    var output_nodes: Array[node] = Array()

    var numWeights:Int = hiddenLength*hiddenIncomming + outputLength*outputIncomming
    var r = new Random(123)

    // Populate arrays
    // Hidden nodes
    for (i <- 0 to hiddenLength-1) {
        hidden_nodes = hidden_nodes :+ (new node(hiddenIncomming))
    }

    // Output nodes
    for (i <- 0 to outputLength-1) {
        output_nodes = output_nodes :+ (new node(outputIncomming))
    }

    // Weights
    for (i <- 0 to numWeights-1) {
        weights = weights :+ (2 * r.nextDouble() - 1)
    }

    def sigmoid(x:Double): Double = { 
        (1/(1+Math.exp(-x)))
    }

    def runNN(inputs:Array[Double]): Array[Double] = {
        input_nodes = inputs

        updateHiddenNeurons()
        updateOutputNeurons()

        for (i <- 0 to outputLength-1) {
            println(output_nodes(i).toString())
        }

        output_nodes.map(el => el.activatedNum)
        
    }

    /**
      * Update the weights vector
      */
    def updateWeights(x:Array[Double]): Unit = {
        weights = x
    }

    /**
      * Loop through the input synopses and update the hidden layer with the weighted input values
      */
    def updateHiddenNeurons(): Unit = {
        var weightedSynops:Double = 0.0
        // Weighted number
        for (i <- 0 to hiddenLength-1) {
            for (j <- 1 to hiddenIncomming) {
                weightedSynops = weights( (hiddenIncomming*i + j) -1)
                hidden_nodes(i).addSynopsValue( input_nodes(j-1) * weightedSynops )
            }
        }

        // Activated number
        for (i <- 0 to hiddenLength-1) {
            hidden_nodes(i).setActivatedNum( sigmoid(hidden_nodes(i).sum()) )
        }

    }

    /**
      * Loop through the hidden synopses and update the output layer with the weighted hidden values
      */
    def updateOutputNeurons(): Unit = {
        var weightedSynops:Double = 0.0
        for (i <- 0 to outputLength-1) {
            for (j <- 1 to outputIncomming) {
                weightedSynops = weights( hiddenLength*hiddenIncomming+(outputIncomming*i + j) -1)
                output_nodes(i).addSynopsValue( hidden_nodes(j-1).getactivatedNum() * weightedSynops )
            }
        }

        // Activated number
        for (i <- 0 to outputLength-1) {
            output_nodes(i).setActivatedNum( sigmoid(output_nodes(i).sum()) )
        }
    }

    
}