/**
 * this file contains the class JavaFXGeneticAlogithm which is the javafx graphics implementation of the geneteic alogrithm
 * this inherits from the GeneticAlgorithm class
 * @see{@link GeneticVectorField.GeneticALgorithm}
 * @author thibaut Van Goethem
 */

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
	
	Point2D ClickPoint1;
	
	Point2D ClickPoint2;

	/**
	 * creates an instance of this class
	 * @param amountOfVectors the vectorgield size
	 * @param population the population for a single generation
	 * @param mutationRate the mutationRate for creating new children
	 * @param time the time each generation gets
	 * @param scene the javafx scene (only used to get screenSize) @todo change this so it isnt needed
	 * @param group the JavaFX Group that is used to draw in
	 */
	public JavaFXGeneticAlgorithm(int amountOfVectors, int population, double mutationRate,double time,Scene scene,Group group) {
		super(amountOfVectors, population, mutationRate,time);
		this.scene=scene;
		this.root=group;
		this.ClickPoint1=null;
		this.ClickPoint2=null;
	}

	/**
	 * draws the entire application on the javaFX Group
	 */
	public void draw() {
		this.drawEnd();
		this.drawObstructions();
		for(Entity i:this.getEntities()) {
			this.drawEntity(i);
		}
		if(this.getHighestScore()!=null) {
			this.drawVectorField(this.getHighestScore());
		}
		
		//this is the generation text at the top right
		Text gen = new Text (scene.getWidth()*.70, 20, "Generation:"+this.getGeneration());
		gen.setFont(Font.font ("Verdana", 20));
		this.root.getChildren().add(gen);
		
	}
	
	/**
	 * performs a click at the screen, after 2 clicks a obstruction is added
	 * @param i x pos of the click
	 * @param j y pos of the click
	 */
	public void click(double i,double j) {
		if(this.ClickPoint1==null) {
			this.ClickPoint1=new Point2D(i,j);
		}else if(this.ClickPoint2==null) {
			this.ClickPoint2=new Point2D(i,j);
		}
		if(this.ClickPoint1!=null && this.ClickPoint2!=null) {
			this.addObstruction();
		}
	}
	
	/**
	 * adds a rectangular obstruction, this happens after 2 clicks
	 */
	private void addObstruction() {
		double XIncrement=scene.getWidth()/50;
		double YIncrement=scene.getHeight()/50;
		
		//this is so you can click everywhere in any order
		//it the highest x value in x1 and lowest in y1(this is actually highest value as y starts at top)
		//and the lowest x and highest gets put into x2 and y2
		double x1=this.ClickPoint1.getX()>this.ClickPoint2.getX() ? this.ClickPoint1.getX():this.ClickPoint2.getX();
		double y1=this.ClickPoint1.getY()<this.ClickPoint2.getY() ? this.ClickPoint1.getY():this.ClickPoint2.getY();
		double x2=this.ClickPoint1.getX()<this.ClickPoint2.getX() ? this.ClickPoint1.getX():this.ClickPoint2.getX();
		double y2=this.ClickPoint1.getY()>this.ClickPoint2.getY() ? this.ClickPoint1.getY():this.ClickPoint2.getY();
		
		//translating the javaFX coordinates to my own coordinate system
		x1=x1/XIncrement;
		x2=x2/XIncrement;
		
		y1=(scene.getHeight()-y1)/YIncrement;
		y2=(scene.getHeight()-y2)/YIncrement;
		
		this.addObstruction(x2, y2, x1, y1);
		
		this.ClickPoint1=null;
		this.ClickPoint2=null;
		
	}
	
	/**
	 * this function draws the end square in green
	 */
	private void drawEnd() {
		double XIncrement=scene.getWidth()/50;
		double YIncrement=scene.getHeight()/50;
		Rectangle2D end=this.getEnd();
		
		Rectangle graphicend=new Rectangle(end.getMinX()*XIncrement,scene.getHeight()-end.getMaxY()*YIncrement,end.getWidth()*XIncrement,end.getHeight()*YIncrement);
		graphicend.setFill(Color.GREEN);
		
		root.getChildren().add(graphicend);
		
	}
	
	/**
	 * this function draws the obstruction in red 
	 */
	private void drawObstructions() {
		double XIncrement=scene.getWidth()/50;
		double YIncrement=scene.getHeight()/50;
		
		for(Rectangle2D r:this.getObstructions()) {
			Rectangle obstruction=new Rectangle(r.getMinX()*XIncrement,scene.getHeight()-r.getMaxY()*YIncrement,r.getWidth()*XIncrement,r.getHeight()*YIncrement);
			obstruction.setFill(Color.RED);
			
			root.getChildren().add(obstruction);
		}
	}
	
	/**
	 * this function draws the entire vectorfield of an entity
	 * each vector will start at it position and will have a size of half the distance between 2 vectors
	 * @param e the entity that it will draw the field for
	 */
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
	
	/**
	 * draws an entity as a triangle/arrow
	 * @param e the entity
	 */
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

        // part orthogonal to main line
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
