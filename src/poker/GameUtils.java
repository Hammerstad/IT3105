package poker;

import java.util.List;

/**
 * Class with utility functions for the Game class. To prevent 500 line classes.
 * @author eirikmh
 *
 */
public class GameUtils {

	/**
	 * This function decides which player wins given a list of possible winners and their classified hands.
	 * @param possiblePlayers - a list with the indexes of possible winners
	 * @param allPlayersHands - a int[][] with the classified hands of the possible winners
	 * @return
	 */
	public static List<Integer> decideTheIndexOfWinningPlayers(List<Integer> possiblePlayers, int[][] allPlayersHands) {
		for (int currentIndexBeingCompared = 0; currentIndexBeingCompared < 6; currentIndexBeingCompared++) {
			int highestClassificationValueFound = 0;
			// Find max value for current attribute
			for (int i = 0; i < possiblePlayers.size(); i++) {
				int currentClassificationValue = allPlayersHands[possiblePlayers.get(i)][currentIndexBeingCompared];
				if (currentClassificationValue > highestClassificationValueFound) {
					highestClassificationValueFound = currentClassificationValue;
				}
			}
			// Remove all players that have less then maximum
			for (int i = 0; i < possiblePlayers.size(); i++) {
				int score = allPlayersHands[possiblePlayers.get(i)][currentIndexBeingCompared];
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
}
