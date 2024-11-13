package ua.nure.pv.task3;

import java.io.IOException;
import java.io.InputStream;

public class Part2 {

	private static final InputStream STD_INPUT = System.in;

	private static class MockedInputStream extends InputStream {
		private boolean returnedNewline = false;

		@Override
		public int read() throws IOException {
			if (returnedNewline) {
				return -1;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ignored) {
			}
			returnedNewline = true;
			return '\n';
		}
	}

	public static void main(String[] args) throws InterruptedException {
		System.setIn(new MockedInputStream());
		Spam.main(new String[0]);
		System.setIn(STD_INPUT);
	}
}
