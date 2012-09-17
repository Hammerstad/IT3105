package ai.player;

import java.util.Arrays;

import poker.Game;
import poker.GameState;
import poker.Table;
import utilities.HandStrength;
import utilities.PreflopReader;

/**
 * A Phase II Player. Bases his betting on preflop calculations and handstrength
 * calculations.
 */
public class PlayerPhaseII extends AbstractPlayer {

    public static final double[][][][] preflopTable = new PreflopReader().read(""); // The preflop table
    protected int noOpponents; // How many opponents left
    protected double willBetIfAbove; // Will bet if above this (lower value for higher number of players)
    protected double riskAversion = 1.000; // How greedy/risky is the player

    /**
     * Default Phase 2 player.
     *
     * @param personality
     */
    public PlayerPhaseII(PlayerPersonality personality) {
        super();
        this.personality = personality;
        setRiskAversion();
        this.name = "Phase II Player " + NO;
    }

    /**
     * Default Phase 2 player. Gets a random personality.
     */
    public PlayerPhaseII() {
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
     * Bet function for the player. Returns how much he wants to bet.
     *
     * @param game - the game, and its state
     * @param toCall - how much you need to raise to match the pot
     * @return -1 (FOLD) / 0 (CHECK) / double (RAISE)
     */
    @Override
    public double bet(Table table, GameState state) {
    	double toCall = table.remainingToMatchPot[this.playerId];
        noOpponents = table.activePlayers.size() - 1;
        willBetIfAbove = Math.pow(0.15, noOpponents);
        switch (state) {
            case PREFLOP_BETTING:
                return preFlopBet(table, toCall);
            case PRERIVER_BETTING:
                return postFlopBet(table, toCall);
            case PRETURN_BETTING:
                return postFlopBet(table, toCall);
            case FINAL_BETTING:
                return postFlopBet(table, toCall);
            default:
                return -1;
        }
    }

    /**
     * Internal function for betting, uses preflop calculations.
     *
     * @param game - the game, and its state
     * @param toCall - how much you need to raise to match the pot
     * @return bet
     */
    private double preFlopBet(Table table, double toCall) {
        // Do something based on preflop table
        int suitedCards = (hand[0].suit == hand[1].suit) ? 1 : 0; // Check if cards are suited
        double preflopCalculation = preflopTable[suitedCards][noOpponents][hand[0].value - 2][hand[1].value - 2]; // Get preflop chances
        if (preflopCalculation * riskAversion < willBetIfAbove) {
            Game.out.writeLine("		" + name + " folds, " + Arrays.toString(getHand()) + " PreflopCalc: " + preflopCalculation);
            return foldBeforeFlop();
        } else if (preflopCalculation * riskAversion > (willBetIfAbove + 0.1 /riskAversion)) {
            Game.out.writeLine("		" + name + " raises " + (toCall + table.blind * riskAversion) + ", " + Arrays.toString(getHand()) + " PreflopCalc: " + preflopCalculation);
            return toCall + table.blind * riskAversion;
        } else {
            Game.out.writeLine("		" + name + " calls " + toCall + ", " + Arrays.toString(getHand()) + " PreflopCalc: " + preflopCalculation);
            return toCall;
        }
    }

    /**
     * Internal function for betting after the flop, uses hand strength
     * calculations.
     *
     * @param game - the state of the game
     * @param toCall - how much you need to raise to match the pot
     * @return
     */
    private double postFlopBet(Table table, double toCall) {
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
     * Overwritten toString method for phase II players.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(" ");
        sb.append("RiskAversion: ").append(riskAversion).append(" ");
        sb.append("Cards: ").append(Arrays.toString(getHand()));
        if (hand[1] != null) {
            int suitedCards = (hand[0].suit == hand[1].suit) ? 1 : 0; // Check if cards are suited
            double preflopCalculation = preflopTable[suitedCards][noOpponents][hand[0].value - 2][hand[1].value - 2]; // Get preflop chances
            sb.append(" PreFlop: ").append(preflopCalculation);
        }
        return sb.toString();
    }
}
