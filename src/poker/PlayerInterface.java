package poker;

public abstract class PlayerInterface {
    protected Card[] hand;
    protected double money;
    public int wins;
    public int[] folds;
    
    public PlayerInterface() {
        this.hand = new Card[2];
        this.folds = new int[2];
    }
    public PlayerInterface(Card[] hand) {
        this.hand = hand;
    }
    public void dealCard(Card card){
        if (this.hand[0] == null){
            this.hand[0] = card;
        }else if (this.hand[1] == null) {
            this.hand[1] = card;
        }
    }

    public Card[] getHand() {
        return this.hand;
    }

    public void resetHand() {
        this.hand = new Card[2];
    }
    public void receiveMoney(double money){
//        System.out.println("Receiving money: "+money);
        this.money+=money;
    }
    public void takeMoney(double money) {
//        System.out.println("Taking money: "+money);
        this.money-=money;
    }
    public abstract void bet(Game state);
}
