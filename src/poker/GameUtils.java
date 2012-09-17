package poker;

import java.util.ArrayList;
import java.util.List;

import utilities.CardUtilities;

/**
 * Class with utility functions for the Game class. To prevent 500 line classes.
 */
public class GameUtils {

	/**
	 * This function decides which player wins given a list of possible winners and their classified hands.
	 * 
	 * @param possiblePlayers
	 *            - a list with the indexes of possible winners
	 * @param allPlayersHands
	 *            - a int[][] with the classified hands of the possible winners
	 * @return
	 */
	public static List<Integer> decideTheIndexOfWinningPlayers(List<Integer> possiblePlayers, int[][] allPlayersHands) {
		for (int currentIndexBeingCompared = 0; currentIndexBeingCompared < 6; currentIndexBeingCompared++) {
			int highestClassificationValueFound = 0;
			// Find max value for current attribute
			for (int i = 0; i < possiblePlayers.size(); i++) {
				int currentClassificationValue = allPlayersHands[i][currentIndexBeingCompared];
				if (currentClassificationValue > highestClassificationValueFound) {
					highestClassificationValueFound = currentClassificationValue;
				}
			}
			// Remove all players that have less then maximum
			for (int i = possiblePlayers.size()-1; i >= 0 ; i--) {
				int score = allPlayersHands[i][currentIndexBeingCompared];
				if (score != highestClassificationValueFound) {
					possiblePlayers.remove(i);
				}
			}
			// At this point possiblePlayers should only contain players with
			// maxValue of attribute
			if (possiblePlayers.size() == 1) {
				break;
			}
		}
		return possiblePlayers;
	}

	public static void main(String[] args) {
		List<Integer> possiblePlayers = new ArrayList<Integer>();
		possiblePlayers.add(0);
		possiblePlayers.add(1);

		int[][] allPlayersHands = new int[2][6];
		Card[] aFullHand = new Card[7];
		aFullHand[0] = new Card(10, Suit.CLUB);
		aFullHand[1] = new Card(5, Suit.SPADE);
		aFullHand[2] = new Card(3, Suit.CLUB);
		aFullHand[3] = new Card(8, Suit.HEART);
		aFullHand[4] = new Card(2, Suit.DIAMOND);
		// Player 1
		aFullHand[5] = new Card(3, Suit.DIAMOND);
		aFullHand[6] = new Card(14, Suit.DIAMOND);
		allPlayersHands[1] = CardUtilities.classification(aFullHand);
		// Player 2
		aFullHand[5] = new Card(12, Suit.DIAMOND);
		aFullHand[6] = new Card(12, Suit.CLUB);
		allPlayersHands[0] = CardUtilities.classification(aFullHand);
		System.out.println(decideTheIndexOfWinningPlayers(possiblePlayers,allPlayersHands));
	}
}
