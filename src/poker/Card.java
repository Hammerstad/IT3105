package poker;

public class Card {
	int value;
	Suit suit;
	
	public Card(int value, Suit suit){
		this.value = value;
		this.suit = suit;
	}
}

enum Suit{
	DIAMOND,HEART,SPADE,CLUB;
}