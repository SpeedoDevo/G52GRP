package commons;

import java.io.Serializable;


public class HiddenLayer extends Layer implements Serializable  {
	private static final long serialVersionUID = 6962299574696704216L;

	/**	Constructs a hidden layer for the neural network
	 * @param size - Used to set the size of the layer
	 * @param prev - Used to set the previous layer
	 */
	public HiddenLayer(int size, Strategy strategy, Layer prev) {
		super(size, strategy);
		
		this.setPrevLayer(prev);
		this.connectPrevLayer();
	}
}
