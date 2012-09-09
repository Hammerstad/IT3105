package poker;

public class Card implements Comparable<Card> {

    public int value;
    public Suit suit;

    public Card(int value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    //Decending order
    @Override
    public int compareTo(Card o) {
        return this.value - o.value;
    }
}
