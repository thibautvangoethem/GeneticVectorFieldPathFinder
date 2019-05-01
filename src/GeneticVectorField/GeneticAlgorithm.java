package GeneticVectorField;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javafx.geometry.Rectangle2D;

public class GeneticAlgorithm {
	
	private ArrayList<Entity> Entities;
	
	private ArrayList<Rectangle2D> Obstructions;
	
	private Rectangle2D End;
	
	private int Population;
	
	private double MutationRate;
	
	private int AmountOfVectors;
	
	private int Generation;
	
	public GeneticAlgorithm(int amountOfVectors,int population,double mutationRate) {
		this.Population=population;
		this.AmountOfVectors=amountOfVectors;
		this.MutationRate=mutationRate;
		this.Generation=0;
		for(int i =0;i<population;i++) {
			Entities.add(new Entity(0,(int)(amountOfVectors/2),new Vector2D(1,0)));
		}
	}
	

}
