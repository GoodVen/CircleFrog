import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Circle {
	private final int numCells;
	private final Lock lock = new ReentrantLock();

	public Circle(int numCells) {
		this.numCells = numCells;
	}

	public Lock getLock() {
		return lock;
	}

	public int getNumCells() {
		return numCells;
	}
}
