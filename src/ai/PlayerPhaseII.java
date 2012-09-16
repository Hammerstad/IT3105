package ai;

import poker.Game;
import poker.PlayerInterface;
import utilities.HandStrength;
import utilities.PreflopReader;

public class PlayerPhaseII extends PlayerInterface {

	static double[][][][] preflopTable;
	int noOpponents;
	PlayerPersonality personality;

	public PlayerPhaseII(PlayerPersonality personality) {
		if (preflopTable == null) {
			preflopTable = new PreflopReader().read("");
		}
		this.personality = personality;
	}

	public PlayerPhaseII() {
		if (preflopTable == null) {
			preflopTable = new PreflopReader().read("");
		}
		this.personality = PlayerPersonality.getRandom();
	}

	@Override
	public void bet(Game game) {
		switch (game.state) {
		case PREFLOP_BETTING:
			preFlopBet(game);
			break;
		case PRERIVER_BETTING:
			preRiverBet(game);
			break;
		case PRETURN_BETTING:
			preTurnBet(game);
			break;
		case FINAL_BETTING:
			finalBet(game);
			break;
		}
	}

	private void preFlopBet(Game game) {
		// Do something based on preflop table
		noOpponents = game.activePlayers.size();
		int suitedCards = (hand[0].suit == hand[1].suit) ? 1 : 0; //Check if cards are suited
		double preflopCalculation = preflopTable[suitedCards][noOpponents][hand[0].value][hand[1].value]; //Get preflop chances
		double personalityWeightedDesicion = 0; //How greedy/risky is the player
		switch (personality) {
		case GREEDY:
			personalityWeightedDesicion = 0.850;
			break;
		case NORMAL:
			personalityWeightedDesicion = 1.000;
			break;
		case RISKY:
			personalityWeightedDesicion = 1.250;
			break;
		}
		double willBetIfAbove = Math.pow(0.75, noOpponents); //Bet if preflop chances times greedyness is above this
		if(preflopCalculation * personalityWeightedDesicion < willBetIfAbove){
			//FOLD
		}else if(preflopCalculation * personalityWeightedDesicion > (willBetIfAbove + 0.1*personalityWeightedDesicion )){
			//RAISE, WE GOT GOOD A HAND!
		}else{
			//CALL/CHECK
		}
	}

	private void preRiverBet(Game game) {
		double handStrength = HandStrength.handstrength(getHand(), game.table, game.activePlayers.size());
		switch (personality) {
		case GREEDY:
			break;
		case NORMAL:
			break;
		case RISKY:
			break;
		}
	}

	private void preTurnBet(Game game) {
		double handStrength = HandStrength.handstrength(getHand(), game.table, game.activePlayers.size());
		switch (personality) {
		case GREEDY:
			break;
		case NORMAL:
			break;
		case RISKY:
			break;
		}
	}

	private void finalBet(Game game) {
		double handStrength = HandStrength.handstrength(getHand(), game.table, game.activePlayers.size());
		switch (personality) {
		case GREEDY:
			break;
		case NORMAL:
			break;
		case RISKY:
			break;
		}
	}
}
