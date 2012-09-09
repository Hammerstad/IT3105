package poker;

public class Card implements Comparable<Card> {
	public int value;
	public Suit suit;

	public Card(int value, Suit suit) {
		this.value = value;
		this.suit = suit;
	}

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

	// Descending order
	@Override
	public int compareTo(Card o) {
		return o.value - this.value;
	}

	@Override
	public String toString() {
		return suit + "" + value;
	}
}
