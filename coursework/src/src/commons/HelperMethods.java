package commons;
import java.io.Serializable;
import java.util.Random;

/** Helper methods should be contained here. */
public class HelperMethods implements Serializable  {
	private static final long serialVersionUID = -248932321840047286L;

	/**@return A float in the range [min, max).*/
	public static float random(float min, float max) {
		Random rand = new Random();
		return rand.nextFloat() * (max - min) + min;
	}
	
	public static double[][] to2dDoubleArray(float[][] arr) {
		if (arr == null) return null;
		int n = arr.length;
		int m = arr[0].length;
		double[][] ret = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				ret[i][j] = (double)arr[i][j];
			}
		}
		return ret;
	}
}
