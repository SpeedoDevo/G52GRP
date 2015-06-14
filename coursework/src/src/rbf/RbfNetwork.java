package rbf;

import java.io.Serializable;

import backprop.BackPropStrategy;
import commons.InputLayer;
import commons.Layer;

public class RbfNetwork implements Serializable {
	private static final long serialVersionUID = 3884795482634857590L;
	private int[] numberOfNodesPerLayer;
	private Layer[] layers = new Layer[3];
	private InputLayer inLayer;
	private RbfOutputLayer outLayer;
	private RbfStrategy strategy;
	private BackPropStrategy backProp = new BackPropStrategy(0.1f);


	/** NeuralNetwork constructor which creates each layer in the nn
	 * @param numberOfNodesPerLayer array to select no. nodes on each layer
	 * @param strategy
	 */
	public RbfNetwork(int[] numberOfNodesPerLayer, RbfStrategy strategy) {
		this.numberOfNodesPerLayer = numberOfNodesPerLayer;
		this.strategy = strategy;

		initialiseLayers();
	}
	
	/** basic layer initialisation */
	private void initialiseLayers() {
		inLayer = new InputLayer(numberOfNodesPerLayer[0],backProp);
		layers[0] = inLayer;
		RbfHiddenLayer hiddenLayer = new RbfHiddenLayer(numberOfNodesPerLayer[1], strategy, layers[0]);
		layers[1] = hiddenLayer;
		outLayer = new RbfOutputLayer(numberOfNodesPerLayer[2], strategy, layers[1]);
		layers[2] = outLayer;
	}
	
	/** Sets output of input nodes to the inputData
	 * @param inputData array
	 */
	public void feedInput(float[] inputData) {
		inLayer.setNodeOutputs(inputData);
	}
	
	public Layer[] getLayers() {
		return layers;
	}
	
	/** Returns output node results
	 * @return array of the output layers weights
	 */
	public float[] getOutput() {
		return outLayer.getOutput();
	}
	
	/** Feeds the data through the network generating the output data
	 */
	public void feedForward() {
		//starting from second layer
		for (int i = 1; i < layers.length; i++) {
			layers[i].calculateLayerOutput();
		}
	}
}
