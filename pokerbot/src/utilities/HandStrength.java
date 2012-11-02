/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.List;

import poker.Card;
import poker.Suit;

/**
 * This file handles hand strength calculation.
 */
public class HandStrength {

	/**
	 * Calculates the handstrength of a hand
	 * 
	 * @param hole
	 *            - the hole cards, sometimes referred to as the "hand"
	 * @param table
	 *            - the cards on table
	 * @param opponents
	 *            - how many opponents are left
	 * @return a double between 0 and 1
	 */
	public static double handstrength(Card[] hole, Card[] table, int opponents) {
		List<Card> deck = new ArrayList<Card>();
		for (int i = 2; i < 15; i++) {
			deck.add(new Card(i, Suit.DIAMOND));
			deck.add(new Card(i, Suit.CLUB));
			deck.add(new Card(i, Suit.SPADE));
			deck.add(new Card(i, Suit.HEART));
		}
		// Removing card that are already in use
		for (Card c : hole) {
			deck.remove(c);
		}
		int tableSize = 0;
		for (Card c : table) {
			if (c != null) {
				deck.remove(c);
				tableSize++;
			}
		}
		double[] status = new double[3];
		Card[] myHand = new Card[tableSize + 2];
		System.arraycopy(hole, 0, myHand, 0, 2);
		System.arraycopy(table, 0, myHand, 2, tableSize);

		Card[] opponentHand = new Card[tableSize + 2];
		System.arraycopy(table, 0, opponentHand, 2, tableSize);

		int[] holePower = CardUtilities.classification(myHand);
		int[] opponentPower;
		for (int i = 0; i < deck.size(); i++) {
			opponentHand[0] = deck.get(i);
			for (int j = i + 1; j < deck.size(); j++) {
				opponentHand[1] = deck.get(j);
				opponentPower = CardUtilities.classification(opponentHand);
				status[compare(holePower, opponentPower)]++;
			}
		}
		return Math.pow((status[0] + (status[1] / 2)) / (status[0] + status[1] + status[2]), opponents);
	}

	/**
	 * Compares two handstrengths (6 elements long int[]).
	 * 
	 * @param a
	 *            - first int[]
	 * @param b
	 *            - second int[]
	 * @return 0 (a wins) / 1 (draw) / 2 (b wins)
	 */
	private static int compare(int[] a, int[] b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] > b[i]) {
				return 0;
			} else if (a[i] < b[i]) {
				return 2;
			}
		}
		return 1;
	}

	/**
	 * Main function for testing purposes.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Card[] hole = new Card[] { new Card(14, Suit.DIAMOND), new Card(12, Suit.CLUB) };
		Card[] flop = new Card[] { new Card(11, Suit.HEART), new Card(4, Suit.CLUB), new Card(3, Suit.HEART), new Card(9, Suit.SPADE) };
		int opponents = 1;

		System.out.println(HandStrength.handstrength(hole, flop, opponents));
	}
}
