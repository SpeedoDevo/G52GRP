package som;

import java.util.Random;
import java.util.Vector;

public class SOM {

	static int iterations;
	static float originalLR;
	static float[][] input;
	static int nodeSize;
	static int xSize;
	static int ySize;
	static KLattice outputLattice;

	public SOM() {}
	
	public SOM(int nIterations, float nOriginalLR, float[][] nInput2,
			int nNodeSize, int nXSize, int nYSize) {
		iterations = nIterations;
		originalLR = nOriginalLR;
		input = nInput2;
		nodeSize = nNodeSize;
		xSize = nXSize;
		ySize = nYSize;
		train();

	}

	public KLattice train() {

		KNode bmu;
		float neighbourRadius;
		int iterationCount = 0;
		Random random = new Random();
		float learningRate = originalLR;
		KLattice lattice = new KLattice(xSize, ySize, nodeSize);
		Vector<Vector<Float>> nodeArray = new Vector<Vector<Float>>(
				input.length);
		
		for (int y = 0; y < input.length; y++) {
			Vector<Float> temp = new Vector<Float>();
			for (int x = 0; x < nodeSize; x++) {

				temp.add(input[y][x]);
			}
			nodeArray.add(temp);
		}

		KInputLayer inputLayer1 = new KInputLayer(nodeArray);

		// The training/running of the SOM network
		while (iterationCount < iterations) {

			int randomNumber = random.nextInt(inputLayer1.size() - 0) + 0;

			bmu = lattice.BestMatchingUnit(lattice,
					inputLayer1.getNode(randomNumber));

			neighbourRadius = (float) lattice.NeighbourCalculation(
					iterations, iterationCount);

			lattice = lattice.FindWeights(lattice,
					inputLayer1.getNode(randomNumber), bmu, neighbourRadius, learningRate);

			learningRate = (float) (originalLR * Math
					.exp(-iterationCount / iterations));
			
			iterationCount++;
		}

		outputLattice = lattice;
		
		return lattice;
	}
	
	public KLattice getLattice() {
		return outputLattice;
	}
	
	public int getNodeSize() {
		return nodeSize;
	}
}
