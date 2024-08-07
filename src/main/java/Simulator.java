import java.util.concurrent.*;

public class Simulator {
	private final GameTable gameTable;
	private final Frog[] frogs;
	private static int round = 1;

	public Simulator(GameTable gameTable, Frog[] frogs) {
		this.gameTable = gameTable;
		this.frogs = frogs;

		// Установка GameTable для каждой лягушки
		for (Frog frog : frogs) {
			frog.setGameTable(gameTable);
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
						while (index > 0 && frogs[index - 1].getPosition() == 0 && !frogs[index - 1].isFinished()) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								System.err.println("Thread interrupted");
							}
						}
					}

					synchronized (gameTable.getLock()) {
						gameTable.addFrog(frogs[index], 0);
					}

					while (!frogs[index].isFinished()) {
						frogs[index].run();

						synchronized (this) {
							printPositions();
							notifyAll();
						}
					}
				} catch (Exception e) {
					Thread.currentThread().interrupt();
					System.err.println("Can't move a frog: " + e.getMessage());
					e.printStackTrace();
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
			if (frog.getPosition() > 0 || frog.isFinished()) {
				if (!frog.isFinished()) {
					System.out.println(frog.getName() + " in " + frog.getPosition() + " cell");
				} else {
					System.out.println(frog.getName() + " is finished!");
				}
			}
		}
		round++;
		System.out.println();
	}
}



