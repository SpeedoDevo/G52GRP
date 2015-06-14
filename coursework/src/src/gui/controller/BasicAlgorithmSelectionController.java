package gui.controller;

import gui.Model;

import java.io.FileNotFoundException;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import rbf.CreateAndTrainRBF;
import som.SOM;
import art1.CreateAndTrainART;
import backprop.CreateAndTrainBP;

import commons.FileParser;
import commons.Normaliser;

public class BasicAlgorithmSelectionController {
	CreateAndTrainBP bp = new CreateAndTrainBP();
	CreateAndTrainRBF rbf = new CreateAndTrainRBF(); 
	boolean tutorial = Model.isTutorialDisabled();
	CreateAndTrainART art1 = new CreateAndTrainART();
	SOM som;
	
	@FXML private AnchorPane rbfSettings;
	@FXML private ComboBox<String> comboBox;
	@FXML private TextField clusters;
	@FXML private Label labelClusters;
	@FXML private Button next;
	
	
	@FXML
	private void initialize() {
		ObservableList<String> supervised = FXCollections.observableArrayList(
			"Backpropagation", "Radial Basis Function"
		);
		
		ObservableList<String> unsupervised = FXCollections.observableArrayList(
			"Self Organising Map", "Adaptive Resonance Theory (1)"
		);
		
		ObservableList<String> both = FXCollections.observableArrayList(
			"Backpropagation", "Radial Basis Function", "Self Organising Map", "Adaptive Resonance Theory (1)"
		);
		
		Model.setAdvancedDisabled(false);

		if (Model.isUsingExample()) {
			comboBox.setItems(both);
		} else {
			boolean b = Model.getDataStrings()[0].length == Model.getTrainingDataWidth()
					|| Model.getTrainingDataWidth() == 0;
			comboBox.setItems(b ? unsupervised : supervised);
		}
		
		comboBox.getSelectionModel().select(0);

		// Initialise the person table with the two columns.
		System.out.println("Basic Algorithm Selection Loaded");
	}
	
	/**
	 * Shows the settings for the selected algorithm
	 */
	@FXML
	void algoChange() {
		hideAllSettings();
		switch(comboBox.getValue()) {
			case "Backpropagation":
				next.setDisable(false);
				
				break;
			case "Radial Basis Function":
				next.setDisable(true);
				clusterFieldChange();
				//Show RBF settings
				rbfSettings.setDisable(false);
				rbfSettings.setOpacity(1.0);
				
				break;
			case "Self Organising Map":
				next.setDisable(false);
				
				break;
			case "Adaptive Resonance Theory (1)":
				next.setDisable(false);
				
				break;
		}
	}
	
	/**
	 * Hides all algorithm settings is used to switch between algorithm settings
	 */
	void hideAllSettings() {
		// RBF settings
		rbfSettings.setDisable(true);
		rbfSettings.setOpacity(0.0);
	}

	@FXML
	void goToAdvancedAlgorithmsSelection(ActionEvent event) {
		VistaNavigator.loadVista(VistaNavigator.AdvancedAlgorithmSelection);
	}

	@FXML
	void goToEndResults(ActionEvent event) {
		
		if (Model.isUsingExample()) {
			
			String file = "";
			
			switch(comboBox.getValue()) {
			case "Backpropagation":
			case "Radial Basis Function":
				file = VistaNavigator.class.getResource("/data/iris.csv").getFile();
				Model.setTrainingDataWidth(4);
				break;
			case "Self Organising Map":
				file = VistaNavigator.class.getResource("/data/rgb.csv").getFile();
				Model.setTrainingDataWidth(3);
				break;
			case "Adaptive Resonance Theory (1)":
				file = VistaNavigator.class.getResource("/data/letters.csv").getFile();
				Model.setTrainingDataWidth(63);
				break;
			}

			FileParser fileParser = null;
			try {
				fileParser = new FileParser(file, ",");
			} catch (FileNotFoundException e) {}
			
			Model.setFileParser(fileParser);
			String[][] strings = fileParser.getParsed(); 
			Model.setDataStrings(strings);
			for (int i = 0; i < strings.length; i++) {
				System.out.println(Arrays.toString(strings[i]));
			}
			Normaliser normaliser = new Normaliser(strings);
			Model.setNormaliser(normaliser);

		}
		
		switch(comboBox.getValue()) {
			case "Backpropagation":
				Model.setWorkingThread(new Thread(new Runnable() {
					@Override
					public void run() {
						int trainingWidth = Model.getTrainingDataWidth();
						int targetWidth = Model.getDataStrings()[0].length - trainingWidth;
						Model.getNormaliser().normaliseAndSplit();
						int[] layerComposition = new int[] {trainingWidth,5,5,targetWidth};
						bp.createTrainMaxOrError(0.7f, layerComposition, 10f, 5000, Model.getTraining(), Model.getTarget());
						if (!Thread.currentThread().isInterrupted()) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									VistaNavigator.loadVista(VistaNavigator.BackPropResults);
								}
							});
						}
					}
				})).start();
				VistaNavigator.loadVista(VistaNavigator.TrainingNetwork, false);
				break;
			case "Radial Basis Function":
				Model.setWorkingThread(new Thread(new Runnable() {
					@Override
					public void run() {
						int trainingWidth = Model.getTrainingDataWidth();
						int targetWidth = Model.getDataStrings()[0].length - trainingWidth;
						Model.getNormaliser().normaliseAndSplit();
						int numberOfClusters = Integer.parseInt(clusters.getText());
						int[] layerComposition = new int[] {trainingWidth,numberOfClusters,targetWidth};
				
						rbf.createTrain(layerComposition, Model.getTraining(), Model.getTarget());
						if (!Thread.currentThread().isInterrupted()) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									VistaNavigator.loadVista(VistaNavigator.RbfResults);
								}
							});
						}
					}
				})).start();
				VistaNavigator.loadVista(VistaNavigator.TrainingNetwork, false);
				break;
			case "Self Organising Map":
				
				Model.setWorkingThread(new Thread(new Runnable() {
					@Override
					public void run() {
						// Parses raw data as SOM cannot use normalised data
						Model.getNormaliser().normaliseForSom();
						
						//Default settings for basic algo selection
						int iterations = 500;
						float learningRate = 0.1f;
						int nodeSize = Model.getTrainingDataWidth();
						int xSize = 8;
						int ySize = 8;

						som = new SOM(iterations, learningRate, Model.getTraining(), nodeSize, xSize, ySize);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								VistaNavigator.loadVista(VistaNavigator.SOMResults);
							}
						});
					}
				})).start();
				VistaNavigator.loadVista(VistaNavigator.TrainingNetwork, false);
				
				break;
			case "Adaptive Resonance Theory (1)":
				
				Model.setWorkingThread(new Thread(new Runnable() {
					@Override
					public void run() {
						Model.setTraining(Model.getNormaliser().normaliseRaw());
						int clusters = Model.isUsingExample() ? 7 : 50;
						art1.createTrain(clusters, 0.8f, Model.getTraining());
						if (!Thread.currentThread().isInterrupted()) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									VistaNavigator.loadVista(VistaNavigator.Art1Results);
								}
							});
						}
					}
				})).start();
				VistaNavigator.loadVista(VistaNavigator.TrainingNetwork, false);
				break;
		}
		
	}
	
	@FXML
	void clusterFieldChange() {
		try {
			Integer.parseInt(clusters.getText());
			next.setDisable(false);
			// is an integer!
		} catch (NumberFormatException e) {
			next.setDisable(true);;
		}
	}
}
