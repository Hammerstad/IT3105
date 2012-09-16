package poker;

public abstract class PlayerInterface {
    private static int NO = 0;
    private String name;
    protected Card[] hand;
    protected double money;
    public int wins;
    public int[] folds;
    
    public PlayerInterface() {
        this.hand = new Card[2];
        this.folds = new int[2];
        this.name = "PHASEIBOT"+(NO++);
    }
    public PlayerInterface(Card[] hand) {
        this();
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
    public abstract double bet(Game state, double toCall);
    public String toString() {
        return name;
    }
}
