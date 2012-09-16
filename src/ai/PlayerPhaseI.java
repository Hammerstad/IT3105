package ai;

import poker.Game;
import poker.GameState;
import poker.PlayerInterface;
import utilities.HandStrength;

public class PlayerPhaseI extends PlayerInterface {
    private double riskAversion;
    
    public PlayerPhaseI(double riskAversion){
        this.riskAversion = riskAversion;
    }
    public PlayerPhaseI(){
        this.riskAversion = 0.5;
    }
    
    
    @Override
    public void bet(Game state) {
        if (state.state == GameState.PRETURN_BETTING
                || state.state == GameState.PRERIVER_BETTING
                || state.state == GameState.FINAL_BETTING) {
            postFlopBetting(state);
        }
    }

    private void postFlopBetting(Game state) {
        double d = HandStrength.handstrength(hand, state.table, state.activePlayers.size());

        if (d >= this.riskAversion){
            
        }
    }
}
