package application;
import GeneticVectorField.Entity;
import GeneticVectorField.GeneticAlgorithm;
import GeneticVectorField.VectorField;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class JavaFXGeneticAlgorithm extends GeneticAlgorithm{
	
	Scene scene;
	
	Group root;

	public JavaFXGeneticAlgorithm(int amountOfVectors, int population, double mutationRate,double time,Scene scene,Group group) {
		super(amountOfVectors, population, mutationRate,time);
		this.scene=scene;
		this.root=group;
	}

	public void draw() {
		this.drawEnd();
		this.drawObstructions();
		for(Entity i:this.getEntities()) {
			this.drawEntity(i);
		}
		if(this.getHighestScore()!=null) {
			this.drawVectorField(this.getHighestScore());
		}
		
		Text gen = new Text (scene.getWidth()*.70, 20, "Generation:"+this.getGeneration());
		gen.setFont(Font.font ("Verdana", 20));
		this.root.getChildren().add(gen);
		
	}
	
	private void drawEnd() {
		double XIncrement=scene.getWidth()/50;
		double YIncrement=scene.getHeight()/50;
		Rectangle2D end=this.getEnd();
		
		Rectangle graphicend=new Rectangle(end.getMinX()*XIncrement,scene.getHeight()-end.getMaxY()*YIncrement,end.getWidth()*XIncrement,end.getHeight()*YIncrement);
		graphicend.setFill(Color.GREEN);
		
		root.getChildren().add(graphicend);
		
	}
	
	private void drawObstructions() {
		double XIncrement=scene.getWidth()/50;
		double YIncrement=scene.getHeight()/50;
		
		for(Rectangle2D r:this.getObstructions()) {
			Rectangle obstruction=new Rectangle(r.getMinX()*XIncrement,scene.getHeight()-r.getMaxY()*YIncrement,r.getWidth()*XIncrement,r.getHeight()*YIncrement);
			obstruction.setFill(Color.RED);
			
			root.getChildren().add(obstruction);
		}
	}
	
	
	private void drawVectorField(Entity e) {
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
	
	private void drawEntity(Entity e) {
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
}
