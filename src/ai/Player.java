package ai;

import poker.Card;
import poker.PlayerInterface;

public class Player implements PlayerInterface {
	Card[] hand;

	public Player() {
		this.hand = new Card[2];
	}

	public Player(Card[] hand) {
		this.hand = hand;
	}

	public void dealCard(Card card) {
		if (hand[0] == null) {
			hand[0] = card;
		} else {
			hand[1] = card;
		}
	}

	@Override
	public Card[] getHand() {
		return hand;
	}
	
	public void resetHand(){
		this.hand = new Card[2];
	}
}
