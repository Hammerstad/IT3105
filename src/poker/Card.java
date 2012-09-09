package poker;

public class Card {
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
}
