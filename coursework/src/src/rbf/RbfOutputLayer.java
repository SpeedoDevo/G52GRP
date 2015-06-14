package rbf;

import java.io.Serializable;

import commons.Connection;
import commons.HelperMethods;
import commons.Layer;
import commons.Node;
import commons.Strategy;

public class RbfOutputLayer extends Layer implements Serializable {
	private static final long serialVersionUID = -5283845425525715727L;

	public RbfOutputLayer(int size, Strategy strategy, Layer prev) {
		super(size, strategy);
		
		this.setPrevLayer(prev);
		this.connectPrevLayer();
	}
	
	/**
	 * Connects each node in the the layer to every node in the previous layer
	 */
	public void connectPrevLayer() {
		for (int i = 0; i < nodes.length; i++) {
			Node outNode = nodes[i];
			RbfHiddenNode[] prevNodes = ((RbfHiddenLayer) getPrevLayer()).getRNodes();
			for (int j = 0; j < prevNodes.length; j++) {
				Node inNode = prevNodes[j];
				float factor = 0.5f/nodes.length;
				float rand = HelperMethods.random(-factor, factor);
				new Connection(inNode, outNode, rand);
			}
		}
	}
	
	/**Gets the output of every node in the layer and returns as a float array
	 * @return The output values of the output layer
	 */
	public float[] getOutput() {
		float[] result = new float[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			result[i] = nodes[i].getOutput();
		}
		return result;
	}
	
	/**
	 * Calculates output of all nodes in the layer
	 */
	public void calculateLayerOutput() {
		for(Node node : nodes) {
			node.calculateOutput();
		}
	}
}
