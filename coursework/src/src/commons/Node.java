package commons;
import java.io.Serializable;
import java.util.ArrayList;


/** Basic node in a neural network.
 */
public class Node implements Serializable  {
	private static final long serialVersionUID = 4778197202199911425L;
	private float output;
	private float bias;
	private float error;
	private Strategy strategy;
	private ArrayList<Connection> inputConns = new ArrayList<Connection>();
	private ArrayList<Connection> outputConns = new ArrayList<Connection>();

	//TODO
	/**
	 * Node constructed with a random bias between [-1,1].
	 */
	public Node (Strategy strategy) {
		bias = HelperMethods.random(-1f, 1f);
		this.strategy = strategy;
	}


	public Node () {}

	/**
	 * @return Sums the node's inputs using the startegy's specified sum function.
	 */
	public float getInput() {
		return strategy.sumFunction(inputConns, bias);
	}

	/**
	 * @return ArrayList of connections.
	 */
	public ArrayList<Connection> getInputConnections() {
		return inputConns;
	}
	
	public Connection getNthInputConnection(int n) {
		return inputConns.get(n);
	}

	/** Adds an input connection to the node.
	 * @param conn An input Connection.
	 */
	public void addInputConnection(Connection conn) {
		this.inputConns.add(conn);
	}

	/**
	 * @return ArrayList of output connections.
	 */
	public ArrayList<Connection> getOutputConnections() {
		return outputConns;
	}

	/** Adds an output connection to the node.
	 * @param conn An output Connection.
	 */
	public void addOutputConnection(Connection conn) {
		outputConns.add(conn);
	}

	/** Removes an in or output connection from the node.
	 * @param conn The connection to remove.
	 * @return True if a connection was removed.
	 */
	public boolean deleteConnection(Connection conn) {
		return inputConns.remove(conn) || outputConns.remove(conn);
	}

	/**
	 * @return The output of the node.
	 */
	public float getOutput() {
		return output;
	}

	/** Sets the output of the node.
	 * ONLY USE THIS DIRECTLY ON THE INPUT LAYER.
	 * @param output The float that's going to be the output.
	 */
	public void setOutput(float output) {
		this.output = output;
	}

	/** Calculates the output of the node using the strategy's activation function.
	 * @return The calculated output.
	 */
	public float calculateOutput() {
		output = strategy.activationFunction(getInput());
		return output;
	}

	/**
	 * @return The node's bias.
	 */
	public float getBias() {
		return this.bias;
	}

	/** Sets the node's bias to specified value.
	 * @param bias Value to be set as bias.
	 */
	public void setBias(float bias) {
		this.bias = bias;
	}

	/** Use calculateError to calculate it first.
	 * @return Calculated error of the Node.
	 */
	public float getError() {
		return error;
	}

	public void setError(float error) {
		this.error = error;
	}

	public float getErrorSum() {
		//ej = Yj * (1 - Yj) * Sum over k (ek * W'j,k)
		float sum = 0.0f;
		for (int i = 0; i < outputConns.size(); i++) {
			Connection conn = outputConns.get(i);
			sum += conn.getOutputNode().getError() * conn.getWeight();
		}
		return sum;
	}

	public Strategy getStrategy() {
		return strategy;
	}


	public String toString() {
		return "e: " + error + ", b: " + bias;
	}
}
