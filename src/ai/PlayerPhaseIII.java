/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import ai.Context.Action;
import java.util.Arrays;
import java.util.List;
import poker.Game;
import utilities.HandStrength;

/**
 * Phase III players. They use opponent modeling all the time, handstrength calculations for postflop, and preflop calculations before the
 * flop. Beware!
 */
public class PlayerPhaseIII extends PlayerPhaseII {

    public PlayerPhaseIII() {
        this(PlayerPersonality.getRandom());
    }

    public PlayerPhaseIII(PlayerPersonality personality) {
        super(personality);
        this.name = "Phase III Player " + NO;
    }
    @Override
    public double bet(Game game, double toCall) {
        switch (game.state) {
            case PREFLOP_BETTING:
                return super.bet(game, toCall);
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
    
    public double postFlopBet(Game game, double toCall) {
        //Find last actions taken by all players
        Action[] lastActions = new Action[game.table.players.length];
        double[] lastPotOdd = new double[game.table.players.length];
        int actionsFound = 0;
        List<Context> last = game.history.getContexts();
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
        if (actionsFound == 0)return super.bet(game, toCall);

        //Build context, and find previous data
        double[][] estimatedHandstrength = new double[game.table.players.length][2];
        for (AbstractPlayer ap : game.table.activePlayers) {
            int apInd = ap.getPlayerId();
            if (apInd == this.playerId) {
                continue;
            }
            Context c = Context.createContext(apInd, game.state, game.table.activePlayers.size(), lastPotOdd[apInd], ((lastActions[apInd] != null) ? lastActions[apInd] : Action.CALL), null);
            //Context c = Context.createContext(apInd, game.state, game.table.amountOfRaisesThisRound, game.table.activePlayers.size(), lastPotOdd[apInd], ((lastActions[apInd] != null) ? lastActions[apInd] : Action.CALL), -1);
            switch (personality) {
                case RISK_AVERSE:
                    //estimatedHandstrength[apInd] = OpponentModeling.getInstance().getMaxData(c);
                    break;
                case NORMAL:
                    //estimatedHandstrength[apInd] = OpponentModeling.getInstance().getAvgData(c);
                    break;
                case RISKFUL:
                    //estimatedHandstrength[apInd] = OpponentModeling.getInstance().getMinData(c);
                    break;
            }
        }
        double myHS = HandStrength.handstrength(hand, game.table.table, game.table.activePlayers.size());
        boolean biggerThenSome = false, biggerThenAvg = false, biggerThenAll = false;
        double[] avgEsti = new double[2];
        int[] counter = new int[2];
        for (AbstractPlayer ap : game.table.activePlayers) {
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
                    return toCall + game.table.blind*riskAversion;
                }
                else if (biggerThenAvg) {
                    return toCall;
                }
                break;
            case RISKFUL:
                if (biggerThenAll) {
                    return toCall + 2*game.table.blind*riskAversion;
                }
                else if (biggerThenAvg) {
                    return toCall + game.table.blind*riskAversion;
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
