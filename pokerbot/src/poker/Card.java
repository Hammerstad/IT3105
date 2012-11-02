package poker;

/**
 * Class to represent Card. Has a SUIT and a VALUE.
 */
public class Card implements Comparable<Card> {
	public int value; // Card's value
	public Suit suit; // Card's suit

	/**
	 * Basic constructor for a Card
	 * 
	 * @param value
	 *            - the value of a Card
	 * @param suit
	 *            - the Suit of a Card (poker.Suit)
	 */
	public Card(int value, Suit suit) {
		this.value = value;
		this.suit = suit;
	}

	/**
	 * Checks if two Cards area equal. They are if suit and value are of same type/value.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		Card rhs = (Card) obj;
		return (rhs.suit == this.suit) && (rhs.value == this.value);
	}

	/**
	 * Compares a Card value to another. Descending order.
	 */
	@Override
	public int compareTo(Card o) {
		return o.value - this.value;
	}

	/**
	 * Basic toString function for a Card. Returns suit + value.
	 */
	@Override
	public String toString() {
		return suit + "" + value;
	}
}
