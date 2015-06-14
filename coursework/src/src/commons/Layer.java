package commons;

import java.io.Serializable;


/**
 * @author James
 * Abstract Layer which groups common functionality of the input, hidden
 * and output layers respectively
 */
public abstract class Layer implements Serializable  {
	private static final long serialVersionUID = -6768790199243504989L;
	protected Node[] nodes;
	private Layer nextLayer;
	private Layer prevLayer;
	protected Strategy strategy;

	protected Layer (int size, Strategy strategy){
		this.strategy = strategy;
		nodes = new Node[size];
		initialseNodes();
	}

	protected Layer (int size){
		nodes = new Node[size];
		initialseNodes();
	}

	
	/** Should return the node specified Return Node
	 * @return
	 */
	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * @param The next layer in the nn
	 */
	public void setNextLayer(Layer layer) {
		nextLayer = layer;
	}

	/** Gets and returns next layer
	 * @return The next layer
	 */
	public Layer getNextLayer() {
		return nextLayer;
	}

	/**
	 * @param The previous layer in the nn
	 */
	public void setPrevLayer(Layer layer) {
		prevLayer = layer;
	}

	/** Sets nodeList
	 * @param nodes
	 */
	public void initialseNodes() {
		for(int x = 0; x < nodes.length; x++) {
			nodes[x] = strategy == null ? new Node() : new Node(strategy);
		}
	}

	/**Gets the previous layer Returns the previous layer
	 * @return
	 */
	public Layer getPrevLayer() {
		return prevLayer;
	}

	/**Gets the size of neurons in the layer
	 * @return No. of neurons in layer
	 */
	public int getSize() {
		return nodes.length;
	}

	/**
	 * Calculates output of all nodes in the layer
	 */
	public void calculateLayerOutput() {
		for(Node node : nodes) {
			node.calculateOutput();
		}
	}

	/**
	 * Connects each node in the the layer to every node in the previous layer
	 */
	public void connectPrevLayer() {
		connectInputLayer(prevLayer);
	}
	
	/** Connects <code>inputLayer</code>'s nodes as an input layer to this, weights are randomly initialised
	 * (ie. this layer's nodes are going to be output nodes for the <code>inputLayer</code>'s nodes) 
	 * @param inputLayer
	 */
	public void connectInputLayer(Layer inputLayer) {
		for (int i = 0; i < nodes.length; i++) {
			Node outNode = nodes[i];
			Node[] inNodes = inputLayer.getNodes();
			for (int j = 0; j < inNodes.length; j++) {
				Node inNode = inNodes[j];
				float factor = 0.5f/nodes.length;
				float rand = HelperMethods.random(-factor, factor);
				new Connection(inNode, outNode, rand);
			}
		}
	}
	
	/** Same as {@link Layer#connectInputLayer(Layer)} except initialises all weights to <code>weight</code>
	 */
	public void connectInputLayerWithWeight(Layer inputLayer, float weight) {
		for (int i = 0; i < nodes.length; i++) {
			Node outNode = nodes[i];
			Node[] inNodes = inputLayer.getNodes();
			for (int j = 0; j < inNodes.length; j++) {
				Node inNode = inNodes[j];
				new Connection(inNode, outNode, weight);
			}
		}
	}
	
	

}
