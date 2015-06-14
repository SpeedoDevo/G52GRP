package gui.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import rbf.CreateAndTrainRBF;
import rbf.RbfNetwork;
import gui.Model;
import commons.FileParser;
import commons.HelperMethods;
import commons.Normaliser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class RbfResultsController {

	@FXML public TableView<String[]> table;
	@FXML public Label clusters;
	@FXML public Label seconds;
	private ObservableList<String[]> observableStrings;
	
	public void initialize() {
		Model.setAdvancedDisabled(true);
		
		String[][] original = Model.getDataStrings();
		observableStrings = FXCollections.observableArrayList();

		for (int i = 0; i < 3; i++) {
			int row = (int) HelperMethods.random(1, original.length);
			observableStrings.add(original[row]);
			runOnNetwork(i);
		}

		table.setItems(observableStrings); // Displays all the data
		
		
		FileParser fp = Model.getFileParser();
		
		for (int i = 0; i < original[0].length; i++) {
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
			
			if (i < Model.getTrainingDataWidth()) {
				tableCol.setCellFactory(TextFieldTableCell.forTableColumn());
				
				tableCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<String[],String>>() {
					
					@Override
					public void handle(CellEditEvent<String[], String> event) {
						TablePosition<String[], String> p = event.getTablePosition();
						observableStrings.get(p.getRow())[p.getColumn()] = event.getNewValue();
						runOnNetwork(p.getRow());
					}
				});				
			}
			
			table.getColumns().add(tableCol); 
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		}
		
		CreateAndTrainRBF rbf = new CreateAndTrainRBF();
		clusters.setText("" + rbf.getNeuralNet().getLayers()[1].getSize());
		seconds.setText(Long.toString(rbf.getDuration()));
	}
	
	private void runOnNetwork(int i) {
		Normaliser normaliser = Model.getNormaliser();

		float[] norm = normaliser.normaliseOne(Arrays.copyOf(observableStrings.get(i), Model.getTrainingDataWidth()));
		RbfNetwork nn = new CreateAndTrainRBF().getNeuralNet();
		nn.feedInput(norm);
		nn.feedForward();

		
		String[] out = normaliser.denormalise(nn.getOutput());
		
		for (int j = 0; j < out.length; j++) {
			String[] temp = observableStrings.get(i).clone();  
			temp[j + Model.getTrainingDataWidth()] = out[j];
			observableStrings.set(i, temp);
		}

		System.out.print(Arrays.toString(nn.getOutput()) + " -> ");
		System.out.println(Arrays.toString(out));
	}
	
	@FXML
	public void addRow() {
		String[][] original = Model.getDataStrings();

		int row = (int) HelperMethods.random(1, original.length);
		observableStrings.add(original[row]);
		
		runOnNetwork(observableStrings.size() -1);

	}


	@FXML
	public void save() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose location to save");
		
		File choosen = fileChooser.showSaveDialog(new Stage());
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
			        new FileOutputStream(choosen));
			
			objectOutputStream.writeObject(new CreateAndTrainRBF());
			objectOutputStream.writeObject(new Model());
			
			objectOutputStream.close();
		} catch (Exception e) {
			
		}
	}
	
	@FXML
	public void goToImportData() {
		VistaNavigator.loadVista(VistaNavigator.ImportData);
	}
}
