package poker;

import ai.ContextHolder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import utilities.CardUtilities;
import ai.AbstractPlayer;
import ai.Context;
import ai.PlayerPersonality;
import ai.PlayerPhaseI;
import ai.PlayerPhaseII;
import utilities.DataOutput;
import utilities.HandStrength;

public class Game {

    public static DataOutput out = DataOutput.getInstance(Game.class);
//	public static void out(String s) {
//		DataOutput.getInstance(Game.class).writeLine(s);
//	}
    // Data for start of round
    public int maxReRaises = 3;
    public final List<Card> newDeck;
    public AbstractPlayer[] players;
    // Data during the rounds
    public List<Card> deck;
    public Card[] table;
    public List<AbstractPlayer> activePlayers;
    // public List<AbstractPlayer> foldingPlayers;
    public GameState state;
    public ContextHolder history;
    // End of round
    public double pot;
    public double blinds;
    public int dealingPlayer;
    public double[] toCall;
    public double[] roundBet;
    public int raises;

    public Game(int players) {
        this.newDeck = newDeck();
        this.players = generatePlayers(players);
        this.blinds = 10;
        // this.foldingPlayers = new LinkedList<AbstractPlayer>();
        this.toCall = new double[players];
        this.roundBet = new double[players];
        this.history = new ContextHolder();
    }

