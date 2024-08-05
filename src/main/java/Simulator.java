import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Simulator {
	private final GameTable circle;
	private final Frog[] frogs;
	private static int round = 1;

	public Simulator(GameTable circle, Frog[] frogs) {
		this.circle = circle;
		this.frogs = frogs;
		for (Frog frog : frogs) {
			circle.addFrog(frog);
		}
	}

	public void startSimulation() {
		ExecutorService executorService = Executors.newFixedThreadPool(frogs.length);

		for (int i = 0; i < frogs.length; i++) {
			int index = i;
			executorService.execute(() -> {
				try {
					if (index > 0) {
						// Ensure previous frog has moved from start position
						while (frogs[index - 1].getPosition() == 0 && !frogs[index - 1].isFinished()) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								System.err.println("Thread interrupted");
							}
						}
					}

					while (!frogs[index].isFinished()) {
						if (circle.moveFrog(frogs[index])) {
							Thread.sleep(500); // Задержка перед следующей попыткой
							printPositions(); // Print positions after this frog has jumped
						} else {
							System.out.println("Frog " + (frogs[index].getFrogIndex() + 1) + " is waiting...");
							Thread.sleep(1000); // Ожидание, если лягушка не может прыгнуть
						}

						// Check if the frog has finished the race
						if (frogs[index].isFinished()) {
							System.out.println("Frog " + (frogs[index].getFrogIndex() + 1) + " has finished the race and is exiting...");
							break; // Exit the loop and terminate the thread
						}
					}

				} catch (Exception e) {
					Thread.currentThread().interrupt();
					System.err.println("Thread interrupted");
				}
			});
		}

		executorService.shutdown();

		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("Thread interrupted during termination");
		}
	}

	private void printPositions() {
		System.out.println("Round № " + round);
		for (Frog frog : frogs) {
			System.out.println(frog.reportPosition());
		}
		round++;
		System.out.println();
	}
}

