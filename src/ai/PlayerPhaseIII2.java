/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import ai.Context.Action;
import java.util.Arrays;
import java.util.List;
import poker.Game;
import poker.GameState;
import utilities.HandStrength;

/**
 * Phase III players. They use opponent modeling all the time, handstrength calculations for postflop, and preflop calculations before the
 * flop. Beware!
 */
public class PlayerPhaseIII2 extends AbstractPlayer {

	protected int noOpponents;
	protected double willBetIfAbove;
	protected double riskAversion = 1.000;

	public PlayerPhaseIII2(PlayerPersonality personality) {
		super();
		this.personality = personality;
		setRiskAversion();
		this.name = "Phase III2 Player " + NO;
	}

	public PlayerPhaseIII2() {
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
	public double bet(Game game, double toCall) {
		noOpponents = game.table.activePlayers.size() - 1;
		willBetIfAbove = Math.pow(0.15, noOpponents);
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
		int suitedCards = (hand[0].suit == hand[1].suit) ? 1 : 0; // Check if cards are suited
		double preflopCalculation = PlayerPhaseII.preflopTable[suitedCards][noOpponents][hand[0].value - 2][hand[1].value - 2]; // Get
																																// preflop
																																// chances
		double[] calculation = getAverageAdjustedEstimate(game, preflopCalculation);
		double avg = calculation[calculation.length - 1];
		if (avg < willBetIfAbove) {
			Game.out.writeLine("		" + name + " folds, " + Arrays.toString(getHand()) + " Preflop: " + preflopCalculation + " Average estemate: "
					+ avg + " Estimate: " + Arrays.toString(calculation));
			return foldBeforeFlop();
		} else if (avg > (willBetIfAbove + (1 / 10. * riskAversion))) {
			Game.out.writeLine("		" + name + " raises " + (toCall + game.table.blind * riskAversion) + ", " + Arrays.toString(getHand())
					+ " Preflop: " + preflopCalculation + " Average estemate: " + avg + " Estimate: " + Arrays.toString(calculation));
			return toCall + game.table.blind * riskAversion;
		} else {
			Game.out.writeLine("		" + name + " calls " + toCall + ", " + Arrays.toString(getHand()) + " Preflop: " + preflopCalculation
					+ " Average estemate: " + avg + " Estimate: " + Arrays.toString(calculation));
			return toCall;
		}
	}

	private double postFlopBet(Game game, double toCall) {
		double handStrength = HandStrength.handstrength(getHand(), game.table.table, noOpponents);
		double[] calculation = getAverageAdjustedEstimate(game, handStrength);
		double avg = calculation[calculation.length - 1];
		// System.out.println("Needed to raise: "+(willBetIfAbove + 1 / 10. * riskAversion)+
		// "Needed to call: "+willBetIfAbove+" Value: "+avg);
		if (avg < willBetIfAbove) {
			Game.out.writeLine("		" + name + " folds, " + Arrays.toString(getHand()) + " Handstrength: " + handStrength + " Average estemate: " + avg
					+ " Estimate: " + Arrays.toString(calculation));
			return foldAfterFlop();
		} else if (avg > (willBetIfAbove + 1 / 10. * riskAversion)) {
			Game.out.writeLine("		" + name + " raises " + (toCall + game.table.blind * riskAversion) + ", " + Arrays.toString(getHand())
					+ " Handstrength: " + handStrength + " Average estemate: " + avg + " Estimate: " + Arrays.toString(calculation));
			return toCall + game.table.blind * riskAversion;
		} else {
			Game.out.writeLine("		" + name + " calls " + toCall + ", " + Arrays.toString(getHand()) + " Handstrength: " + handStrength
					+ " Average estemate: " + avg + " Estimate: " + Arrays.toString(calculation));
			return toCall;
		}
	}

	private double[] getAverageAdjustedEstimate(Game game, double myStrength) {
		double[] estimate = getEstimatedHands(game);

		double[] calculation = new double[game.table.players.length + 1];
		for (int i = 0; i < game.table.activePlayers.size(); i++) {
			AbstractPlayer ap = game.table.activePlayers.get(i);
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

	private double[] getEstimatedHands(Game game) {
		double[] estimate = new double[game.table.players.length];
		// Fill with -1 to indicate not found
		Arrays.fill(estimate, -1.0);

		// Get last action every player did
		Action[] lastAction = new Action[game.table.players.length];
		// Assume Call if not previous actions is found
		Arrays.fill(lastAction, Action.CALL);
		double[] lastPotodds = new double[game.table.players.length];
		Arrays.fill(lastPotodds, 0.0);
		List<Context> history = game.history.getContexts();
		for (int i = history.size() - 1; i >= 0; i--) {
			Context c = history.get(i);
			if (lastAction[c.getPlayerId()] == null) {
				lastAction[c.getPlayerId()] = c.getAction();
				lastPotodds[c.getPlayerId()] = c.getPotOdds();
			}
		}
		// Build searchcontexts for every player
		Context[] search = new Context[game.table.players.length];
		for (int i = 0; i < game.table.players.length; i++) {
			search[i] = Context.createContext(i, game.state, game.table.amountOfRaisesThisRound, game.table.activePlayers.size(), lastPotodds[i],
					lastAction[i], 0);
		}
		ContextHolder[] results = new ContextHolder[game.table.players.length];
		OpponentModeling om = OpponentModeling.getInstance();
		for (int i = 0; i < game.table.players.length; i++) {
			results[i] = om.getData(search[i]);
		}

		// Handle data based on personality
		for (int i = 0; i < game.table.players.length; i++) {
			if (results[i] == null) {
				continue;
			}
			estimate[i] = results[i].getAverageHandstrength();
		}
		return estimate;
	}
}
