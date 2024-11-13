package ua.nure.pv.task3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Part4 {

	private int n; // Количество столбцов
	private int m; // Количество строк
	private int[][] matrix;
	private Thread[] threads;
	private int[] maxValues; // Массив для хранения максимальных значений из потоков

	public Part4() {
		loadMatrix("part4.txt");
		maxValues = new int[m];
	}

	private void loadMatrix(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			m = 0;
			while ((line = br.readLine()) != null) {
				String[] values = line.trim().split("\\s+");
				if (matrix == null) {
					n = values.length;
					matrix = new int[5][n]; // Предполагаем, что M = 5
				}
				for (int j = 0; j < n; j++) {
					matrix[m][j] = Integer.parseInt(values[j]);
				}
				m++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class MaxFinder extends Thread {
		private final int row;

		public MaxFinder(int row) {
			this.row = row;
		}

		@Override
		public void run() {
			int max = matrix[row][0];
			for (int j = 1; j < n; j++) {
				try {
					Thread.sleep(1); // Задержка в 1 миллисекунду
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				if (matrix[row][j] > max) {
					max = matrix[row][j];
				}
			}
			maxValues[row] = max;
		}
	}

	public int findMaxParallel() throws InterruptedException {
		threads = new Thread[m];
		for (int i = 0; i < m; i++) {
			threads[i] = new MaxFinder(i);
			threads[i].start();
		}

		for (int i = 0; i < m; i++) {
			threads[i].join();
		}

		int max = Integer.MIN_VALUE;
		for (int value : maxValues) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	public int findMaxSequential() {
		int max = matrix[0][0];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				if (matrix[i][j] > max) {
					max = matrix[i][j];
				}
			}
		}
		return max;
	}

	public static void main(String[] args) {
		Part4 part4 = new Part4();

		try {
			long startTime = System.currentTimeMillis();
			int maxParallel = part4.findMaxParallel();
			long timeParallel = System.currentTimeMillis() - startTime;
			System.out.println("Parallel result =  " + maxParallel);
			System.out.println("Parallel time =  " + timeParallel + "ms");

			startTime = System.currentTimeMillis();
			int maxSequential = part4.findMaxSequential();
			long timeSequential = System.currentTimeMillis() - startTime;
			System.out.println("Sequential result = " + maxSequential);
			System.out.println("Sequential time = " + timeSequential + "ms");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
