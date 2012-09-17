package ai;

import java.util.List;

public class PlayerGenerator {

	public PlayerGenerator() {

	}
	
	//PHASE I PLAYERS
	
	public AbstractPlayer[] generatePhaseIPlayers(int n) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseI();
		}
		return newPlayers;
	}

	public AbstractPlayer[] generatePhaseIPlayers(int n, PlayerPersonality personality) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseI(personality);
		}
		return newPlayers;
	}

	public AbstractPlayer[] generatePhaseIPlayers(List<PlayerPersonality> personalities) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[personalities.size()];
		for (int i = 0; i < personalities.size(); i++) {
			newPlayers[i] = new PlayerPhaseI(personalities.get(i));
		}
		return newPlayers;
	}

	// PHASE II PLAYERS
	
	public AbstractPlayer[] generatePhaseIIPlayers(int n) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseII();
		}
		return newPlayers;
	}

	public AbstractPlayer[] generatePhaseIIPlayers(int n, PlayerPersonality personality) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseII(personality);
		}
		return newPlayers;
	}

	public AbstractPlayer[] generatePhaseIIPlayers(List<PlayerPersonality> personalities) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[personalities.size()];
		for (int i = 0; i < personalities.size(); i++) {
			newPlayers[i] = new PlayerPhaseII(personalities.get(i));
		}
		return newPlayers;
	}

	// PHASE III PLAYERS
	
	public AbstractPlayer[] generatePhaseIIIPlayers(int n) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseIII();
		}
		return newPlayers;
	}

	public AbstractPlayer[] generatePhaseIIIPlayers(int n, PlayerPersonality personality) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new PlayerPhaseIII(personality);
		}
		return newPlayers;
	}

	public AbstractPlayer[] generatePhaseIIIPlayers(List<PlayerPersonality> personalities) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[personalities.size()];
		for (int i = 0; i < personalities.size(); i++) {
			newPlayers[i] = new PlayerPhaseIII(personalities.get(i));
		}
		return newPlayers;
	}
	
	// RANDOM PHASE PLAYERS

	public AbstractPlayer[] generateRandomPhasePlayers(int n) {
		AbstractPlayer[] newPlayers = new AbstractPlayer[n];
		for (int i = 0; i < n; i++) {
			int random = (int)(Math.random()*3);
			switch(random){
			case 0:
				newPlayers[i] = new PlayerPhaseI();
				break;
			case 1:
				newPlayers[i] = new PlayerPhaseII();
				break;
			case 2:
				newPlayers[i] = new PlayerPhaseIII();
				break;
			}
		}
		return newPlayers;
	}

}
