import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Rename to geme table
public class Circle {
	private final int numCells;// no need for this field just use List of Objects/Frog/Or list of generics
	private final Lock lock = new ReentrantLock();

	public Circle(int numCells) {
		this.numCells = numCells;
	}

	//Add method move that will use lock to move frog from i to j

//	public Lock getLock() {
//		return lock;
//	}
//
//	public int getNumCells() {
//		return numCells;
//	}
}
