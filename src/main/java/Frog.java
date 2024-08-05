public class Frog implements Runnable {
	private final int frogIndex;
	private final String name;
	private final int speed;
	private int position;
	private boolean finished;
	private GameTable gameTable;

	public Frog(int frogIndex, int speed) {
		this.frogIndex = frogIndex;
		this.name = "Frog " + (frogIndex + 1);
		this.speed = speed;
		this.position = 0;
		this.finished = false;
	}

	public void setGameTable(GameTable gameTable) {
		this.gameTable = gameTable;
	}

	public int getFrogIndex() {
		return frogIndex;
	}

	public String getName() {
		return name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isFinished() {
		return finished;
	}

	public void finish() {
		finished = true;
	}

	@Override
	public void run() {
		while (!finished) {
			try {
				synchronized (gameTable.getLock()) {
					if (gameTable.canJump(this)) {
						gameTable.moveFrog(this);
						System.out.println(name + " jumped to position " + position);
					} else {
						System.out.println(name + " is waiting...");
					}
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.err.println("Thread was interrupted for frog " + (frogIndex + 1));
				return;
			}
		}
	}

	public int getSpeed() {
		return speed;
	}
}



