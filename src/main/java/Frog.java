public class Frog implements Runnable {

	private final int frogIndex;
	private final String name;
	private final int speed;
	private int position;
	private boolean finished;

	public Frog(int frogIndex, int speed) {
		this.frogIndex = frogIndex;
		this.name = "Frog " + (frogIndex + 1);
		this.speed = speed;
		this.position = 0;
		this.finished = false;
	}

	public int getFrogIndex() {
		return frogIndex;
	}

	public String getName(){
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

	public void jump() {
		if (!finished) {
			position += speed;
		}
	}

	public void finish() {
		finished = true;
	}

	public String reportPosition() {
		if (!finished) {
			return name + " in " + position + " cell";
		} else {
			return name + " has finished!";
		}
	}

	@Override
	public void run() {
		while (!finished) {
			try {
				jump();
				Thread.sleep(1000); // время между прыжками
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.err.println("Thread was interrupted for frog " + (frogIndex + 1));
				return;
			}
		}
	}

	@Override
	public String toString() {
		return name + " (position: " + position + ", finished: " + finished + ")";
	}

	public int getSpeed() {
		return speed;
	}
}


