package rbf;

import java.io.Serializable;

import commons.Node;

public class RbfHiddenNode extends Node implements Serializable {
	private static final long serialVersionUID = 2726688404691755127L;
	private float rOutput;
	private float[] center;

	/**
	 * Node constructed with a random bias between [-1,1].
	 */
	public RbfHiddenNode (RbfStrategy strategy) {
		super(strategy);
	}
	
	/**
	 * Sets the vector centroid of this cluser
	 * @param The vector center for this node
	 */
	public void setCenter(float[] center) {
		this.center = center;
	}
	
	/**
	 * Gets centroid of this node
	 */
	public float[] getCenter() {
		return center;
	}
	
	/**
	 * Calculate the out from the node
	 * @param the cluster which the node belongs to
	 * @return output of the node
	 */
	public float calculateOutput(int cluster) {
		rOutput = ((RbfStrategy) getStrategy()).activationFunction(getInputs(), center, cluster);
		return rOutput;
	}

	/**
	 * Gets the output for this node
	 */
	public float getOutput() {
		return rOutput;
	}
	
	/**
	 * Gets all of the inputs to this hidden node
	 * @return
	 */
	public float[] getInputs() {
		float[] inputs = new float[getInputConnections().size()]; 
		for(int i = 0; i < getInputConnections().size(); i++) {
			inputs[i] = getInputConnections().get(i).getInputNode().getOutput();
		}
		return inputs;
	}
}
