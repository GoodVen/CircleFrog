import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulator {
	private Circle circle;
	private Frog[] frogs;
	private static int round = 1;

	public Simulator(Circle circle, Frog[] frogs) {
		this.circle = circle;
		this.frogs = frogs;
	}

	public void startSimulation() {
		ExecutorService executorService = Executors.newFixedThreadPool(frogs.length);
		for (Frog frog : frogs) {
			executorService.execute(() -> {
				try {
					while (!frog.isFinished()) {
						attemptJump(frog);
						Thread.sleep(1000);
					}
				} catch (InterruptedException | FrogRaceException e) {
					Thread.currentThread().interrupt();
					System.err.println(e.getMessage());
				}
			});
		}
		executorService.shutdown();
		while (!executorService.isTerminated()) {
			// Wait for all tasks to finish
		}
	}

	private void attemptJump(Frog frog) throws InterruptedException, FrogRaceException {
		boolean jumped = false;
		int frogIndex = frog.getFrogIndex();

		// Ensure previous frog has moved from start position
		if (frogIndex > 0) {
			while (frogs[frogIndex - 1].getPosition() == 0 && !frogs[frogIndex - 1].isFinished()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new FrogRaceException("Thread was interrupted for frog " + (frogIndex + 1));
				}
			}
		}

		while (true) {
			circle.getLock().lock();
			try {
				// Try to jump
				if (frogIndex == 0 || frogs[frogIndex - 1].getPosition() > 0 || frogs[frogIndex - 1].isFinished()) {
					int nextPosition = frog.getPosition() + frog.getSpeed();
					if (nextPosition >= circle.getNumCells()) {
						System.out.println("Frog " + (frogIndex + 1) + " is finished!");
						frog.finish();
						printPositions();
						return;
					}

					boolean positionOccupied = false;
					for (Frog otherFrog : frogs) {
						if (otherFrog != frog && otherFrog.getPosition() == nextPosition && !otherFrog.isFinished()) {
							positionOccupied = true;
							break;
						}
					}

					if (!positionOccupied) {
						frog.jump();
						printPositions();
						jumped = true;
					} else {
						System.out.println("Frog " + (frogIndex + 1) + " attempted to jump to an occupied position! Waiting for next round...");
						printPositions();
					}
				}
			} finally {
				circle.getLock().unlock();
			}

			if (!jumped) {
				Thread.sleep(500); // wait before trying again
			} else {
				break; // exit loop if frog successfully jumped
			}
		}
	}

	private void printPositions() {
		System.out.println("Round № " + round);
		for (Frog frog : frogs) {
			if (frog.getPosition() > 0 || frog.isFinished()) {
				if (!frog.isFinished()) {
					System.out.println("Frog " + (frog.getFrogIndex() + 1) + " in " + frog.getPosition() + " cell");
				} else {
					System.out.println("Frog " + (frog.getFrogIndex() + 1) + " is finished!");
				}
			}
		}
		round++;
		System.out.println();
	}
}
