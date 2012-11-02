/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.player;

import ai.opponentmodeling.Context;
import java.util.Arrays;
import java.util.List;

import poker.Game;
import poker.GameState;
import poker.Table;
import utilities.HandStrength;
import ai.opponentmodeling.Context.Action;
import ai.opponentmodeling.ContextHolder;
import ai.opponentmodeling.OpponentModeling;

/**
 * Phase III players. They use opponent modeling all the time, handstrength calculations for postflop, and preflop calculations before the
 * flop. Beware!
 */
public class PlayerPhaseIII extends AbstractPlayer {

	protected int noOpponents;
	protected double willBetIfAbove;
	protected double riskAversion = 1.000;

	public PlayerPhaseIII(PlayerPersonality personality) {
		super();
		this.personality = personality;
		setRiskAversion();
		this.name = "Phase III Player " + NO;
	}

	public PlayerPhaseIII() {
		this(PlayerPersonality.getRandom());
	}

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

	@Override
	public double bet(Table table, GameState state) {
		noOpponents = table.activePlayers.size() - 1;
		willBetIfAbove = Math.pow(0.15, noOpponents);
		switch (state) {
		case PREFLOP_BETTING:
			return preFlopBet(table, state);
		case PRERIVER_BETTING:
			return postFlopBet(table, state);
		case PRETURN_BETTING:
			return postFlopBet(table, state);
		case FINAL_BETTING:
			return postFlopBet(table, state);
		default:
			return -1;
		}
	}

	private double preFlopBet(Table table, GameState state) {
		int suitedCards = (hand[0].suit == hand[1].suit) ? 1 : 0; // Check if cards are suited
		double preflopCalculation = PlayerPhaseII.preflopTable[suitedCards][noOpponents][hand[0].value - 2][hand[1].value - 2]; // Get
																																// preflop
		double toCall = table.remainingToMatchPot[this.playerId];																														// chances
		double[] calculation = getAverageAdjustedEstimate(table, preflopCalculation, state);
		double avg = calculation[calculation.length - 1];
		if (avg < willBetIfAbove) {
			Game.out.writeLine("		" + name + " folds, " + Arrays.toString(getHand()) + " Preflop: " + preflopCalculation + " Average estemate: "
					+ avg + " Estimate: " + Arrays.toString(calculation));
			return foldBeforeFlop();
		} else if (avg > (willBetIfAbove + (1 / 10. * riskAversion))) {
			Game.out.writeLine("		" + name + " raises " + (toCall + table.blind * riskAversion) + ", " + Arrays.toString(getHand())
					+ " Preflop: " + preflopCalculation + " Average estemate: " + avg + " Estimate: " + Arrays.toString(calculation));
			return toCall + table.blind * riskAversion;
		} else {
			Game.out.writeLine("		" + name + " calls " + toCall + ", " + Arrays.toString(getHand()) + " Preflop: " + preflopCalculation
					+ " Average estemate: " + avg + " Estimate: " + Arrays.toString(calculation));
			return toCall;
		}
	}

	private double postFlopBet(Table table, GameState state) {
		double toCall = table.remainingToMatchPot[this.playerId];
		double handStrength = HandStrength.handstrength(getHand(), table.table, noOpponents);
		double[] calculation = getAverageAdjustedEstimate(table, handStrength, state);
		double avg = calculation[calculation.length - 1];
		// System.out.println("Needed to raise: "+(willBetIfAbove + 1 / 10. * riskAversion)+
		// "Needed to call: "+willBetIfAbove+" Value: "+avg);
		if (avg < willBetIfAbove) {
			Game.out.writeLine("		" + name + " folds, " + Arrays.toString(getHand()) + " Handstrength: " + handStrength + " Average estemate: " + avg
					+ " Estimate: " + Arrays.toString(calculation));
			return foldAfterFlop();
		} else if (avg > (willBetIfAbove + 1 / 10. * riskAversion)) {
			Game.out.writeLine("		" + name + " raises " + (toCall + table.blind * riskAversion) + ", " + Arrays.toString(getHand())
					+ " Handstrength: " + handStrength + " Average estemate: " + avg + " Estimate: " + Arrays.toString(calculation));
			return toCall + table.blind * riskAversion;
		} else {
			Game.out.writeLine("		" + name + " calls " + toCall + ", " + Arrays.toString(getHand()) + " Handstrength: " + handStrength
					+ " Average estemate: " + avg + " Estimate: " + Arrays.toString(calculation));
			return toCall;
		}
	}

	private double[] getAverageAdjustedEstimate(Table table, double myStrength, GameState state) {
		double[] estimate = getEstimatedHands(table, state);

		double[] calculation = new double[table.players.length + 1];
		for (int i = 0; i < table.activePlayers.size(); i++) {
			AbstractPlayer ap = table.activePlayers.get(i);
			calculation[ap.getPlayerId()] = (estimate[ap.getPlayerId()] < 0) ? -1 : Math.sqrt(estimate[ap.getPlayerId()] * myStrength) * riskAversion;
		}
		double sum = 0;
		int nof = 0;
		for (double d : calculation) {
			// Found no estimate
			if (d == -1) {
				continue;
			}
			sum += d;
			nof++;
		}
		calculation[calculation.length - 1] = sum / nof;
		return calculation;
	}

	private double[] getEstimatedHands(Table table, GameState state) {
		double[] estimate = new double[table.players.length];
		// Fill with -1 to indicate not found
		Arrays.fill(estimate, -1.0);

		// Get last action every player did
		Action[] lastAction = new Action[table.players.length];
		// Assume Call if not previous actions is found
		Arrays.fill(lastAction, Action.CALL);
		double[] lastPotodds = new double[table.players.length];
		Arrays.fill(lastPotodds, 0.0);
		List<Context> history = Game.history.getContexts();
		for (int i = history.size() - 1; i >= 0; i--) {
			Context c = history.get(i);
			if (lastAction[c.getPlayerId()] == null) {
				lastAction[c.getPlayerId()] = c.getAction();
				lastPotodds[c.getPlayerId()] = c.getPotOdds();
			}
		}
		// Build searchcontexts for every player
		Context[] search = new Context[table.players.length];
		for (int i = 0; i < table.players.length; i++) {
			search[i] = Context.createContext(i, state, table.activePlayers.size(), lastPotodds[i],
					lastAction[i], null);
		}
		ContextHolder[] results = new ContextHolder[table.players.length];
		OpponentModeling om = OpponentModeling.getInstance();
		for (int i = 0; i < table.players.length; i++) {
			results[i] = om.getData(search[i]);
		}

		// Handle data based on personality
		for (int i = 0; i < table.players.length; i++) {
			if (results[i] == null) {
				continue;
			}
			estimate[i] = results[i].getAverageHandstrength();
		}
		return estimate;
	}
}
