package poker;

/**
 * The Suit of a Card. </br></br> Can be DIAMOND, HEART, SPADE or CLUB.
 */
public enum Suit {
	DIAMOND(0), HEART(1), SPADE(2), CLUB(3);
	int suitValue;

	/**
	 * Creates a Suit from a given int value
	 * </br></br>DIAMOND - 0</br>HEART - 1</br>SPADE - 2</br>CLUB - 3
	 * @param suitValue - int
	 */
	Suit(int suitValue) {
		this.suitValue = suitValue;
	}

	/**
	 * Returns the Suit's value as an integer
	 * @return value - int
	 */
	public int getSuitValue() {
		return this.suitValue;
	}
}
