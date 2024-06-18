

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CircleFrog {
	public static void main(String[] args) {
		int numFrogs = 0;
		int numCells = 0;

		// Reading config file
		Properties properties = new Properties();
		try (InputStream input = CircleFrog.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new ConfigurationException("Sorry, unable to find config.properties");
			}
			properties.load(input);
			numFrogs = Integer.parseInt(properties.getProperty("num_frogs"));
			numCells = Integer.parseInt(properties.getProperty("num_cells"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		catch (ConfigurationException ex){
			System.err.println(ex.getMessage());
		}
		//initialize circle and frog, set frog speed
		Circle circle = new Circle(numCells);
		Frog[] frogs = new Frog[numFrogs];
		for (int i = 0; i < numFrogs; i++) {
			//frog speed
			frogs[i] = new Frog(i, 2);
		}
		//Simulation
		Simulator simulator = new Simulator(circle, frogs);
		simulator.startSimulation();
	}
}
