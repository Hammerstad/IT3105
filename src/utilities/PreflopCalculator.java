package utilities;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import poker.Card;
import poker.Game;
import poker.Suit;

public class PreflopCalculator {
	public static void main(String[] args) {
		double[][][][] table = new double[2][9][13][13];
		int won = 0;
		for (int players = 2; players < 11; players++) {
			for (int i = 1; i < 14; i++) {
				for (int j = 1; j <= i; j++) {
					// TODO: 1000-100000 simulations of this card, how often
					// Unsuited
					Card[] cards = new Card[] { new Card(i, Suit.DIAMOND),
							new Card(j, Suit.CLUB) };
					Game game = new Game(players, cards);
					won = game.playRounds(1000);
					table[0][players - 2][i - 1][j - 1] = won / 1000.0;

					// Suited
					if (i == j) {
						continue; // Because you can't have two card which has
									// the same suit and value
					}

					cards = new Card[] { new Card(i, Suit.DIAMOND),
							new Card(j, Suit.DIAMOND) };
					game = new Game(players, cards);
					won = game.playRounds(1000);
					table[1][players - 2][i - 1][j - 1] = won / 1000.0;
				}
			}
		}
		
		DecimalFormat myFormatter = new DecimalFormat("0.000");
		
		try {
			// Create file
			FileOutputStream fos = new FileOutputStream("txt");
			DataOutputStream dos = new DataOutputStream(fos);
			for (int players = 2; players < 11; players++) {
				for (int j = 0; j < 2; j++) {
					dos.write(("Players: "+players+((j==0)?" unsigned":" signed")+"\n").getBytes());
					for(int a = 1; a < 14 ; a++){
						for(int b = 1; b < 14; b++){
							Double d = table[j][players-2][a-1][b-1];
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
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}