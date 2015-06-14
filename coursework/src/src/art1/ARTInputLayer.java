package art1;
import commons.InputLayer;
import commons.Node;


public class ARTInputLayer extends InputLayer {
	private static final long serialVersionUID = -5721022706484749619L;

	/** Creates an input layer for the ART network.
	 * @param size Number of nodes.
	 */
	public ARTInputLayer(int size) {
		super(size);
	}
	
	public float calculateLayerSum() {
		float sum = 0f;
		for (Node node : nodes) {
			sum += node.getOutput();
		}
		return sum;
	}

}
