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
public class Human extends AbstractPlayer {

    private Scanner in;

    public Human() {
        in = new Scanner(System.in);
    }

    @Override
    public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
        System.out.println(board);
        System.out.println("");
        System.out.println("Your piece: " + givenPiece.pieceString());
        System.out.println("Enter two single digit number (row col):");
        int row = in.nextInt();
        int col = in.nextInt();
        while (board.getPiece(row, col) != null) {
            System.out.println("There is allready something there.");
            System.out.println(board);
            System.out.println("");
            System.out.println("Your piece: " + givenPiece.pieceString());
            System.out.println("Enter two single digit number (row col):");
            row = in.nextInt();
            col = in.nextInt();
        }
        board.setPiece(row, col, givenPiece);
        return board;
    }

    @Override
    public Piece givePiece(Board board, Piece[] pieces) {
        while (true) {
            System.out.println(board);
            System.out.println("");
            System.out.print("Pieces available: ");
            for (Piece p : pieces) {
                System.out.print(p.pieceString() + " ");
            }
            System.out.println("");
            System.out.println("Enter the piece you would like to give away (B*)");

            String piece = in.nextLine();
            for (Piece p : pieces) {
                if (p.pieceString().equals(piece)) {
                    return p;
                }
            }
            System.out.println("Could not find your piece, please try again.");
        }

    }

    @Override
    public void gameEnd() {
    }

    @Override
    public void roundEnd() {
    }

    @Override
    public void win() {
        System.out.println("Game ended, you won");
    }

    @Override
    public void loss() {
        System.out.println("Game ended, you lost");
    }

    @Override
    public void draw() {
        System.out.println("Game ended, draw.");
    }
}
