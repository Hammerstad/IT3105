package player;

import game.Evaluatation;
import game.Piece;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import util.ArrayHelper;

public class MiniMax {
	static int alfa = Integer.MIN_VALUE;
	static int beta = Integer.MAX_VALUE;

	public static int miniMax(int depth, int[] board, Piece[] piecesAvailable, Piece givenPiece) {

		if (Evaluatation.evaluateIfMoveCanWin(board, givenPiece.getByte())) {
			System.out.println("Winning by placing " + givenPiece.getByte() + " on place " + (Evaluatation.position - 1));
			return Evaluatation.position;
		}
		int[] copyOfBoard = new int[board.length];
		System.arraycopy(board, 0, copyOfBoard, 0, board.length);

		Map<Integer, Integer> results = new HashMap<>();
		for (int i = 0; i < board.length; i++) {
			if (copyOfBoard[i] == 0) {
				copyOfBoard[i] = givenPiece.getByte();
				int canWin = minValue(depth - 1, copyOfBoard, piecesAvailable, alfa, beta);
				results.put(i, canWin);
				System.arraycopy(board, 0, copyOfBoard, 0, board.length);
			}
		}
		Entry<Integer, Integer> maxEntry = null;
		for (Entry<Integer, Integer> entry : results.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		if (maxEntry.getValue() == 1) {
			System.out.println("WINNING SOON");
		}
		return maxEntry.getKey();
	}

	public static Piece maxiMin(int depth, int[] board, Piece[] piecesAvailable) {

		int[] copyOfBoard = new int[board.length];
		System.arraycopy(board, 0, copyOfBoard, 0, board.length);
		Piece[] remainingPieces = new Piece[piecesAvailable.length - 1];
		Map<Piece, Integer> results = new HashMap<>();
		for (int i = 0; i < piecesAvailable.length; i++) {
			if (Evaluatation.evaluateIfMoveCanWin(board, piecesAvailable[i].getByte())) {
				// System.err.println("Fuck - no: Piece" + piecesAvailable[i] + " at pos " + Evaluatation.position);
				continue;
			}
			remainingPieces = ArrayHelper.removeElement(piecesAvailable, piecesAvailable[i]);
			for (int j = 0; j < board.length; j++) {
				if (copyOfBoard[i] == 0) {
					copyOfBoard[i] = piecesAvailable[i].getByte();
					int canWin = maxValue(depth - 1, copyOfBoard, remainingPieces, alfa, beta);
					if (!results.containsKey(piecesAvailable[i])) {
						results.put(piecesAvailable[i], canWin);
					} else if (results.containsKey(piecesAvailable[i]) && results.get(piecesAvailable[i]) > canWin) {
						int temp = results.get(piecesAvailable[i]);
						results.put(piecesAvailable[i], canWin);
						int temp2 = results.get(piecesAvailable[i]);
						System.out.println("FORCING: " + (temp - temp2));
					}
					System.arraycopy(board, 0, copyOfBoard, 0, board.length);
				}
			}
		}

		Entry<Piece, Integer> minEntry = null;
		for (Entry<Piece, Integer> entry : results.entrySet()) {
			if (minEntry == null || entry.getValue() < minEntry.getValue()) {
				minEntry = entry;
			}
		}
		if (minEntry == null) {
			return piecesAvailable[(int) (Math.random() * piecesAvailable.length)];
		}
		if (minEntry.getValue() == -1) {
			System.out.println("FUCK " + piecesAvailable.length);
		} else if (minEntry.getValue() == 1) {
			//System.out.println("WEE");
		}
		return minEntry.getKey();
	}

	private static int maxValue(int depth, int[] board, Piece[] piecesAvailable, int alfa, int beta) {
		if (piecesAvailable.length == 0) {
			return 0;
		} else if (depth == 0) {
			return 0;
		}
		for (Piece piece : piecesAvailable) {
			if (Evaluatation.evaluateIfMoveCanWin(board, piece.getByte())) {
				return 1;
			}
		}
		// Else: check EVERYTHING!
		int worstCase = Integer.MIN_VALUE;

		int[] copyOfBoard = new int[board.length];
		System.arraycopy(board, 0, copyOfBoard, 0, board.length);
		Piece[] remainingPieces = new Piece[piecesAvailable.length - 1];
		int current = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i] == 0) {
				for (int j = 0; j < piecesAvailable.length; j++) {
					copyOfBoard[i] = piecesAvailable[j].getByte();
					remainingPieces = ArrayHelper.removeElement(piecesAvailable, piecesAvailable[j]);
					current = minValue(depth - 1, copyOfBoard, remainingPieces, alfa, beta);
					if (current > worstCase) {
						worstCase = current;
					}
					if (current >= beta) {
						return current;
					}
					if (alfa < current) {
						alfa = current;
					}
					System.arraycopy(board, 0, copyOfBoard, 0, board.length);
				}
			}
		}
		return worstCase;
	}

	private static int minValue(int depth, int[] board, Piece[] piecesAvailable, int alfa, int beta) {
		if (piecesAvailable.length == 0) {
			return 0;
		} else if (depth == 0) {
			return 0;
		}
		for (Piece piece : piecesAvailable) {
			if (Evaluatation.evaluateIfMoveCanWin(board, piece.getByte())) {
				return -1;
			}
		}
		// Else: check EVERYTHING!
		int worstCase = Integer.MAX_VALUE;

		int[] copyOfBoard = new int[board.length];
		System.arraycopy(board, 0, copyOfBoard, 0, board.length);
		Piece[] remainingPieces = new Piece[piecesAvailable.length - 1];
		int current = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i] == 0) {
				for (int j = 0; j < piecesAvailable.length; j++) {
					copyOfBoard[i] = piecesAvailable[j].getByte();
					remainingPieces = ArrayHelper.removeElement(piecesAvailable, piecesAvailable[j]);
					current = maxValue(depth - 1, copyOfBoard, remainingPieces, alfa, beta);
					if (current < worstCase) {
						worstCase = current;
					}
					if (current <= alfa) {
						return current;
					}
					if (beta > current) {
						beta = current;
					}
					System.arraycopy(board, 0, copyOfBoard, 0, board.length);
				}
			}
		}
		return worstCase;
	}
}
