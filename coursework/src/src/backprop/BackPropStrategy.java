package backprop;
import java.io.Serializable;
import java.util.ArrayList;

import commons.Connection;
import commons.Layer;
import commons.Node;
import commons.Strategy;

public class BackPropStrategy implements Strategy, Serializable  {

	private static final long serialVersionUID = 7827370024166944657L;
	private float learningRate;
	private float accumulatedError;
	private ArrayList<Float> errorEveryEpoch = new ArrayList<Float>();

	/** Generates a simple BackProp {@link Strategy} using the <code>learningRate</code> specified.
	 */
	public BackPropStrategy(float learningRate) {
		this.learningRate = learningRate;
	}
	
	/**
	 * Gets the array list containing percentage error every epoch
	 */
	public ArrayList<Float> getErrorOverEpochs() {
		return errorEveryEpoch;
	}

	/**
	 *  Weighted sum.
	 */
	public float sumFunction(ArrayList<Connection> inputConns, float bias) {

		float sum = bias;

		for (int x = 0; x < inputConns.size(); x++) {
			Connection connection = inputConns.get(x);
			float output = connection.getInputNode().getOutput();
			float weight = connection.getWeight();
			sum += output * weight;
		}

		return sum;
	}

	/** Sigmoid activation. */
	public float activationFunction(float sum) {
		return (float) (1 / (1 + Math.exp(sum * -1)));
	}

	/** Finds new bias. bias += learningRate * error */
	public float findNewBias(float error, float bias) {
		return bias + learningRate * error;

	}

	/**
	 * Updates a node's bias using the formula: <code> bias += learningRate * error </code>
	 */
	public void updateBias(Node node) {
		float newBias = findNewBias(node.getError(), node.getBias());
		node.setBias(newBias);
	}

	/** Updates weights on all connections to a node. weight += learningRate * outputFromConnectedNode * error */
	public void updateWeights(ArrayList<Connection> inputConns) {
		// New Weight  = Old Weight +  LEARNING_RATE * 1 * Output Of InputNeuron * Delta
		float newWeight;
		for(int i=0; i < inputConns.size(); i++){
			Connection conn = inputConns.get(i);
			newWeight = conn.getWeight() + learningRate * conn.getInputNode().getOutput() * conn.getOutputNode().getError();
			conn.setWeight(newWeight);
		}
	}

	/** Finds new error.
	 * @return output * (1 - output) * factor
	 */
	public float calculateError(float output, float factor) {
		return output * (1 - output) * factor;
	}

	/**
	*	Propagates the error back on the network for one set of input/expected output.
	*/
	public void backPropagate(Layer[] layers, float[] targetDataRow) {
		int numberOfLayers = layers.length;
		//loop thru layers backwards
		for (int i = numberOfLayers - 1; 0 <= i; i--) {
			Node[] nodes = layers[i].getNodes();
			//loop thru nodes
			for (int j = 0; j < nodes.length; j++) {
				Node node = nodes[j];
				//calculate error values
				if (i == numberOfLayers - 1) {
					float output = node.getOutput();
					float target = targetDataRow[j];
					float error = calculateError(output, targetDataRow[j] - output);
					node.setError(error);
					accumulatedError += Math.abs(target - output);
				} else {
					float error = calculateError(node.getOutput(), node.getErrorSum());
					node.setError(error);
				}
				//update weights and biases
				updateWeights(node.getInputConnections());
				updateBias(node);
			}
		}
	}

	private float calculatePercentageError(int inputDataLength) {
		return Math.abs(accumulatedError/inputDataLength) * 100;
	}

	/** The network is trained with all of the inputData once.
	 */
	public float trainNetwork(FeedForwardNetwork nn, float[][] inputData, float[][] targetData) {
		accumulatedError = 0f;
		for (int i = 0; i < inputData.length; i++) {
			nn.feedInput(inputData[i]);
			nn.feedForward();
			backPropagate(nn.getLayers(), targetData[i]);
		}

		errorEveryEpoch.add(calculatePercentageError(inputData.length));
		return calculatePercentageError(inputData.length);
	}


	/**The network is trained with the training data <code>epochCount</code> times.
	 */
	public float trainForNEpochs(int epochCount, FeedForwardNetwork nn, float[][] inputData, float[][] targetData) {
		accumulatedError = 0f;
		for (int i = 0; i < epochCount; i++) {
			trainNetwork(nn, inputData, targetData);
		}
		return calculatePercentageError(inputData.length);
	}

	public int trainUntilThreshold(float percentageErrorThreshold, FeedForwardNetwork nn, float[][] inputData, float[][] targetData) {
		float percentageError = 0f;
		int epochCount = 0;
		do {
			trainNetwork(nn, inputData, targetData);
			percentageError = calculatePercentageError(inputData.length);
			epochCount++;
			if (epochCount % 500 == 0) {
				System.out.println(percentageError);
			}
		} while (percentageError > percentageErrorThreshold);
		return epochCount;
	}

	public int trainUntilThresholdOrMaxEpoch(float percentageErrorThreshold, int maxEpoch, FeedForwardNetwork nn, float[][] inputData, float[][] targetData) {
		float percentageError = 0f;
		int epochCount = 0;
		do {
			trainNetwork(nn, inputData, targetData);
			percentageError = calculatePercentageError(inputData.length);
			epochCount++;
			if (epochCount % 100 == 0) {
				System.out.println(percentageError);
			}

		} while (!(percentageError < percentageErrorThreshold || maxEpoch-1 < epochCount));
		return epochCount;
	}
}
