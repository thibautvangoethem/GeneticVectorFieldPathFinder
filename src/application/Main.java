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
			 
			this.drawVectorField(scene, root, e);
			this.drawEntity(scene, root, e);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//timer that will advance the entities and redraw the scene
			new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	            	if(e.isFinished()) {
	            		e.reset();
	            	}
	            	root.getChildren().clear();
	            	e.updatePosition(0.05);
	            	drawVectorField(scene, root, e);
	            	drawEntity(scene,root,e);       
	            	
	            }
	        }.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void drawVectorField(Scene scene,Group root,Entity e) {
		VectorField Field=e.getField();

		double XIncrement=scene.getWidth()/50;
		double YIncrement=scene.getHeight()/50;

		for(int i=0;i<50;i++) {
			for(int j=0;j<50;j++) {
				int startX=(int) (i*XIncrement);
				int startY=(int) (scene.getHeight()-(j*YIncrement));
				int endX=(int) (startX+Field.getSingleVector(i, j).getX()*XIncrement/2);
				int endY=(int) (startY-Field.getSingleVector(i, j).getY()*YIncrement/2);
				Line l=new Line(startX,startY,endX,endY);
				root.getChildren().add(l);
			}
		}

	}
	
	public void drawEntity(Scene scene,Group root,Entity e) {
		Point2D p=e.getPosition();
		
		final double arrowLength = 20;
	    final double arrowWidth = 5;
		
		double XWay=e.getXSpeed()==0 ? 0 : e.getXSpeed();
		double YWay=e.getYSpeed()==0 ? 0 : e.getYSpeed();
		
		double XIncrement=scene.getWidth()/50;
		double YIncrement=scene.getHeight()/50;
		
		//drawing of arrow found at https://stackoverflow.com/questions/41353685/how-to-draw-arrow-javafx-pane
		double ex = p.getX()*XIncrement;;
        double ey = scene.getHeight()- p.getY()*YIncrement;
        double sx =  p.getX()*XIncrement-XWay*XIncrement;
        double sy = ey+YWay*YIncrement;
        double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
        double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

        // part in direction of main line
        double dx = (sx - ex) * factor;
        double dy = (sy - ey) * factor;

        // part ortogonal to main line
        double ox = (sx - ex) * factorO;
        double oy = (sy - ey) * factorO;
        
        
        double x1=ex;
        double y1=ey;
        double x2=(ex + dx - oy);
        double y2=(ey + dy + ox);
        double x3=(ex + dx + oy);
        double y3=(ey + dy - ox);
        
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
            x1,y1,
            x2,y2,
            x3,y3});

        root.getChildren().add(triangle);

        }
	
	public static void main(String[] args) {
		launch(args);
	}
}
