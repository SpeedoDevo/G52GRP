package gui.controller;
import gui.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for controlling navigation between vistas.
 *
 * All methods on the navigator are static to facilitate simple access from
 * anywhere in the application.
 */
public class VistaNavigator {

	/**
	 * Strings for main fxml layouts managed by the navigator.
	 */
	public static final String MAIN = "/gui/view/main.fxml";
	public static final String MainMenu = "/gui/view/MainMenuView.fxml";
	public static final String ImportData = "/gui/view/ImportDataView.fxml";
	public static final String InputAndOutputSelection = "/gui/view/InputAndOutputSelection.fxml";
	public static final String BasicAlgorithmSelection = "/gui/view/BasicAlgorithmSelectionView.fxml";
	public static final String AdvancedAlgorithmSelection = "/gui/view/AdvancedAlgorithmSelectionView.fxml";
	public static final String AdvancedInputAndOutputSelection = "/gui/view/AdvancedInputAndOutputSelectionView.fxml";
	public static final String TrainingDataSelection = "/gui/view/TrainingDataSelectionView.fxml";
	public static final String TrainingNetwork = "/gui/view/TrainingNetworkView.fxml";
	public static final String BackPropErrorOverTime = "/gui/view/BackPropErrorOverTimeView.fxml";
	public static final String BackPropResults = "/gui/view/BackPropResultsView.fxml";
	public static final String RbfResults = "/gui/view/RbfResultsView.fxml";
	public static final String Art1Results = "/gui/view/ArtResultsView.fxml";
	public static final String SOMResults = "/gui/view/SOMResultsView.fxml";

	
	/**
	 * String for help fxml layouts managed by the navigator.
	 */
	public static final String MainMenuHelp = "/gui/view/MainMenuHelpView.fxml";
	public static final String ImportDataHelp = "/gui/view/ImportDataHelpView.fxml";
	public static final String InputAndOutputSelectionHelp = "/gui/view/InputAndOutputSelectionHelpView.fxml";
	public static final String BasicAlgorithmSelectionHelp = "/gui/view/BasicAlgorithmSelectionViewHelpView.fxml";
	


	
	/** The main application layout controller. */
	private static MainController mainController;

	/**
	 * Stores the main controller for later use in navigation tasks.
	 *
	 * @param mainController
	 *            the main application layout controller.
	 */
	public static void setMainController(MainController mainController) {
		VistaNavigator.mainController = mainController;
	}

	/**
	 * Loads the vista specified by the fxml file into the vistaHolder pane of
	 * the main application layout.
	
	 * @param fxml
	 *            the fxml file to be loaded.
	 */
	public static void loadVista(String fxml, boolean pushToStack) {
		try {
			Model.setCurrentVista(fxml, pushToStack);
			mainController.setVista(FXMLLoader.load(VistaNavigator.class.getResource(fxml)));
			if ((Model.isTutorialDisabled() == false) && (Model.getCurrentVista() != VistaNavigator.MainMenu)) {
				MainController helpShow = new MainController();
				helpShow.showHelp();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Loads the vista specified by the fxml file into the a new Stage(Window) used to display
	 * the help window.
	 * 
	 * @param fxml
	 *            the fxml file to be loaded.
	 */
	public static void loadHelpVista(String fxml) {
		try {
			Parent root  = FXMLLoader.load(VistaNavigator.class.getResource(fxml));
			Stage stage = new Stage();
	        Scene scene = new Scene(root);
	        stage.setTitle("Help");
	        stage.setScene(scene);
	        stage.setResizable(false);
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	public static void loadVista(String fxml) {
		loadVista(fxml, true);
	}



}