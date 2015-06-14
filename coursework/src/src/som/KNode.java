package som;

import java.util.Random;
import java.util.Vector;

public class KNode {
	
	private Vector<Float> weights = new Vector<Float>();
	private int x;
	private int y;
	private int nodeSize;
	private float randomWeight;
	
	/**
	 * Initialises a node that contains (size) amount of weights and 
	 * gives each weight a random number between 0 and 10
	 * @param size
	 */
	public KNode(int nodeSize) {
		this.nodeSize = nodeSize;
		randomizeWeights();
	}
	
	private void randomizeWeights() {
		for (int x = 0; x < nodeSize; x++) {
			Random random = new Random();
			randomWeight = (random.nextFloat() * (50 - 0) + 0);
			weights.add(randomWeight);
		}
	}
	
	public int getX(){
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getNodeSize() {
		
		return nodeSize;
	}
	
	public Vector<Float> getVector() {
		
		return weights;
	}

	public float getWeight(int q) {
		
		return weights.get(q);
	}
	
	public void setWeight(int index, float newWeight) {
		weights.set(index, (float) newWeight);
	}
	
}
