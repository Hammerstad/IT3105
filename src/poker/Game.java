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
		int[][] allPlayersHands = classifyAllPlayersHands();
		List<Integer> indexOfActivePlayers = table.getIndexOfActivePlayers();
		List<Integer> indexOfWinners = GameUtils.decideTheIndexOfWinningPlayers(indexOfActivePlayers, allPlayersHands);
		return table.returnPlayersOfIndex(indexOfWinners);
	}

	void bet(GameState current, GameState next) {
		out.writeLine("	Betting: " + current);
		List<AbstractPlayer> foldingPlayers = new LinkedList<AbstractPlayer>();
		table.amountOfRaisesThisRound = 0;
		for (int i = 0; i < maxReRaises; i++) {
			for (int pl = 0; pl < table.activePlayers.size(); pl++) {
				AbstractPlayer pi = table.activePlayers.get(pl);
				int plIndex = -1;
				for (int j = 0; j < table.players.length; j++) {
					if (pi == table.players[j]) {
						plIndex = j;
						break;
					}
				}
				double d = pi.bet(this, table.remainingToMatchPot[plIndex]);
				if (d < 0) {
					history.addHistoryEntry(Context.createContext(plIndex, state, table.amountOfRaisesThisRound, table.activePlayers.size(),
							table.currentBetForPlayers[plIndex] / (table.currentBetForPlayers[plIndex] + table.pot * 1.0), Context.Action.FOLD,
							HandStrength.handstrength(pi.getHand(), table.table, table.activePlayers.size())));
					foldingPlayers.add(table.players[plIndex]);
				} else {
					table.currentBetForPlayers[plIndex] += d;
					if (d > table.remainingToMatchPot[plIndex]) {
						history.addHistoryEntry(Context.createContext(plIndex, state, table.amountOfRaisesThisRound, table.activePlayers.size(),
								table.currentBetForPlayers[plIndex] / (table.currentBetForPlayers[plIndex] + table.pot * 1.0), Context.Action.RAISE,
								HandStrength.handstrength(pi.getHand(), table.table, table.activePlayers.size())));
						table.raisePot(d - table.remainingToMatchPot[plIndex]);
						table.amountOfRaisesThisRound++;
					} else {
						history.addHistoryEntry(Context.createContext(plIndex, state, table.amountOfRaisesThisRound, table.activePlayers.size(),
								table.currentBetForPlayers[plIndex] / (table.currentBetForPlayers[plIndex] + table.pot * 1.0), Context.Action.CALL,
								HandStrength.handstrength(pi.getHand(), table.table, table.activePlayers.size())));
					}
					table.remainingToMatchPot[plIndex] -= d;
				}
			}
			for (AbstractPlayer pi : foldingPlayers) {
				table.activePlayers.remove(pi);
			}
			foldingPlayers = new LinkedList<AbstractPlayer>();
			if (this.table.activePlayers.size() == 1) {
				setState(GameState.SHOWDOWN);
				return;
			}
			int sum = 0;
			for (int pl = 0; pl < table.players.length; pl++) {
				if (!table.activePlayers.contains(table.players[pl])) {
					continue;
				}
				sum += table.remainingToMatchPot[pl];
			}
			if (sum == 0) {
				break;
			}
		}
		setState(next);
	}

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
			AbstractPlayer[] pis;
			if (table.activePlayers.size() != 1) {
				pis = getWinner();
			} else {
				pis = new AbstractPlayer[] { table.activePlayers.get(0) };
			}
			double potShare = table.pot / pis.length;
			for (AbstractPlayer pi : pis) {
				pi.receiveMoney(potShare);
				pi.wins++;
			}
			// out("Pot size: "+pot);
			history.pushToOpponentModeler();
			table.pot = 0;
			table.dealingPlayer++;
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
		int NOF_GAMES = 10;
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
