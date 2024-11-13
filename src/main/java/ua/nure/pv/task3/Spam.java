package ua.nure.pv.task3;

import java.util.Scanner;

public class Spam {

	private Thread[] threads;

	public Spam(String[] messages, int[] timeouts) {
		threads = new Thread[messages.length];

		for (int i = 0; i < messages.length; i++) {
			final int index = i;
			threads[i] = new Thread(() -> {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						Thread.sleep(timeouts[index]);
						System.out.println(messages[index]);
					} catch (InterruptedException e) {
						return;
					}
				}
			});
		}
	}

	public void start() {
		for (Thread thread : threads) {
			thread.start();
		}
	}

	public void stop() {
		for (Thread thread : threads) {
			thread.interrupt();
			try {
				thread.join();
			} catch (InterruptedException ignored) {
			}
		}
	}

	public static void main(String[] args) {
		Spam spam = new Spam(
				new String[] {"AAAAA", "777", "@@"},
				new int[] {333, 555, 1555});
		spam.start();

		Scanner sc = new Scanner(System.in);
		sc.nextLine();

		spam.stop();
	}
}
