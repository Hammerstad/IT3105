package utilities;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import poker.Card;
import poker.Game;
import poker.Suit;

public class PreflopCalculator {

	public static int NOFGAMES = 100000;
	DecimalFormat myFormatter;
	double[][][][] table;
	String filename;
	Card[] cards;
	Game game;

	public PreflopCalculator() {
		myFormatter = new DecimalFormat("0.000");
		table = new double[2][9][13][13];
		filename = "preflop.txt";
	}

	/**
	 * Calculates a preflopTable for x players.
	 * 
	 * @param noPlayers
	 *            - number of players, 2 to 10
	 */
	public void calculatePreflopTable(int noPlayers) {
        int won = 0;
        if (noPlayers < 2) {
            noPlayers = 2;
        }
        if (noPlayers > 10) {
            noPlayers = 10;
        }
        for (int players = 2; players < noPlayers + 1; players++) {
            for (int i = 2; i < 15; i++) {
                for (int j = 2; j <= i; j++) {
                    // Unsuited
                    cards = new Card[]{new Card(i, Suit.DIAMOND), new Card(j, Suit.CLUB)};
//					game = new Game(players, cards);
//					won = game.playRoundsSimulate(NOFGAMES, cards);
                    won = preflop(players, cards, NOFGAMES);
                    table[0][players - 2][i - 2][j - 2] = won / (NOFGAMES * 1.);
                    // Suited
                    if (i == j) {
                        continue; // Because you can't have two card which has
                        // the same suit and value

                    }
                    cards = new Card[]{new Card(i, Suit.DIAMOND), new Card(j, Suit.DIAMOND)};
//					game = new Game(players, cards);
//					won = game.playRoundsSimulate(NOFGAMES, cards);
                    won = preflop(players, cards, NOFGAMES);
                    table[1][players - 2][i - 2][j - 2] = won / (NOFGAMES * 1.);
                }
            }
        }
        writeTableToFile(table);
    }

    /**
     * Writes the table to disk
     *
     * @param table - double[2][9][13][13]
     */
    private void writeTableToFile(double[][][][] table) {
        Double d;
        try {
            // Create file
            FileOutputStream fos = new FileOutputStream(filename);
            DataOutputStream dos = new DataOutputStream(fos);
            for (int players = 2; players < 11; players++) {
                for (int j = 0; j < 2; j++) {
                    dos.write(("Players: " + players + ((j == 0) ? " unsigned" : " signed") + "\n").getBytes());
                    for (int a = 1; a < 14; a++) {
                        for (int b = 1; b < 14; b++) {
                            d = table[j][players - 2][a - 1][b - 1];
                            dos.write(myFormatter.format(d).getBytes());
                            dos.write(" ".getBytes());
                        }
                        fos.write('\n');
                    }
                    fos.write('\n');
                }
            }
            // Close the output stream
            dos.close();
        } catch (Exception e) {
            // Catch exception if any
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //Generates a preflop table and writes to disk
        PreflopCalculator calculator = new PreflopCalculator();
        calculator.calculatePreflopTable(10);
    }

    public static int preflop(int players, Card[] pocketCards, int rounds) {
        //Setup
        List<Card> deck = new ArrayList<Card>();
        int won = 0;
        for (int i = 2; i < 15; i++) {
            deck.add(new Card(i, Suit.DIAMOND));
            deck.add(new Card(i, Suit.CLUB));
            deck.add(new Card(i, Suit.SPADE));
            deck.add(new Card(i, Suit.HEART));
        }
        List<Card> roundDeck = new LinkedList<Card>();
        List<Card> tempDeck;
        Card[][] cards = new Card[players][7];
        Card[] table = new Card[5];
        for (int round = 0; round < rounds; round++) {
            //Copy deck to temp
            tempDeck = new ArrayList<Card>(deck);
            //shuffleDeck
            for (int c = 0; c < deck.size(); c++) {
                roundDeck.add(tempDeck.remove((int) (Math.random() * tempDeck.size())));
            }
            //Give cards to players
            //Player 0 == testcase
            cards[0][0] = pocketCards[0];
            cards[0][1] = pocketCards[1];
            for (Card c : pocketCards) {
                roundDeck.remove(c);
            }
            //Rest of players
            for (int player = 1; player < players; player++) {
                cards[player][0] = roundDeck.remove(0);
                cards[player][1] = roundDeck.remove(0);
            }

            //Burn card before flop
            for (int tableCard = 2; tableCard < 7; tableCard++) {
                if (tableCard == 2 || tableCard == 5 || tableCard == 6) {
                    roundDeck.remove(0);
                }
                Card card = roundDeck.remove(0);
                for (int player = 0; player < players; player++) {
                    cards[player][tableCard] = card;
                }
            }
            //Find best hand
            int[][] scores = new int[players][6];
            for (int player = 0; player < players; player++) {
                scores[player] = CardUtilities.classification(cards[player]);
            }
            //Candidates for winning hand includes everybody at the start
            List<Integer> possiblePlayers = new LinkedList<>();
            for (int i = 0; i < players; i++) {
                possiblePlayers.add(i);
            }
            //iterate throughcolumns
            for (int column = 0; column < 6; column++) {
                //Find max value for column
                int max = -1;
                for (int player : possiblePlayers) {
                    if (scores[player][column] > max){
                        max = scores[player][column];
                    }
                }
                //Remove everyone not having max
                for (int player = 0;player < possiblePlayers.size();player++){
                    if (scores[player][column] < max){
                        possiblePlayers.remove(player);
                    }
                }
                if (possiblePlayers.size() == 1)break;
            }
            if (possiblePlayers.contains(0)){
                won++;
            }
        }
        return won;
    }
}
