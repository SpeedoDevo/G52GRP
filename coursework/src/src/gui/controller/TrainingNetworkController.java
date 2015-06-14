package gui.controller;
import gui.Model;

import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller class for the first vista.
 */
public class TrainingNetworkController {
	
	@FXML private ImageView spinner;

	/**
	 * Event handler fired when the user requests a new vista.
	 *
	 * @param event
	 *            the event that triggered the handler.
	 */
	@FXML
	private void initialize() {
		// Initialise the person table with the two columns.
		Model.setAdvancedDisabled(true);
		
		
		
		InputStream is = TrainingNetworkController.class.getResourceAsStream("/gui/view/spinner.gif");
		spinner.setImage(new Image(is));
		System.out.println("Main Menu Screen Loaded");
	}
	
	@FXML public void cancel() {
		Model.getWorkingThread().interrupt();
		VistaNavigator.loadVista(Model.getVistaStack().pop());
		new MainController().toggleAdvanced();
	}

}