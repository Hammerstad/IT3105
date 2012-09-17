package ai.player;

import java.util.Arrays;

import poker.Game;
import poker.GameState;
import poker.Table;
import utilities.HandStrength;

/**
 * Phase I players. They are quite random at preflop betting, but uses
 * handstrength calculations for postflop.
 */
public class PlayerPhaseI extends AbstractPlayer {

    private double riskAversion;
    protected int noOpponents; // How many opponents left
    protected double willBetIfAbove; // Will bet if above this (lower value for higher number of players)

    public PlayerPhaseI(PlayerPersonality personality) {
        super();
        this.personality = personality;
        this.name = "Phase I Player " + NO;
        setRiskAversion();
    }

    /**
     * Default Phase 2 player. Gets a random personality.
     */
    public PlayerPhaseI() {
        this(PlayerPersonality.getRandom());
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
    public double bet(Table table, GameState state) {
        noOpponents = table.activePlayers.size() - 1;
        willBetIfAbove = Math.pow(0.15, noOpponents);
        if (state == GameState.PRETURN_BETTING || state == GameState.PRERIVER_BETTING || state == GameState.FINAL_BETTING) {
            return postFlopBetting(table, state);
        } else if (state == GameState.PREFLOP_BETTING) {
            return preFlopBetting(table.remainingToMatchPot[this.playerId]);
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
    private double postFlopBetting(Table table, GameState state) {
    	double toCall = table.remainingToMatchPot[this.playerId];
        double handStrength = HandStrength.handstrength(getHand(), table.table, noOpponents);
        if (handStrength * riskAversion < willBetIfAbove && toCall > 0) {
            Game.out.writeLine("		" + name + " folds, " + Arrays.toString(getHand()) + " Handstrength: " + handStrength);
            return foldAfterFlop();
        } else if (handStrength * riskAversion > (willBetIfAbove + 0.1 / riskAversion)) {
            Game.out.writeLine("		" + name + " raises " + (toCall + table.blind * riskAversion) + ", " + Arrays.toString(getHand()) + " Handstrength: " + handStrength);
            return toCall + table.blind * riskAversion;
        } else if (handStrength * riskAversion > willBetIfAbove && toCall == 0) {
            Game.out.writeLine("		" + name + " raises " + (2*table.blind) + ", " + Arrays.toString(getHand()) + " Handstrength: " + handStrength);
            return 2*table.blind;
        } else {
            Game.out.writeLine("		" + name + " calls " + toCall + ", " + Arrays.toString(getHand()) + " Handstrength: " + handStrength);
            return toCall;
        }
    }

    /**
     * Overwritten toString method for Phase I players.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(" ");
        sb.append("RiskAversion: ").append(riskAversion).append(" ");
        sb.append("Cards: ").append(Arrays.toString(getHand()));
        return sb.toString();
    }
}
