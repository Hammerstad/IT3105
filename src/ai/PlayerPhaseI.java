package ai;

import poker.Game;
import poker.GameState;
import poker.PlayerInterface;
import utilities.HandStrength;

public class PlayerPhaseI extends PlayerInterface {

    private double riskAversion;

    public PlayerPhaseI(double riskAversion) {
        this.riskAversion = riskAversion;
    }

    public PlayerPhaseI() {
        this.riskAversion = 0.5;
    }

    @Override
    public void bet(Game state) {
        if (state.state == GameState.PRETURN_BETTING
                || state.state == GameState.PRERIVER_BETTING
                || state.state == GameState.FINAL_BETTING) {
            postFlopBetting(state);
        }else if (state.state == GameState.PREFLOP_BETTING) {
            if (Math.random() > 0.0){
//                System.out.println("PreFlopBetting: "+state.blinds);
                this.money -= state.blinds;
                state.pot += state.blinds;
            }else {
//                System.out.println("PreFlopBetting: Fold");
                state.foldingPlayers.add(this);
                this.folds[0]++;
            }
        }
    }

    private void postFlopBetting(Game state) {
        double d = HandStrength.handstrength(hand, state.table, state.activePlayers.size());

        if (d >= this.riskAversion || (state.activePlayers.size() - state.foldingPlayers.size()) == 1) {
            //Bet
//            System.out.println("Bettings: "+(2*state.blinds));
            this.money -= 2*state.blinds;
            state.pot += 2*state.blinds;
        }else {
            //fold
//            System.out.println("Bettings: Folds");
            state.foldingPlayers.add(this);
            this.folds[1]++;
        }
    }
}
