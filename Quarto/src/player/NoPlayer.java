/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import game.Board;
import game.Piece;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class NoPlayer extends AbstractPlayer {


    @Override
    public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
        return board;
    }
    @Override
    public Piece givePiece(Board board, Piece[] pieces){
        return null;
    }

    @Override
    public void gameEnd() {
    }

    @Override
    public void roundEnd() {
    }

    @Override
    public void win() {
    }

    @Override
    public void loss() {
    }

    @Override
    public void draw() {
    }
    
}
