package gui.controller;
import gui.Model;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import rbf.CreateAndTrainRBF;
import som.SOM;
import art1.CreateAndTrainART;
import backprop.CreateAndTrainBP;

public class AdvancedAlgorithmSelectionController {

	@FXML public ComboBox<String> algo;

	@FXML public GridPane bpPane;
	@FXML public TextField bpEpochs;
	@FXML public TextField errorPercent;
	@FXML public TextField bpLearningRate;
	@FXML public TextField layerComposition;
	@FXML public ComboBox<String> trainingType;
	@FXML public ComboBox<String> activation;
	
	@FXML public GridPane rbfPane;
	@FXML public TextField rbfNodes;


	@FXML public GridPane somPane;
	@FXML public TextField width;
	@FXML public TextField height;
	@FXML public TextField somEpochs;
	@FXML public TextField somLearningRate;
	
	@FXML public GridPane artPane;
	@FXML public TextField maxClusters;
	@FXML public TextField vigilance;
	
	@FXML public Button train;
	
	@FXML
	public void initialize() {
		// Initialise the person table with the two columns.
		System.out.println("Advanced Algorithms Screen Loaded");
		Model.setAdvancedDisabled(false);

		ObservableList<String> algos = FXCollections.observableArrayList(
			"Backpropagation", "Radial Basis Function", "Self Organising Map", "Adaptive Resonance Theory (1)"
		);

		GridPane[] panes = new GridPane[] {bpPane, rbfPane, somPane, artPane};
		
		for (GridPane p : panes) {
			p.setVisible(false);
		}

		algo.setItems(algos);
		
		
		Initialiser bp = new Initialiser() {

			@Override
			public void init() {
				ObservableList<String> bpTrainingTypes = FXCollections.observableArrayList(
						"Train for N epochs", "Train until error threshold", "Train until threshold or max epochs"
					);

					ObservableList<String> activations = FXCollections.observableArrayList(
						"Sigmoid"
					);
					
					trainingType.setItems(bpTrainingTypes);
					trainingType.getSelectionModel().select(2);
					
					activation.setItems(activations);
					activation.getSelectionModel().select(0);
					
					train.setDisable(true);
					

					ChangeListener<String> cl = new ChangeListener<String>() {

						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
							try {

								if (!bpEpochs.disabledProperty().get()) {
									Integer.parseInt(bpEpochs.getText());						
								}
								if (!errorPercent.disabledProperty().get()) {
									Float.parseFloat(errorPercent.getText());
								}
								Float.parseFloat(bpLearningRate.getText());
								parseIntArray(layerComposition.getText());
								train.setDisable(false);
							} catch (NumberFormatException e) {
								train.setDisable(true);
							}
							
						}

					};
					
					bpEpochs.textProperty().addListener(cl);
					errorPercent.textProperty().addListener(cl);
					bpLearningRate.textProperty().addListener(cl);
					layerComposition.textProperty().addListener(cl);
					
					trainingType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
							switch (newValue.intValue()) {
							case 0:
								bpEpochs.setDisable(false);
								errorPercent.setDisable(true);
								break;
							case 1:
								bpEpochs.setDisable(true);
								errorPercent.setDisable(false);
								break;
							case 2:
								bpEpochs.setDisable(false);
								errorPercent.setDisable(false);
								break;
							}
						}

					});				
			}
			
		};
		
		Initialiser rbf = new Initialiser() {

			@Override
			public void init() {
				train.setDisable(true);
				
				ChangeListener<String> cl = new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						try {
							Integer.parseInt(rbfNodes.getText());						
							train.setDisable(false);
						} catch (NumberFormatException e) {
							train.setDisable(true);
						}
					}
				};

				rbfNodes.textProperty().addListener(cl);
				
			}
			
		};
		
		Initialiser som = new Initialiser() {

			@Override
			public void init() {
				train.setDisable(true);
				
				ChangeListener<String> cl = new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						try {
							Integer.parseInt(width.getText());
							Integer.parseInt(height.getText());
							Integer.parseInt(somEpochs.getText());
							Float.parseFloat(somLearningRate.getText());
							train.setDisable(false);
						} catch (NumberFormatException e) {
							train.setDisable(true);
						}
					}
				};

				width.textProperty().addListener(cl);
				height.textProperty().addListener(cl);
				somEpochs.textProperty().addListener(cl);
				somLearningRate.textProperty().addListener(cl);
	
			}
			
		};
		
		Initialiser art = new Initialiser() {

			@Override
			public void init() {
				train.setDisable(true);
				
				ChangeListener<String> cl = new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						try {
							Integer.parseInt(maxClusters.getText());
							Float.parseFloat(vigilance.getText());
							train.setDisable(false);
						} catch (NumberFormatException e) {
							train.setDisable(true);
						}
					}
				};

				vigilance.textProperty().addListener(cl);
				maxClusters.textProperty().addListener(cl);	
			}
			
		};
		
		Initialiser[] inits = new Initialiser[] { bp, rbf, som, art };

		ChangeListener<Number> cl = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				if (0 <= oldValue.intValue()) {
					panes[oldValue.intValue()].setVisible(false);
				}
				panes[newValue.intValue()].setVisible(true);
				inits[newValue.intValue()].init();
				
			}
		};
		
		algo.getSelectionModel().selectedIndexProperty().addListener(cl);

		algo.getSelectionModel().select(0);

		
	}
	
	private interface Initialiser {
		public void init();
	}
	
	private int[] parseIntArray(String text) throws NumberFormatException{
		String[] strings = text.split(",");
		int[] ints = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].trim();
			try {
				ints[i] = Integer.parseInt(strings[i]);
			} catch (NumberFormatException e) {
				throw e;
			}
		}
		return ints;
	}

	
	@FXML
	public void train(ActionEvent event) {
		switch(algo.getValue()) {
		case "Backpropagation":
			Model.setWorkingThread(new Thread(new Runnable() {
				@Override
				public void run() {
					CreateAndTrainBP bp = new CreateAndTrainBP();
					
					int trainingWidth = Model.getTrainingDataWidth();
					int targetWidth = Model.getDataStrings()[0].length - trainingWidth;
					
					Model.getNormaliser().normaliseAndSplit();
					
					int[] hiddens = parseIntArray(layerComposition.getText());
					int[] layers = new int[hiddens.length + 2];
					for (int i = 0; i < layers.length; i++) {
						if (i == 0) {
							layers[i] = trainingWidth;
						} else if (i == layers.length-1) {
							layers[i] = targetWidth;
						} else {
							layers[i] = hiddens[i-1];							
						}
					}
					
					float lr = Float.parseFloat(bpLearningRate.getText());
					
					switch (trainingType.getSelectionModel().getSelectedIndex()) {
					case 0:
						int es = Integer.parseInt(bpEpochs.getText());						
						
						bp.createTrainEpochs(lr, layers, es, Model.getTraining(), Model.getTarget());
						break;
					case 1:
						float err = Float.parseFloat(errorPercent.getText());
						
						bp.createTrainError(lr, layers, err, Model.getTraining(), Model.getTarget());
						break;
					case 2:
						int maxEpochs = Integer.parseInt(bpEpochs.getText());
						float error = Float.parseFloat(errorPercent.getText());
						
						bp.createTrainMaxOrError(lr, layers, error, maxEpochs, Model.getTraining(), Model.getTarget());
						break;
					}
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
					CreateAndTrainRBF rbf = new CreateAndTrainRBF();
					
					int trainingWidth = Model.getTrainingDataWidth();
					int targetWidth = Model.getDataStrings()[0].length - trainingWidth;
					
					Model.getNormaliser().normaliseAndSplit();
					
					int numberOfClusters = Integer.parseInt(rbfNodes.getText());
					
					rbf.createTrain(new int[] {trainingWidth,numberOfClusters,targetWidth}, Model.getTraining(), Model.getTarget());
					
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
					int iterations = Integer.parseInt(somEpochs.getText());
					float learningRate = Float.parseFloat(somLearningRate.getText());
					int nodeSize = Model.getTrainingDataWidth();
					int xSize = Integer.parseInt(width.getText());
					int ySize = Integer.parseInt(height.getText());

					new SOM(iterations, learningRate, Model.getTraining(), nodeSize, xSize, ySize);
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
					int clusters = Integer.parseInt(maxClusters.getText());
					float vigilanceF = Float.parseFloat(vigilance.getText());
					new CreateAndTrainART().createTrain(clusters, vigilanceF, Model.getTraining());
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
}
