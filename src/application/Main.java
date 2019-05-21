/**
 * this file contains the main class/application
 * @author Thibaut Van Goethem
 */

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;;




/**
 * main class 
 * @author Thibaut Van Goethem
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Settings set=readSettings();
			Pane root = new Pane();
			Group drawBoard = new Group();
			root.getChildren().add(drawBoard);
			Scene scene = new Scene(root,set.getScreenWidth(),set.getScreenHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			 
			//creating the genetic algorithm and drawing it for the first time
			JavaFXGeneticAlgorithm algo=new JavaFXGeneticAlgorithm(set.getVectorFieldSize(),set.getPopulationSize(),set.getMutationRate(),set.getMaxTime(),scene,drawBoard);
			algo.draw();
			
			//creating the reset button at the top left
			Button button = new Button("reset");
			button.setText("reset");
			button.setWrapText(true);
			button.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	algo.reset();
			    }
			});
			
			//creating the click event to make obstructions
			root.setOnMouseClicked(new EventHandler<MouseEvent>() {
				 @Override
				    public void handle(MouseEvent event) {
				        algo.click(event.getSceneX(), event.getSceneY());
				    }
			});
			root.getChildren().add(button);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//timer that will advance the entities and redraw the scene
			new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	            	drawBoard.getChildren().clear();
	            	algo.update(set.getTickRate());
	            	algo.draw();
	            }
	        }.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * function that reads the settings with hte name settings.xml in this directory
	 * @return a settings object 
	 */
	public Settings readSettings() {
		try {
			File f = new File("./settings.xml");
			if(f.exists() && !f.isDirectory()) { 
				JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Settings set=(Settings) jaxbUnmarshaller.unmarshal(f);
				return set;
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("error while reading settings, creating and reading default settings");
			e.printStackTrace();
			Settings set=new Settings(100,50,50,0.01,0.10,1000,1000);
			 try
		        {
		            JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
		            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		            StringWriter sw = new StringWriter();
		            jaxbMarshaller.marshal(set, sw);
		            String xmlContent = sw.toString();
		            PrintWriter writer = new PrintWriter("./settings.xml", "UTF-8");
		            writer.write(xmlContent);
		            writer.close();
		            return set;
		        } catch (JAXBException jaxE) {
		            jaxE.printStackTrace();
		        } catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			
		}
		return null;  	
	}
	
}
