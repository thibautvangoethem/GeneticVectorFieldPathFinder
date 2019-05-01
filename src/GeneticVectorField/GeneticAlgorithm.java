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
	
	private double MaxTime;
	
	private double CurrentTime;
	
	
	public GeneticAlgorithm(int amountOfVectors,int population,double mutationRate,double maxtime) {
		this.Entities=new ArrayList<>();
		this.Population=population;
		this.AmountOfVectors=amountOfVectors;
		this.MutationRate=mutationRate;
		this.Generation=0;
		this.MaxTime=maxtime;
		this.CurrentTime=0;
		for(int i =0;i<population;i++) {
			this.Entities.add(new Entity(0,(int)(amountOfVectors/2)));
		}
	}
	
	public void update(double time) {
		for(Entity i:this.Entities) {
			i.updatePosition(time);
		}
		this.CurrentTime+=time;
		if(this.CurrentTime>this.MaxTime) {
			this.reset();
			this.CurrentTime=0;
		}
	}
	
	public void reset() {
		for(Entity i:this.Entities) {
			i.reset();
		}
	}

	public ArrayList<Entity> getEntities() {
		return Entities;
	}

	public ArrayList<Rectangle2D> getObstructions() {
		return Obstructions;
	}

	public Rectangle2D getEnd() {
		return End;
	}

	public int getPopulation() {
		return Population;
	}

	public double getMutationRate() {
		return MutationRate;
	}

	public int getAmountOfVectors() {
		return AmountOfVectors;
	}

	public int getGeneration() {
		return Generation;
	}
	
	
	

}
