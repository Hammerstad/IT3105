/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.player;

import java.util.Arrays;
import java.util.List;

import poker.Game;
import poker.GameState;
import poker.Table;
import utilities.HandStrength;
import ai.opponentmodeling.Context;
import ai.opponentmodeling.Context.Action;
import ai.opponentmodeling.OpponentModeling;

/**
 * Phase III players. They use opponent modeling all the time, handstrength calculations for postflop, and preflop calculations before the
 * flop. Beware!
 */
public class PlayerPhaseIII3 extends PlayerPhaseII {

    public PlayerPhaseIII3() {
        this(PlayerPersonality.getRandom());
    }

    public PlayerPhaseIII3(PlayerPersonality personality) {
        super(personality);
        this.name = "Phase III Player " + NO;
    }
    @Override
    public double bet(Table table, GameState state) {
        switch (state) {
            case PREFLOP_BETTING:
                return super.bet(table, state);
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
    
    public double postFlopBet(Table table, GameState state) {
    	double toCall = table.remainingToMatchPot[this.playerId];
        //Find last actions taken by all players
        Action[] lastActions = new Action[table.players.length];
        double[] lastPotOdd = new double[table.players.length];
        int actionsFound = 0;
        List<Context> last = Game.history.getContexts();
        for (int i = last.size() - 1; i >= 0; i--) {
            Context c = last.get(i);
            if (lastActions[c.getPlayerId()] == null) {
                lastActions[c.getPlayerId()] = c.getAction();
                lastPotOdd[c.getPlayerId()] = c.getPotOdds();
                actionsFound++;
            }
            if (actionsFound == lastActions.length) {
                break;
            }
        }
        if (actionsFound == 0)return super.bet(table, state);

        //Build context, and find previous data
        double[][] estimatedHandstrength = new double[table.players.length][2];
        for (AbstractPlayer ap : table.activePlayers) {
            int apInd = ap.getPlayerId();
            if (apInd == this.playerId) {
                continue;
            }
            Context c = Context.createContext(apInd, state, table.activePlayers.size(), lastPotOdd[apInd], ((lastActions[apInd] != null) ? lastActions[apInd] : Action.CALL), null);
           switch (personality) {
                case RISK_AVERSE:
                    estimatedHandstrength[apInd] = OpponentModeling.getInstance().getMaxData(c);
                    break;
                case NORMAL:
                    estimatedHandstrength[apInd] = OpponentModeling.getInstance().getAvgData(c);
                    break;
                case RISKFUL:
                    estimatedHandstrength[apInd] = OpponentModeling.getInstance().getMinData(c);
                    break;
            }
        }
        double myHS = HandStrength.handstrength(hand, table.table, table.activePlayers.size());
        boolean biggerThenSome = false, biggerThenAvg = false, biggerThenAll = false;
        double[] avgEsti = new double[2];
        int[] counter = new int[2];
        for (AbstractPlayer ap : table.activePlayers) {
            if (ap.getPlayerId() == this.playerId) {
                continue;
            }
            double e = estimatedHandstrength[ap.getPlayerId()][0];
            if (myHS > e) {
                biggerThenSome = true;
                counter[0]++;
            }
            avgEsti[0] += e;
            avgEsti[1]++;
            counter[1]++;
        }
        if (myHS > (avgEsti[0] / avgEsti[1])) {
            biggerThenAvg = true;
        }
        if (counter[0] == counter[1]) {
            biggerThenAll = true;
        }
        switch (personality) {
            case RISK_AVERSE:
                if (biggerThenAll) {
                    //bet
                    return toCall;
                }
                break;
            case NORMAL:
                if (biggerThenAll) {
                    return toCall + table.blind*riskAversion;
                }
                else if (biggerThenAvg) {
                    return toCall;
                }
                break;
            case RISKFUL:
                if (biggerThenAll) {
                    return toCall + 2*table.blind*riskAversion;
                }
                else if (biggerThenAvg) {
                    return toCall + table.blind*riskAversion;
                }
                else if (biggerThenSome) {
                    return toCall;
                }
                break;
            default:
        }
        return foldAfterFlop();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append(" ");
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
