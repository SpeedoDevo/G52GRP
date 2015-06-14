package art1;

import java.io.Serializable;

public class CreateAndTrainART implements Serializable {
	private static final long serialVersionUID = 5790348378859013437L;
	private static ART1Network nn;
	private static float artVigilance;
	private static int artMaxClusters;
	private static long duration;
	private static int[] memberships;
	
	public void createTrain(int maxClusters, float vigilance, float[][] patterns) {
		artVigilance = vigilance;
		artMaxClusters = maxClusters;
		
		nn = new ART1Network(maxClusters, vigilance, patterns[0].length, patterns.length);
		long startTime = System.nanoTime();
		memberships = nn.train(patterns);
		long endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
	}

	/**
	 * Gets the neural network
	 * @return ART1 Neural Network
	 */
	public ART1Network getNeuralNet() {
		return nn;
	}
	
	/**
	 * Gets the NN's vigilance
	 * @return float
	 */
	public float getVigilance() {
		return artVigilance;
	}
	
	/**
	 * Gets the NN's maximum clusters
	 * @return int
	 */
	public int getMaxClusters() {
		return artMaxClusters;
	}
	
	/**The time taken to train network in milliseconds
	 * @return long
	 */
	public long getDuration() {
		return duration;
	}
	
	/**Array of each data sets cluster membership
	 * @return int[]
	 */
	public int[] getMemberships() {
		return memberships;
	}
}
