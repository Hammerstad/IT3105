package ai;

import poker.Game;
import poker.PlayerInterface;

public class PlayerPhaseII extends PlayerInterface{

	@Override
	public void bet(Game state) {
		switch(state.state){
		case PREFLOP_BETTING:
			preFlopBet();
			break;
		case PRERIVER_BETTING:
			preRiverBet();
			break;
		case PRETURN_BETTING:
			preTurnBet();
			break;
		case FINAL_BETTING:
			finalBet();
			break;
		}
	}
	
	private void preFlopBet(){
		
	}
	private void preRiverBet(){
		
	}
	private void preTurnBet(){
		
	}
	private void finalBet(){
		
	}
}
