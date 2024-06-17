import java.util.concurrent.locks.Lock;

public class Frog implements Runnable {
	private int frogIndex;
	private int[] positions;
	private boolean[] finished;
	private int numCells;
	private Lock lock;

	public Frog(int frogIndex, int[] positions, boolean[] finished, int numCells, Lock lock) {
		this.frogIndex = frogIndex;
		this.positions = positions;
		this.finished = finished;
		this.numCells = numCells;
		this.lock = lock;
	}

	@Override
	public void run() {
		try {
			// Check if the first cell is free
			if (frogIndex > 0) {
				while (positions[frogIndex - 1] == 0 && !finished[frogIndex - 1]) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						throw new FrogRaceException("Thread was interrupted for frog " + (frogIndex + 1));
					}
				}
			}

			while (true) {
				lock.lock();
				try {
					// Try to jump
					if (frogIndex == 0 || positions[frogIndex - 1] > 0 || finished[frogIndex - 1]) {
						int nextPosition = positions[frogIndex] + 1;
						if (nextPosition == numCells) {
							System.out.println("Frog " + (frogIndex + 1) + " is finished!");
							finished[frogIndex] = true;
							positions[frogIndex] = -1; // Delete a frog if it's finished
							break;
						}

						boolean positionOccupied = false;
						for (int j = 0; j < positions.length; j++) {
							if (j != frogIndex && positions[j] == nextPosition && !finished[j]) {
								positionOccupied = true;
								break;
							}
						}

						if (!positionOccupied) {
							positions[frogIndex] = nextPosition;
							Circle.printPositions(positions, finished);
						}
					}
				} finally {
					lock.unlock();
				}
				if (finished[frogIndex]) {
					break;
				}

				try {
					//throw new InterruptedException();
					// For imitate rounds
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new FrogRaceException("Thread was interrupted for frog " + (frogIndex + 1));
				}
			}
		} catch (FrogRaceException e) {
			System.err.println(e.getMessage());
		}
	}
}

