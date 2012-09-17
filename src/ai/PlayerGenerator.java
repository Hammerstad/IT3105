package ai;

import java.util.List;

/**
 * A generator for players. If only used once/a few times it can be applied as follows:</br> AbstractPlayer[] players = new
 * PlayerGenerator.(pick_a_method);
 */
public class PlayerGenerator {

	/**
	 * Default constructor. Does nothing at all. NOTHING I TELL YOU!
	 */
	public PlayerGenerator() {

	}

	// PHASE I PLAYERS

	/**
	 * Generates n phase I players with random personalities.
	 * 
	 * @param n
	 *            - amount of players
	 * @return AbstractPlayer[] - array of phase I players
	 */
	public AbstractPlayer[] generatePhaseIPlayers(int n) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseI();
		}
		return newPlayers;
	}

	/**
	 * Generates n phase I players with a specific personality
	 * 
	 * @param n
	 *            - amount of players
	 * @param personality
	 *            - set personality for the players
	 * @return AbstractPlayer[] - array of phase I players
	 */
	public AbstractPlayer[] generatePhaseIPlayers(int n, PlayerPersonality personality) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseI(personality);
		}
		return newPlayers;
	}

	/**
	 * Generates a set of phase I players from a list of PlayerPersonalities
	 * 
	 * @param personalities
	 *            - a list of personalities
	 * @return AbstractPlayer[] - one player per personality in the personalities list.
	 */
	public AbstractPlayer[] generatePhaseIPlayers(List<PlayerPersonality> personalities) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[personalities.size()];
		for (int i = 0; i < personalities.size(); i++) {
			newPlayers[i] = new PlayerPhaseI(personalities.get(i));
		}
		return newPlayers;
	}

	// PHASE II PLAYERS

	/**
	 * Generates n phase II players with random personalities.
	 * 
	 * @param n
	 *            - amount of players
	 * @return AbstractPlayer[] - array of phase II players
	 */
	public AbstractPlayer[] generatePhaseIIPlayers(int n) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseII();
		}
		return newPlayers;
	}

	/**
	 * Generates n phase II players with a specific personality
	 * 
	 * @param n
	 *            - amount of players
	 * @param personality
	 *            - set personality for the players
	 * @return AbstractPlayer[] - array of phase II players
	 */
	public AbstractPlayer[] generatePhaseIIPlayers(int n, PlayerPersonality personality) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseII(personality);
		}
		return newPlayers;
	}

	/**
	 * Generates a set of phase II players from a list of PlayerPersonalities
	 * 
	 * @param personalities
	 *            - a list of personalities
	 * @return AbstractPlayer[] - one player per personality in the personalities list.
	 */
	public AbstractPlayer[] generatePhaseIIPlayers(List<PlayerPersonality> personalities) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[personalities.size()];
		for (int i = 0; i < personalities.size(); i++) {
			newPlayers[i] = new PlayerPhaseII(personalities.get(i));
		}
		return newPlayers;
	}

	// PHASE III PLAYERS

	/**
	 * Generates n phase III players with random personalities.
	 * 
	 * @param n
	 *            - amount of players
	 * @return AbstractPlayer[] - array of phase III players
	 */
	public AbstractPlayer[] generatePhaseIIIPlayers(int n) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseIII();
		}
		return newPlayers;
	}

	/**
	 * Generates n phase III players with a specific personality
	 * @param n - amount of players
	 * @param personality - set personality for the players
	 * @return AbstractPlayer[] - array of phase III players
	 */
	public AbstractPlayer[] generatePhaseIIIPlayers(int n, PlayerPersonality personality) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseIII(personality);
		}
		return newPlayers;
	}

	/**
	 * Generates a set of phase II players from a list of PlayerPersonalities
	 * 
	 * @param personalities
	 *            - a list of personalities
	 * @return AbstractPlayer[] - one player per personality in the personalities list.
	 */
	public AbstractPlayer[] generatePhaseIIIPlayers(List<PlayerPersonality> personalities) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[personalities.size()];
		for (int i = 0; i < personalities.size(); i++) {
			newPlayers[i] = new PlayerPhaseIII(personalities.get(i));
		}
		return newPlayers;
	}

	// RANDOM PHASE PLAYERS

	/**
	 * Generates a list of random phase players with random personalities
	 * @param n - amount of players
	 * @return AbstractPlayer[] - n random phase players with random personalities
	 */
	public AbstractPlayer[] generateRandomPhasePlayers(int n) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			int random = (int) (Math.random() * 3);
			switch (random) {
			case 0:
				newPlayers[i] = new PlayerPhaseI();
				break;
			case 1:
				newPlayers[i] = new PlayerPhaseII();
				break;
			case 2:
				newPlayers[i] = new PlayerPhaseIII();
				break;
			}
		}
		return newPlayers;
	}

}
