package gui.controller;

import gui.Model;

import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import commons.FileParser;
import commons.Normaliser;

public class InputAndOutputSelectionController {
	Normaliser norm = new Normaliser();
	boolean tutorial = Model.isTutorialDisabled();
	@FXML
	private Button changeFile;
	@FXML
	private TableView<String[]> table;
	@FXML
	private Slider slider;

	@FXML
	private void initialize() {
		System.out.println("Input And Output Screen Loaded");
		Model.setAdvancedDisabled(false);

		//load data
		String[][] strings = Model.getDataStrings();
		
		//add data to tableview
		ObservableList<String[]> observableStrings = FXCollections.observableArrayList(); // Sets a observable list
		table.setItems(observableStrings); // Displays all the data
		observableStrings.addAll(Arrays.asList(strings));

		FileParser fp = Model.getFileParser();


		
		for (int i = 0; i < strings[0].length; i++) {
			String header = fp.hasHeader() ? fp.getHeaders()[i] : "Col" + i;
			
			TableColumn<String[], String> tableCol = new TableColumn<String[], String>(header);
			
			final int colNo = i;
			tableCol.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
				@Override
				// Creates the table column
				public ObservableValue<String> call(CellDataFeatures<String[], String> cellData) {
					return new SimpleStringProperty((cellData.getValue()[colNo]));
				}
			});
			
			table.getColumns().add(tableCol); 
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		}


		slider.setMax(fp.getParsed()[0].length);

		slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				ObservableList<TableColumn<String[], ?>> cols = table.getColumns();
				for (int j = 0; j < cols.size(); j++) {
					TableColumn<String[], ?> col = cols.get(j);
					if (j < slider.getValue()) {
						col.getStyleClass().remove("bold");
					} else {
						col.getStyleClass().add("bold");
					}
				}
			}
		});
		
		slider.valueProperty().set(observableStrings.get(0).length - 1);
	}

	@FXML
	void goBasicAlgorithmSelection(ActionEvent event) {
		norm.setAdvanced(false);
		Model.setTrainingDataWidth((int) slider.getValue());
		VistaNavigator.loadVista(VistaNavigator.BasicAlgorithmSelection);
	}

}
