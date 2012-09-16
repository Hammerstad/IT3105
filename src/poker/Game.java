package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import utilities.CardUtilities;
import ai.AbstractPlayer;
import ai.PlayerPersonality;
import ai.PlayerPhaseI;
import ai.PlayerPhaseII;

public class Game {

    public static void out(String s) {
//         out(s);
    }
    // Data for start of round
    public int maxReRaises = 3;
    public final List<Card> newDeck;
    public AbstractPlayer[] players;
    // Data during the rounds
    public List<Card> deck;
    public Card[] table;
    public List<AbstractPlayer> activePlayers;
//    public List<AbstractPlayer> foldingPlayers;
    public GameState state;
    // End of round
    public double pot;
    public double blinds;
    public int dealingPlayer;
    public double[] toCall;
    public int raises;

    public Game(int players) {
        this.newDeck = newDeck();
        this.players = generatePlayers(players);
        this.blinds = 10;
//        this.foldingPlayers = new LinkedList<AbstractPlayer>();
        this.toCall = new double[players];
    }

    private void setState(GameState state) {
        this.state = state;
        switch (state) {
            case START:
                deck = new ArrayList<Card>(newDeck);
                table = new Card[5];
                activePlayers = new ArrayList<AbstractPlayer>(
                        Arrays.asList(players));
                Arrays.fill(toCall, 2 * blinds);
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
                out("Winner: " + Arrays.toString(pis));
                double potShare = this.pot / pis.length;
                for (AbstractPlayer pi : pis) {
                    pi.receiveMoney(potShare);
                    pi.wins++;
                }
//                out("Pot size: "+pot);
                pot = 0;
                dealingPlayer++;
                break;
        }
    }

    void bet(GameState current, GameState next) {
        out("Bettings. " + Arrays.toString(toCall));
        out("Rounds: " + current);
        List<AbstractPlayer> foldingPlayers = new LinkedList<AbstractPlayer>();
        raises = 0;
        for (int i = 0; i < maxReRaises; i++) {
            out("ActivePlayers: " + activePlayers.size());
            for (int pl = 0; pl < activePlayers.size(); pl++) {


//                out("Bettings. " + Arrays.toString(toCall));
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
                    out("Fold, or error: " + d);
                    out("Player " + activePlayers.get(pl) + " folded");
                    foldingPlayers.add(players[plIndex]);
                } else {
                    if (d > toCall[plIndex]) {
                        raises++;
                    }
                    out("Player " + activePlayers.get(pl) + " tossed " + d + " into the pot");
                    toCall[plIndex] -= d;
                }
                out("RaisCeing: " + Arrays.toString(toCall));
            }
            int act = activePlayers.size() - foldingPlayers.size();
//            activePlayers.removeAll(this.foldingPlayers);
//            this.foldingPlayers.clear();

            out("ActivePlayers: " + Arrays.toString(activePlayers.toArray()));
            out("FoldingPlayers: " + Arrays.toString(foldingPlayers.toArray()));
            for (AbstractPlayer pi : foldingPlayers) {
//                out("Removing "+pi);
                activePlayers.remove(pi);
            }
            foldingPlayers = new LinkedList<AbstractPlayer>();
            if (act != activePlayers.size()) {
                out("ASIASFJOASJFOIJSAF; I HATE LssssssssssssssssssssssssssssssssssssssssssssssssssssssssSITSSSSSS");
            }
            out("Active players left: " + activePlayers.size());
            if (this.activePlayers.size() == 1) {
                out("WiCnner: " + this.activePlayers.get(0) + " of " + pot);
//                this.activePlayers.get(0).receiveMoney(pot);
//                this.activePlayers.get(0).wins++;
//                pot = 0;dealingPlayer++;
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
                out("No raises");
                break;
            }
            out("BetRoundEnd: " + Arrays.toString(toCall));
        }
        setState(next);
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
        for (int i = 0; i < n - 2; i++) {
            newPlayers[i] = new PlayerPhaseI(0.5);
        }
        newPlayers[n - 2] = new PlayerPhaseII(PlayerPersonality.GREEDY);
        newPlayers[n - 1] = new PlayerPhaseII(PlayerPersonality.RISKY);
        out("Created " + n + " players");
        return newPlayers;
    }

    void dealHands() {
        for (int i = 0; i < players.length * 2; i++) {
            players[i % players.length].dealCard(deck.remove(0));
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
        out("New deck created, containing " + newDeck.size() + " cards");
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
//            out("FUllHand: " + Arrays.toString(fullHand));
            scores[i] = CardUtilities.classification(fullHand);
            out("Fullhand: " + Arrays.toString(fullHand) + " Classification: "
                    + Arrays.toString(scores[i]));
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
            out("MaxValueFound: " + maxFound);
            // Remove all players that have less then maximum
            for (int i = 0; i < possiblePlayers.size(); i++) {
                int score = scores[possiblePlayers.get(i)][testIndex];
                if (score != maxFound) {
                    possiblePlayers.remove(i);
                    // out("Player "+i+" have less and is removed");
                }
            }
            out("Number of players left after " + (testIndex + 1) + " tests: "
                    + possiblePlayers.size());
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
        out("Winners: " + Arrays.toString(bests));
        return bests;
    }

    public static void main(String[] args) {
//        Card[] cards = new Card[]{new Card(10, Suit.DIAMOND),
//            new Card(11, Suit.DIAMOND)};
//        Game game = new Game(9, cards);
        Game game = new Game(5);
        for (int i = 0; i < 100; i++) {
            game.setState(GameState.START);
        }
        int i = 1;
        for (AbstractPlayer pi : game.players) {
            System.out.println("Player " + (i++) + ": " + pi.money + " Wins: " + pi.wins + " Folds: " + Arrays.toString(pi.folds));
        }
    }

    private void takeBlinds() {
//        out("Taking blinds from " + dealingPlayer);
        int[] blindsPlayer = new int[]{
            (dealingPlayer + 1) % players.length,
            (dealingPlayer + 2) % players.length
        };
        out("Taking blinds from " + players[blindsPlayer[0]] + " " + players[blindsPlayer[1]]);
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
//        out("Raise: " + Arrays.toString(toCall));
    }
}
