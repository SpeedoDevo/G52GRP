package art1;
import java.util.ArrayList;

import commons.Connection;
import commons.Layer;
import commons.Node;



public class ClusterLayer extends Layer {
	private static final long serialVersionUID = 4175191929690610194L;
	private ArrayList<Node> activeNodes = new ArrayList<Node>();
	private ArrayList<Node> rejectedNodes = new ArrayList<Node>();

	/** Creates a cluster layer for the ART network.
	 * @param size Number of nodes.
	 */	
	public ClusterLayer(int size) {
		super(size);
		activeNodes.add(nodes[0]);
	}

	/**
	 * Sets all node outputs to 0f
	 */
	public void resetOutput() {
		for (Node node : nodes) {
			node.setOutput(0.0f);
		}
	}
	
	public void calculateLayerOutput() {
		for(Node node : activeNodes) {
			float newOutput = node.getOutput();
			for (Connection conn : node.getInputConnections()){
				newOutput += conn.getInputNode().getOutput() * conn.getWeight();
			}
			node.setOutput(newOutput);
		}
	}
	
	public Node getMaxNode() {
		Node maxNode = activeNodes.get(0);
		float maxNodeValue = maxNode.getOutput();
		for (int i = 1; i < activeNodes.size(); i++) {
			Node node = activeNodes.get(i);
			if (maxNodeValue < node.getOutput() && !isRejected(node)){
				maxNode = node;
				maxNodeValue = node.getOutput();
			}
		}
		return maxNode;
	}
	
	public int getNodeIndex(Node targetNode) {
		for (int i = 0; i < activeNodes.size(); i++) {
			Node node = activeNodes.get(i);
			if (node == targetNode) {
				return i;
			}
		}
		return -1;
	}
	
	public void setNodeOutputs(float inputValue) {

		for (int x = 0; x < this.getSize(); x++) {
			nodes[x].setOutput(inputValue);
		} 

	}
	
	public void rejectNode(Node node) {
		rejectedNodes.add(node);	
	}

	private boolean isRejected(Node node) {
		return rejectedNodes.contains(node);
	}
	
	public boolean allRejected() {
		return rejectedNodes.size() == activeNodes.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Node node : nodes) {
			for (Connection conn : node.getInputConnections()) {
				sb.append(conn.getWeight() + ", ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public Node createNewCluster(float[] inputData) {
		if (activeNodes.size() < nodes.length) {
			Node node = nodes[activeNodes.size()];
			activeNodes.add(node);
			
			float inputSum = 0;
			for (float data : inputData) {
				inputSum += data;
			}
			
			ArrayList<Connection> conns = node.getInputConnections();
			for (int i = 0; i < conns.size(); i++) {
				conns.get(i).setWeight(inputData[i] / (0.5f + inputSum));
			}
			
			conns = node.getOutputConnections();
			for (int i = 0; i < conns.size(); i++) {
				Connection conn = conns.get(i);
				conn.setWeight(conn.getWeight() * inputData[i]);
			}
			return node;
		} else {
			Node maxNode = activeNodes.get(0);
			float maxNodeSimilarity = 0;
			for (int i = 0; i < activeNodes.size(); i++) {
				Node node = activeNodes.get(i);
				float inputSum = 0;
				float outputSum = 0;
				for(Connection conn : node.getOutputConnections()){
					float inputValue = conn.getOutputNode().getOutput(); 
					inputSum += inputValue;
					outputSum += inputValue * conn.getWeight();
				}
				float similarity = outputSum/inputSum;
				if (maxNodeSimilarity < similarity){
					maxNode = node;
					maxNodeSimilarity = similarity;
				}
			}
			return maxNode;
		}
	}

	public void resetRejectedNodes() {
		rejectedNodes.clear();
	}

}
