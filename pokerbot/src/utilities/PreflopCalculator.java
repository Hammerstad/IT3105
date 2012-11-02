package utilities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import poker.Card;
import poker.Game;
import poker.Suit;

/**
 * This class handles calculation of the preflop table.
 */
public class PreflopCalculator {

	public static int NOFGAMES = 100000;
	double[][][][] table;
	String filename;
	Card[] cards;
	Game game;

	/**
	 * Default constructor for the preflop calculator. </br></br>Usage:</br>PreflopCalculator pc = new PreflopCalculator();
	 * </br>pc.calculatePreflopTable(int players);</br></br>This writes a preflop table to disk for x players.
	 */
	public PreflopCalculator() {
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
		if (noPlayers < 2) {
			noPlayers = 2;
		}
		if (noPlayers > 10) {
			noPlayers = 10;
		}
		for (int players = 2; players < noPlayers + 1; players++) {
			for (int i = 2; i < 15; i++) {
				for (int j = 2; j <= i; j++) {
					// Unsuited
					cards = new Card[] { new Card(i, Suit.DIAMOND), new Card(j, Suit.CLUB) };
					won = preflop(players, cards, NOFGAMES);
					table[0][players - 2][i - 2][j - 2] = won / (NOFGAMES * 1.);
					// Suited
					if (i == j) {
						continue; // Because you can't have two card which has
						// the same suit and value
					}
					cards = new Card[] { new Card(i, Suit.DIAMOND), new Card(j, Suit.DIAMOND) };
					won = preflop(players, cards, NOFGAMES);
					table[1][players - 2][i - 2][j - 2] = won / (NOFGAMES * 1.);
				}
			}
		}
		new PreflopWriter(table, true);
	}

	/**
	 * Used for creating a preflop table. Gets written to disk.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PreflopCalculator calculator = new PreflopCalculator();
		calculator.calculatePreflopTable(10);
	}

	/**
	 * Internal function to make sure we are independent of what happens in the game class.
	 * 
	 * @param players
	 *            - how many players.
	 * @param pocketCards
	 *            - which cards are we playing with.
	 * @param rounds
	 *            - how many rounds will we play? Typically 1000-100000
	 * @return how many times player 1 won
	 */
	private static int preflop(int players, Card[] pocketCards, int rounds) {
		// Setup
		List<Card> deck = new ArrayList<Card>();
		int won = 0;
		for (int i = 2; i < 15; i++) {
			deck.add(new Card(i, Suit.DIAMOND));
			deck.add(new Card(i, Suit.CLUB));
			deck.add(new Card(i, Suit.SPADE));
			deck.add(new Card(i, Suit.HEART));
		}
		List<Card> roundDeck = new LinkedList<Card>();
		List<Card> tempDeck;
		Card[][] cards = new Card[players][7];
		for (int round = 0; round < rounds; round++) {
			// Copy deck to temp
			tempDeck = new ArrayList<Card>(deck);
			// shuffleDeck
			for (int c = 0; c < deck.size(); c++) {
				roundDeck.add(tempDeck.remove((int) (Math.random() * tempDeck.size())));
			}
			// Give cards to players
			// Player 0 == testcase
			cards[0][0] = pocketCards[0];
			cards[0][1] = pocketCards[1];
			for (Card c : pocketCards) {
				roundDeck.remove(c);
			}
			// Rest of players
			for (int player = 1; player < players; player++) {
				cards[player][0] = roundDeck.remove(0);
				cards[player][1] = roundDeck.remove(0);
			}

			// Burn card before flop
			for (int tableCard = 2; tableCard < 7; tableCard++) {
				if (tableCard == 2 || tableCard == 5 || tableCard == 6) {
					roundDeck.remove(0);
				}
				Card card = roundDeck.remove(0);
				for (int player = 0; player < players; player++) {
					cards[player][tableCard] = card;
				}
			}
			// Find best hand
			int[][] scores = new int[players][6];
			for (int player = 0; player < players; player++) {
				scores[player] = CardUtilities.classification(cards[player]);
			}
			// Candidates for winning hand includes everybody at the start
			List<Integer> possiblePlayers = new LinkedList<Integer>();
			for (int i = 0; i < players; i++) {
				possiblePlayers.add(i);
			}
			// iterate throughcolumns
			for (int column = 0; column < 6; column++) {
				// Find max value for column
				int max = -1;
				for (int player : possiblePlayers) {
					if (scores[player][column] > max) {
						max = scores[player][column];
					}
				}
				// Remove everyone not having max
				for (int player = 0; player < possiblePlayers.size(); player++) {
					if (scores[player][column] < max) {
						possiblePlayers.remove(player);
					}
				}
				if (possiblePlayers.size() == 1)
					break;
			}
			if (possiblePlayers.contains(0)) {
				won++;
			}
		}
		return won;
	}
}
