package gui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Stack;

import commons.FileParser;
import commons.Normaliser;

public class Model implements Serializable  {
	private static final long serialVersionUID = 1077525353648519174L;
	private static FileParser fileParser;
	private static Normaliser normaliser;
	private static String[][] dataStrings;
	private static float[][] training;
	private static float[][] target;
	private static String currentVista;
	private static Stack<String> vistaStack = new Stack<>();
	private static boolean advancedDisabled;
	private static boolean tutorialDisabled;
	private static int trainingDataWidth;
	private static Thread workingThread;
	private static boolean isAdvanced;
	private static boolean usingExample;
	
	
	
	public static FileParser getFileParser() {
		return fileParser;
	}
	public static void setFileParser(FileParser fileParser) {
		Model.fileParser = fileParser;
	}
	public static Normaliser getNormaliser() {
		return normaliser;
	}
	public static void setNormaliser(Normaliser normaliser) {
		Model.normaliser = normaliser;
	}
	public static String[][] getDataStrings() {
		return dataStrings;
	}
	public static void setDataStrings(String[][] strings) {
		Model.dataStrings = strings;
	}
	public static float[][] getTraining() {
		return training;
	}
	public static void setTraining(float[][] training) {
		Model.training = training;
	}
	public static float[][] getTarget() {
		return target;
	}
	public static void setTarget(float[][] target) {
		Model.target = target;
	}
	public static String getCurrentVista() {
		return currentVista;
	}
	public static void setCurrentVista(String currentVista, boolean pushToStack) {
		if (pushToStack) {
			vistaStack.push(currentVista);
		}
		Model.currentVista = currentVista;
	}
	public static Stack<String> getVistaStack() {
		return vistaStack;
	}
	public static boolean isAdvancedDisabled() {
		return advancedDisabled;
	}
	public static void setAdvancedDisabled(boolean advancedEnabled) {
		Model.advancedDisabled = advancedEnabled;
	}
	public static boolean isTutorialDisabled() {
		return tutorialDisabled;
	}
	public static void setTutorialDisabled(boolean tutorialDisabled) {
		Model.tutorialDisabled = tutorialDisabled;
	}
	public static int getTrainingDataWidth() {
		return trainingDataWidth;
	}
	public static void setTrainingDataWidth(int trainingDataWidth) {
		Model.trainingDataWidth = trainingDataWidth;
	}
	public static Thread getWorkingThread() {
		return workingThread;
	}
	public static Thread setWorkingThread(Thread workingThread) {
		Model.workingThread = workingThread;
		return workingThread;
	}
	public static boolean isAdvanced() {
		return isAdvanced;
	}
	public static void setAdvanced(boolean isAdvanced) {
		Model.isAdvanced = isAdvanced;
	}
	public static boolean isUsingExample() {
		return usingExample;
	}
	public static void setUsingExample(boolean usingExample) {
		Model.usingExample = usingExample;
	}
	/**
	 * Used in serialisation to save static variables
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		fileParser = (FileParser) in.readObject();
		normaliser = (Normaliser) in.readObject();
		dataStrings = (String[][]) in.readObject();
		training = (float[][]) in.readObject();
		target = (float[][]) in.readObject();
		currentVista = (String) in.readObject();
		vistaStack = (Stack<String>) in.readObject();
		advancedDisabled = in.readBoolean();
		tutorialDisabled = in.readBoolean();
		trainingDataWidth = in.readInt();
		isAdvanced = in.readBoolean();
	}
	
	/**
	 * Used in serialisation to write static variables
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream  out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(fileParser);
		out.writeObject(normaliser);
		out.writeObject(dataStrings);
		out.writeObject(training);
		out.writeObject(target);
		out.writeObject(currentVista);
		out.writeObject(vistaStack);
		out.writeBoolean(advancedDisabled);
		out.writeBoolean(tutorialDisabled);
		out.writeInt(trainingDataWidth);
		out.writeBoolean(isAdvanced);
	}

}
