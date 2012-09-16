package ai;

import poker.Game;
import poker.GameState;
import utilities.HandStrength;

public class PlayerPhaseI extends AbstractPlayer {

	private double riskAversion;

	public PlayerPhaseI(double riskAversion) {
		this.riskAversion = riskAversion;
	}

	public PlayerPhaseI() {
		this.riskAversion = 0.5;
	}

	@Override
	public double bet(Game state, double toCall) {
		if (state.state == GameState.PRETURN_BETTING || state.state == GameState.PRERIVER_BETTING || state.state == GameState.FINAL_BETTING) {
			return postFlopBetting(state, toCall);
		} else if (state.state == GameState.PREFLOP_BETTING) {
			if (toCall == 0) {
				return 0;
			} else if (Math.random() > 0.3) {
				this.money -= toCall;
				state.pot += toCall;
				return toCall;
			} else {
				return foldBeforeFlop();
			}
		}
		return Integer.MIN_VALUE;
	}

	private double postFlopBetting(Game state, double toCall) {
		double d = HandStrength.handstrength(hand, state.table, state.activePlayers.size()) * (state.activePlayers.size() - 1);
		// System.out.println("D: " + d);
		if (d >= this.riskAversion || (state.activePlayers.size() - state.foldingPlayers.size()) == 1 || toCall == 0) {
			// Bet
			if (d > 0.8 || (d > 0.5 && toCall == 0)) {
				// raise
				state.addToCall(2 * state.blinds);
				this.money -= toCall + 2 * state.blinds;
				state.pot += toCall + 2 * state.blinds;
				return toCall + 2 * state.blinds;
			} else if (toCall == 0) {
				return 0;
			} else {
				this.money -= toCall;
				state.pot += toCall;
				return toCall;
			}

		} else {
			return foldAfterFlop();
		}
	}
}