    void bet(GameState current, GameState next) {
        out.writeLine("	Betting: "+current);
        List<AbstractPlayer> foldingPlayers = new LinkedList<AbstractPlayer>();
        raises = 0;
        for (int i = 0; i < maxReRaises; i++) {
            for (int pl = 0; pl < activePlayers.size(); pl++) {
                AbstractPlayer pi = activePlayers.get(pl);
                int plIndex = -1;
                for (int j = 0; j < players.length; j++) {
                    if (pi == players[j]) {
                        plIndex = j;
                        break;
                    }
                }
                double d = pi.bet(this, toCall[plIndex]);
                if (d < 0) {
                    history.addHistoryEntry(Context.createContext(plIndex, state, raises, activePlayers.size(), roundBet[plIndex]/(roundBet[plIndex]+pot*1.0), Context.Action.FOLD, HandStrength.handstrength(pi.getHand(), table, activePlayers.size())));
                    foldingPlayers.add(players[plIndex]);
                } else {
                    roundBet[plIndex]+=d;
                    if (d > toCall[plIndex]) {
                        history.addHistoryEntry(Context.createContext(plIndex, state, raises, activePlayers.size(), roundBet[plIndex]/(roundBet[plIndex]+pot*1.0), Context.Action.RAISE, HandStrength.handstrength(pi.getHand(), table, activePlayers.size())));
                        raises++;
                    }else {
                        history.addHistoryEntry(Context.createContext(plIndex, state, raises, activePlayers.size(), roundBet[plIndex]/(roundBet[plIndex]+pot*1.0), Context.Action.CALL, HandStrength.handstrength(pi.getHand(), table, activePlayers.size())));
                    }
                    toCall[plIndex] -= d;
                }
            }
            for (AbstractPlayer pi : foldingPlayers) {
                activePlayers.remove(pi);
            }
            foldingPlayers = new LinkedList<AbstractPlayer>();
            if (this.activePlayers.size() == 1) {
                setState(GameState.SHOWDOWN);
                return;
            }
            int sum = 0;
            for (int pl = 0; pl < players.length; pl++) {
                if (!activePlayers.contains(players[pl])) {
                    continue;
                }
                sum += toCall[pl];
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
                deck = new ArrayList<Card>(newDeck);
                table = new Card[5];
                activePlayers = new ArrayList<AbstractPlayer>(Arrays.asList(players));
                for (AbstractPlayer player : activePlayers) {
                    player.resetHand();
                }
                out.writeLine("New round");
                Arrays.fill(toCall, 2 * blinds);
                Arrays.fill(roundBet, 0);
                history.clearHistory();
                shuffleCards();
                takeBlinds();
                dealHands();
                break;
            case PREFLOP_BETTING:
                bet(GameState.PREFLOP_BETTING, GameState.FLOP);
                break;
            case FLOP:
                dealToTable();
                setState(GameState.PRETURN_BETTING);
                break;
            case PRETURN_BETTING:
                Arrays.fill(toCall, 0);
                bet(GameState.PRETURN_BETTING, GameState.TURN);
                break;
            case TURN:
                dealToTable();
                setState(GameState.PRERIVER_BETTING);
                break;
            case PRERIVER_BETTING:
                Arrays.fill(toCall, 0);
                bet(GameState.PRERIVER_BETTING, GameState.RIVER);
                break;
            case RIVER:
                dealToTable();
                setState(GameState.FINAL_BETTING);
                break;
            case FINAL_BETTING:
                Arrays.fill(toCall, 0);
                bet(GameState.FINAL_BETTING, GameState.SHOWDOWN);
                break;
            case SHOWDOWN:
                AbstractPlayer[] pis;
                if (activePlayers.size() != 1) {
                    pis = getWinner();
                } else {
                    pis = new AbstractPlayer[]{activePlayers.get(0)};
                }
                double potShare = this.pot / pis.length;
                for (AbstractPlayer pi : pis) {
                    pi.receiveMoney(potShare);
                    pi.wins++;
                }
                // out("Pot size: "+pot);
                history.pushToOpponentModeler();
                pot = 0;
                dealingPlayer++;
                break;
        }
    }

    void shuffleCards() {
        List<Card> newDeck = new ArrayList<Card>();
        for (int i = 0, ds = deck.size(); i < ds; i++) {
            int random = (int) (Math.random() * deck.size());
            newDeck.add(deck.remove(random));
        }
        deck = newDeck;
    }

    AbstractPlayer[] generatePlayers(int n) {
        AbstractPlayer[] newPlayers = new AbstractPlayer[n];
        for (int i = 0; i < n - 3; i++) {
            newPlayers[i] = new PlayerPhaseI();
        }
        newPlayers[2] = new PlayerPhaseII(PlayerPersonality.NORMAL);
        newPlayers[3] = new PlayerPhaseII(PlayerPersonality.RISK_AVERSE);
        newPlayers[4] = new PlayerPhaseII(PlayerPersonality.RISKFUL);
        return newPlayers;
    }

    void dealHands() {
        for (int i = 0; i < players.length * 2; i++) {
            players[i % players.length].dealCard(deck.remove(0));
        }
        for (AbstractPlayer ap : players) {
            out.writeLine("	"+ap.toString());
        }
        setState(GameState.PREFLOP_BETTING);
    }

    void dealToTable() {
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

    List<Card> newDeck() {
        List<Card> newDeck = new ArrayList<Card>();
        for (int i = 2; i < 15; i++) {
            newDeck.add(new Card(i, Suit.DIAMOND));
            newDeck.add(new Card(i, Suit.CLUB));
            newDeck.add(new Card(i, Suit.SPADE));
            newDeck.add(new Card(i, Suit.HEART));
        }
        return newDeck;
    }

    public AbstractPlayer[] getWinner() {
        int[][] scores = new int[activePlayers.size()][6];
        Card[] fullHand = new Card[7];
        System.arraycopy(table, 0, fullHand, 0, table.length);
        // Get hand classification
        for (int i = 0; i < activePlayers.size(); i++) {
            AbstractPlayer pi = activePlayers.get(i);
            System.arraycopy(pi.getHand(), 0, fullHand, 5, 2);
            // out("FUllHand: " + Arrays.toString(fullHand));
            scores[i] = CardUtilities.classification(fullHand);
        }
        List<Integer> bestPlayers = new LinkedList<Integer>();
        List<Integer> possiblePlayers = new LinkedList<Integer>();
        for (int i = 0; i < activePlayers.size(); i++) {
            possiblePlayers.add(i);
        }
        int testIndex = 0;
        while (testIndex < 6) {
            bestPlayers.clear();
            int maxFound = 0;
            // Find max value for current attribute
            for (int i = 0; i < possiblePlayers.size(); i++) {
                int score = scores[possiblePlayers.get(i)][testIndex];
                if (score > maxFound) {
                    maxFound = score;
                }
            }
            // Remove all players that have less then maximum
            for (int i = 0; i < possiblePlayers.size(); i++) {
                int score = scores[possiblePlayers.get(i)][testIndex];
                if (score != maxFound) {
                    possiblePlayers.remove(i);
                    // out("Player "+i+" have less and is removed");
                }
            }
            // At this point possiblePlayers should only contain players with
            // maxValue of attribute
            if (possiblePlayers.size() == 1) {
                break;
            } else {
                testIndex++;
            }
        }
        AbstractPlayer[] bests = new AbstractPlayer[possiblePlayers.size()];
        for (int i = 0; i < possiblePlayers.size(); i++) {
            bests[i] = activePlayers.get(possiblePlayers.get(i));
        }
        return bests;
    }

    public static void main(String[] args) {
        int NOF_GAMES = 1000;
        int NOF_PLAYERS = 5;
        Game game = new Game(NOF_PLAYERS);
        out.writeLine("Creating new game, players: " + NOF_PLAYERS + " Rounds: " + NOF_GAMES);
        for (int i = 0; i < NOF_GAMES; i++) {
            game.setState(GameState.START);
        }
        int i = 1;
        for (AbstractPlayer pi : game.players) {
            out.writeLine("Player " + (i++) + ": " + pi.money + " Wins: " + pi.wins + " Folds: " + Arrays.toString(pi.folds));
        }
        DataOutput.close();
    }

    private void takeBlinds() {
        int[] blindsPlayer = new int[]{(dealingPlayer + 1) % players.length, (dealingPlayer + 2) % players.length};
        players[blindsPlayer[0]].takeMoney(2 * blinds);
        toCall[blindsPlayer[0]] = 0;
        players[blindsPlayer[1]].takeMoney(blinds);
        toCall[blindsPlayer[1]] = blinds;
        pot += 3 * blinds;
    }

    public void addToCall(double raise) {

        for (int i = 0; i < toCall.length; i++) {
            toCall[i] += raise;
        }
    }
}
