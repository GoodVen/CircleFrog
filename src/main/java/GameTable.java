
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;

public class GameTable {
	private final int numCells;
	private final List<Frog> frogs;

	private final Lock lock = new ReentrantLock();

	public GameTable(int numCells) {
		this.numCells = numCells;
		this.frogs = new ArrayList<>();

	}

	public Lock getLock() {
		return lock;
	}

	public int getNumCells() {
		return numCells;
	}

	public void addFrog(Frog frog) {
		frogs.add(frog);
	}

	public List<Frog> getFrogs() {
		return frogs;
	}

	public boolean canJump(Frog frog) {
		int frogIndex = frog.getFrogIndex();
		int nextPosition = frog.getPosition() + frog.getSpeed();


		// Ensure the next position is within bounds
		if (nextPosition >= numCells) {
			return false;
		}

		// Ensure no other frog is at the next position
		for (Frog otherFrog : frogs) {
			if (otherFrog != frog && otherFrog.getPosition() == nextPosition && !otherFrog.isFinished()) {
				return false;
			}
		}

		// Ensure previous frog has moved from start position if applicable
		if (frogIndex > 0) {
			if (frogs.get(frogIndex - 1).getPosition() == 0 && !frogs.get(frogIndex - 1).isFinished()) {
				return false;
			}
		}

		return true;

	}


	public void moveFrog(Frog frog) {
		int nextPosition = frog.getPosition() + frog.getSpeed();
		if (nextPosition >= numCells-1) {
			frog.finish();
		} else {
			frog.setPosition(nextPosition);
		}
	}
}


// no need for this field just use List of Objects/Frog/Or list of generics


	//Add method move that will use lock to move frog from i to j

