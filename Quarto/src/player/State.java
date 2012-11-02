/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import game.Board;
import game.Piece;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import util.ArrayHelper;

/**
 *
 * @author Nicklas
 */
public class State {

        Board b;
        Piece p;
        Piece[] ps;

        public State(Board b, Piece p, Piece[] ps) {
            this.b = b;
            this.p = p;
            this.ps = ps;
        }

        public State[] children() {
            List<State> l = new LinkedList<>();
            Board copyOfBoard;
            for (int i = 0; i < 16; i++) {
                if (b.getBoard()[i] == null) {
                    copyOfBoard = b.clone();
                    copyOfBoard.setPiece(i, p);
                    for (Piece g : ps) {
                        Piece[] r = ArrayHelper.removeElement(ps, g);
                        l.add(new State(copyOfBoard, g, r));
                    }
                }
            }
            State[] lA = new State[l.size()];
            return l.toArray(lA);
        }
        public int utility(int player) {
            return (b.isWinningState()?100:0)*player;
        }
    }
