package ai;

import java.util.Arrays;
import poker.Game;
import poker.GameState;
import utilities.HandStrength;

public class PlayerPhaseI extends AbstractPlayer {

    private double riskAversion;
    protected int noOpponents; // How many opponents left
    protected double willBetIfAbove; // Will bet if above this (lower value for higher number of players)

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
    public double bet(Game game, double toCall) {
        noOpponents = game.table.activePlayers.size() - 1;
        willBetIfAbove = Math.pow(0.15, noOpponents);
        if (game.state == GameState.PRETURN_BETTING || game.state == GameState.PRERIVER_BETTING || game.state == GameState.FINAL_BETTING) {
            return postFlopBetting(game, toCall);
        } else if (game.state == GameState.PREFLOP_BETTING) {
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
            Game.out.writeLine("		" + name + " checks, " + Arrays.toString(getHand()));
            return 0;
        } else if (Math.random() * riskAversion > 0.3) {
            Game.out.writeLine("		" + name + " calls " + toCall + ", " + Arrays.toString(getHand()));
            return toCall;
        } else {
            Game.out.writeLine("		" + name + " folds (pre flop), " + Arrays.toString(getHand()));
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
    private double postFlopBetting(Game game, double toCall) {
        double handStrength = HandStrength.handstrength(getHand(), game.table.table, noOpponents);
        if (handStrength * riskAversion < willBetIfAbove) {
            Game.out.writeLine("		" + name + " folds, " + Arrays.toString(getHand()) + " Handstrengt: " + handStrength);
            return foldAfterFlop();
        } else if (handStrength * riskAversion > (willBetIfAbove + 0.1 / riskAversion)) {
            Game.out.writeLine("		" + name + " raises " + (toCall + game.table.blind * riskAversion) + ", " + Arrays.toString(getHand()) + " Handstrengt: " + handStrength);
            return toCall + game.table.blind * riskAversion;
        } else {
            Game.out.writeLine("		" + name + " calls " + toCall + ", " + Arrays.toString(getHand()) + " Handstrengt: " + handStrength);
            return toCall;
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
