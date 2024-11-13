package ua.nure.pv.task3;

public class Part6 {

	private static final Object M = new Object();

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread() {
			@Override
			public void run() {
				synchronized (M) {
					try {
						M.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};

		System.out.println(t.getState()); // NEW

		t.start();

		while (t.getState() == Thread.State.NEW) {
			Thread.yield();
		}
		System.out.println(t.getState()); // RUNNABLE
		synchronized (M) {
			while (t.getState() != Thread.State.BLOCKED) {
				Thread.yield();
			}
			System.out.println(t.getState()); // BLOCKED
		}

		while (t.getState() != Thread.State.WAITING && t.getState() != Thread.State.TIMED_WAITING) {
			Thread.yield();
		}
		System.out.println(t.getState()); // WAITING

		synchronized (M) {
			M.notify();
		}

		while (t.getState() != Thread.State.TERMINATED) {
			Thread.yield();
		}
		System.out.println(t.getState()); // TERMINATED
	}
}
