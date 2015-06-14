package gui.controller;

import gui.Model;

import java.util.Arrays;

import commons.FileParser;
import commons.Normaliser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class AdvancedInputAndOutputSelectionController {
	Normaliser norm = new Normaliser();
	FileParser fp = Model.getFileParser();
	boolean tutorial = Model.isTutorialDisabled();
	@FXML
	private Button changeFile;
	@FXML
	private TableView<String[]> table;
	@FXML
	private Slider slider;
	@FXML
	private ComboBox<String> stringNumRowSelection;
	@FXML
	private CheckBox stringOrNum;
	
	private boolean[] columnsThatAreStrings;

	@FXML
	private void initialize() {
		
		System.out.println("Input And Output Screen Loaded");
		Model.setAdvancedDisabled(false);

		if( tutorial == false){
		}
		//load data
		String[][] strings = Model.getDataStrings();
		

		columnsThatAreStrings = new boolean[strings[0].length];
		Arrays.fill(columnsThatAreStrings, true);
		
		//add data to tableview
		ObservableList<String[]> observableStrings = FXCollections.observableArrayList(); // Sets a observable list
		table.setItems(observableStrings); // Displays all the data
		observableStrings.addAll(Arrays.asList(strings));


		for (int i = 0; i < strings[0].length; i++) {
			stringNumRowSelection.getItems().add(fp.hasHeader() ? fp.getHeaders()[i] : "Col" + i);
		}
		
		stringNumRowSelection.getSelectionModel().select(0);

		
		for (int i = 0; i < strings[0].length; i++) {
			String header = fp.hasHeader() ? fp.getHeaders()[i] + " (S)" : "Col" + i + " (S)";
			
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
	void updateCheckBox() {
		String selected = stringNumRowSelection.getSelectionModel().getSelectedItem();
		for (int i = 0; i < columnsThatAreStrings.length; i++) {
			if (selected.contains("Col" + i) || (fp.hasHeader() && selected.contains(fp.getHeaders()[i]))) {
				stringOrNum.setSelected(columnsThatAreStrings[i]);
			}
		}
	}
	
	@FXML
	void toggleString() {
		for (int i = 0; i < columnsThatAreStrings.length; i++) {
			String selected = stringNumRowSelection.getSelectionModel().getSelectedItem();
			if (selected.contains(fp.getHeaders()[i]) || selected.equals("Col" + i)) {
				columnsThatAreStrings[i] = stringOrNum.isSelected();
				if (fp.hasHeader()) {
					table.getColumns().get(i).setText(columnsThatAreStrings[i] ? fp.getHeaders()[i] + " (S)" : fp.getHeaders()[i] + " (N)");
				} else {
					table.getColumns().get(i).setText(columnsThatAreStrings[i] ? "Col" + i + " (S)" : "Col" + i + " (N)");
				}
			}
		}
	}

	@FXML
	void goToImportData(ActionEvent event) {
		VistaNavigator.loadVista(VistaNavigator.ImportData);
	}

	@FXML
	void goToInputAndOutputSelection(ActionEvent event) {
		VistaNavigator.loadVista(VistaNavigator.InputAndOutputSelection);
	}

	@FXML
	void goToAdvancedSelection(ActionEvent event) {
		norm.setFromString(columnsThatAreStrings);
		norm.setAdvanced(true);
		Model.setTrainingDataWidth((int) slider.getValue());
		VistaNavigator.loadVista(VistaNavigator.AdvancedAlgorithmSelection);
	}

}
