package ua.nure.pv.task3;

public class Part1 {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("First method: Thread inheritance");
		Thread thread1 = new ThreadExample();
		thread1.start();
		thread1.join();

		System.out.println("\nSecond method: Runnable implementation");
		Thread thread2 = new Thread(new RunnableExample());
		thread2.start();
		thread2.join();

		System.out.println("\nThird method: Static method reference");
		Thread thread3 = new Thread(Part1::staticMethodExample);
		thread3.start();
		thread3.join();
	}

	static class ThreadExample extends Thread {
		@Override
		public void run() {
			printThreadName();
		}
	}

	static class RunnableExample implements Runnable {
		@Override
		public void run() {
			printThreadName();
		}
	}

	public static void staticMethodExample() {
		printThreadName();
	}

	public static void printThreadName() {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 1000) {
			try {
				System.out.println(Thread.currentThread().getName());
				Thread.sleep(300);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}
}
