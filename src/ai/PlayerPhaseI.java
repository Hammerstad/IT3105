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
        if (toCall == 0) {
            return 0;
        } else if (state.state == GameState.PRETURN_BETTING
                || state.state == GameState.PRERIVER_BETTING
                || state.state == GameState.FINAL_BETTING) {
            return postFlopBetting(state, toCall);
        } else if (state.state == GameState.PREFLOP_BETTING) {
            if (Math.random() > 0.3) {
//                System.out.println("PreFlopBetting: "+state.blinds);
                this.money -= toCall;
                state.pot += toCall;
                return toCall;
            } else {
//                System.out.println("PreFlopBetting: Fold");
//                state.foldingPlayers.add(this);
                this.folds[0]++;
                return -1;
            }
        }
        return Integer.MIN_VALUE;
    }

    private double postFlopBetting(Game state, double toCall) {
        double d = HandStrength.handstrength(hand, state.table, state.activePlayers.size())*(state.activePlayers.size()-1);

        if (d >= this.riskAversion || (state.activePlayers.size() - state.foldingPlayers.size()) == 1) {
            //Bet
//            System.out.println("Bettings: "+(2*state.blinds));
            if (d > 0.8) {
                //raise
//                System.out.println("Raise");
                state.addToCall(2*toCall);
                this.money -= 3*toCall;
                state.pot += 3*toCall;
                return 3*toCall;
            } else {
                this.money -= toCall;
                state.pot += toCall;
                return toCall;
            }

        } else {
            //fold
//            System.out.println("Bettings: Folds");
//            System.out.println("FOLDING: "+d);
//            state.foldingPlayers.add(this);
            this.folds[1]++;
            return -1;
        }
    }
}
