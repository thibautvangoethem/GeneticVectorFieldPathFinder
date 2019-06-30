package application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;


@XmlRootElement(name = "settings")
public class Settings {
	private final Logger logger = LoggerFactory.getLogger(Settings.class);
	
	@XmlElement(name = "pupolationSize")
	private int populationSize;
	
	@XmlElement(name = "vectorFieldSize")
	private int vectorFieldSize;
	
	@XmlElement(name = "maxTime")
	private double maxTime;
	
	@XmlElement(name = "mutationRate")
	private double mutationRate;
	
	@XmlElement(name = "tickRate")
	private double tickRate;
	
	@XmlElement(name = "screenWidth")
	private int screenWidth;
	
	@XmlElement(name = "screenHeight")
	private int screenHeight;
	
	public Settings(){}

	public Settings(int populationSize, int vectorFieldSize, double maxTime, double mutationRate, double tickRate,
			int screenWidth, int screenHeight) {
		this.populationSize = populationSize;
		this.vectorFieldSize = vectorFieldSize;
		this.maxTime = maxTime;
		this.mutationRate = mutationRate;
		this.tickRate = tickRate;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		logger.info("made new settings object with following members:");
		logger.info("populationsize: {}",this.populationSize);
		logger.info("vectorFieldSize: {}",this.vectorFieldSize);
		logger.info("maxTime: {}",this.maxTime);
		logger.info("tickrate: {}",this.tickRate);
		logger.info("screeWidth: {}",this.screenWidth);
		logger.info("screeheight: {}",this.screenHeight);
	}
	
	public int getPopulationSize() {
		return populationSize;
	}

	public int getVectorFieldSize() {
		return vectorFieldSize;
	}

	public double getMaxTime() {
		return maxTime;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public double getTickRate() {
		return tickRate;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
}
