package ai;

import poker.Game;
import poker.PlayerInterface;
import utilities.HandStrength;
import utilities.PreflopReader;

public class PlayerPhaseII extends PlayerInterface {

	static double[][][][] preflopTable;
	int noOpponents;
	double willBetIfAbove;
	PlayerPersonality personality;
	double riskAversion = 0; // How greedy/risky is the player

	public PlayerPhaseII(PlayerPersonality personality) {
		if (preflopTable == null) {
			preflopTable = new PreflopReader().read("");
		}
		this.personality = personality;
		setriskAversion();
	}

	public PlayerPhaseII() {
		if (preflopTable == null) {
			preflopTable = new PreflopReader().read("");
		}
		this.personality = PlayerPersonality.getRandom();
		setriskAversion();
	}

	private void setriskAversion() {
		switch (personality) {
		case GREEDY:
			riskAversion = 0.850;
			break;
		case NORMAL:
			riskAversion = 1.000;
			break;
		case RISKY:
			riskAversion = 1.250;
			break;
		}
	}

	@Override
	public double bet(Game game, double toCall) {
		noOpponents = game.activePlayers.size();
		willBetIfAbove = Math.pow(0.75, noOpponents);
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

	private double preFlopBet(Game game, double toCall) {
		// Do something based on preflop table
		int suitedCards = (hand[0].suit == hand[1].suit) ? 1 : 0; // Check if cards are suited
		double preflopCalculation = preflopTable[suitedCards][noOpponents][hand[0].value][hand[1].value]; // Get preflop chances

		if (preflopCalculation * riskAversion < willBetIfAbove) {
			return -1;
		} else if (preflopCalculation * riskAversion > (willBetIfAbove + 0.1 * riskAversion)) {
			return toCall + game.blinds * riskAversion;
		} else {
			return toCall;
		}
	}

	private double postFlopBet(Game game, double toCall) {
		double handStrength = HandStrength.handstrength(getHand(), game.table, game.activePlayers.size());
		if (handStrength * riskAversion < willBetIfAbove) {
			return -1;
		} else if (handStrength * riskAversion > (willBetIfAbove + 0.1 * riskAversion)) {
			return toCall + game.blinds * riskAversion;
		} else {
			return toCall;
		}
	}
}
