package backprop;
import java.io.Serializable;

import commons.HiddenLayer;
import commons.InputLayer;
import commons.Layer;
import commons.OutputLayer;
import commons.Strategy;



public class FeedForwardNetwork implements Serializable {
	private static final long serialVersionUID = -376896049519266496L;
	private int[] numberOfNodesPerLayer;
	private Layer[] layers;
	private int numberOfLayers;
	private InputLayer inLayer;
	private OutputLayer outLayer;
	private Strategy strategy;


	/** NeuralNetwork constructor which creates each layer in the nn
	 * @param inputData in a 2d array
	 * @param targetData in a 2d array
	 * @param numberOfNodesPerLayer array to select no. nodes on each layer
	 * @param learningRate passed as a float
	 * @param maxIterations
	 * @param strategy
	 */
	public FeedForwardNetwork(int[] numberOfNodesPerLayer, Strategy strategy) {
		this.numberOfNodesPerLayer = numberOfNodesPerLayer;
		this.numberOfLayers = numberOfNodesPerLayer.length;
		layers = new Layer[numberOfLayers];
		this.strategy = strategy;

		initialiseLayers();
	}

	/** basic layer initialisation */
	private void initialiseLayers() {
		for (int i = 0; i < numberOfLayers; i++) {
			// first layer is an input layer
			if(i == 0) {
				inLayer = new InputLayer(numberOfNodesPerLayer[i], strategy);
				layers[i] = inLayer;
			} else {
				/* If creating the last layer create an output layer
				 * otherwise create a hidden layer
				 */
				if (i == numberOfLayers - 1) {
					outLayer = new OutputLayer(numberOfNodesPerLayer[i], strategy, layers[i-1]);
					layers[i] = outLayer;
				} else {
					HiddenLayer newLayer = new HiddenLayer(numberOfNodesPerLayer[i], strategy, layers[i-1]);
					layers[i] = newLayer;
					layers[i-1].setNextLayer(newLayer);
				}
			}
		}
	}

	/**Sets output of input nodes to the inputData
	 * @param inputData array
	 */
	public void feedInput(float[] inputData) {
		inLayer.setNodeOutputs(inputData);
	}

	/**
	 * Feeds the data through the network generating the output data
	 */
	public void feedForward() {
		//starting from second layer
		for (int i = 1; i < layers.length; i++) {
			layers[i].calculateLayerOutput();
		}
	}

	/** Returns output node results
	 * @return array of the output layers weights
	 */
	public float[] getOutput() {
		return outLayer.getOutput();
	}


	/** Loops through training sets calculating and correcting the network as it goes */
	public void train(float[][] inputData, float[][] targetData) {
		strategy.trainNetwork(this, inputData, targetData);
	}

	public Layer[] getLayers() {
		return layers;
	}

}
