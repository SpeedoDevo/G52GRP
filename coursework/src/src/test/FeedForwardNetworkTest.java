package test;

import org.junit.Assert;
import org.junit.Test;

import backprop.BackPropStrategy;
import backprop.FeedForwardNetwork;

import commons.Layer;

public class FeedForwardNetworkTest {

	@Test
	/**Tests whether layers are intialised properly
	 * Compares lengths of each layer to check they match expected
	 */
	public void testInitialiseLayers() {
		BackPropStrategy bp=new BackPropStrategy(0.1f);
		FeedForwardNetwork ffn=new FeedForwardNetwork(new int[] {5,4,3,2},bp);
		Layer[] layers=ffn.getLayers();
		int[] actual={layers[0].getSize(),layers[1].getSize(),layers[2].getSize(),layers[3].getSize()};
		int[] expected={5,4,3,2};//orignally inserted sizes
		Assert.assertArrayEquals(expected, actual);
	}

}
