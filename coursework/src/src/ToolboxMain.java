
import gui.controller.MainController;
import gui.controller.VistaNavigator;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main application class.
 */
public class ToolboxMain extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Alfred");

		stage.setScene(createScene(loadMainPane()));

		stage.getIcons().add(new Image(VistaNavigator.class.getResourceAsStream( "/gui/view/icon.png" )));

		stage.show();
		
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
	}

	/**
	 * Loads the main fxml layout. Sets up the vista switching VistaNavigator.
	 * Loads the first vista into the fxml layout.
	 *
	 * @return the loaded pane.
	 * @throws IOException
	 *             if the pane could not be loaded.
	 */
	private Pane loadMainPane() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		
		Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(VistaNavigator.MAIN));
		

		MainController mainController = loader.getController();

		VistaNavigator.setMainController(mainController);
		VistaNavigator.loadVista(VistaNavigator.MainMenu);

		return mainPane;
	}

	/**
	 * Creates the main application scene.
	 *
	 * @param mainPane
	 *            the main application layout.
	 *
	 * @return the created scene.
	 */
	private Scene createScene(Pane mainPane) {
		Scene scene = new Scene(mainPane);
		scene.getStylesheets().setAll(
				VistaNavigator.class.getResource("/gui/view/vista.css").toExternalForm());

		return scene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
