package ai;

public enum PlayerPersonality {
	GREEDY, NORMAL, RISKY;
	
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
