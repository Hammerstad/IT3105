package poker;

import java.util.ArrayList;
import java.util.List;

import utilities.DataOutput;

import ai.player.AbstractPlayer;

/**
 * Class to represent the deck. Has a logger, a new deck for reference and the current deck.
 */
public class Deck {

	// Data before game
	public static DataOutput out = DataOutput.getInstance(Game.class);
	public static List<Card> newDeck;
	// Data during the rounds
	public List<Card> deck;

	/**
	 * Default constructor for deck. Sets the newDeck variable if it's not set. Also sets the deck variable.
	 */
	public Deck() {
		if (newDeck == null) {
			newDeck = newDeck();
		}
		deck = new ArrayList<Card>(newDeck);
	}

	/**
	 * Shuffles the deck
	 */
	void shuffleCards() {
		List<Card> newDeck = new ArrayList<Card>();
		for (int i = 0, ds = deck.size(); i < ds; i++) {
			int random = (int) (Math.random() * deck.size());
			newDeck.add(deck.remove(random));
		}
		deck = newDeck;
	}

	/**
	 * Deals two cards to each player.
	 * 
	 * @param players
	 *            - which players who wants cards
	 */
	void dealHands(AbstractPlayer[] players) {
		for (int i = 0; i < players.length * 2; i++) {
			players[i % players.length].dealCard(deck.remove(0));
		}
		for (AbstractPlayer ap : players) {
			out.writeLine("	" + ap.toString());
		}
	}

	/**
	 * Deals cards to the table. Will be the top 3 if the table is empty, else it will be the top one. Will also throw a way a card, like in
	 * "real" poker.
	 * 
	 * @param table
	 *            - The table we deal to.
	 */
	void dealToTable(Card[] table) {
		deck.remove(0);
		if (table[0] == null) {
			for (int i = 0; i < 3; i++) {
				table[i] = deck.remove(0);
			}
		} else if (table[3] == null) {
			table[3] = deck.remove(0);
		} else {
			table[4] = deck.remove(0);
		}
	}

	/**
	 * This method returns an entirely new deck.
	 * 
	 * @return List(Card) - 52 different cards.
	 */
	public List<Card> newDeck() {
		List<Card> newDeck = new ArrayList<Card>();
		for (int i = 2; i < 15; i++) {
			newDeck.add(new Card(i, Suit.DIAMOND));
			newDeck.add(new Card(i, Suit.CLUB));
			newDeck.add(new Card(i, Suit.SPADE));
			newDeck.add(new Card(i, Suit.HEART));
		}
		return newDeck;
	}
}
