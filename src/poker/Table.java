package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import utilities.DataOutput;

import ai.AbstractPlayer;

/**
 * Class to represent the table. Has a logger. Handles the players, both currently in the game and the ones who have folded. Also handles
 * the cards at the table, the pot, the blinds, who is the dealer, how much players need to bet to match the pot, the current bet for all
 * players and the amount of raises so far this round.
 */
public class Table {

	// Before round
	public static DataOutput out = DataOutput.getInstance(Game.class);
	public AbstractPlayer[] players;
	public Card[] table;
	// During round
	public List<AbstractPlayer> activePlayers;
	// End of round
	public double pot;
	public double blind;
	public int dealingPlayer;
	public double[] remainingToMatchPot;
	public double[] currentBetForPlayers;
	public int amountOfRaisesThisRound;

	/**
	 * Default constructor for a table. It:</br> - Creates a new empty table.</br> - Sets players to be equal the input.</br> - Sets active
	 * players.</br> - Sets blinds. </br> - Creates empty tables for remaining bets to match the pot and current bets.
	 * 
	 * @param players
	 *            - a list of AbstractPlayer, will be the players.
	 */
	public Table(AbstractPlayer[] players) {
		this.table = new Card[5];
		this.players = players;
		activePlayers = new ArrayList<AbstractPlayer>(Arrays.asList(players));
		this.blind = 10;
		this.remainingToMatchPot = new double[players.length];
		this.currentBetForPlayers = new double[players.length];
	}

	/**
	 * This method initiates a new round. It:</br> - Sets all players as active again.</br> - Reset all players' hands.</br> - Resets how
	 * much each player needs to bet to match the pot.</br> - Resets current bets.</br> - Takes big blind and small blind.
	 */
	public void newRound() {
		activePlayers = new ArrayList<AbstractPlayer>(Arrays.asList(players));
		for (AbstractPlayer player : activePlayers) {
			player.resetHand();
		}
		out.writeLine("New round");
		Arrays.fill(remainingToMatchPot, 2 * blind);
		Arrays.fill(currentBetForPlayers, 0);
		takeBlinds();
	}

	/**
	 * This method withdraws blinds from players at the beginning of the round
	 */
	public void takeBlinds() {
		// Big blind
		int bigBlind = (dealingPlayer + 1) % players.length;
		activePlayers.get(bigBlind).takeMoney(2 * blind);
		remainingToMatchPot[bigBlind] = 0;
		// Big blind
		int smallBlind = (dealingPlayer + 2) % players.length;
		activePlayers.get(smallBlind).takeMoney(blind);
		remainingToMatchPot[smallBlind] = blind;
		// Update the pot
		pot += 3 * blind;
	}

	/**
	 * This method makes sure the amount every player needs to bet to match the current pot is updated frequently.
	 * 
	 * @param raise
	 *            - how much to increase the pot with
	 */
	public void raisePot(double raise) {
		for (int i = 0; i < remainingToMatchPot.length; i++) {
			remainingToMatchPot[i] += raise;
		}
	}

	/**
	 * Returns a subset of activePlayers specified from a list of integers
	 * 
	 * @param possiblePlayers
	 *            - which players you want returned
	 * @return AbstractPlayer[] - a list of players
	 */
	public AbstractPlayer[] returnPlayersOfIndex(List<Integer> possiblePlayers) {
		AbstractPlayer[] players = new AbstractPlayer[possiblePlayers.size()];
		for (int i = 0; i < possiblePlayers.size(); i++) {
			players[i] = activePlayers.get(possiblePlayers.get(i));
		}
		return players;
	}

	/**
	 * Returns the index of all active players.
	 * @return List of integers
	 */
	public List<Integer> getIndexOfActivePlayers() {
		List<Integer> possiblePlayers = new LinkedList<Integer>();
		for (int i = 0; i < activePlayers.size(); i++) {
			possiblePlayers.add(i);
		}
		return possiblePlayers;
	}
}
