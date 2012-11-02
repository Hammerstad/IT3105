/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import game.Board;
import game.Piece;
import player.HumanGUIComponent.Frame;

/**
 *
 * @author Nicklas
 */
public class HumanGUI extends AbstractPlayer {
    private Frame frame;
    
    public HumanGUI() {
        this.frame = new Frame();
    }

    @Override
    public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
        return frame.getContent().requestMove(givenPiece, board);
    }

    @Override
    public Piece givePiece(Board board, Piece[] pieces) {
        return frame.getContent().requestPiece(board, pieces);
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
