package poker;

public enum Suit {
	DIAMOND(0), HEART(1), SPADE(2), CLUB(3);
	int suitValue;

	Suit(int suitValue) {
		this.suitValue = suitValue;
	}

	public int getSuitValue() {
		return this.suitValue;
	}
}
