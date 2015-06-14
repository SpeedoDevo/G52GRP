package gui.controller;
import gui.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rbf.CreateAndTrainRBF;
import backprop.CreateAndTrainBP;

/**
 * Controller class for the first vista.
 */
public class MainMenuController {
	CreateAndTrainBP bp = new CreateAndTrainBP();
	CreateAndTrainRBF rbf = new CreateAndTrainRBF();
	Model model = new Model();

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
		System.out.println("Main Menu Screen Loaded");
	}

	@FXML
	void goToImportData(ActionEvent event) {
		Model.setTutorialDisabled(true);
		VistaNavigator.loadVista(VistaNavigator.ImportData);
		
	}
	
	@FXML
	void loadTutorial(){
		Model.setTutorialDisabled(false);
		Model.setUsingExample(true);
		VistaNavigator.loadVista(VistaNavigator.BasicAlgorithmSelection);		
	}
		
	/**
	 * Loading works by trying to load the CreateAndTrainBP object if the object cannot
	 * be cast then a exception is thrown and it tries to load RBF instead
	 * @param event
	 */
	@FXML
	void loadNN(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a file");
		File file = fileChooser.showOpenDialog(new Stage());
		FileInputStream fin;
		ObjectInputStream ois = null;

		try {
			fin = new FileInputStream(file.getPath());
			ois = new ObjectInputStream(fin);
			// Loads a CreateAndTrainBP object then the Model
			bp = (CreateAndTrainBP) ois.readObject();
			model = (Model) ois.readObject();
			VistaNavigator.loadVista(VistaNavigator.BackPropResults);
			ois.close();
		} catch (ClassCastException e) {
			try {
				fin = new FileInputStream(file.getPath());
				ois = new ObjectInputStream(fin);
				// Loads a CreateAndTrainRBF object then the Model
				rbf = (CreateAndTrainRBF) ois.readObject();
				model = (Model) ois.readObject();
				VistaNavigator.loadVista(VistaNavigator.RbfResults);
				ois.close();
			} catch (Exception e1) {
				System.out.println("File cound not be read");
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}