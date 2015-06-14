package commons;

import java.io.Serializable;

public class InputLayer extends Layer implements Serializable  {
	private static final long serialVersionUID = 4675960158820357393L;

	public InputLayer(int size, Strategy strategy) {
		super(size, strategy);
	}

	public InputLayer(int size) {
		super(size);
	}

	public void setNodeOutputs(float[] inputValues) {

		for (int x = 0; x < this.getSize(); x++) {
			nodes[x].setOutput(inputValues[x]);
		} 

	}

	/** 
	 * Overrides abstract Layer as function is not needed
	 */
	public Layer getPrevLayer() {
		return null;
	}

	/** 
	 * Overrides abstract Layer as function is not needed
	 */
	public void setPrevLayer(Layer layer) {}
	
	/** 
	 * Overrides abstract Layer as function is not needed
	 */
	public void connectPrevLayer() {}
	
	/** 
	 * Overrides abstract Layer as function is not needed
	 */
	public void calculateLayerOutput() {}
	
}
