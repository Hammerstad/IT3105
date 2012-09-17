package poker;

/**
 * All the different game states.</br></br>
 * Can be START, PREFLOP_BETTING, FLOP, PRETURN_BETTING, TURN, PRERIVER_BETTING, RIVER, FINAL_BETTING and SHOWDOWN.
 */
public enum GameState {

    START(-1), PREFLOP_BETTING(0), FLOP(-1), PRETURN_BETTING(1), TURN(-1), PRERIVER_BETTING(2), RIVER(-1), FINAL_BETTING(3), SHOWDOWN(-1);
    int i;
    private GameState(int i){
        this.i = i;
    }
    public int getBucket(){
        return i;
    }
}