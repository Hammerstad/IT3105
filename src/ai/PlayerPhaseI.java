package ai;

import java.util.Arrays;
import poker.Game;
import poker.GameState;
import utilities.HandStrength;

public class PlayerPhaseI extends AbstractPlayer {

    private double riskAversion;

    public PlayerPhaseI(PlayerPersonality personality) {
        this();
        this.personality = personality;
    }

    /**
     * Default Phase 2 player. Gets a random personality.
     */
    public PlayerPhaseI() {
        super();
        this.personality = PlayerPersonality.getRandom();
        this.name = "Phase I Player " + NO;
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
     * Bet function for phase 1 player. Preflop is handled quite randomly and
     * postflop is handstrength based.
     *
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
     *
     * @param toCall - how much you need to raise to match the pot
     * @return -1 (FOLD) / 0 (CHECK) / double (RAISE)
     */
    private double preFlopBetting(double toCall) {
        if (toCall == 0) {
            Game.out.writeLine("		"+name+" checks, "+Arrays.toString(getHand()));
            return 0;
        } else if (Math.random() > 0.3) {
            Game.out.writeLine("		"+name+" calls "+toCall+", "+Arrays.toString(getHand()));
            return toCall;
        } else {
            Game.out.writeLine("		"+name + " folds (pre flop), " + Arrays.toString(getHand()));
            return foldBeforeFlop();
        }
    }

    /**
     * Private function for preflop betting. Everything is random!
     *
     * @param toCall - how much you need to raise to match the pot
     * @param state - the current game state
     * @return -1 (FOLD) / 0 (CHECK) / double (RAISE)
     */
    private double postFlopBetting(Game state, double toCall) {
        double hs = HandStrength.handstrength(hand, state.table.table, state.table.activePlayers.size()),d = hs * (state.table.activePlayers.size() - 1);
        if (d >= this.riskAversion || (state.table.activePlayers.size()) == 1 || toCall == 0) {
            if (d > 0.8 || (d > 0.5 && toCall == 0)) {
                Game.out.writeLine("		"+name+" raises "+(toCall + 2 * state.table.blind)+", "+Arrays.toString(getHand())+" HandStrength: "+hs);
                return toCall + 2 * state.table.blind;
            } else if (toCall == 0) {
                Game.out.writeLine("		"+name+" checks, "+Arrays.toString(getHand())+" HandStrength: "+hs);
                return 0;
            } else {
                Game.out.writeLine("		"+name+" calls "+toCall+", "+Arrays.toString(getHand())+" HandStrength: "+hs);
                return toCall;
            }
        } else {
            Game.out.writeLine("	"+name + " folds (post flop), " + Arrays.toString(getHand())+" HandStrength: "+hs);
            return foldAfterFlop();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(" ");
        sb.append("RiskAversion: ").append(riskAversion).append(" ");
        sb.append("Cards: ").append(Arrays.toString(getHand()));
        return sb.toString();
    }
}
