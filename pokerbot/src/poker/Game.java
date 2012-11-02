package poker;

import ai.opponentmodeling.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utilities.CardUtilities;
import utilities.DataOutput;
import ai.player.AbstractPlayer;
import ai.opponentmodeling.ContextHolder;
import ai.player.PlayerGenerator;

public class Game {

    public static DataOutput out = DataOutput.getInstance(Game.class);
    // Data for start of round
    public int maxReRaises = 3;
    public final List<Card> newDeck;
    // Data during the rounds
    private Deck deck;
    public Table table;
    public GameState state;
    public static ContextHolder history;

    /*
     * CONSTRUCTORS
     */
    /**
     * Constructor for game. Input specifies how many players you want.
     *
     * @param players - amount of players.
     */
    public Game(int players) {
        this();
        AbstractPlayer[] playerTable = new PlayerGenerator().generateRandomPhasePlayers(players);
        table = new Table(playerTable);
    }

    /**
     * Constructor for game. Input specifies which players you want.
     *
     * @param playerTable - AbstractPlayer[] with the players you want in the
     * game .
     */
    public Game(AbstractPlayer[] playerTable) {
        this();
        table = new Table(playerTable);
    }

    /**
     * Default constructor. Creates a game for 10 people.</br> NOT TO BE CALLED
     * EXPLICITLY, ONLY IMPLICITLY!
     */
    private Game() {
        deck = new Deck();
        newDeck = deck.newDeck();
        history = new ContextHolder();
    }

    /*
     * IMPORTANT GAME FUNCTIONS
     */
    /**
     * Checks who wins the game
     *
     * @return AbstractPlayer[] - list of winners. Yes, more than one can win
     * (that's a draw).
     */
    public AbstractPlayer[] getWinner() {
        if (table.activePlayers.size() == 1) {
            return new AbstractPlayer[]{table.activePlayers.get(0)};
        }
        int[][] allPlayersHands = classifyAllPlayersHands();
        List<Integer> indexOfActivePlayers = table.getIndexOfActivePlayers();
        List<Integer> indexOfWinners = GameUtils.decideTheIndexOfWinningPlayers(indexOfActivePlayers, allPlayersHands);
        return table.returnPlayersOfIndex(indexOfWinners);
    }

    /**
     * Betting function of the game. Sends the game state to the next state
     * after done.
     *
     * @param current - current game state.
     * @param next - next game state.
     */
    public void bet(GameState current, GameState next) {
        out.writeLine("Betting: " + current);
        double sum = 0;
        int raises = 0;
        List<AbstractPlayer> foldingPlayers = new ArrayList<AbstractPlayer>();
        int playerIndex = (table.dealingPlayer + 2) % table.players.length;
        AbstractPlayer player = table.players[playerIndex];
        int playersPlayed = 0;
        do {
            foldingPlayers.clear();
            player = table.nextActivePlayer(player);
            playersPlayed++;
            out.writeLine("Players turn: " + player);
            if (player == null) {
                setState(GameState.SHOWDOWN);
                return;
            }
            int id = player.getPlayerId();
            double bet;
            if (raises < 3) {
                bet = player.bet(table, state);
            } else {
                table.checkRemainingPlayers(state);
                setState(next);
                return;
            }
            if (bet < 0) {
                foldingPlayers.add(player);
                history.addHistoryEntry(Context.createContext(id, state, table.activePlayers.size(), table.remainingToMatchPot[id]/(table.remainingToMatchPot[id]+table.pot*1.0), Context.Action.FOLD, CardUtilities.classification(player.getHand(), table.table)));
                table.remainingToMatchPot[id] = 0;
            } else {
                if (bet > table.remainingToMatchPot[id]) {
                    table.raiseRemainingToMatchPot(bet - table.remainingToMatchPot[id]);
                    raises++;
                    history.addHistoryEntry(Context.createContext(id, state, table.activePlayers.size(), table.remainingToMatchPot[id]/(table.remainingToMatchPot[id]+table.pot*1.0), Context.Action.RAISE, CardUtilities.classification(player.getHand(), table.table)));
                }else {
                    history.addHistoryEntry(Context.createContext(id, state, table.activePlayers.size(), table.remainingToMatchPot[id]/(table.remainingToMatchPot[id]+table.pot*1.0), Context.Action.CALL, CardUtilities.classification(player.getHand(), table.table)));
                }
                table.pot += bet;
                table.remainingToMatchPot[id] -= bet;
                table.currentBetForPlayers[id] += bet;
            }
            for (AbstractPlayer fplayer : foldingPlayers) {
                table.activePlayers.remove(fplayer);
            }
            // If only one player remains, go to showdown
            if (table.activePlayers.size() == 1) {
                setState(GameState.SHOWDOWN);
                return;
            }
            //Calculate remainings
            sum = 0;
            for (double r : table.remainingToMatchPot) {
                sum += r;
            }
            if(playersPlayed > 50){
                table.checkRemainingPlayers(state);
                setState(next);
                return;
            }
            //System.out.println("SPIN"+sum+" "+playersPlayed+" "+table.activePlayers.size() + " " + Arrays.toString(table.remainingToMatchPot));
        } while (sum != 0 || playersPlayed < table.activePlayers.size());	
        setState(next);
    }
    /**
     * The main function of the game. Changes the state of the game and makes
     * sure everything is played according to poker rules.
     *
     * @param state - which state to change to.
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
                // Give winners their money
                history.pushToOpponentModeler(table.activePlayers);
                AbstractPlayer[] winners = getWinner();
                double potShare = table.pot / winners.length;
                for (AbstractPlayer winner : winners) {
                    winner.receiveMoney(potShare);
                    winner.wins++;
                    out.writeLine("Table: " + table.toString());
                    out.writeLine("Winner: " + winner.getName() + " Pot: " + potShare);
                }
                // Withdraw whatever the players played for
                for (AbstractPlayer player : table.players) {
                    player.takeMoney(table.currentBetForPlayers[player.getPlayerId()]);
                    out.writeLine(player.getName() + " lost: " + table.currentBetForPlayers[player.getPlayerId()]);
                }
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
        int NOF_GAMES = 1000;

        int NOF_PLAYERS = 6;
        PlayerGenerator pg = new PlayerGenerator();
        Game game = new Game(pg.checkList7());
        out.writeLine("Creating new game, players: " + NOF_PLAYERS + " Rounds: " + NOF_GAMES);

        for (int i = 0; i < NOF_GAMES; i++) {
            game.setState(GameState.START);
            if(i%10==0)System.out.println(i);
        }
        for (AbstractPlayer pi : game.table.players) {
            out.writeLine(pi.getName() + ": " + pi.getMoney() + " Wins: " + pi.wins + " Folds: " + Arrays.toString(pi.folds));
            System.out.println(pi.getName() + ": " + pi.getMoney() + " Wins: " + pi.wins + " Folds: " + Arrays.toString(pi.folds));
        }
        DataOutput.close();
    }
}
