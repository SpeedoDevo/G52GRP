package rbf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tomgibara.cluster.gvm.flt.*;

import Jama.Matrix;
import backprop.FeedForwardNetwork;
import commons.Connection;
import commons.HelperMethods;
import commons.Node;
import commons.Strategy;


public class RbfStrategy implements Strategy, Serializable {
	
	private static final long serialVersionUID = -9203070912985552045L;
	// Used to store cluster variances for later use by the activation function
	private double[] clusterVariance;
	
	/** Generates a simple Radial Basis Function {@link Strategy}.
	 */
	public RbfStrategy() {
	}
	
	/** 
	 * Calculates clusters and returns array of all centroids.
	 * It is currently using Greedy Variance Minimization clustering algorithm developed by Tom Gibara.
	 * More info on this algorithm and its advantages over k means clustering can be found at:
	 * http://www.tomgibara.com/clustering/fast-spatial/
	 * @param The 2d float array of input training data
	 * @param The number of centroids
	 * @return 2d float array of cluster centroids
	 */
	private float[][] calcCenters(float[][] inputData, int numberOfCenters) {
		float[][] centers = new float[numberOfCenters][];
		clusterVariance = new double[numberOfCenters];
		
		FltClusters<List<float[]>> clusters = new FltClusters<List<float[]>>(inputData[0].length, numberOfCenters);
		clusters.setKeyer(new FltListKeyer<float[]>());
		
		// Adds each point to the clusters
		for (int i = 0; i < inputData.length; i++) {
			ArrayList<float[]> key = new ArrayList<float[]>();
            key.add(inputData[i]);
			clusters.add(1.0f, inputData[i], key);
		}
		
		List<FltResult<List<float[]>>> results = clusters.results();
		// Saves the centroids of each cluster
		for (int j = 0; j < results.size(); j++) {
			// Saves the cluster variances for later use by the activation function
			clusterVariance[j] = results.get(j).getVariance();
			for (float[] pt : results.get(j).getKey()) {
				centers[j] = pt;
			}
		}
		
		return centers;
	}
	
	/** 
	 * Calculates the Euclidean distance between one array of points and another
	 * @param array1
	 * @param array2
	 * @return The Euclidean distance
	 */
	private float calculateEuclideanDistance(float[] array1, float[] array2)
    {
        float sum = 0.0f;
        for(int i = 0; i < array1.length; i++) {
           sum = (float) (sum + Math.pow((array1[i]-array2[i]),2.0));
        }
        return (float) Math.sqrt(sum);
    }
	
	
	/**
	 * The Gaussian activation function
	 * @param input floats to the node
	 * @param cluster center of the node
	 * @param integer telling us which cluster we are working which, used to retrieve cluster variance
	 * @return
	 */
	public float activationFunction(float[] input, float[] center, int cluster) {
		float euclideanDistance = calculateEuclideanDistance(input, center);
		
		// returns exp( - clusterVariance * Euclidean Distance^2 ) aka the activation function
		return (float) Math.exp( - clusterVariance[cluster] * (Math.pow((double) (euclideanDistance), 2)));
	}
	
	/**
	 * The network is trained with all of the inputData at once.
	 * @param The RBF neural network being worked on 
	 * @param 2d float array of the input data
	 * @param 2d float array of the target data
	 */
	public void trainNetwork(RbfNetwork nn, float[][] inputData, float[][] targetData) {
		int numberOfCenters = ((RbfHiddenLayer) nn.getLayers()[1]).getRNodes().length;
		int numberOfOutputs = targetData[0].length;
		float[][] matrices = new float[inputData.length][numberOfCenters + 1];
		float[][] centers =  new float[numberOfCenters][numberOfCenters];
		
		// Calculates centers for each node and sets them
		centers = calcCenters(inputData, numberOfCenters);
		for(int j = 0; j < numberOfCenters; j++) {
			((RbfHiddenLayer) nn.getLayers()[1]).getRNodes()[j].setCenter(centers[j]);
		}
		
		// Runs all the training data and creates matrix of the outputs from the activation functions in the hidden layer
		// this is used to calculate the weights of the connections between the hidden and output layers
		for (int i = 0; i < inputData.length; i++) {
			nn.feedInput(inputData[i]);
			nn.feedForward();
			// Used to calculate weight of bias weight
			matrices[i][0] = 1.0f;
			for(int j = 1; j < (numberOfCenters + 1); j++) {
				matrices[i][j] = ((RbfHiddenLayer) nn.getLayers()[1]).getRNodes()[j - 1].getOutput();
			}
		}
		
		//Does the matrix transformation to calculate weights between hidden and output layers
		Matrix input = new Matrix(HelperMethods.to2dDoubleArray(matrices));
		Matrix tOutput = new Matrix(HelperMethods.to2dDoubleArray(targetData));
		Matrix weights = new Matrix(numberOfOutputs, numberOfCenters + 1);

		// Performs pseudo inverse on input
		input = input.inverse();
		// Times inverted input with the expected tOutput 
		weights = input.times(tOutput);
		// Transposes the calculated weights 
		weights = weights.transpose();
		
		// Updates weights between hidden and output layers
		for (int i = 0; i < numberOfOutputs; i++) {
			for (int j = 0; j < weights.getColumnDimension(); j++) {
				// if first weight of output node, it is the bias weight so setBias of the output node
				if (j == 0) nn.getLayers()[2].getNodes()[j].setBias((float) weights.get(j, i));
				else nn.getLayers()[2].getNodes()[i].getInputConnections().get(j - 1).setWeight((float) weights.get(i, j));
			}
		}
	}

	public float calculateError(float output, float errorfactor) {
		return output * (1 - output) * errorfactor;
	}

	@Override
	public float sumFunction(ArrayList<Connection> inputConns, float bias) {
		float sum = bias;
		for (int i = 0; i < inputConns.size(); i++) {
			// Sum = sum + (weight of connection * output from hidden node activation function)
			sum = sum + (inputConns.get(i).getWeight() * inputConns.get(i).getInputNode().getOutput());
		}
		return sum;
	}

	@Override
	/**
	 * returns parameter that was given. Used to make output layer nodes
	 * calculate correct output as the output nodes in rbf just use the summing function and don't have an activation function
	 */
	public float activationFunction(float sum) {
		return sum;
	}

	/**
	 * Not used in rbf but required by Strategy interface
	 */
	@Override
	public float findNewBias(float error, float bias) {
		return 0.0f;
	}
	
	/**
	 * Not used in rbf but required by Strategy interface
	 */
	public void updateBias(Node node) {
		float newBias = findNewBias(node.getError(), node.getBias());
		node.setBias(newBias);
	}

	/**
	 * Not used in rbf but required by Strategy interface
	 */
	@Override
	public float trainNetwork(FeedForwardNetwork feedForwardNetwork,
			float[][] inputData, float[][] targetData) {
		return 0;
	}

	/**
	 * Not used in rbf but required by Strategy interface
	 */
	@Override
	public void updateWeights(ArrayList<Connection> inputConns) {
	}
}
