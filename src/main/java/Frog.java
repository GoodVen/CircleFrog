public class Frog implements Runnable {
	private final int frogIndex;
	private final int speed;
	private int position;
	private boolean finished;

	public Frog(int frogIndex, int speed) {
		this.frogIndex = frogIndex;
		this.speed = speed;
		this.position = 0;
		this.finished = false;
	}

	public int getFrogIndex() {
		return frogIndex;
	}

	public int getPosition() {
		return position;
	}

	public boolean isFinished() {
		return finished;
	}

	public void jump() {
		if (!finished) {
			position += speed;
		}
	}

	public void finish() {
		finished = true;
	}

	@Override
	public void run() {
		while (!finished) {
			try {
				Thread.sleep(1000);
				jump();
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

