import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Simulator {
	private final GameTable gameTable;
	private final Frog[] frogs;
	private static int round = 1;

	public Simulator(GameTable gameTable, Frog[] frogs) {
		this.gameTable = gameTable;
		this.frogs = frogs;
		for (Frog frog : frogs) {
			frog.setGameTable(gameTable);
			gameTable.addFrog(frog);
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

					while (!frogs[index].isFinished()) {

						frogs[index].run();
						//printPositions();

						// Check if the frog has finished the race
						if (frogs[index].isFinished()) {
							System.out.println(frogs[index].getName() + " has finished the race and is exiting...");
							synchronized (gameTable.getLock()) {
								gameTable.getFrogs().remove(frogs[index]);
							}
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

	//private void printPositions() {
		//System.out.println("Round № " + round);
		//for (Frog frog : frogs) {
			//if (frog.getPosition() > 0 || frog.isFinished()) {
				//if (!frog.isFinished()) {
					//System.out.println(frog.getName() + " in " + frog.getPosition() + " cell");
				//} else {
					//System.out.println(frog.getName() + " is finished!");
				//}
			//}
		//}
		//round++;
		//System.out.println();
	//}
}

