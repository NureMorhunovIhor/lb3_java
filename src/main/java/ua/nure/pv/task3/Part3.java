package ua.nure.pv.task3;

public class Part3 {
	private int c1;
	private int c2;
	private final Thread[] threads;
	private final int n;
	private final int k;
	private final int t;

	public Part3(int n, int k, int t) {
		this.n = n;
		this.k = k;
		this.t = t;
		threads = new Thread[n];
	}

	public void reset() {
		c1 = 0;
		c2 = 0;
	}

	public void test() {
		System.out.println("Starting unsynchronized test:");
		for (int i = 0; i < n; i++) {
			threads[i] = new Thread(new Worker(false));
			threads[i].start();
		}
		waitForThreads();
		System.out.printf("Non-sync result: %d %d%n", c1, c2);
	}

	public void testSync() {
		System.out.println("Starting synchronized test:");
		for (int i = 0; i < n; i++) {
			threads[i] = new Thread(new Worker(true));
			threads[i].start();
		}
		waitForThreads();
		System.out.printf("Sync result: %d %d%n", c1, c2);
	}

	private void waitForThreads() {
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.err.println("Thread interrupted: " + e.getMessage());
			}
		}
	}

	private class Worker implements Runnable {
		private final boolean sync;

		public Worker(boolean sync) {
			this.sync = sync;
		}

		@Override
		public void run() {
			for (int i = 0; i < k; i++) {
				if (sync) {
					synchronized (Part3.this) {
						printAndIncrement();
					}
				} else {
					System.out.printf("%d %d%n", c1, c2);
					c1++;
					try {
						Thread.sleep(t);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						System.err.println("Thread sleep interrupted: " + e.getMessage());
					}
					c2++;
				}
			}
		}

		private void printAndIncrement() {
			System.out.printf("%d %d%n", c1, c2);
			c1++;
			try {
				Thread.sleep(t);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			c2++;
		}
	}

	public static void main(String[] args) {
		Part3 p = new Part3(3, 5, 100);
		p.test();
		p.reset();
		p.testSync();
	}
}
