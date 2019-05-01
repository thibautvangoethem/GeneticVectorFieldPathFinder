/**
 * this file only contains the Entity class
 * @author thibaut Van Goethem
 */
package GeneticVectorField;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javafx.geometry.Point2D;


/**
 * 
 * @author thibaut Van Goethem
 * an object of this class contains an acceleration vector, a xspeed , a yspeed and a position. These are used to simulate the movement over the screen
 * it also contains a vectorfield which will be used to update the acceleration accordingly
 */
public class Entity {
	private Vector2D Acceleration;
	
	private double XSpeed;
	
	private double YSpeed;
	
	private Point2D Position;
	
	private VectorField Field;
	
	private double Score;
	
	boolean Finished; 
	
	boolean GotToEnd;
	
	public Entity(double xpos,double ypos) {
		this.Score=0;
		this.Position=new Point2D(xpos,ypos);
		this.Acceleration=new Vector2D(0,0);
		this.XSpeed=0;
		this.YSpeed=0;
		this.GotToEnd=false;
		this.Field=new VectorField(50,50);
		this.Finished=false;
	}
	
	public Entity(double xpos,double ypos,Vector2D v) {
		this.Score=0;
		this.Position=new Point2D(xpos,ypos);
		this.Acceleration=new Vector2D(0,0);
		this.XSpeed=0;
		this.YSpeed=0;
		this.GotToEnd=false;
		this.Field=new VectorField(50,50,v.normalize());
		this.Finished=false;
		this.updateAcceleration();
	}
	
	public Entity(double xpos,double ypos, VectorField  v) {
		this.Score=0;
		this.Position=new Point2D(xpos,ypos);
		this.Acceleration=new Vector2D(0,0);
		this.XSpeed=0;
		this.YSpeed=0;
		this.GotToEnd=false;
		this.Field=v;
		this.Finished=false;
		this.updateAcceleration();
	}
	
	public void updateAcceleration() {
		try {
			Acceleration=this.Field.getInterpolatedVector(this.Position.getX(), this.Position.getY());
		}catch(Exception e) {
			this.Finished=true;
		}
	}
	
	public void updatePosition(double amount) {
		if(!this.Finished) {
			this.updateAcceleration();
			this.XSpeed+=amount*this.Acceleration.getX();
			this.YSpeed+=amount*this.Acceleration.getY();
			this.Position=new Point2D(this.Position.getX()+(amount*XSpeed),this.Position.getY()+(amount*YSpeed));
		}
	}
	
	/**
	 * this function calculates the score of this entities, which is just an inverted distance^2 and distance^3 if the entities got to the end
	 * @param end the end point
	 */
	public void calculateScore(Point2D end) {
		double distance = Math.sqrt((end.getY() - this.Position.getY()) * (end.getY() - this.Position.getY()) + (end.getX() - this.Position.getX()) * (end.getX() - this.Position.getX()));
		if(!this.GotToEnd) {
			this.Score=1/(Math.pow(distance, 2));
		}else {
			this.Score=1/(Math.pow(distance, 3));
		}
	}

	public Point2D getPosition() {
		return Position;
	}

	public void setPosition(Point2D position) {
		Position = position;
	}

	public Vector2D getAcceleration() {
		return Acceleration;
	}

	public double getXSpeed() {
		return XSpeed;
	}

	public double getYSpeed() {
		return YSpeed;
	}

	public VectorField getField() {
		return Field;
	}

	public boolean isFinished() {
		return Finished;
	}
	
	public void stop() {
		this.Finished=true;
		this.GotToEnd=true;
	}
	
	
	public double getScore() {
		return Score;
	}

	public void reset() {
		this.Field=new VectorField(50,50);
		this.Position=new Point2D(0,25);
		this.Finished=false;
		this.XSpeed=0;
		this.YSpeed=0;
	}
	
	

}
