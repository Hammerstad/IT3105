/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import game.Board;
import game.Piece;
import java.util.Scanner;

/**
 *
 * @author Nicklas
 */
public class MiniMaxPlayer extends NovicePlayer {
    public int depth = 3;
    State res;
    
    public MiniMaxPlayer() {
        System.out.println("Please enter the number of plies:");
        Scanner in = new Scanner(System.in);
        depth = in.nextInt();
    }
    public MiniMaxPlayer(int depth){
        this.depth = depth;
    }

    @Override
    public void draw() {
        
    }

    @Override
    public void gameEnd() {
        
    }

    @Override
    public Piece givePiece(Board board, Piece[] pieces) {
        if (res == null){
            return super.givePiece(board, pieces);
        }else {
            return res.p;
        }
    }

    @Override
    public void loss() {
        
    }

    @Override
    public void roundEnd() {
        res = null;
    }

    @Override
    public void win() {
        
    }

    @Override
    public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
        if (piecesAvailable.length == 0 || piecesAvailable.length > 13){
            return super.yourMove(givenPiece, board, piecesAvailable);
        }
        State root = new State(board, givenPiece, piecesAvailable);
        res = new MiniMaxer().MiniMax(root, depth);
        return res.b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MiniMaxPlayer other = (MiniMaxPlayer) obj;
        return true;
    }

    @Override
    public int hashCode() {
        return depth;
    }
    
    
}
