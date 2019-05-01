package application;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import GeneticVectorField.Entity;
import GeneticVectorField.VectorField;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Entity e=new Entity(25,25);
			e.updatePosition(1);
			Group root = new Group();
			Scene scene = new Scene(root,1000,1000);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			 
			JavaFXGeneticAlgorithm algo=new JavaFXGeneticAlgorithm(50,100,0.05,20,scene,root); 
			algo.update(0.1);
			algo.draw();
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//timer that will advance the entities and redraw the scene
			new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	            	root.getChildren().clear();
	            	algo.update(0.10);
	            	algo.draw();
	            	    
	            	
	            }
	        }.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
