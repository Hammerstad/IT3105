package utilities;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import poker.Card;
import poker.Game;
import poker.Suit;

public class PreflopCalculator {

	public static int NOFGAMES = 100000;
	DecimalFormat myFormatter;
	double[][][][] table;
	String filename;
	Card[] cards;
	Game game;

	public PreflopCalculator() {
		myFormatter = new DecimalFormat("0.000");
		table = new double[2][9][13][13];
		filename = "preflop.txt";
	}

	/**
	 * Calculates a preflopTable for x players.
	 * 
	 * @param noPlayers
	 *            - number of players, 2 to 10
	 */
	public void calculatePreflopTable(int noPlayers) {
		int won = 0;
		if (noPlayers < 2)
			noPlayers = 2;
		if (noPlayers > 10)
			noPlayers = 10;
		for (int players = 2; players < noPlayers + 1; players++) {
			for (int i = 2; i < 15; i++) {
				for (int j = 2; j <= i; j++) {
					// Unsuited
					cards = new Card[] { new Card(i, Suit.DIAMOND), new Card(j, Suit.CLUB) };
					game = new Game(players, cards);
					won = game.playRoundsSimulate(NOFGAMES, cards);
					table[0][players - 2][i - 2][j - 2] = won / (NOFGAMES * 1.);
					// Suited
					if (i == j) {
						continue; // Because you can't have two card which has
									// the same suit and value

					}
					cards = new Card[] { new Card(i, Suit.DIAMOND), new Card(j, Suit.DIAMOND) };
					game = new Game(players, cards);
					won = game.playRoundsSimulate(NOFGAMES, cards);
					table[1][players - 2][i - 2][j - 2] = won / (NOFGAMES * 1.);
				}
			}
		}
		writeTableToFile(table);
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

	public static void main(String[] args) {
		PreflopCalculator calculator = new PreflopCalculator();
		calculator.calculatePreflopTable(10);
	}
}
