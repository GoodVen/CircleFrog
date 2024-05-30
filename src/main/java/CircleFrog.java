import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CircleFrog {
	private static int NUM_FROGS;
	private static int NUM_CELLS;
	private static int[] positions;
	private static boolean[] finished;
	private static final Lock lock = new ReentrantLock();
	private static int round = 1; // Переменная для отслеживания номера хода

	public static void main(String[] args) {
		// Чтение конфигурации из файла
		Properties properties = new Properties();
		try (InputStream input = CircleFrog.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
				return;
			}
			properties.load(input);
			NUM_FROGS = Integer.parseInt(properties.getProperty("num_frogs"));
			NUM_CELLS = Integer.parseInt(properties.getProperty("num_cells"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}


		positions = new int[NUM_FROGS];
		finished = new boolean[NUM_FROGS];

		Thread[] frogs = new Thread[NUM_FROGS];

		for (int i = 0; i < NUM_FROGS; i++) {
			int frogIndex = i;
			frogs[i] = new Thread(() -> {
				// Ждем, пока первая клетка не освободится, если это не первая лягушка
				if (frogIndex > 0) {
					while (positions[frogIndex - 1] == 0 && !finished[frogIndex - 1]) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				}

				while (true) {
					lock.lock();
					try {
						// Лягушка прыгает, если это первая лягушка или если предыдущая лягушка покинула первую клетку
						if (frogIndex == 0 || positions[frogIndex - 1] > 1 || finished[frogIndex - 1]) {
							int nextPosition = positions[frogIndex] + 1;
							if (nextPosition == NUM_CELLS) {
								System.out.println("Лягушка " + (frogIndex + 1) + " финишировала!");
								finished[frogIndex] = true;
								positions[frogIndex] = -1; // Убираем лягушку с поля
								break;
							}

							boolean positionOccupied = false;
							for (int j = 0; j < NUM_FROGS; j++) {
								if (j != frogIndex && positions[j] == nextPosition && !finished[j]) {
									positionOccupied = true;
									break;
								}
							}

							if (!positionOccupied) {
								positions[frogIndex] = nextPosition;
								printPositions();
							}
						}
					} finally {
						lock.unlock();
					}

					if (finished[frogIndex]) {
						break;
					}

					try {
						Thread.sleep(1000); // Пауза между прыжками для визуализации раундов
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			});

			frogs[i].start();
		}

		for (Thread frog : frogs) {
			try {
				frog.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private static void printPositions() {
		System.out.println("Ход № " + round);
		for (int i = 0; i < NUM_FROGS; i++) {
			if (positions[i] > 0 || finished[i]) {
				if (!finished[i]) {
					System.out.println("Лягушка " + (i + 1) + " - " + positions[i] + " клетка");
				} else {
					System.out.println("Лягушка " + (i + 1) + " финишировала!");
				}
			}
		}
		round++;
		System.out.println();
	}
}
