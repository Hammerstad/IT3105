package ai;

import poker.Card;
import poker.Game;
import poker.PlayerInterface;
import utilities.HandStrength;

public class PlayerPhaseI extends PlayerInterface {

    @Override
    public void bet(Game state) {
       if (state.state == Game.GameState.PRETURN_BETTING || 
               state.state == Game.GameState.PRERIVER_BETTING ||
               state.state == Game.GameState.FINAL_BETTING){
           postFlopBetting(state);
       }
    }
    private void postFlopBetting(Game state){
        double D = HandStrength.handstrength(hand, state.table, state.activePlayers.size());
        
    }
	
}
