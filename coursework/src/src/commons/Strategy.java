package commons;
import java.util.ArrayList;

import backprop.FeedForwardNetwork;


public interface Strategy {

	public float calculateError(float output,float errorfactor);
	public float sumFunction(ArrayList<Connection> inputConns,float bias);
	public float activationFunction(float sum);
	public float findNewBias(float delta,float bias);
	public void  updateWeights(ArrayList<Connection> inputConns);
	public float trainNetwork(FeedForwardNetwork feedForwardNetwork, float[][] inputData, float[][] targetData);
	
}
