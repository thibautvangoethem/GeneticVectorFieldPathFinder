/**
 * this file only contains the Entity class
 * @author thibaut Van Goethem
 */
package geneticVectorField;

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
	
	/**
	 * creates an Entity instance with all vectors set to random
	 * @param xpos 
	 * @param ypos
	 */
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
	
	/**
	 * creates an entitiy instance with all vectors set to the given vector
	 * @param xpos
	 * @param ypos
	 * @param v the vector that will be used in all positions
	 */
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
	
	/**
	 * creates an instance of an entity with the vectorfield given
	 * @param xpos
	 * @param ypos
	 * @param v the entire vectorfield
	 */
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
	
	/**
	 * updates the acceleration of this entity by looking into the vectorfield and interpolating the surrounding vectors
	 */
	public void updateAcceleration() {
		try {
			Acceleration=this.Field.getInterpolatedVector(this.Position.getX(), this.Position.getY());
		}catch(Exception e) {
			this.Finished=true;
		}
	}
	
	/**
	 * updates  the acceleration/speed and position of the entity accordingly
	 * @param amount the amount of time it must be updated with 
	 */
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
			this.Score=1/Math.pow(distance+1,2);
		}else {
			this.Score=1/(distance+1);
		}
		
	}

	/**
	 * getter of the current position
	 * @return a Point2D object
	 */
	public Point2D getPosition() {
		return Position;
	}

	/**
	 * setter for the current position
	 * @param position a Point2D object
	 */
	public void setPosition(Point2D position) {
		Position = position;
	}

	/**
	 * getter for the acceleration
	 * @return a Vector2D object
	 */
	public Vector2D getAcceleration() {
		return Acceleration;
	}

	/**
	 * getter for the x speed/velocity
	 * @return a double
	 */
	public double getXSpeed() {
		return XSpeed;
	}

	/**
	 * getter for the y speed/velocity
	 * @return a double
	 */
	public double getYSpeed() {
		return YSpeed;
	}

	/**
	 * getter for the vectorField
	 * @return a vectorField object
	 */
	public VectorField getField() {
		return Field;
	}

	/**
	 * getter for the finished bool
	 * @return a boolean
	 */
	public boolean isFinished() {
		return Finished;
	}
	
	/**
	 * makes the entity win
	 */
	public void win() {
		this.Finished=true;
		this.GotToEnd=true;
	}
	
	/**
	 * makes the entity stop
	 */
	public void stop() {
		this.Finished=true;
	}
	
	/**
	 * getter for the current stored score
	 * @return a double
	 */
	public double getScore() {
		return Score;
	}
	
	

}
