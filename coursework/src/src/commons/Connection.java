package commons;

import java.io.Serializable;

/**
 * Creates a connection between the nodes in the neural network to hold the weight
 */
public class Connection implements Serializable  {
	private static final long serialVersionUID = -3388745236530311658L;
	private float weight;
	private Node inputNode;
	private Node outputNode;

	public Connection(Node inputNode, Node outputNode, float weight){
		this.inputNode = inputNode;
		this.outputNode = outputNode;
		this.weight = weight;
		inputNode.addOutputConnection(this);
		outputNode.addInputConnection(this);
	}

	public Connection(Node inputNode, Node outputNode){
		this.inputNode = inputNode;
		this.outputNode = outputNode;
		weight = HelperMethods.random(-0.5f, 0.5f);
		inputNode.addOutputConnection(this);
		outputNode.addInputConnection(this);
	}

	/** This method returns the input node of the connection
	 * @return input node of type Node
	 */
	public Node getInputNode(){
		return inputNode;
	}

	/**	This method returns the output node of the connection
	 * @return output node of type Node
	 */
	public Node getOutputNode(){
		return outputNode;
	}

	/**	This method returns the weight of the connection
	 * @return weight
	 */
	public float getWeight(){
		return weight;
	}

	/** This method allows the weight of the connection to be set
	 * @param weight
	 */
	public void setWeight(float weight){
		this.weight = weight;
	}

	public String toString(){
		return "" + weight;
	}

}
