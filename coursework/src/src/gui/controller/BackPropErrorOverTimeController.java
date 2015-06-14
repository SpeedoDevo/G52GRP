package gui.controller;

import gui.Model;

import java.util.ArrayList;

import backprop.CreateAndTrainBP;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class BackPropErrorOverTimeController {

	@FXML public ScatterChart<Integer, Float> chart;
	
	public void initialize() {
		Model.setAdvancedDisabled(true);
		chart.getXAxis().setLabel("Epochs");
		chart.getYAxis().setLabel("Percentage error");
		
		XYChart.Series<Integer,Float> series = new Series<Integer, Float>();
        
		ArrayList<Float> data = new CreateAndTrainBP().getErrorOverEpochs();
		for (int i = 0; i < data.size(); i++) {
			series.getData().add(new Data<Integer, Float>(i, data.get(i)));
		}
		
		chart.getData().add(series);
		
		chart.legendVisibleProperty().set(false);
	}

}
