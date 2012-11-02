package ai.player;

/**
 * A Player's personality. May be either: RISK_AVERSE, NORMAL, RISKFUL
 */
public enum PlayerPersonality {
	RISK_AVERSE, NORMAL, RISKFUL;
	
	/**
	 * Returns a random personality
	 * @return GREEDY/NORMAL/RISKY
	 */
	public static PlayerPersonality getRandom(){
		int random = (int)(Math.random()*3);
		switch (random){
		case 0:
			return RISK_AVERSE;
		case 1:
			return NORMAL;
		case 2:
			return RISKFUL;
		default:
			return NORMAL;
		}
	}
}
