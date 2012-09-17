package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import utilities.CardUtilities;
import utilities.DataOutput;
import utilities.HandStrength;
import ai.AbstractPlayer;
import ai.Context;
import ai.ContextHolder;
import ai.PlayerGenerator;

public class Game {

	public static DataOutput out = DataOutput.getInstance(Game.class);

	// Data for start of round
	public int maxReRaises = 3;
	public final List<Card> newDeck;
	// Data during the rounds
	private Deck deck;
	public Table table;
	public GameState state;
	public ContextHolder history;

	/*
	 * CONSTRUCTORS
	 */

	/**
	 * Constructor for game. Input specifies how many players you want.
	 * 
	 * @param players
	 *            - amount of players.
	 */
	public Game(int players) {
		this();
		AbstractPlayer[] playerTable = new PlayerGenerator().generateRandomPhasePlayers(players);
		table = new Table(playerTable);
	}

	/**
	 * Constructor for game. Input specifies which players you want.
	 * 
	 * @param playerTable
	 *            - AbstractPlayer[] with the players you want in the game .
	 */
	public Game(AbstractPlayer[] playerTable) {
		this();
		table = new Table(playerTable);
	}

	/**
	 * Default constructor. Creates a game for 10 people.</br> NOT TO BE CALLED EXPLICITLY, ONLY IMPLICITLY!
	 */
	private Game() {
		deck = new Deck();
		newDeck = deck.newDeck();
		this.history = new ContextHolder();
	}

	/*
	 * IMPORTANT GAME FUNCTIONS
	 */

	/**
	 * Checks who wins the game
	 * 
	 * @return AbstractPlayer[] - list of winners. Yes, more than one can win (that's a draw).
	 */
	public AbstractPlayer[] getWinner() {
		if (table.activePlayers.size() == 1)
			return new AbstractPlayer[] { table.activePlayers.get(0) };
		int[][] allPlayersHands = classifyAllPlayersHands();
		List<Integer> indexOfActivePlayers = table.getIndexOfActivePlayers();
		List<Integer> indexOfWinners = GameUtils.decideTheIndexOfWinningPlayers(indexOfActivePlayers, allPlayersHands);
		return table.returnPlayersOfIndex(indexOfWinners);
	}

	/**
	 * Betting function of the game. Sends the game state to the next state after done.
	 * 
	 * @param current
	 *            - current game state.
	 * @param next
	 *            - next game state.
	 */
	public void bet(GameState current, GameState next) {
		out.writeLine("	Betting: " + current);
		for (int i = 0; i < maxReRaises; i++) {
			List<AbstractPlayer> foldingPlayers = new ArrayList<AbstractPlayer>();
			// Iterate through the players and see what they decide to do
			for (AbstractPlayer player : table.activePlayers) {
				int id = player.getPlayerId();
				double bet = player.bet(this, table.remainingToMatchPot[id]);
				if (bet < 0) { // THIS MEANS FOLD
					foldingPlayers.add(player);
				} else { // RAISE AND CALLING
					if (bet > table.remainingToMatchPot[id]) { // THIS IS ONLY FOR RAISE
						table.raiseRemainingToMatchPot(bet - table.remainingToMatchPot[id]);
					}
					table.pot += bet;
					table.remainingToMatchPot[id] -= bet;
					table.currentBetForPlayers[id] += bet;
				}
			}
			// Remove folding players from active players
			for (AbstractPlayer foldingPlayer : foldingPlayers) {
				out.writeLine("removing folding player" + foldingPlayer.getPlayerId());
				table.activePlayers.remove(foldingPlayer);
			}
			// If only one player remains, go to showdown
			if (table.activePlayers.size() == 1) {
				out.writeLine("only one left");
				setState(GameState.SHOWDOWN);
				return;
			}
			// Check if all players have called each other
			for (AbstractPlayer player : table.activePlayers) {
				if (table.remainingToMatchPot[player.getPlayerId()] != 0)
					continue;
				setState(next);
				return;
			}
		}
		// Time to go to the next phase of the game
		setState(next);
	}

	/**
	 * The main function of the game. Changes the state of the game and makes sure everything is played according to poker rules.
	 * 
	 * @param state
	 *            - which state to change to.
	 */
	private void setState(GameState state) {
		this.state = state;
		switch (state) {
		case START:
			deck.deck = new ArrayList<Card>(newDeck);
			table.newRound();
			history.clearHistory();
			deck.shuffleCards();
			deck.dealHands(table.players);
			setState(GameState.PREFLOP_BETTING);
			break;
		case PREFLOP_BETTING:
			bet(GameState.PREFLOP_BETTING, GameState.FLOP);
			break;
		case FLOP:
			deck.dealToTable(table.table);
			setState(GameState.PRETURN_BETTING);
			break;
		case PRETURN_BETTING:
			Arrays.fill(table.remainingToMatchPot, 0);
			bet(GameState.PRETURN_BETTING, GameState.TURN);
			break;
		case TURN:
			deck.dealToTable(table.table);
			setState(GameState.PRERIVER_BETTING);
			break;
		case PRERIVER_BETTING:
			Arrays.fill(table.remainingToMatchPot, 0);
			bet(GameState.PRERIVER_BETTING, GameState.RIVER);
			break;
		case RIVER:
			deck.dealToTable(table.table);
			setState(GameState.FINAL_BETTING);
			break;
		case FINAL_BETTING:
			Arrays.fill(table.remainingToMatchPot, 0);
			bet(GameState.FINAL_BETTING, GameState.SHOWDOWN);
			break;
		case SHOWDOWN:
			AbstractPlayer[] winners = getWinner();
			double potShare = table.pot / winners.length;
			for (AbstractPlayer winner : winners) {
				winner.receiveMoney(potShare);
				winner.wins++;
			}
			history.pushToOpponentModeler();
			break;
		}
	}

	/*
	 * AUXILIARY FUNCTIONS
	 */

	/**
	 * Classifies all the active players' hands.
	 * 
	 * @return int[][] - all hands classified.
	 */
	private int[][] classifyAllPlayersHands() {
		int[][] allPlayersHands = new int[table.activePlayers.size()][6];
		Card[] aFullHand = new Card[7];
		// Copy the table into the first five elements of aFullHand
		System.arraycopy(table.table, 0, aFullHand, 0, table.table.length);
		// Get hand classification
		for (int i = 0; i < table.activePlayers.size(); i++) {
			AbstractPlayer selectedPlayer = table.activePlayers.get(i);
			// Copy the selected player's hand to the two last elements of aFullHand
			System.arraycopy(selectedPlayer.getHand(), 0, aFullHand, 5, 2);
			// Classify this player's hand and store it in allPlayersHands
			allPlayersHands[i] = CardUtilities.classification(aFullHand);
		}
		return allPlayersHands;
	}

	/*
	 * MAIN FUNCTION
	 */

	/**
	 * Main function, currently for testing that everything works.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int NOF_GAMES = 100;
		int NOF_PLAYERS = 6;
		Game game = new Game(NOF_PLAYERS);
		out.writeLine("Creating new game, players: " + NOF_PLAYERS + " Rounds: " + NOF_GAMES);
		for (int i = 0; i < NOF_GAMES; i++) {
			game.setState(GameState.START);
		}
		int i = 1;
		for (AbstractPlayer pi : game.table.players) {
			out.writeLine("Player " + (i++) + ": " + pi.money + " Wins: " + pi.wins + " Folds: " + Arrays.toString(pi.folds));
		}
		DataOutput.close();
	}

}
