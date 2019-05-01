package GeneticVectorField;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public class GeneticAlgorithm {
	
	private ArrayList<Entity> Entities;
	
	private Entity HighestScore;
	
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
		this.Obstructions=new ArrayList<>();
		this.Population=population;
		this.AmountOfVectors=amountOfVectors;
		this.MutationRate=mutationRate;
		this.Generation=0;
		this.MaxTime=maxtime;
		this.CurrentTime=0;
		this.HighestScore=null;
		this.End=new Rectangle2D(amountOfVectors-3, amountOfVectors/2-2, 3, 4);
		for(int i =0;i<population;i++) {
			this.Entities.add(new Entity(0,(int)(amountOfVectors/2)));
		}
	}
	
	public void update(double time) {
		for(Entity i:this.Entities) {
			i.updatePosition(time);
		}
		this.CurrentTime+=time;
		this.calculateScores();
		this.checkEnd();
		this.checkObstructions();
		if(this.CurrentTime>this.MaxTime) {
			this.advanceGeneration();
			this.CurrentTime=0;
		}
	}
	
	public void reset() {
		for(Entity i:this.Entities) {
			i.reset();
		}
	}
	
	public void advanceGeneration() {
		this.Entities=this.createNewPopulation();
		Generation++;
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
	
	public Entity getHighestScore() {
		return HighestScore;
	}
	
	public void addObstruction(double x1,double y1,double x2,double y2) {
		this.Obstructions.add(new Rectangle2D(x1,y1,x2-x1,y2-y1));
	};
	
	private ArrayList<Entity> createNewPopulation() {
		
		double totalScore=0;
		for(Entity e:this.Entities) {
			totalScore+=e.getScore();
		}
		ArrayList<Entity> newEntities=new ArrayList<>();
		for(int i=0;i<this.Population;i++) {
			Entity newEntity=getNewEntityFromParents(this.getWeightedParent(totalScore),this.getWeightedParent(totalScore));
			newEntities.add(newEntity);
		}
		return newEntities;
		
	}
	
	private Entity getWeightedParent(double totalScore) {
		double mutate =ThreadLocalRandom.current().nextDouble(0, 1);
		if(mutate<this.MutationRate) {
			return new Entity(0,this.AmountOfVectors/2);
		}
		 double rand=ThreadLocalRandom.current().nextDouble(0, totalScore);
		 for(Entity e:this.Entities) {
			rand-=e.getScore();
			if(rand<=0) {
				return e;
				}
		}
		 return new Entity(0,this.AmountOfVectors/2);
		 
	}
	
	private Entity getNewEntityFromParents(Entity parent1,Entity parent2) {
		//the way the new entity is created is by first using parent 1 for all the vectors and when it passes a random position it will use the vectors of parent 2
		int xChange=ThreadLocalRandom.current().nextInt(0, this.AmountOfVectors-1);
		int yChange=ThreadLocalRandom.current().nextInt(0, this.AmountOfVectors-1);
		Entity using=parent1;
		VectorField childField=new VectorField(this.AmountOfVectors,this.AmountOfVectors);
		for(int i=0;i<this.AmountOfVectors;i++) {
			for(int j=0;j<this.AmountOfVectors;j++) {
				childField.setVector(i, j, using.getField().getSingleVector(i, j));
				if(i==xChange && j==yChange) {
					using=parent2;
				}
			}
		}
		return new Entity(0,this.AmountOfVectors/2,childField);
	}
	
	private void checkEnd() {
		double x1=this.End.getMinX();
		double y1=this.End.getMinY();
		double x2=this.End.getMaxX();
		double y2=this.End.getMaxY();
		for(Entity e:this.Entities) {
			double x=e.getPosition().getX();
			double y=e.getPosition().getY();
			if (x > x1 && x < x2 && y > y1 && y < y2) {
				e.win();
			}
		}
	}
	
	private void checkObstructions() {
		for(Rectangle2D r:this.Obstructions) {
			double x1=r.getMinX();
			double y1=r.getMinY();
			double x2=r.getMaxX();
			double y2=r.getMaxY();
			for(Entity e:this.Entities) {
				double x=e.getPosition().getX();
				double y=e.getPosition().getY();
				if (x > x1 && x < x2 && y > y1 && y < y2) {
					e.stop();
				}
			}
		}
		
	}
	
	private void calculateScores() {
		Point2D midpoint=new Point2D(this.End.getMinX()+this.End.getWidth()/2,this.End.getMinY()+this.End.getHeight()/2);
		for(Entity e:this.Entities) {
			e.calculateScore(midpoint);
			if(this.HighestScore==null) {
				this.HighestScore=e;
			}else if(e.getScore()>this.HighestScore.getScore()) {
				this.HighestScore=e;
			}
		}
	}
	
	
	

}
