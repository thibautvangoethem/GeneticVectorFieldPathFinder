/**
 * this file contains the class for the entire genetic algorithm
 * @author Thibaut Van Goethem
 */
package geneticVectorField;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

/**
 * this class is the actual non graphic implementation of the Genetic Algorithm
 * @author thibaut
 *
 */
public class GeneticAlgorithm {
	private final Logger logger = LoggerFactory.getLogger(GeneticAlgorithm.class);
	
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
		
	/**
	 * creates an instance of the GeneticAlgorithm
	 * @param amountOfVectors the amount of vectors teh field will contains, this will also be the x and y size of the field
	 * @param population the population size for each generation
	 * @param mutationRate the chance that a vector mutates when creating a new entity from two parents
	 * @param maxtime the maximum time a generation has before restarting
	 */
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
		//creates a certain amount of entities with a fully random vectorField
		for(int i =0;i<population;i++) {
			this.Entities.add(new Entity(1,(int)(amountOfVectors/2)));
		}
	}
	
	/**
	 * updates the current population with a certain amount of time
	 * @param time the time it will advance with
	 */
	public void update(double time) {
		//first it updates all the entities and their position(also updates the acceleration and speed)
		for(Entity i:this.Entities) {
			i.updatePosition(time);
		}
		this.CurrentTime+=time;
		
		//calculates the score first each entity and stores the one with the highest
		this.calculateScores();
		
		//checks if there is no entity that hasn't reached the end yet and should thus stop
		this.checkEnd();
		
		//checks if there aren't any entities that collided with obstructions and should stop
		this.checkObstructions();
		
		//if the time goes beyond maxtime the generation needs to be advanced
		if(this.CurrentTime>this.MaxTime) {
			this.advanceGeneration();
			this.CurrentTime=0;
		}
	}
	
	/**
	 * resets the entire genetic algorithm
	 * this means that 
	 * - entities are set back to random ones
	 * - generation is set to 0
	 * - obstructions are removed
	 * 
	 */
	public void reset() {
		this.Entities=new ArrayList<>();
		for(int i =0;i<this.Population;i++) {
			this.Entities.add(new Entity(0,(int)(this.AmountOfVectors/2)));
		}
		this.Generation=0;
		this.CurrentTime=0;
		this.Obstructions=new ArrayList<>();
		logger.debug("reset done in algortihm");
	}
	
	/**
	 * advances the algorithm 1 generation thus the population will be remade based on the scores of the previous population
	 */
	private void advanceGeneration() {
		this.Entities=this.createNewPopulation();
		this.HighestScore=null;
		Generation++;
		logger.info("advanced to generation {}",this.Generation);
	}

	/**
	 * getter for all the entities
	 * @return an ArrayList of entities
	 */
	public ArrayList<Entity> getEntities() {
		return Entities;
	}

	/**
	 * getter for all obstruction
	 * @return a list of 2d rectangles
	 */
	public ArrayList<Rectangle2D> getObstructions() {
		return Obstructions;
	}

	/**
	 * getter for the end rectangle
	 * @return a 2d rectangle 
	 */
	public Rectangle2D getEnd() {
		return End;
	}

	/**
	 * getter for the population
	 * @return an int
	 */
	public int getPopulation() {
		return Population;
	}

	/**
	 * getter for the mutationrate
	 * @return a double between 0 and 1
	 */
	public double getMutationRate() {
		return MutationRate;
	}

	/**
	 * getter of the amount of vectors/field size
	 * @return an int
	 */
	public int getAmountOfVectors() {
		return AmountOfVectors;
	}

	/**
	 * getter for the current generation
	 * @return an int
	 */
	public int getGeneration() {
		return Generation;
	}
	
	/**
	 * getter for the score of the best entities
	 * @return a double
	 */
	public Entity getHighestScore() {
		return HighestScore;
	}
	
	/**
	 * adds an obstruction to the field
	 * @param x1 x value of point 1
	 * @param y1 y values of point 1
	 * @param x2 x value of point 2
	 * @param y2 y value of point 2
	 */
	public void addObstruction(double x1,double y1,double x2,double y2) {
		this.Obstructions.add(new Rectangle2D(x1,y1,x2-x1,y2-y1));
		logger.info("obstruction added");
		logger.debug("points of obstruction: 1){}, {} 2){}, {}",x1,y1,x2-x1,y2-y1);
	};
	
	/**
	 * this function creates a new "better" population out of the current one
	 * @return a list of new entities
	 */
	private ArrayList<Entity> createNewPopulation() {
		
		//first we calculate the total score to be able calculate the chances for an entities to be represented in the next generation
		double totalScore=0;
		for(Entity e:this.Entities) {
			totalScore+=e.getScore();
		}
		//creates n amount of entities and put them in a list
		ArrayList<Entity> newEntities=new ArrayList<>();
		for(int i=0;i<this.Population;i++) {
			Entity newEntity=getNewEntityFromParents(this.getWeightedParent(totalScore),this.getWeightedParent(totalScore));
			newEntities.add(newEntity);
		}
//		logger.info("new population created");
		return newEntities;
	}
	
	/**
	 * a private function that gets a somewhat random parent from the current list of entities
	 * the score of all entities will be taken into account to get this parent
	 * @param totalScore the totalscores of all curernt entities
	 * @return a parent
	 */
	private Entity getWeightedParent(double totalScore) {
		
		//the way it is chosen is by generating a random double between 0 and totalscore
		//then we go over all entities and substract the score of said entiteis frol the random generated one
		//if the score goes below 0 after substraction we return the current entity
		 double rand=ThreadLocalRandom.current().nextDouble(0, totalScore);
		 for(Entity e:this.Entities) {
			rand-=e.getScore();
			if(rand<=0) {
				return e;
			}
		}
		 //for the off chance the number was larger than the totalscore (shouldn't happen) it just creates a random one
		 return new Entity(0,this.AmountOfVectors/2);
		 
	}
	
	/**
	 * creates a new entity from 2 parents
	 * @param parent1 first parent entity
	 * @param parent2 second parent entity
	 * @return a new child entity
	 */
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
		//here is the calculation of the mutation algorithm, this will just randomize the amount of vectors that need to be metated and will then mutate random positions
		double mutate =ThreadLocalRandom.current().nextDouble(0, this.MutationRate);
		mutate=mutate*this.AmountOfVectors*this.AmountOfVectors;
		while(mutate>0) {
			int i=ThreadLocalRandom.current().nextInt(0, this.AmountOfVectors-1);
			int j=ThreadLocalRandom.current().nextInt(0, this.AmountOfVectors-1);
			double vecx=ThreadLocalRandom.current().nextDouble(-1, 1);
			double vecy=ThreadLocalRandom.current().nextDouble(-1, 1);
			childField.setVector(i, j, new Vector2D(vecx,vecy).normalize());
			mutate--;
		}
		logger.debug("created new entity");
		return new Entity(1,this.AmountOfVectors/2,childField);
	}
	
	/**
	 * quick function to check if any entity has reached the end yet if so call the win function on them
	 */
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
				logger.debug("entity made it to the end");
			}
		}
	}
	
	/**
	 * quick function to check if an entity has crashed into an obstruction if so call the stop function
	 */
	private void checkObstructions() {
		for(Rectangle2D r:this.Obstructions) {
			double x1=r.getMinX();
			double y1=r.getMinY();
			double x2=r.getMaxX();
			double y2=r.getMaxY();
			for(Entity e:this.Entities) {
				double x=e.getPosition().getX();
				double y=e.getPosition().getY();
				if (x > x1 && x < x2 && y > y1 && y < y2 && !e.isFinished()) {
					e.stop();
				}
			}
		}

	}
	
	/**
	 * quick function that calls the calculatescore function on entities and stores the entity with the highest score
	 */
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
