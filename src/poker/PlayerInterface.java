package poker;

public interface PlayerInterface {
	public void dealCard(Card card);
	public Card[] getHand();
	public void resetHand();
}
