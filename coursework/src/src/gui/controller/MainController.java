package gui.controller;

import gui.Model;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;

/**
 * Main controller class for the entire layout.
 */
public class MainController {

	/** Holder of a switchable vista. */
	@FXML
	private StackPane vistaHolder;
	@FXML
	private CheckBox advanced;
	@FXML private Button back;

	/**
	 * Replaces the vista displayed in the vista holder with a new vista.
	 *
	 * @param node
	 *            the vista node to be swapped in.
	 */
	public void setVista(Node node) {
		
		advanced.setDisable(Model.isAdvancedDisabled());

		advanced.setVisible(!Model.isAdvancedDisabled());

		if (Model.getCurrentVista() == VistaNavigator.TrainingNetwork
		 || Model.getVistaStack().size() <= 1) {
			back.setDisable(true);
		} else {
			back.setDisable(false);
		}

		vistaHolder.getChildren().setAll(node);

		advancedChanged();
	}


	/**
	 * Switch on which vista is loaded, therefore different help sections can be
	 * loaded on each screen.
	 */
	
	@FXML
	public void back() {
		Model.getVistaStack().pop();
		VistaNavigator.loadVista(Model.getVistaStack().pop());
		toggleAdvanced();
	}
	
	/** Switch on which vista is loaded, therefore different help sections can be loaded on each screen.
	 */
	@FXML
	public void showHelp() {

		String helpContent;

		switch (Model.getCurrentVista()) {
		case VistaNavigator.MAIN:
			helpContent = "main";
		case VistaNavigator.MainMenu:
			helpContent = VistaNavigator.MainMenuHelp;
			break;
		case VistaNavigator.ImportData:
			helpContent = VistaNavigator.ImportDataHelp;
			break;
		case VistaNavigator.InputAndOutputSelection:
			helpContent = VistaNavigator.InputAndOutputSelectionHelp;
			break;
		case VistaNavigator.BasicAlgorithmSelection:
			helpContent = VistaNavigator.BasicAlgorithmSelectionHelp;
			break;
		case VistaNavigator.AdvancedAlgorithmSelection:
		case VistaNavigator.AdvancedInputAndOutputSelection:
		case VistaNavigator.TrainingDataSelection:
		default:
			helpContent = null;
			break;
		}
		if (helpContent != null) {
			VistaNavigator.loadHelpVista(helpContent);
		}

	}

	@FXML
	public void toggleAdvanced() {
		if (Model.isAdvanced()) {
			switch (Model.getCurrentVista()) {
			case VistaNavigator.BasicAlgorithmSelection:
				VistaNavigator.loadVista(VistaNavigator.AdvancedAlgorithmSelection, false);
				break;
			case VistaNavigator.InputAndOutputSelection:
				VistaNavigator.loadVista(VistaNavigator.AdvancedInputAndOutputSelection, false);
				break;
			default:
				break;
			}
		} else {
			switch (Model.getCurrentVista()) {
			case VistaNavigator.AdvancedAlgorithmSelection:
				VistaNavigator.loadVista(VistaNavigator.BasicAlgorithmSelection, false);
				break;
			case VistaNavigator.AdvancedInputAndOutputSelection:
				VistaNavigator.loadVista(VistaNavigator.InputAndOutputSelection, false);
				break;
			default:
				break;
			}

		}

	}
	
	@FXML
	public void advancedChanged() {
		Model.setAdvanced(advanced.isSelected());
		System.out.println(Model.getCurrentVista());
		System.out.println(Model.isAdvanced());
		toggleAdvanced();		
	}

}