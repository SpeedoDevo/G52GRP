package som;

/**
 * Sources: http://www.ai-junkie.com/ann/som/som2.html , http://www.heatonresearch.com/articles/6/page3.html
 */
import java.util.Vector;

public class KLattice {

	private int width;
	private int height;
	private int nodeSize;
	private KNode[][] lattice;

	/**
	 * Creates a 2D array of KNode objects
	 * 
	 * @param width
	 * @param height
	 */
	public KLattice(int width, int height, int nodeSize) {

		this.width = width;
		this.height = height;
		this.nodeSize = nodeSize;
		generateLattice();
	}
	
	public void generateLattice() {
		lattice = new KNode[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				lattice[x][y] = new KNode(nodeSize);
				lattice[x][y].setX(x);
				lattice[x][y].setY(y);
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public KNode getNode(int x, int y) {
		return lattice[x][y];
	}

	/**
	 * Compares the selectedVector to each node in the lattice and determines
	 * which node is the most similar and then stores it as the best matching
	 * unit
	 * 
	 * @param testLattice
	 * @param selectedVector
	 * @return
	 */
	public KNode BestMatchingUnit(KLattice testLattice,
			Vector<Float> selectedVector) {

		float tempDist = 0;
		float shortestDist = Integer.MAX_VALUE;
		KNode bestNode = testLattice.getNode(0, 0);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int q = 0; q < testLattice.getNode(x, y).getNodeSize(); q++) {

					tempDist += calcEucDist(testLattice.getNode(x, y)
							.getWeight(q), selectedVector.get(q));
				}

				if (tempDist < shortestDist) {
					shortestDist = tempDist;
					bestNode = testLattice.getNode(x, y);
				}
				tempDist = 0;
			}
		}
		return bestNode;
	}

	/**
	 * Calculates the neighbourhood for all other nodes to be affected by the
	 * weight updates
	 * 
	 * @param totalIterations
	 * @param iterations
	 * @return
	 */
	public double NeighbourCalculation(float totalIterations, float iterations) {

		int maxDimension = Math.max(width,height);
		float radius = maxDimension / 2;
		float time = (float) (totalIterations / Math.log(radius));
		
		return radius * Math.exp(-iterations / time);
	}

	/**
	 * Finds all nodes within the neighbourhood previously determined and calls
	 * the method to update them with an altered influence
	 * 
	 * @param lattice
	 * @param selectedVector
	 * @param bmu
	 * @param neighbourhoodRadius
	 * @param learningRate
	 * @return
	 */
	public KLattice FindWeights(KLattice lattice, Vector<Float> selectedVector,
			KNode bmu, float neighbourhoodRadius, float learningRate) {

		double tempDist = 0;
		float influence;
		float radiusSquared = neighbourhoodRadius * neighbourhoodRadius;
		int x1 = (bmu.getX());
		int y1 = (bmu.getY());

		for (int x = 0; x < lattice.getWidth(); x++) {
			for (int y = 0; y < lattice.getHeight(); y++) {
				for (int q = 0; q < lattice.getNode(x, y).getNodeSize(); q++) {

					tempDist = calcEucDist(lattice.getNode(x, y).getX(), x1)
							+ calcEucDist(lattice.getNode(x, y).getY(), y1);
				}
				if (tempDist < radiusSquared) {
					influence = (float) Math.exp(-(tempDist)
							/ (2 * (radiusSquared)));

					UpdateWeights(lattice.getNode(x, y), learningRate,
							influence, selectedVector);
				}
				tempDist = 0;
			}
		}
		return lattice;
	}

	/**
	 * Updates the weights based on the current learning rate and how close they
	 * are to the central node
	 * 
	 * @param node
	 * @param learningRate
	 * @param influence
	 * @param selectedVector
	 */
	private void UpdateWeights(KNode node, float learningRate, float influence,
			Vector<Float> selectedVector) {
		float temp;
		for (int x = 0; x < node.getNodeSize(); x++) {
			temp = node.getWeight(x);
			float difference = selectedVector.get(x) - temp;
			temp += influence * learningRate * (difference);
			node.setWeight(x, temp);
		}
	}

	/**
	 * Calculates how close the node is to the selected vector
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private float calcEucDist(float x, float y) {

		float temp;
		temp = (y - x) * (y - x);

		return temp;
	}

}
