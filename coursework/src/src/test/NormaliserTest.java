package test;

import org.junit.Assert;
import org.junit.Test;

import commons.Normaliser;

public class NormaliserTest {

	@Test
	/**Test whether normalisation works
	 * param actual (output of normaliseRaw())
	 * parma expected (manually calculated expected output of normaliseRaw())
	 */
	public void testNormaliseRaw() {
		String[][] data = new String[][] {
				{ "5.1", "3.5", "1.4", "0.2"},
				{ "4.9", "3.0", "1.4", "0.2"},
				{ "4.7", "3.2", "1.2", "0.1"},
		};
		Normaliser nTest=new Normaliser(data);
		float[][] actual=nTest.normaliseRaw();
		float[][] expected={{1.0f, 1.0f, 1.0f, 1.0f}, {0.5000006f, 0.0f, 1.0f, 1.0f}, {0.0f, 0.4000001f, 0.0f, 0.0f},};;
		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	/**Tests whether denormalisation works
	 * param@ actual (output of denormalise())
	 * param@ expected (manually calculated values that are expected from denormalise())
	 */
	public void testDenormalise() {
		String[][] data = new String[][] {
				{ "5.1", "3.5", "1.4", "0.2","Iris-setosa"},
				{ "4.9", "3.0", "1.4", "0.2","Iris-setosa"},
				{ "4.7", "3.2", "1.2", "0.1","Iris-setosa"},
		};
		Normaliser n=new Normaliser(data);
		n.normaliseRaw();
		float[] o = {0.11f, 0.2f, 0.0f, 0.1f,0.1f};
		String[] actual=n.denormalise(o);
		String[] expected={"4.744", "3.1", "1.2", "0.11", "Iris-setosa~0"};
		Assert.assertArrayEquals(expected, actual);
		
		
	}


}
