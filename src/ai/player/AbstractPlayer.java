package ai.player;

import poker.Card;
import poker.GameState;
import poker.Table;

/**
 * The abstract player. Makes sure Phase I/II/III players have some similarities. This class remembers the amount of currently active bots,
 * the name of bots, the hands of the bots, the money of the bots, their personalities, and an id, as well as some statistics for wins/folds.
 */
public abstract class AbstractPlayer {

    protected static int NO = 0; // Number of player
    protected String name; // Name of the player
    protected Card[] hand; // The hand of the player
    private double money; // How much money the player has
    public int wins; // Amount of times the player has won
    public int[] folds; // Information about folding
    protected PlayerPersonality personality; //Which personality the player has
    protected int playerId;

    /**
     * Constructor for an abstract player. Creates a hand, name, etc.
     */
    public AbstractPlayer() {
        this.hand = new Card[2];
        this.folds = new int[2];
        this.playerId = NO++;
    }

    /**
     * Constructor for an abstract player. Creates a hand from a preset input,
     * name, etc.
     */
    public AbstractPlayer(Card[] hand) {
        this();
        this.hand = hand;
    }

    /**
     * Deals a card to this player
     *
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
     *
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
     *
     * @param money
     */
    public void receiveMoney(double money) {
        this.money += money;
    }

    /**
     * Takes money from a player - typically when he lost
     *
     * @param money
     */
    public void takeMoney(double money) {
        this.money -= money;
    }
    
    /**
     * Get function for money
     * @return money - this player's money
     */
    public double getMoney(){
    	return this.money;
    }

    /**
     * The betting function for a player
     *
     * @param game - the state of the game
     * @param toCall - how much you need to raise in order to match the pot
     * @return
     */
    public abstract double bet(Table table, GameState state);

    /**
     * Tostring function for player, returns the name as String
     */
    public String toString() {
        return name;
    }

    /**
     * This player folds before flop.
     *
     * @return -1
     */
    public double foldBeforeFlop() {
        this.folds[0]++;
        return -1;
    }

    /**
     * This player folds after flop.
     *
     * @return -1
     */
    public double foldAfterFlop() {
        this.folds[1]++;
        return -1;
    }

    /**
     * This returns the name of the bot, as used by toString methods
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }
    public int getPlayerId() {
        return this.playerId;
    }
}
