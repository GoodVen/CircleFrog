import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;

public class GameTable {
	private final String name;
	private final int numCells;
	private final List<Frog> gameTable;
	private final Lock lock = new ReentrantLock();

	public GameTable(int numCells, String name) {
		this.name = name;
		this.numCells = numCells;
		this.gameTable = new ArrayList<>();
		for (int i = 0; i < numCells; i++) {
			gameTable.add(null);
		}
	}

	public Lock getLock() {
		return lock;
	}

	public void addFrog(Frog frog, int position) {
		lock.lock();
		try {
			if (gameTable.get(position) == null) {
				gameTable.set(position, frog);
				frog.setPosition(position);
			} else {
				throw new IllegalStateException("Position " + position + " is already occupied by another frog.");
			}
		} finally {
			lock.unlock();
		}
	}

	public Frog getFrogAt(int position) {
		lock.lock();
		try {
			return gameTable.get(position);
		} finally {
			lock.unlock();
		}
	}

	public String getName() {
		return name;
	}

	public void moveFrog(Frog frog) {
		lock.lock();
		try {
			int currentPosition = frog.getPosition();
			int nextPosition = currentPosition + frog.getSpeed();

			if (nextPosition >= numCells - 1) {
				frog.finish();
				gameTable.set(currentPosition, null);
				gameTable.set(numCells - 1, frog);
			} else {
				if (gameTable.get(nextPosition) == null) {
					gameTable.set(currentPosition, null);
					gameTable.set(nextPosition, frog);
					frog.setPosition(nextPosition);
				} else {
					throw new IllegalStateException("Position " + nextPosition + " is already occupied by another frog.");
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean canJump(Frog frog) {
		int frogIndex = frog.getFrogIndex();
		int nextPosition = frog.getPosition() + frog.getSpeed();

		// Ensure the next position is within bounds
		if (nextPosition >= gameTable.size()) {
			return false;
		}

		// Ensure no other frog is at the next position
		for (Frog otherFrog : gameTable) {
			if (otherFrog != frog && otherFrog != null && otherFrog.getPosition() == nextPosition && !otherFrog.isFinished()) {
				return false;
			}
		}

		return true;
	}
}











