package poker;

import ai.opponentmodeling.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import utilities.DataOutput;
import ai.player.AbstractPlayer;
import utilities.CardUtilities;

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
		pot = 0;
		dealingPlayer++;
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
	public void raiseRemainingToMatchPot(double raise) {
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
			players[i] = this.players[possiblePlayers.get(i)];
		}
		return players;
	}

	/**
	 * Returns the index of all active players.
	 * 
	 * @return List of integers
	 */
	public List<Integer> getIndexOfActivePlayers() {
		List<Integer> possiblePlayers = new LinkedList<Integer>();
		for (int i = 0; i < activePlayers.size(); i++) {
			possiblePlayers.add(activePlayers.get(i).getPlayerId());
		}
		return possiblePlayers;
	}

	/**
	 * Method which should be called at the end of the round. Asks all the players if they want to call the current top-bet. No raising
	 * allowed.
	 */
	public void checkRemainingPlayers(GameState state) {
        List<AbstractPlayer> foldingPlayers = new ArrayList<AbstractPlayer>();
        for (AbstractPlayer player : activePlayers) {
            int playerId = player.getPlayerId();
            if (remainingToMatchPot[playerId] != 0) {
                double bet = player.bet(this, state);
                if (bet < 0) {// This means a fold
                    foldingPlayers.add(player);
                    Game.history.addHistoryEntry(Context.createContext(player.getPlayerId(), state, activePlayers.size(), remainingToMatchPot[playerId]/(remainingToMatchPot[playerId]+pot*1.0), Context.Action.FOLD, CardUtilities.classification(player.getHand(), table)));
                    remainingToMatchPot[player.getPlayerId()] = 0;
                } else {// Player wants to raise or call, we won't allow raising so...
                    pot += remainingToMatchPot[playerId];
                    currentBetForPlayers[playerId] += remainingToMatchPot[playerId];
                    remainingToMatchPot[playerId] = 0;
                    Game.history.addHistoryEntry(Context.createContext(player.getPlayerId(), state, activePlayers.size(), remainingToMatchPot[playerId]/(remainingToMatchPot[playerId]+pot*1.0), Context.Action.CALL, CardUtilities.classification(player.getHand(), table)));
                }
            }
        }
        for (AbstractPlayer player : foldingPlayers) {
            activePlayers.remove(player);
        }
    }

    public AbstractPlayer nextActivePlayer(AbstractPlayer player) {
        int positionInPlayers = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                positionInPlayers = i;
                break;
            }
        }
//        System.out.println("Current: "+player);
//        System.out.println("Players: "+Arrays.toString(players));
//        System.out.println("Active: "+Arrays.toString(activePlayers.toArray()));
        for (int i = 1; i < players.length; i++) {
            if (activePlayers.contains(players[(positionInPlayers + i) % players.length])) {
                return players[(positionInPlayers + i) % players.length];
            }
        }
        return null;
    }
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Card card : table){
			sb.append(" [");
			sb.append(card);
			sb.append("], ");
		}
		return sb.toString();
	}
}
