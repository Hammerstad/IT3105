package utilities;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

/**
 * Writes a preflop table to disk
 */
public class PreflopWriter {
	DecimalFormat myFormatter;
	String filename = "preflop100000.txt";

	public PreflopWriter(double[][][][] matrix) {
		myFormatter = new DecimalFormat("0.000");
		writeTableToFile(matrix);
	}

	/**
	 * Writes the table to disk
	 * 
	 * @param table
	 *            - double[2][9][13][13]
	 */
	private void writeTableToFile(double[][][][] table) {
		Double d;
		try {
			// Create file
			FileOutputStream fos = new FileOutputStream(filename);
			DataOutputStream dos = new DataOutputStream(fos);
			for (int players = 2; players < 11; players++) {
				for (int j = 0; j < 2; j++) {
					dos.write(("Players: " + players + ((j == 0) ? " unsigned" : " signed") + "\n").getBytes());
					for (int a = 1; a < 14; a++) {
						for (int b = 1; b < 14; b++) {
							d = table[j][players - 2][a - 1][b - 1];
							dos.write(myFormatter.format(d).getBytes());
							dos.write(" ".getBytes());
						}
						fos.write('\n');
					}
					fos.write('\n');
				}
			}
			// Close the output stream
			dos.close();
		} catch (Exception e) {
			// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
