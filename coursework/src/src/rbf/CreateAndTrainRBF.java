package rbf;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CreateAndTrainRBF implements Serializable {
	
	private static final long serialVersionUID = -7309249697629153285L;
	private static RbfNetwork nn;
	private static RbfStrategy rbf;
	private static float error;
	private static long duration;
	
	/**Creates a Radial Basis Function network and trains it with
	 * the given training data
	 * @param A int array specifying number of nodes in each layer
	 * @param 2D array of input data
	 * @param 2D array of output data
	 */
	public void createTrain(int[] nodes, float[][] trainingData, float[][] targetData) {
		rbf = new RbfStrategy();
		nn = new RbfNetwork(nodes, rbf);

		long startTime = System.nanoTime();
		rbf.trainNetwork(nn, trainingData, targetData);
		long endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
	}
	
	/**
	 * Gets the neural network
	 * @return
	 */
	public RbfNetwork getNeuralNet() {
		return nn;
	}
	
	/**
	 * Gets the strategy
	 * @return
	 */
	public RbfStrategy getStrategy() {
		return rbf;
	}
	
	/**
	 * @return Total percentage error after training
	 */
	public float getError() {
		return error;
	}

	/**The time taken to train network in milliseconds
	 * @return long
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Used by the gui to display which algorithm it is
	 * @return String of algorithm name
	 */
	public String getAlgo() {
		return "Radial Basis Function";
	}

	/**
	 * Feeds the a input array through the network
	 * @param float array of inputs
	 */
	public void feedInput(float[] input) {
		nn.feedInput(input);
		nn.feedForward();
	}
	
	/**
	 * Used in serialisation to save static variables
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		nn = (RbfNetwork) in.readObject();
		rbf = (RbfStrategy) in.readObject();
		error = in.readFloat();
		duration = in.readLong();
	}

	/**
	 * Used in serialisation to write static variables
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream  out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(nn);
		out.writeObject(rbf);;
		out.writeFloat(error);
		out.writeLong(duration);
	}
}
