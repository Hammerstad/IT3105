package player;

import java.util.Arrays;

import game.Board;
import game.Evaluatation;
import game.Piece;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class MiniMaxDPlayer extends AbstractPlayer {

	public int depth = 3;

	@Override
	public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
		System.out.println("PLACING");
		System.out.println("Board:\n"+board);
		System.out.println("Pieces:\n"+Arrays.toString(piecesAvailable));
		System.out.println("PIECE:\n"+givenPiece);
		// Let's create an int[] out of the board
		Piece[] boardAsPieces = board.getBoard();
		int[] actualBoard = new int[boardAsPieces.length];
		for (int i = 0; i < boardAsPieces.length; i++) {
			if (boardAsPieces[i] == null) {
				actualBoard[i] = 0;
			} else {
				actualBoard[i] = boardAsPieces[i].getByte();
			}
		}
		int entry = MiniMax.miniMax(depth, actualBoard, piecesAvailable, givenPiece);
		board.setPiece(entry, givenPiece);
		return board;
		// Let's create an int[] out of the board
//		Piece[] boardAsPieces = board.getBoard();
//		int[] actualBoard = new int[boardAsPieces.length];
//		for (int i = 0; i < boardAsPieces.length; i++) {
//			if (boardAsPieces[i] == null) {
//				actualBoard[i] = 0;
//			} else {
//				actualBoard[i] = boardAsPieces[i].getByte();
//			}
//		}
//
//		Evaluatation.evaluateIfMoveCanWin(actualBoard, givenPiece.getByte());
//		int pos = Evaluatation.position;
//		if(pos==-1){
//			for(int i = 0; i < board.getBoard().length; i++){
//				if(board.getBoard()[i] == null){
//					pos = i;
//					break;
//				}
//			}
//		}
//		board.setPiece(pos, givenPiece);
//		return board;
	}

	@Override
	public Piece givePiece(Board board, Piece[] pieces) {
		System.out.println("GIVING");
		System.out.println("Board:\n"+board);
		System.out.println("Pieces:\n"+Arrays.toString(pieces));
//		// Let's create an int[] out of the board
//		Piece[] boardAsPieces = board.getBoard();
//		int[] actualBoard = new int[boardAsPieces.length];
//		for (int i = 0; i < boardAsPieces.length; i++) {
//			if (boardAsPieces[i] == null) {
//				actualBoard[i] = 0;
//			} else {
//				actualBoard[i] = boardAsPieces[i].getByte();
//			}
//		}
//		return MiniMax.maxiMin(depth, actualBoard, pieces);
		// Let's create an int[] out of the board
		Piece[] boardAsPieces = board.getBoard();
		int[] actualBoard = new int[boardAsPieces.length];
		for (int i = 0; i < boardAsPieces.length; i++) {
			if (boardAsPieces[i] == null) {
				actualBoard[i] = 0;
			} else {
				actualBoard[i] = boardAsPieces[i].getByte();
			}
		}

		for (Piece piece : pieces) {
			if (!Evaluatation.evaluateIfMoveCanWin(actualBoard, piece.getByte())) {
				return piece;
			}
		}
		return pieces[(int) (Math.random() * pieces.length - 1)];
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
