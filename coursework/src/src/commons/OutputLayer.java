package commons;


public class OutputLayer extends Layer {
	private static final long serialVersionUID = -7435137193514558425L;

	public OutputLayer(int size, Strategy strategy, Layer prev) {
		super(size, strategy);
		
		this.setPrevLayer(prev);
		this.connectPrevLayer();
	}

	/** 
	 * Overrides abstract Layer as function is not needed
	 */
	public Layer getNextLayer() {
		return null;
	}

	/** 
	 * Overrides abstract Layer as function is not needed
	 */
	public void setNextLayer(Layer layer) {}
	
	/**Gets the output of every node in the layer and returns as a float array
	 * @return The output values of the output layer
	 */
	public float[] getOutput() {
		float[] result = new float[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			result[i] = nodes[i].getOutput();
			//System.out.print(result[i]+",");
		}
		return result;
	}

}
