package utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class PreflopReader {
	double[][][][] table;
	DecimalFormat df;
	DecimalFormatSymbols symbols;

	public PreflopReader() {
		table = new double[2][9][13][13];
		df = new DecimalFormat();
		symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		df.setDecimalFormatSymbols(symbols);
	}

	public double[][][][] read() {
		try {
			// Create file
			FileInputStream fis = new FileInputStream("preflop.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String temp;
			String[] tempToArray;
			for (int players = 2; players < 11; players++) {
				for (int j = 0; j < 2; j++) {
					br.readLine();
					for (int a = 1; a < 14; a++) {
						temp = br.readLine();
						tempToArray = temp.split(" ");
						for (int b = 1; b < 14; b++) {
							table[j][players - 2][a - 1][b - 1] = df.parse(
									tempToArray[b - 1]).doubleValue();
						}
					}
					br.readLine();
				}
			}
			// Close the input streams
			br.close();
			fis.close();
		} catch (Exception e) {
			// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return table;
	}

	public static void checkDifference(double[][][][] temp) {
		// How many unsigned are better than signed?
		int better = 0;
		for (int j = 2; j < 11; j++) {
			for (int a = 1; a < 14; a++) {
				for (int b = 1; b < 14; b++) {
					if (temp[0][j - 2][a - 1][b - 1] > temp[1][j - 2][a - 1][b - 1]) {
						better++;
						System.out.println(a + "-" + b);
					}
				}
			}
		}
		System.out.println(better);
	}

	public static void main(String[] args) {
		PreflopReader pr = new PreflopReader();
		double[][][][] temp = pr.read();
		checkDifference(temp);
	}
}