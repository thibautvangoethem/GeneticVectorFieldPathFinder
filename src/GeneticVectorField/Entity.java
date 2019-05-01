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
	
	boolean Finished; 
	
	public Entity(double xpos,double ypos) {
		this.Position=new Point2D(xpos,ypos);
		this.Acceleration=new Vector2D(0,0);
		this.XSpeed=0;
		this.YSpeed=0;
		this.Field=new VectorField(50,50);
		this.Finished=false;
	}
	
	public Entity(double xpos,double ypos,Vector2D v) {
		this.Position=new Point2D(xpos,ypos);
		this.Acceleration=new Vector2D(0,0);
		this.XSpeed=0;
		this.YSpeed=0;
		this.Field=new VectorField(50,50,v.normalize());
		this.Finished=false;
		this.updateAcceleration();
	}
	
	public Entity(double xpos,double ypos, VectorField  v) {
		this.Position=new Point2D(xpos,ypos);
		this.Acceleration=new Vector2D(0,0);
		this.XSpeed=0;
		this.YSpeed=0;
		this.Field=v;
		this.Finished=false;
		this.updateAcceleration();
	}
	
	public void updateAcceleration() {
		try {
			Acceleration=this.Field.getInterpolatedVector(this.Position.getX(), this.Position.getY());
		}catch(Exception e) {
//			this.Position=new Point2D(0,25);
//			this.XSpeed=0;
//			this.YSpeed=0;
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
	
	public void reset() {
		this.Field=new VectorField(50,50);
		this.Position=new Point2D(25,25);
		this.Finished=false;
		this.XSpeed=0;
		this.YSpeed=0;
	}
	
	

}
