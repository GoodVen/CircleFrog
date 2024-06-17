import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CircleFrog {
	public static int round = 1;

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

		Lock lock = new ReentrantLock();
		Circle circle = new Circle(numFrogs, numCells, lock);
		circle.start();
	}
}
