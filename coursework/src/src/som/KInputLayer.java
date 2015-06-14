package som;

import java.util.Vector;


public class KInputLayer {
	
	private int size;
	private Vector<Vector<Float>> input = new Vector<Vector<Float>>(size);
	
	/**
	 * Creates a vector of vectors containing doubles to act as
	 * the input layer
	 * @param nodeArray
	 */
	public KInputLayer(Vector<Vector<Float>> nodeArray) {
		
		size = nodeArray.size();
		for (int x = 0; x < size; x++) {
			input.add(nodeArray.get(x));	
		}
	}
	
	public Vector<Float> getNode(int x){
		return input.get(x);
	}
	
	public int size() {
		return size;
	}

}
