package gui.controller;

import gui.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import art1.CreateAndTrainART;

public class ArtResultsController {

	CreateAndTrainART art1 = new CreateAndTrainART();
	
    TitledPane[] tps;
    @FXML Accordion clusters;
    @FXML TextArea rowDataText;
	
	public void initialize() {
		int max = 0;
		
		Model.setAdvancedDisabled(true);
		
		int[] members = art1.getMemberships();
		
		rowDataText.setEditable(false);
		
		for (int i = 0; i < members.length; i++) {
			if (members[i] > max) max = members[i];
		}
		
		tps = new TitledPane[max];
		
		for (int i = 0; i < tps.length; i++) {
			ObservableList<String> clusterElements = FXCollections.observableArrayList();
			tps[i] = new TitledPane();
			
			int memberCount = 0;
			for (int j = 0; j < members.length; j++) {
				if (members[j] == i) {
					clusterElements.add("Row " + j);
					memberCount++;
				}
			}
			tps[i].setText("Cluster " + (i + 1) + " (number of members: " + memberCount + ")");
			
			ListView<String> clusterList = new ListView<String>();
			clusterList.setPrefHeight(100);
			clusterList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    	changeDataOutput(newValue);
			    }
			    
			});
			
			clusterList.setItems(clusterElements);
			tps[i].setContent(clusterList);
			tps[i].setPrefHeight(100);
			
		}
		clusters.getPanes().addAll(tps);
		
		
	}
	
	private void changeDataOutput(String label) {
		label = label.substring(4);
		int row = Integer.parseInt(label);
		int patternLength = Model.getTraining()[row].length;
		int[] output = new int[patternLength]; 
		
		for (int i = 0; i < Model.getTraining()[row].length; i++) {
			output[i] = Math.round(Model.getTraining()[row][i]);
		}
		
		rowDataText.setText(dataToString(output));
	}
	
	private String dataToString(int[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			sb.append(data[i] == 1 ? "#" : ".");
		}
		return sb.toString();
	}
	
	@FXML
	public void goToInputAndOutputSelection() {
		VistaNavigator.loadVista(VistaNavigator.InputAndOutputSelection);
	}
	
	@FXML
	public void goToImportData() {
		VistaNavigator.loadVista(VistaNavigator.ImportData);
	}
}
