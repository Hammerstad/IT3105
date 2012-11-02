/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import game.Board;
import game.Piece;
import game.Streak;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class NovicePlayer extends RandomPlayer {

    @Override
    public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
        Streak[] streaks = board.findStreaks();
        for (Streak streak : streaks) {
            if (streak.getLength() == 3 && Piece.getSimilarities(givenPiece, new Piece(streak.getType())) != 0) {
                // Place piece

                // Find free places
                int[] placeFree = new int[]{0, 1, 2, 3};
                for (int i = 0; i < 4; i++) {
                    Piece p = null;
                    switch (streak.getOrientation()) {
                        case HORIZONTAL:
                            p = board.getPiece(streak.getNo(), i);
                            break;
                        case VERTICAL:
                            p = board.getPiece(i, streak.getNo());
                            break;
                        case DIAGONAL:
                            if (streak.getNo() == 0) {
                                p = board.getPiece(i, i);
                            } else {
                                p = board.getPiece(3 - i, i);
                            }
                    }
                    if (p != null) {
                        placeFree[i] = -1;
                    }
                }
                for (int place : placeFree) {
                    if (place >= 0) {
                        if (streak.getOrientation() == Streak.Orientation.HORIZONTAL) {
                            board.setPiece(streak.getNo(), place, givenPiece);
                        } else if (streak.getOrientation() == Streak.Orientation.VERTICAL) {
                            board.setPiece(place, streak.getNo(), givenPiece);
                        } else {
                            if (streak.getNo() == 0) {
                                board.setPiece(place, place, givenPiece);
                            } else {
                                board.setPiece(3 - place, place, givenPiece);
                            }
                        }
                    }
                }
                return board;
            }
        }
        return super.yourMove(givenPiece, board, piecesAvailable);
    }

    @Override
    public Piece givePiece(Board board, Piece[] pieces) {
        List<Piece> nonWinning = new ArrayList<>(Arrays.asList(pieces));
        Streak[] streaks = board.findStreaks();
        for (Streak streak : streaks) {
            if (streak.getLength() == 3) {
                Piece streakPiece = new Piece(streak.getType());
                // Remove matching from nonWinning list
                LinkedList<Piece> winning = new LinkedList<>();
                for (Piece p : nonWinning) {
                    if (Piece.getSimilarities(p, streakPiece) != 0) {
                        winning.add(p);
                    }
                }
                nonWinning.removeAll(winning);
            }
        }

        if (nonWinning.isEmpty()) {
//            System.out.println("Must give a winning piece");
            return super.givePiece(board, pieces);
        } else {
//            System.out.println("Non winning piece");
            return nonWinning.get(0);
        }
    }
    @Override
    public void gameEnd() {
    }
}
