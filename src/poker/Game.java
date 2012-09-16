package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utilities.CardUtilities;

import ai.Player;
import java.util.LinkedList;

public class Game {
    public static void out(String s) {
//        System.out.println(s);
    }
    
    // Data for start of round

    int maxReRaises = 3;
    final List<Card> newDeck;
    PlayerInterface[] players;
    Card[] presetHand;
    // Data during the rounds
    List<Card> deck;
    Card[] table;
    List<PlayerInterface> activePlayers;
    GameState state;
    //End of round
    Player winner;

    public Game(int players) {
        this.newDeck = newDeck();
        this.players = generatePlayers(players);
        this.presetHand = null;
    }

    public Game(int players, Card[] presetHand) {
        out("Creating new game");
        this.newDeck = newDeck();
        this.players = generatePlayers(players);
        this.presetHand = presetHand;

    }

    public void playRound(int rounds) {
        for (int i = 0; i < rounds; i++) {
            deck = new ArrayList<Card>(newDeck);
            table = new Card[5];
            activePlayers = new ArrayList<PlayerInterface>(
                    Arrays.asList(players));
            setState(GameState.START);
        }
    }

    public int playRounds(int rounds) {
        int won = 0;
        for (int i = 0; i < rounds; i++) {
            deck = new ArrayList<Card>(newDeck);
            table = new Card[5];
            activePlayers = new ArrayList<PlayerInterface>(
                    Arrays.asList(players));
            setState(GameState.START);
            if (winner == players[0]) {
                won++;
            }
        }
        return won;
    }

    public int playRoundsSimulate(int rounds, Card[] myCards) {
        out("Starting simulation for "+Arrays.toString(myCards));
        int won = 0;
        for (int i = 0; i < rounds; i++) {
            //resetting deck
            deck = new ArrayList<Card>(newDeck);
            table = new Card[5];
            activePlayers = new ArrayList<PlayerInterface>(Arrays.asList(players));
            shuffleCards();
            out("Round "+i);
            out("Deck: "+Arrays.toString(deck.toArray()));

            //Deal all cards, no betting needed
            this.presetHand = myCards;
            dealHands();
            //Flop
            dealToTable();
            //Turn
            dealToTable();
            //River
            dealToTable();
            out("Cards dealt, myHand: "+Arrays.toString(players[0].getHand()));
            out("On table: "+Arrays.toString(table));

            PlayerInterface[] pi = getWinner();
            out("Winner: "+Arrays.toString(pi));
//            System.out.println("Winner: "+Arrays.toString(pi));
            if (pi.length == 1 && pi[0] == players[0])won++;
        }
        out("Won: "+won);
        return won;
    }

    void setState(GameState state) {
        this.state = state;
        switch (state) {
            case START:
                shuffleCards();
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
                bet(GameState.PRETURN_BETTING, GameState.TURN);
                break;
            case TURN:
                dealToTable();
                setState(GameState.PRERIVER_BETTING);
                break;
            case PRERIVER_BETTING:
                bet(GameState.PRERIVER_BETTING, GameState.RIVER);
                break;
            case RIVER:
                dealToTable();
                setState(GameState.FINAL_BETTING);
                break;
            case FINAL_BETTING:
                bet(GameState.FINAL_BETTING, GameState.SHOWDOWN);
                break;
            case SHOWDOWN:
                // System.out.println("SHOWDOWN: " + players[0].getHand()[0].suit
                // + players[0].getHand()[0].value + " "
                // + players[0].getHand()[1].suit
                // + players[0].getHand()[1].value);
                //TODO: Decide winner
                break;
        }
    }

