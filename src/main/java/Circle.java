import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

public class Circle {
	private int numFrogs;
	private int numCells;
	private int[] positions;
	private boolean[] finished;
	private Lock lock;

	public Circle(int numFrogs, int numCells, Lock lock) {
		this.numFrogs = numFrogs;
		this.numCells = numCells;
		this.lock = lock;
		this.positions = new int[numFrogs];
		this.finished = new boolean[numFrogs];
	}

	public void start() {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		for (int i = 0; i < numFrogs; i++) {
			executorService.execute(new Frog(i, positions, finished, numCells, lock));
		}

		executorService.shutdown();
		while (!executorService.isTerminated()) {
			// Wait for all tasks to finish
		}
	}

	public static void printPositions(int[] positions, boolean[] finished) {
		System.out.println("Round № " + CircleFrog.round);
		for (int i = 0; i < positions.length; i++) {
			if (positions[i] > 0 || finished[i]) {
				if (!finished[i]) {
					System.out.println("Frog " + (i + 1) + " in " + positions[i] + " cell");
				} else {
					System.out.println("Frog " + (i + 1) + " is finished!");
				}
			}
		}
		CircleFrog.round++;
		System.out.println();
	}
}
