package gui.controller;

import gui.Model;
import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import som.KLattice;
import som.SOM;

public class SOMResultsController {
	
	@FXML public GridPane histogram;
	
	@FXML
	private void initialize() {
		Model.setAdvancedDisabled(true);
		
		KLattice lattice = new SOM().getLattice();
		int nodeSizeG = new SOM().getNodeSize();

	
		if (nodeSizeG == 3) {

			for (int x = 0; x < lattice.getWidth(); x++) {
				for (int y = 0; y < lattice.getHeight(); y++) {
					
							
					Color color = Color.rgb((int) lattice.getNode(x, y).getWeight(0),
											(int) lattice.getNode(x, y).getWeight(1),
											(int) lattice.getNode(x, y).getWeight(2));
	
					Region pixel = new Region();
					pixel.setBackground(new Background(new BackgroundFill(color, null, null)));
					GridPane.setVgrow(pixel, Priority.ALWAYS);
					GridPane.setHgrow(pixel, Priority.ALWAYS);
					histogram.add(pixel, x, y);
					
				}
			}
		} else {
			double dist = 0, tempDist = 0;
			for (int x = 0; x < lattice.getWidth(); x++) {
				for (int y = 0; y < lattice.getHeight(); y++) {
					for (int q = 0; q < lattice.getNode(x, y).getNodeSize(); q++) {

						dist += (Model.getTraining()[0][q] - lattice.getNode(x, y)
								.getWeight(q))
								* (Model.getTraining()[0][q] - lattice.getNode(x, y)
										.getWeight(q));

						tempDist = Math.sqrt(dist);

					}
					dist = 0;
					
					
					Color color = Color.GREEN;
					
					if (tempDist < 4) {
						color = Color.GREEN;
					} else if (tempDist > 4 && tempDist < 15) {
						color = Color.LIMEGREEN;
					} else if (tempDist > 15 && tempDist < 30) {
						color = Color.RED;
					} else if (tempDist > 30) {
						color = Color.DARKRED;
					}
					
					Region pixel = new Region();
					pixel.setBackground(new Background(new BackgroundFill(color, null, null)));
					GridPane.setVgrow(pixel, Priority.ALWAYS);
					GridPane.setHgrow(pixel, Priority.ALWAYS);
					histogram.add(pixel, x, y);
					
				}
			}
		}
		
	}
}