    void bet(GameState current, GameState next) {
        // TODO: BET
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

    PlayerInterface[] generatePlayers(int n) {
        PlayerInterface[] newPlayers = new PlayerInterface[n];
        for (int i = 0; i < n; i++) {
            newPlayers[i] = new Player();
        }
        out("Created "+n+" players");
        return newPlayers;
    }

    void dealHands() {
        if (presetHand != null) {
            deck.remove(presetHand[0]);
            deck.remove(presetHand[1]);
        }
        for (int i = 0; i < players.length * 2; i++) {
            players[i % players.length].dealCard(deck.remove(0));
        }
        if (presetHand != null) {
            deck.add(players[0].getHand()[0]);
            deck.add(players[0].getHand()[1]);
            players[0].resetHand();
            players[0].dealCard(presetHand[0]);
            players[0].dealCard(presetHand[1]);
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
        for (int i = 1; i < 14; i++) {
            newDeck.add(new Card(i, Suit.DIAMOND));
            newDeck.add(new Card(i, Suit.CLUB));
            newDeck.add(new Card(i, Suit.SPADE));
            newDeck.add(new Card(i, Suit.HEART));
        }
        out("New deck created, containing "+newDeck.size()+" cards");
        return newDeck;
    }

    public PlayerInterface[] getWinner() {
        int[][] scores = new int[players.length][6];
        Card[] fullHand = new Card[7];
        System.arraycopy(table, 0, fullHand, 0, table.length);
        //Get hand classification
        for (int i = 0; i < players.length; i++) {
            PlayerInterface pi = players[i];
            System.arraycopy(pi.getHand(), 0, fullHand, 5, 2);
            scores[i] = CardUtilities.classification(fullHand);
            out("Fullhand: "+Arrays.toString(fullHand)+" Classification: "+Arrays.toString(scores[i]));
        }
        List<Integer> bestPlayers = new LinkedList<Integer>();
        List<Integer> possiblePlayers = new LinkedList<Integer>();
        for (int i = 0; i < players.length; i++) {
            possiblePlayers.add(i);
        }
        int testIndex = 0;
        while (testIndex < 6) {
            bestPlayers.clear();
            int maxFound = 0;
            //Find max value for current attribute
            for (int i = 0; i < possiblePlayers.size(); i++) {
                int score = scores[possiblePlayers.get(i)][testIndex];
                if (score > maxFound) {
                    maxFound = score;
                }
            }
            out("MaxValueFound: "+maxFound);
            //Remove all players that have less then maximum
            for (int i = 0; i < possiblePlayers.size(); i++) {
                int score = scores[possiblePlayers.get(i)][testIndex];
                if (score != maxFound) {
                    possiblePlayers.remove(i);
//                    System.out.println("Player "+i+" have less and is removed");
                }
            }
            out("Number of players left after "+(testIndex+1)+" tests: "+possiblePlayers.size());
            //At this point possiblePlayers should only contain players with maxValue of attribute
            if (possiblePlayers.size() == 1)break;
            else {
                testIndex++;
            }
        }
        PlayerInterface[] bests = new PlayerInterface[possiblePlayers.size()];
        for (int i = 0; i < possiblePlayers.size(); i++) {
            bests[i] = players[possiblePlayers.get(i)];
        }
        out("Winners: "+Arrays.toString(bests));
        return bests;
    }

    public static void main(String[] args) {
        Card[] cards = new Card[]{new Card(10, Suit.DIAMOND),
            new Card(11, Suit.DIAMOND)};
        Game game = new Game(9, cards);
        // for (PlayerInterface player : game.players) {
        // Card[] hand = player.getHand();
        // Card[] table = game.table;
        // List<Card> allCards = new ArrayList<Card>(Arrays.asList(hand));
        // allCards.addAll(Arrays.asList(table));
        // allCards.removeAll(Collections.singletonList(null));
        // Card[] handAsList = new Card[allCards.size()];
        // handAsList = allCards.toArray(handAsList);
        //
        // checkShit(handAsList);
        // }
        game.playRound(10);
        Card[] straightFlush = new Card[]{new Card(13, Suit.CLUB),
            new Card(12, Suit.CLUB), new Card(11, Suit.CLUB),
            new Card(10, Suit.CLUB), new Card(9, Suit.CLUB)};
        Card[] fullHouse = new Card[]{new Card(13, Suit.CLUB),
            new Card(13, Suit.SPADE), new Card(13, Suit.DIAMOND),
            new Card(12, Suit.CLUB), new Card(12, Suit.DIAMOND)};
        Card[] fourOfAKind = new Card[]{new Card(13, Suit.CLUB),
            new Card(13, Suit.CLUB), new Card(11, Suit.CLUB),
            new Card(13, Suit.CLUB), new Card(13, Suit.CLUB)};
        Card[] threeOfAKind = new Card[]{new Card(13, Suit.DIAMOND),
            new Card(13, Suit.CLUB), new Card(11, Suit.CLUB),
            new Card(13, Suit.CLUB), new Card(9, Suit.CLUB)};
        Card[] twoPairs = new Card[]{new Card(13, Suit.DIAMOND),
            new Card(13, Suit.CLUB), new Card(11, Suit.CLUB),
            new Card(11, Suit.CLUB), new Card(9, Suit.CLUB)};
        Card[] pair = new Card[]{new Card(13, Suit.DIAMOND),
            new Card(13, Suit.CLUB), new Card(11, Suit.CLUB),
            new Card(10, Suit.CLUB), new Card(9, Suit.CLUB)};
        Card[] highcard = new Card[]{new Card(13, Suit.DIAMOND),
            new Card(3, Suit.CLUB), new Card(11, Suit.CLUB),
            new Card(10, Suit.CLUB), new Card(9, Suit.CLUB)};
        checkShit("StraightFlush", straightFlush);
        checkShit("Fullhouse", fullHouse);
        checkShit("Four of a kind", fourOfAKind);
        checkShit("Three of a kind", threeOfAKind);
        checkShit("Two pairs", twoPairs);
        checkShit("Pair", pair);
        checkShit("Highcard", highcard);

    }

    public static void checkShit(String name, Card[] handAsList) {
        // LOTS OF CHECKS AND SYSOUTS:
        System.out.println("Hand: " + name + " " + Arrays.toString(handAsList));
        System.out.println("Classification: " + Arrays.toString(CardUtilities.classification(handAsList)));

    }
}

enum GameState {

    START, PREFLOP_BETTING, FLOP, PRETURN_BETTING, TURN, PRERIVER_BETTING, RIVER, FINAL_BETTING, SHOWDOWN;
}
