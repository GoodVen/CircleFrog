
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

		return true;
	}

	public void moveFrog(Frog frog) {
		lock.lock();
		try {
			int nextPosition = frog.getPosition() + frog.getSpeed();
			if (nextPosition >= numCells - 1) {
				frog.finish();
			} else {
				frog.setPosition(nextPosition);
			}
		} finally {
			lock.unlock();
		}
	}
}









