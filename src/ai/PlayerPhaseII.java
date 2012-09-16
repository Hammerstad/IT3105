package ai;

import poker.Game;
import utilities.HandStrength;
import utilities.PreflopReader;

/**
 * A Phase II Player. Bases his betting on preflop calculations and handstrength calculations.
 */
public class PlayerPhaseII extends AbstractPlayer {

	static double[][][][] preflopTable; // The preflop table
	int noOpponents; // How many opponents left
	double willBetIfAbove; // Will bet if above this (lower value for higher number of players)
	double riskAversion = 1.000; // How greedy/risky is the player

	/**
	 * Default Phase 2 player.
	 * 
	 * @param personality
	 */
	public PlayerPhaseII(PlayerPersonality personality) {
		if (preflopTable == null) {
			PreflopReader pr = new PreflopReader();
			preflopTable = pr.read("");
		}
		this.personality = personality;
		setRiskAversion();
	}

	/**
	 * Default Phase 2 player. Gets a random personality.
	 */
	public PlayerPhaseII() {
		if (preflopTable == null) {
			preflopTable = new PreflopReader().read("");
		}
		this.personality = PlayerPersonality.getRandom();
		setRiskAversion();
	}

	/**
	 * Sets the risk aversion of a player. Greedy players are risk averse.
	 */
	private void setRiskAversion() {
		switch (personality) {
		case GREEDY:
			riskAversion = 0.900;
			break;
		case NORMAL:
			riskAversion = 1.000;
			break;
		case RISKY:
			riskAversion = 1.100;
			break;
		}
	}

	/**
	 * Bet function for the player. Returns how much he wants to bet.
	 * @return -1 (FOLD) / 0 (CHECK) / double (RAISE)
	 */
	@Override
	public double bet(Game game, double toCall) {
		noOpponents = game.activePlayers.size()-1;
		willBetIfAbove = Math.pow(0.30, noOpponents);
		switch (game.state) {
		case PREFLOP_BETTING:
			return preFlopBet(game, toCall);
		case PRERIVER_BETTING:
			return postFlopBet(game, toCall);
		case PRETURN_BETTING:
			return postFlopBet(game, toCall);
		case FINAL_BETTING:
			return postFlopBet(game, toCall);
		default:
			return -1;
		}
	}

	/**
	 * Internal function for betting, uses preflop calculations.
	 * @param game - the game, and its state
	 * @param toCall - how much left to the pot
	 * @return bet
	 */
	private double preFlopBet(Game game, double toCall) {
		// Do something based on preflop table
		int suitedCards = (hand[0].suit == hand[1].suit) ? 1 : 0; // Check if cards are suited
		double preflopCalculation = preflopTable[suitedCards][noOpponents][hand[0].value-2][hand[1].value-2]; // Get preflop chances

		if (preflopCalculation * riskAversion < willBetIfAbove) {
			return foldBeforeFlop();
		} else if (preflopCalculation * riskAversion > (willBetIfAbove + 0.1 * riskAversion)) {
			return toCall + game.blinds * riskAversion;
		} else {
			return toCall;
		}
	}

	/**
	 * Internal function for betting, uses hand strength calculations.
	 * @param game
	 * @param toCall
	 * @return
	 */
	private double postFlopBet(Game game, double toCall) {
		double handStrength = HandStrength.handstrength(getHand(), game.table, noOpponents);
		if (handStrength * riskAversion < willBetIfAbove) {
			return foldAfterFlop();
		} else if (handStrength * riskAversion > (willBetIfAbove + 0.1 * riskAversion)) {
			return toCall + game.blinds * riskAversion;
		} else {
			return toCall;
		}
	}
}
