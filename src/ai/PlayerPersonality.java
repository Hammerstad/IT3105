package ai;

/**
 * A Player's personality. May be either: GREEDY, NORMAL, RISKY
 */
public enum PlayerPersonality {
	GREEDY, NORMAL, RISKY;
	
	/**
	 * Returns a random personality
	 * @return GREEDY/NORMAL/RISKY
	 */
	public static PlayerPersonality getRandom(){
		int random = (int)(Math.random()*3);
		switch (random){
		case 0:
			return GREEDY;
		case 1:
			return NORMAL;
		case 2:
			return RISKY;
		default:
			return NORMAL;
		}
	}
}
