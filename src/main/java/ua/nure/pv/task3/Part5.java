package ua.nure.pv.task3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Part5 {

	private static final int K = 10; // Количество потоков
	private static final int N = 20; // Количество записей для каждого потока
	private RandomAccessFile raf;

	public static void main(String[] args) {
		new Part5().start();
	}

	private void start() {
		File file = new File("part5.txt");
		if (file.exists()) {
			file.delete();
		}

		try {
			raf = new RandomAccessFile(file, "rw");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Thread[] threads = new Thread[K];
		for (int i = 0; i < K; i++) {
			final int digit = i;
			threads[i] = new Thread(() -> writeToFile(digit));
			threads[i].start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		displayFileContent();
	}

	private void writeToFile(int digit) {
		try {
			synchronized (raf) {
				raf.seek((long) digit * (N + System.lineSeparator().length()));

				for (int i = 0; i < N; i++) {
					raf.writeBytes(String.valueOf(digit));
					Thread.sleep(1);
				}
				raf.writeBytes(System.lineSeparator());
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void displayFileContent() {
		try {
			raf.seek(0);
			String line;
			while ((line = raf.readLine()) != null) {
				System.out.println(line);
			}
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
