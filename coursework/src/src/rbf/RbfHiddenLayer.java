package rbf;

import java.io.Serializable;

import commons.Connection;
import commons.HelperMethods;
import commons.Layer;
import commons.Node;

public class RbfHiddenLayer extends Layer implements Serializable {
	private static final long serialVersionUID = 6852616051178668167L;
	RbfHiddenNode[] rNodes;
	
	/**	Constructs a hidden layer for the neural network
	 * @param size - Used to set the size of the layer
	 * @param prev - Used to set the previous layer
	 */
	public RbfHiddenLayer(int size, RbfStrategy strategy, Layer prev) {
		super(0, strategy);
		rNodes = new RbfHiddenNode[size];
		for(int i = 0; i < rNodes.length; i++) {
			rNodes[i] = new RbfHiddenNode(strategy);
		}
		this.setPrevLayer(prev);
		this.connectPrevLayer();
	}
	
	public RbfHiddenNode[] getRNodes() {
		return rNodes;
	}
	
	/**
	 * Calculates output of all nodes in the layer
	 */
	public void calculateLayerOutput() {
		for(int i = 0; i < rNodes.length; i++) {
			rNodes[i].calculateOutput(i);
		}
	}
	
	public void setCenters(float[][] centers) {
		for(int i = 0; i < centers.length; i++) {
			rNodes[i].setCenter(centers[i]);
		}
	}
	
	public int getSize() {
		return rNodes.length;
	}
	
	public void connectPrevLayer() {
		for (int i = 0; i < rNodes.length; i++) {
			RbfHiddenNode outNode = rNodes[i];
			Node[] prevNodes = getPrevLayer().getNodes();
			for (int j = 0; j < prevNodes.length; j++) {
				Node inNode = prevNodes[j];
				float factor = 0.5f/rNodes.length;
				float rand = HelperMethods.random(-factor, factor);
				new Connection(inNode, outNode, rand);
			}
		}
	}
}
