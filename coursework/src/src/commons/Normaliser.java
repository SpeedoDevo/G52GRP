package commons;
import gui.Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Normaliser implements Serializable  {
	private static final long serialVersionUID = 5360523009187852949L;
	private static float mins[];
	private static float maxs[];
	private static String[][] rawData;
	private static boolean[] fromString;
	private static ArrayList<HashMap<String, Float>> mappings;
	private static boolean isAdvanced;

	
	public Normaliser() {};
	
	/** Creates a de/Normaliser for this data. Each data set should have its own instance of Normaliser,
	 * 		as data about normalising is stored in it. 
	 * @param data raw 2D String array from read file
	 */
	public Normaliser(String[][] data) {
		rawData = data;
		int width = rawData[0].length;
		mins = new float[width];
		maxs = new float[width];
		fromString = new boolean[width];
		mappings = new ArrayList<HashMap<String, Float>>(width);
		for (int i = 0; i < width; i++) {
			mappings.add(new HashMap<String, Float>());
		}
	}
	
	public void setAdvanced(boolean setting) {
		isAdvanced = setting;
	}
	
	public void setFromString(boolean[] newFromString) {
		fromString = newFromString;
	}

	/** Normalise the raw data. Generates information for denormalising.
	 * 		Usually used to normalise data for training with a network.
	 * @return normalised 2D float array
	 */
	public float[][] normaliseRaw() {
		float arr[][];
		if (isAdvanced) {
			arr = transpose(toFloatAdvanced(rawData));
		} else {
			arr = transpose(toFloat(rawData));
		}
		
		//find minimum and maximum in each column to determine range of values
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			min = Float.MAX_VALUE;
			max = Float.MIN_VALUE;
			for (int j = 0; j < arr[0].length; j++) {
				float el = arr[i][j];
				if (el < min) {
					min = el;
				}
				if (max < el) {
					max = el;
				}
			}
			mins[i] = min;
			maxs[i] = max;
		}
		
		//normalise using the range
		float[][] normtransposed = new float[arr.length][arr[0].length]; 
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				normtransposed[i][j] = (arr[i][j] - mins[i]) / (maxs[i] - mins[i]);
			}
		}

		return transpose(normtransposed);
	}
	
	public void normaliseForSom() {
		float[][] norm = new float[Model.getDataStrings().length][Model.getDataStrings()[0].length];
		for (int i = 0; i < norm.length; i++) {
			for (int j = 0; j < norm[0].length; j++) {
				String el = rawData[i][j];
				try {
					norm[i][j] = Float.parseFloat(el);
				//if not parseable
				} catch (NumberFormatException e) {
					//was converted from a String
					fromString[j] = true;
					if (mappings.get(j).containsKey(el)) {
						//if we already have a mapping set that
						norm[i][j] = mappings.get(j).get(el);
					} else {
						//otherwise create new mapping
						float max = mappings.get(j).size();
						mappings.get(j).put(el, max);
						norm[i][j] = max;
					}
				}
			}
		}
		
		Model.setTraining(norm);

	}
	
	public void normaliseAndSplit() {
		float[][] norm = normaliseRaw();
		int trainingWidth = Model.getTrainingDataWidth();
		float[][] training = new float[norm.length][trainingWidth];
		float[][] target = new float[norm.length][norm[0].length - trainingWidth];
		
		for (int i = 0; i < norm.length; i++) {
			for (int j = 0; j < norm[0].length; j++) {
				if (j < trainingWidth) {
					training[i][j] = norm[i][j];
				} else {
					target[i][j - trainingWidth] = norm[i][j];
				}
			}
		}
		
		Model.setTraining(training);
		Model.setTarget(target);
		
		
	}

	
	/** Normalises data for testing a network, ie. probably shouldn't include target data.
	 * 		Usually used for user benchmarking on the UI.
	 * @param raw 1D array of values
	 * @return normalised array using previously determined ranges
	 */
	public float[] normaliseOne(String[] raw) {
		float[] data;
		if (isAdvanced) {
			data = toFloatAdvanced(new String[][] {raw})[0];
		} else {
			data = toFloat(new String[][] {raw})[0];
		}
		float[] out = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = (data[i] - mins[i]) / (maxs[i] - mins[i]);
		}
		return out;
	}

	/** Converts a raw 2D String array to floats. If a value is not parseable as float,
	 * 		then it is mapped to a float and the mapping is saved for denormalising. 
	 * @param arr array to be parsed
	 * @return 2D pure float array
	 */
	private float[][] toFloat(String[][] arr) {
		float[][] out = new float[arr.length][arr[0].length];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				String el = arr[i][j];
				try {
					out[i][j] = Float.parseFloat(el);
				//if not parseable
				} catch (NumberFormatException e) {
					//was converted from a String
					fromString[j] = true;
					if (mappings.get(j).containsKey(el)) {
						//if we already have a mapping set that
						out[i][j] = mappings.get(j).get(el);
					} else {
						//otherwise create new mapping
						float max = mappings.get(j).size();
						mappings.get(j).put(el, max);
						out[i][j] = max;
					}
				}
			}
		}
		return out;
	}
	
	/** Converts a raw 2D String array to floats. If a value is not parseable as float,
	 * 		then it is mapped to a float and the mapping is saved for denormalising. 
	 * @param arr array to be parsed
	 * @return 2D pure float array
	 */
	private float[][] toFloatAdvanced(String[][] arr) {
		float[][] out = new float[arr.length][arr[0].length];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				String el = arr[i][j];
				
				if (!fromString[j]) {
					try {
						out[i][j] = Float.parseFloat(el);
					} catch (NumberFormatException e) {
						//user fucked up
						isAdvanced = false;
						mappings.clear();
						for (int k = 0; k < rawData[0].length; k++) {
							mappings.add(new HashMap<String, Float>());
						}
						return toFloat(arr);
					}
				} else {
					if (mappings.get(j).containsKey(el)) {
						//if we already have a mapping set that
						out[i][j] = mappings.get(j).get(el);
					} else {
						//otherwise create new mapping
						float max = mappings.get(j).size();
						mappings.get(j).put(el, max);
						out[i][j] = max;
					}
				}
			}
		}
		return out;
	}

	/** Denormalises an array to a String array. If there was a column where the values were
	 * 		obtained by mapping to a float then the value is rounded so that it matches the closest mapping.
	 * 		Usually used to transform a network's output to a human readable form.
	 * @param data
	 * @return
	 */
	public String[] denormalise(float[] data) {
		String[] out = new String[data.length];
		int o = rawData[0].length - data.length;
		for (int i = 0; i < data.length; i++) {

			float unranged = (data[i] * (maxs[i+o] - mins[i+o])) + mins[i+o];
			if (Float.isNaN(unranged)) {
				out[i] = "unknown";
			} else if (fromString[i+o]) {
				float rounded = Math.round(unranged);
				HashMap<String, Float> mapping = mappings.get(i+o);
				Float max = Collections.max(mapping.values());
				Float min = Collections.min(mapping.values());
								
				if (rounded < min) {
					out[i] = getKeyByValue(mappings.get(i+o), min) + String.format("~%.0f", Math.abs(unranged-min)*10);
				} else if (max < rounded) {
					out[i] = getKeyByValue(mappings.get(i+o), max) + String.format("~%.0f", Math.abs(unranged-max)*10);
				} else {
					out[i] = getKeyByValue(mappings.get(i+o), rounded) + String.format("~%.0f", Math.abs(unranged-rounded)*10);
				}

			} else {
				out[i] = "" + unranged;
			}
		}
		return out;
	}

	
	/** Reverse mapping, one-to-one relation required. 
	 */
	private <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}

	/** Transposes a 2D array.
	 */
	private float[][] transpose(float[][] data) {
		float[][] out = new float[data[0].length][data.length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				out[j][i] = data[i][j];
			}
		}
		return out;
	}

	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		out.defaultWriteObject();
		out.writeObject(mins);
		out.writeObject(maxs);
		out.writeObject(rawData);
		out.writeObject(fromString);
		out.writeObject(mappings);
		out.writeObject(isAdvanced);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		mins = (float[]) in.readObject();
		maxs = (float[]) in.readObject();
		rawData = (String[][]) in.readObject();
		fromString = (boolean[]) in.readObject();
		mappings = (ArrayList<HashMap<String, Float>>) in.readObject();
		isAdvanced = (boolean) in.readObject();

	}

	
}
