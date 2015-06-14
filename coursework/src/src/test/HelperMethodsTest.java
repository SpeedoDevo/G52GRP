package test;

import org.junit.Assert;
import org.junit.Test;

import commons.HelperMethods;

public class HelperMethodsTest {

	@Test
	/**Test if method correctly converts to 2ddoublearray
	 * Assert actuals with expecteds
	 * 
	 */
	public void testTo2dDoubleArray() {
		float[][] arr={{1.02f,2.156f,3.115f}};
		double[][] actual  =HelperMethods.to2dDoubleArray(arr);
		double[][] expected = {{1.0199999809265137,2.1559998989105225,3.115000009536743}};
		Assert.assertArrayEquals(expected, actual);
		
	}

}
