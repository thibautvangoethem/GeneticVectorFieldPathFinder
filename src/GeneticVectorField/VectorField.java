/**
 * @author Thibaut Van Goethem
 * this file contains the implementation of a vectorfield, this is just an arraylist of and arraylist of vector2ds with some additional functioanlity
 */
package GeneticVectorField;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * the field is represented with (0,0) being at the lower left and i being the x-axis/ j being the y-axis
 * 
 */
public class VectorField {
	ArrayList<ArrayList<Vector2D> > Field;
	
	/**
	 * creates an instance of the vectorfield object with the field empty
	 */
	public VectorField() {
		this.Field=new ArrayList<>();
	}
	
	public VectorField(ArrayList<ArrayList<Vector2D> > field) {
		this.Field=field;
	}
	
	/**
	 * create a vectorfield with the vectors set to random size
	 * @param i the x size
	 * @param j the y size
	 */
	public VectorField(int i, int j) {
		this.Field=new ArrayList<>();
		Random r = new Random();
		for(int xsize=0;xsize<i;xsize++) {
			ArrayList<Vector2D> row =new ArrayList<>();
			for(int ysize=0;ysize<j;ysize++) {
				double x =  ThreadLocalRandom.current().nextDouble(-1, 1);
				double y =  ThreadLocalRandom.current().nextDouble(-1, 1);
				row.add(new Vector2D(x,y).normalize());
			}
			this.Field.add(row);
		}
	}
	
	/**
	 * create a vectorfield with given size of a given vectors
	 * @param i
	 * @param j
	 * @param v
	 */
	public VectorField(int i, int j,Vector2D v) {
		this.Field=new ArrayList<>();
		for(int xsize=0;xsize<i;xsize++) {
			ArrayList<Vector2D> row =new ArrayList<>();
			for(int ysize=0;ysize<j;ysize++) {
				row.add(new Vector2D(v.getX(),v.getY()));
			}
			this.Field.add(row);
		}
	}
	
	public void setVector(int i,int j,Vector2D v) {
		Field.get(i).set(j, v);
	}
	
	public Vector2D getSingleVector(int i,int j) {
		return Field.get(i).get(j);
	}
	
	public Vector2D getInterpolatedVector(double i,double j) {
		//getting all the surrounding vectors out of the Field and also calculating the distance of the vurrent point to the point where the vector is projected
		Vector2D upperLeft=Field.get((int)Math.floor(i)).get((int)Math.ceil(j));
		double distanceUL=calculateDistanceBetweenPoints(i,j,Math.floor(i),Math.ceil(j));
		Vector2D lowerLeft=Field.get((int)Math.floor(i)).get((int)Math.floor(j));
		double distanceLL=calculateDistanceBetweenPoints(i,j,Math.floor(i),Math.floor(j));
		Vector2D upperRight=Field.get((int)Math.ceil(i)).get((int)Math.ceil(j));
		double distanceUR=calculateDistanceBetweenPoints(i,j,Math.ceil(i),Math.ceil(j));
		Vector2D lowerRight=Field.get((int)Math.ceil(i)).get((int)Math.floor(j));
		double distanceLR=calculateDistanceBetweenPoints(i,j,Math.ceil(i),Math.floor(j));
		
		//testing if one of the distances are 0, as that would mean the point is on a vector and there are no surrounding vectors
		//doesnt really need to be upperlLeft vector as all vectors should be the same
		if(distanceUL==0) {
			return upperLeft;
		}
		
		//to create the new vector I use the inverse distance to make closer vectors have more of an effect
		double xResult=(1/distanceUL)*upperLeft.getX()+(1/distanceLL)*lowerLeft.getX()+(1/distanceUR)*upperRight.getX()+(1/distanceLR)*lowerRight.getX();
		double yResult=(1/distanceUL)*upperLeft.getY()+(1/distanceLL)*lowerLeft.getY()+(1/distanceUR)*upperRight.getY()+(1/distanceLR)*lowerRight.getY();
		Vector2D result=new Vector2D(xResult,yResult);
		
		//normalize the vector to make sure it doesnt give a crazy high value
		result=result.normalize();
		return result;
		
	}
	
	static private double calculateDistanceBetweenPoints(double x1,double y1,double x2,double y2) {
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
	

}
