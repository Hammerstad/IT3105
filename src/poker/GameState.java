package poker;

/**
 * All the different game states.</br></br>
 * Can be START, PREFLOP_BETTING, FLOP, PRETURN_BETTING, TURN, PRERIVER_BETTING, RIVER, FINAL_BETTING and SHOWDOWN.
 */
public enum GameState {

    START(0), PREFLOP_BETTING(1), FLOP(2), PRETURN_BETTING(3), TURN(4), PRERIVER_BETTING(5), RIVER(6), FINAL_BETTING(7), SHOWDOWN(9);
    int i;
    private GameState(int i){
        this.i = i;
    }
    public int getBucket(){
        return i;
    }
}