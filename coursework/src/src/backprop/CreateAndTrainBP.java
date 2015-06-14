package backprop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class CreateAndTrainBP implements Serializable {
	private static final long serialVersionUID = 5790348378859013437L;
	private static FeedForwardNetwork nn;
	private static BackPropStrategy backProp;
	private static float error;
	private static int epochs;
	private static long duration;
	private static ArrayList<Float> errorEveryEpoch;
	
	/**Creates a Back Propagation network and trains it with
	 * the given training data
	 * 
	 * @param The step increment to use when adjusting weights of connections
	 * @param A int array specifying number of nodes in each layer
	 * @param The number of times to back propagate the network
	 * @param 2D array of input data
	 * @param 2D array of output data
	 */
	public void createTrainEpochs(float learningRate, int[] nodes, int nEpochs, float[][] trainingData,
			float[][] targetData) {
		epochs = nEpochs;
		backProp = new BackPropStrategy(learningRate);
		nn = new FeedForwardNetwork(nodes, backProp);
		
		long startTime = System.nanoTime();
		error = backProp.trainForNEpochs(epochs, nn, trainingData, targetData);
		long endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		
		errorEveryEpoch = backProp.getErrorOverEpochs();
	}
	
	public void createTrainError(float learningRate, int[] nodes, float pError, float[][] trainingData,
			float[][] targetData) {
		backProp = new BackPropStrategy(learningRate);
		nn = new FeedForwardNetwork(nodes, backProp);
		
		long startTime = System.nanoTime();
		epochs = backProp.trainUntilThreshold(pError, nn, trainingData, targetData);
		long endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		
		errorEveryEpoch = backProp.getErrorOverEpochs();
		
		error = errorEveryEpoch.get(epochs-1);
	}
	
	public void createTrainMaxOrError(float learningRate, int[] nodes, float pError, int maxEpochs, float[][] trainingData,
			float[][] targetData) {
		backProp = new BackPropStrategy(learningRate);
		nn = new FeedForwardNetwork(nodes, backProp);
		
		long startTime = System.nanoTime();
		epochs = backProp.trainUntilThresholdOrMaxEpoch(pError, maxEpochs, nn, trainingData, targetData);
		long endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		
		errorEveryEpoch = backProp.getErrorOverEpochs();
		
		error = errorEveryEpoch.get(epochs-1);
	}
	
	/**
	 * Gets the neural network
	 * @return
	 */
	public FeedForwardNetwork getNeuralNet() {
		return nn;
	}
	
	/**
	 * Gets the strategy
	 * @return
	 */
	public BackPropStrategy getStrategy() {
		return backProp;
	}
	
	/**
	 * @return Total percentage error after training
	 */
	public float getError() {
		return error;
	}
	
	/**Returns number of epochs
	 * @return integer of epochs
	 */
	public int getEpochs() {
		return epochs;
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
		return "Back Propagation";
	}
	
	/**
	 * Gets the array list containing percentage error every epoch
	 */
	public ArrayList<Float> getErrorOverEpochs() {
		return errorEveryEpoch;
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
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		nn = (FeedForwardNetwork) in.readObject();
		backProp = (BackPropStrategy) in.readObject();
		error = in.readFloat();
		epochs = in.readInt();
		duration = in.readLong();
		errorEveryEpoch = (ArrayList<Float>) in.readObject();
	}
	
	/**
	 * Used in serialisation to write static variables
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream  out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(nn);
		out.writeObject(backProp);
		out.writeFloat(error);
		out.writeInt(epochs);
		out.writeLong(duration);
		out.writeObject(errorEveryEpoch);
	}
}
