package application;

import GeneticVectorField.Entity;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Entity e=new Entity(25,25);
			e.updatePosition(1);
			Pane root = new Pane();
			Group drawBoard = new Group();
			root.getChildren().add(drawBoard);
			Scene scene = new Scene(root,1000,1000);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			 
			JavaFXGeneticAlgorithm algo=new JavaFXGeneticAlgorithm(50,100,0.05,100,scene,drawBoard);
			algo.addObstruction(23, 15, 27, 45);
			algo.update(0.1);
			algo.draw();
			
			Button button = new Button("Accept");
			button.setText("reset");
			button.setWrapText(true);
			button.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	algo.reset();
			    }
			});
			
			root.setOnMouseClicked(new EventHandler<MouseEvent>() {
				 @Override
				    public void handle(MouseEvent event) {
				        algo.click(event.getSceneX(), event.getSceneY());
				    }
			});
			root.getChildren().add(button);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//timer that will advance the entities and redraw the scene
			new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	            	drawBoard.getChildren().clear();
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
