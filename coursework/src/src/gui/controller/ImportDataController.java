package gui.controller;
import gui.Model;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import commons.FileParser;
import commons.Normaliser;

public class ImportDataController {
	private FileParser fileParser;
	private Normaliser normaliser = new Normaliser();
	@FXML private TextField delimeter;
	@FXML private Button bSelectFile;
	
	@FXML
	private void initialize() {
		System.out.println("Import Data Screen Loaded");
		Model.setAdvancedDisabled(true);
		delimeter.setText(",");
	}
	

	@FXML
	void loadFile(ActionEvent event) {
		Model.setUsingExample(false);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a file");
		File file = fileChooser.showOpenDialog(new Stage());
		if (file != null) {
			String delim = delimeter.getText();
			try {
				fileParser = new FileParser(file.getAbsolutePath(),	delim);
			} catch (FileNotFoundException e) {}
			Model.setFileParser(fileParser);
			String[][] strings = fileParser.getParsed(); 
			Model.setDataStrings(strings);
			normaliser = new Normaliser(strings);
			Model.setNormaliser(normaliser);
			VistaNavigator.loadVista(VistaNavigator.InputAndOutputSelection);
		}
	}

	@FXML
	void exampleData() {
		Model.setUsingExample(true);
		VistaNavigator.loadVista(VistaNavigator.BasicAlgorithmSelection);
	}
	
	@FXML 
	void delimeterFieldChange(KeyEvent event){
		if(delimeter.getText().isEmpty()){
			bSelectFile.setDisable(true);
		} else {
			bSelectFile.setDisable(false);
		}
	}
}
