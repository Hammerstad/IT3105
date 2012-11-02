/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import game.Board;
import game.Piece;

import java.util.ArrayList;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class RandomPlayer extends AbstractPlayer {

    /**
     * RandomPlayer plays the piece he has been given to a random empty place on
     * the board.
     *
     * @param givenPiece - the piece to play
     * @param board - the current board
     * @return board - the board after we have placed our piece
     */
    @Override
    public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
        // The 4x4 matrix of pieces
        Piece[] actualBoard = board.getBoard();
        // Let's create a list of all the elements which are null (empty spots on the board)
        ArrayList<Integer> nullElements = new ArrayList<>();
        for (int j = 0; j < actualBoard.length; j++) {
            if (actualBoard[j] == null) {
                nullElements.add(j); // Fill nullElements with empty spots
            }		// Pick a random element out of those empty spots
        }
        int randomNullElement = nullElements.get((int) (Math.random() * nullElements.size()));
        // Place the given piece on that spot
        actualBoard[randomNullElement] = givenPiece;
        // Return a new board with that... board
        return new Board(actualBoard);
    }

    /**
     * Gives a random piece to the other player
     *
     * @param board - the current board. We don't care.
     * @param pieces - the pieces we can choose from!
     * @return - A random piece.
     */
    @Override
    public Piece givePiece(Board board, Piece[] pieces) {
        return pieces[(int) (Math.random() * pieces.length)];
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
