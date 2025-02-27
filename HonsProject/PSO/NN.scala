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

    var numWeights:Int = hiddenLength*hiddenIncomming + outputLength*outputIncomming
    var weights: Array[Double] = Array.fill(numWeights){0.0}
    var input_nodes: Array[Double] = Array()
    var hidden_nodes: Array[node] = Array.fill(hiddenLength){new node(hiddenIncomming)}
    var output_nodes: Array[node] = Array.fill(outputLength){new node(outputIncomming)}

    def activationFunc(x:Double): Double = sigmoid(x)

    def sigmoid(x:Double): Double = {
        (1/(1+Math.exp(-x)))
    }

    def tanh(x:Double): Double = {
        ( (Math.exp(2*x)-1) / (Math.exp(2*x)+1) )
    }

    def leakyReLU(x:Double): Double = {
        ( Math.max(0.01*x, x) )
    }

    def softplus(x:Double): Double = {
        ( Math.log(1+Math.exp(x)) )
    }

    def runNN(inputs:Array[Double]): Array[Double] = {
        input_nodes = inputs

        updateHiddenNeurons()
        updateOutputNeurons()

        output_nodes.map(el => el.getactivatedNum())
    }

    /**
      * Update the weights vector
      */
    def updateWeights(x:Array[Double]): Unit = {
        weights = x.clone()
    }

    /**
      * Loop through the input synopses and update the hidden layer with the weighted input values
      */
    def updateHiddenNeurons(): Unit = {
        var weightedSynops:Double = 0.0
        // Weighted number
        for (i <- 0 to hiddenLength-1) {
            for (j <- 0 to hiddenIncomming-1) {
                weightedSynops = weights( (hiddenIncomming*i + j) )
                hidden_nodes(i).addSynopsValue( input_nodes(j) * weightedSynops)
            }
        }

        // Activated number
        for (i <- 0 to hiddenLength-1) {
            hidden_nodes(i).setActivatedNum( activationFunc(hidden_nodes(i).sum()) )
        }

    }

    /**
      * Loop through the hidden synopses and update the output layer with the weighted hidden values
      */
    def updateOutputNeurons(): Unit = {
        var weightedSynops:Double = 0.0
        for (i <- 0 to outputLength-1) {
            for (j <- 0 to outputIncomming-1) {
                weightedSynops = weights( hiddenLength*hiddenIncomming+(outputIncomming*i + j) )
                output_nodes(i).addSynopsValue( hidden_nodes(j).getactivatedNum() * weightedSynops)
            }
        }

        // Activated number
        for (i <- 0 to outputLength-1) {
            output_nodes(i).setActivatedNum( sigmoid(output_nodes(i).sum()) )
        }
    }

    /**
      * Return the activated output of the hidden layer
      */
    def getHiddenLayerOutput():Array[Double] = {
        hidden_nodes.map(el => el.getactivatedNum())
    }
    
}
