package ai;

import java.util.Arrays;
import poker.Game;
import poker.GameState;
import utilities.HandStrength;

public class PlayerPhaseI extends AbstractPlayer {

	private double riskAversion;

	public PlayerPhaseI(PlayerPersonality personality) {
		this.personality = personality;
		setRiskAversion();
	}

	/**
	 * Default Phase 2 player. Gets a random personality.
	 */
	public PlayerPhaseI() {
		this.personality = PlayerPersonality.getRandom();
		setRiskAversion();
	}

	/**
	 * Sets the risk aversion of a player. Greedy players are risk averse.
	 */
	private void setRiskAversion() {
		switch (personality) {
		case RISK_AVERSE:
			riskAversion = 0.900;
			break;
		case NORMAL:
			riskAversion = 1.000;
			break;
		case RISKFUL:
			riskAversion = 1.100;
			break;
		}
	}

	/**
	 * Bet function for phase 1 player. Preflop is handled quite randomly and postflop is handstrength based.
	 * @return -1 (FOLD) / 0 (CHECK) / double (RAISE)
	 */
	
	@Override
	public double bet(Game state, double toCall) {
		if (state.state == GameState.PRETURN_BETTING || state.state == GameState.PRERIVER_BETTING || state.state == GameState.FINAL_BETTING) {
			return postFlopBetting(state, toCall);
		} else if (state.state == GameState.PREFLOP_BETTING) {
			return preFlopBetting(toCall);
		}
		return -1;
	}

	/**
	 * Private function for preflop betting. Everything is random!
	 * @param toCall - how much you need to raise to match the pot
	 * @return -1 (FOLD) / 0 (CHECK) / double (RAISE)
	 */
	private double preFlopBetting(double toCall) {
		if (toCall == 0) {
			return 0;
		} else if (Math.random() > 0.3) {
			return toCall;
		} else {
			return foldBeforeFlop();
		}
	}

	/**
	 * Private function for preflop betting. Everything is random!
	 * @param toCall - how much you need to raise to match the pot
	 * @param state - the current game state
	 * @return -1 (FOLD) / 0 (CHECK) / double (RAISE)
	 */
	private double postFlopBetting(Game state, double toCall) {
		double d = HandStrength.handstrength(hand, state.table, state.activePlayers.size()) * (state.activePlayers.size() - 1);
		if (d >= this.riskAversion || (state.activePlayers.size()) == 1 || toCall == 0) {
			if (d > 0.8 || (d > 0.5 && toCall == 0)) {
				return toCall + 2 * state.blinds;
			} else if (toCall == 0) {
				return 0;
			} else {
				return toCall;
			}
		} else {
			return foldAfterFlop();
		}
	}
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString()).append(" ");
            sb.append("RiskAversion: "+riskAversion).append(" ");
            sb.append("Cards: "+Arrays.toString(getHand()));
            return sb.toString();
        }
}
