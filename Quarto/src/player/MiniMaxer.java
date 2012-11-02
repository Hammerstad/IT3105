/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

/**
 *
 * @author Nicklas
 */
public class MiniMaxer {

    public int alpha;
    public int beta;
    
    public State MiniMax(State state, int depth) {
        alpha = Integer.MIN_VALUE;
        beta = Integer.MAX_VALUE;
        State[] children = state.children();
        int max = Integer.MIN_VALUE;
        State maxState = null;
        for (State s : children) {
            int val = minValue(s, depth, alpha, beta);
            if (val > max) {
                max = val;
                maxState = s;
            }
        }
        if (maxState == null) {
            return children[(int) (Math.random() * children.length)];
        }
        return maxState;
    }

    public int minValue(State s, int depth, int alpha, int beta) {
        if (isTerminal(s, depth)) {
            return s.utility(1);
        }
        State[] children = s.children();
        int v = Integer.MAX_VALUE;
        for (State child : children) {
            v = Math.min(v, maxValue(child, depth - 1, alpha, beta));
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

    public int maxValue(State s, int depth, int alpha, int beta) {
        if (isTerminal(s, depth)) {
            return s.utility(-1);
        }
        State[] children = s.children();
        int v = Integer.MIN_VALUE;
        for (State child : children) {
            v = Math.max(v, minValue(child, depth - 1, alpha, beta));
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }

    private boolean isTerminal(State s, int depth) {
        return s.b.isWinningState() || s.ps.length == 0 || depth == 0;
    }
}
