package art1;
import java.util.ArrayList;

import commons.Connection;
import commons.Node;


public class ART1Network {
	private float vigilance;
	private int[] memberships;
	private ARTInputLayer inputLayer;
	private ClusterLayer clusterLayer;
	

	/** 
	 * Creates an ART network ready to train. Training data should be in binary.
	 * @param maxClusters Maximum number of clusters to make.
	 * @param vigilance Vigilance factor, this determines how similar the input pattern has to be
	 * 		compared to the cluster to be classified as a member of it.
	 * @param inputPatternSize The length of one pattern.
	 * @param inputDataLength The length of patterns,
	 * 		that is if the training data is data[100][10] then <code>inputPatternSize</code>
	 * 		is 10 and <code>inputDataLength</code> is 100.
	 */
	public ART1Network(int maxClusters, float vigilance, int inputPatternSize, int inputDataLength) {
		this.vigilance = vigilance;
		inputLayer = new ARTInputLayer(inputPatternSize);
		clusterLayer = new ClusterLayer(maxClusters);
		
		float weight = (float) (1.0 / (1.0 + inputPatternSize));
		clusterLayer.connectInputLayerWithWeight(inputLayer, weight);
		inputLayer.connectInputLayerWithWeight(clusterLayer, 1);
		
		memberships = new int[inputDataLength];
		
	}
	
	/** Rounds a 2D float array to a 2D binary (float) array.*/
	private float[][] roundToBinary(float[][] in) {
		float[][] out = new float[in.length][in[0].length];
		for (int i = 0; i < in.length; i++) {
			for (int j = 0; j < in[0].length; j++) {
				out[i][j] = Math.round(in[i][j]);
			}
		}
		return out;
	}
	

	/** Trains the network using the passed in training data. Training data should be binary,
	 * 		as it goes through a lossy binary conversion.
	 * @param inputData 2D float array of binary values. It's size must be
	 * 		<code>inputData[inputDataLength][inputPatternSize]</code> (from the constructor).
	 * @return A membership array where <code>membership[j] = k</code> means that the jth element of the <code>inputData</code> belongs to kth cluster.
	 */
	public int[] train(float[][] inputData) {
		float[][] trainingData = roundToBinary(inputData);
		for (int i = 0; i < trainingData.length; i++) {
			inputLayer.setNodeOutputs(trainingData[i]);
			
			clusterLayer.resetRejectedNodes();

			Node maxNode = new Node();
			
			boolean done = false;
			while (!done) {
				clusterLayer.calculateLayerOutput();
				maxNode = clusterLayer.getMaxNode();
				float similarity = calculateSimilarity(maxNode);
				if (vigilance <= similarity) {
					updateWeights(maxNode, similarity);
					done = true;
				} else {
					clusterLayer.rejectNode(maxNode);
					if (clusterLayer.allRejected()){
						maxNode = clusterLayer.createNewCluster(trainingData[i]);
						done = true;
					}
				}
			}
			memberships[i] = clusterLayer.getNodeIndex(maxNode);
//			System.out.println(memberships[i]);
		}
		return memberships;
	}
	
	private float calculateSimilarity(Node node) {
		float inputSum = 0;
		float outputSum = 0;
		for(Connection conn : node.getOutputConnections()){
			float inputValue = conn.getOutputNode().getOutput(); 
			inputSum += inputValue;
			outputSum += inputValue * conn.getWeight();
		}
		return outputSum/inputSum;
	}
	
	private void updateWeights(Node maxNode, float similarity) {
		ArrayList<Connection> clusterToInputConns = maxNode.getOutputConnections();
		ArrayList<Connection> inputToClusterConns = maxNode.getInputConnections();
		for (int i = 0; i < inputToClusterConns.size(); i++) {
			Connection conn = inputToClusterConns.get(i);
			float newWeight = 
					(clusterToInputConns.get(i).getWeight() * conn.getInputNode().getOutput())
					/
					(0.5f + similarity);
			conn.setWeight(newWeight);
		}
		for (Connection conn : clusterToInputConns) {
			float newWeight = conn.getWeight() * conn.getOutputNode().getOutput();
			conn.setWeight(newWeight);
		}
	}
}
