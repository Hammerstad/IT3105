package player;

import game.Board;
import game.Evaluatation;
import game.Piece;

public class NewNovicePlayer extends AbstractPlayer {

	@Override
	public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
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

		Evaluatation.evaluateIfMoveCanWin(actualBoard, givenPiece.getByte());
		int pos = Evaluatation.position;
		if(pos==-1){
			for(int i = 0; i < board.getBoard().length; i++){
				if(board.getBoard()[i] == null){
					pos = i;
					break;
				}
			}
		}
		board.setPiece(pos, givenPiece);
		return board;
	}

	@Override
	public Piece givePiece(Board board, Piece[] pieces) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void roundEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void win() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loss() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

}
