package ai;

import poker.Card;
import poker.Game;

public abstract class AbstractPlayer {
	protected static int NO = 0; // Number of player
	protected String name; // Name of the player
	protected Card[] hand; // The hand of the player
	public double money; // How much money the player has
	public int wins; // Amount of times the player has won
	public int[] folds; // Information about folding
	protected PlayerPersonality personality; //Which personality the player has

	/**
	 * Constructor for an abstract player. Creates a hand, name, etc.
	 */
	public AbstractPlayer() {
		this.hand = new Card[2];
		this.folds = new int[2];
                
		NO++;
	}

	/**
	 * Constructor for an abstract player. Creates a hand from a preset input, name, etc.
	 */
	public AbstractPlayer(Card[] hand) {
		this();
		this.hand = hand;
	}
	
	/**
	 * Deals a card to this player
	 * @param card
	 */
	public void dealCard(Card card) {
		if (this.hand[0] == null) {
			this.hand[0] = card;
		} else if (this.hand[1] == null) {
			this.hand[1] = card;
		}
	}

	/**
	 * Returns the hand of a player
	 * @return hand - two cards
	 */
	public Card[] getHand() {
		return this.hand;
	}

	/**
	 * Resets the hand of a player to an empty hand
	 */
	public void resetHand() {
		this.hand = new Card[2];
	}

	/**
	 * Gives money to a player - typically when he won
	 * @param money
	 */
	public void receiveMoney(double money) {
		this.money += money;
	}

	/**
	 * Takes money from a player - typically when he lost
	 * @param money
	 */
	public void takeMoney(double money) {
		this.money -= money;
	}

	/**
	 * The betting function for a player
	 * @param game - the state of the game
	 * @param toCall - how much you need to raise in order to match the pot
	 * @return
	 */
	public abstract double bet(Game game, double toCall);

	/**
	 * Tostring function for player, returns the name as String
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * This player folds before flop.
	 * @return -1
	 */
	public double foldBeforeFlop(){
		this.folds[0]++;
		return -1;
	}
	
	/**
	 * This player folds after flop.
	 * @return -1
	 */
	public double foldAfterFlop(){
		this.folds[1]++;
		return -1;
	}
}
