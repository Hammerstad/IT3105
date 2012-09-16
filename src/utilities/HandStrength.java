/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import poker.Card;
import poker.Suit;

/**
 *
 * @author Nicklas
 */
public class HandStrength {

    public static double handstrength(Card[] hole, Card[] flop, int opponents) {
        List<Card> deck = new ArrayList<Card>();
        for (int i = 2; i < 15; i++) {
            deck.add(new Card(i, Suit.DIAMOND));
            deck.add(new Card(i, Suit.CLUB));
            deck.add(new Card(i, Suit.SPADE));
            deck.add(new Card(i, Suit.HEART));
        }
//        System.out.println("NOF_CARDS: "+deck.size());
        //Removing card that are already in use
        for (Card c : hole) {
            deck.remove(c);
        }
//        System.out.println("NOF_CARDS: "+deck.size());
        for (Card c : flop) {
            deck.remove(c);
        }
        double[] status = new double[3];
        int wins = 0, ties = 0, losses = 0;
//        System.out.println("NOF_CARDS: "+deck.size());
        Card[] myHand = new Card[5];
        System.arraycopy(hole, 0, myHand, 0, 2);
        System.arraycopy(flop, 0, myHand, 2, 3);
        
        Card[] opponentHand = new Card[5];
        System.arraycopy(flop, 0, opponentHand, 2, 3);
        
        int[] holePower = CardUtilities.classification(myHand);
//        System.out.println("MyHand: "+Arrays.toString(holePower));
        int[] opponentPower;
        for (int i = 0; i < deck.size(); i++) {
            opponentHand[0] = deck.get(i);
            for (int j = i + 1; j < deck.size(); j++) {
                opponentHand[1] = deck.get(j);
                opponentPower = CardUtilities.classification(opponentHand);
//                System.out.println("OppHand: "+Arrays.toString(opponentHand));
//                System.out.println("OppHand: "+Arrays.toString(opponentPower));
                status[compare(holePower, opponentPower)]++;
            }
        }
        System.out.println(Arrays.toString(status));
        return Math.pow((status[0] + (status[1] / 2)) / (status[0] + status[1] + status[2]), opponents);
//        return 0;
    }
    //Return 0 if a wins, 1 if draw and 2 if b wins

    private static int compare(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] > b[i]) {
                return 0;
            } else if (a[i] < b[i]) {
                return 2;
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        Card[] hole = new Card[]{new Card(14, Suit.DIAMOND), new Card(12, Suit.CLUB)};
        Card[] flop = new Card[]{new Card(11, Suit.HEART), new Card(4, Suit.CLUB), new Card(3, Suit.HEART)};
        int opponents = 1;

        System.out.println(HandStrength.handstrength(hole, flop, opponents));
    }
}
