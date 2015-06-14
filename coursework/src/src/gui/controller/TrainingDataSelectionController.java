package gui.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TrainingDataSelectionController {
	@FXML
	private void initialize() {
		// Initialise the person table with the two columns.
		System.out.println("Training Data Selection Screen Loaded");
	}

	@FXML
	void goToInputAndOutputSelection(ActionEvent event) {
		VistaNavigator.loadVista(VistaNavigator.InputAndOutputSelection);
	}

	@FXML
	void goToAdvancedAlgorithmSelection(ActionEvent event) {
		VistaNavigator.loadVista(VistaNavigator.AdvancedAlgorithmSelection);
	}

}
