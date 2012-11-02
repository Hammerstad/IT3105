package game;

public class Evaluatation {
	public static int position = -1;

	/**
	 * Static function which checks if a board can be won if we give it a given piece.
	 * 
	 * @param board
	 *            - one dimensional int array
	 * @param piece
	 *            - the piece to place, int value
	 * @return - true if it can be won, false else
	 */
	public static boolean evaluateIfMoveCanWin(int[] board, int piece) {
		position = -1;
		return (evaluate0(board, piece) || evaluate1(board, piece) || evaluate2(board, piece) || evaluate3(board, piece) || evaluate4(board, piece)
				|| evaluate5(board, piece) || evaluate6(board, piece) || evaluate7(board, piece) || evaluate8(board, piece)
				|| evaluate9(board, piece) || evaluate10(board, piece) || evaluate11(board, piece) || evaluate12(board, piece)
				|| evaluate13(board, piece) || evaluate14(board, piece) || evaluate15(board, piece));
	}

	private static boolean evaluate0(int[] board, int piece) {
		if (board[0] == 0) {
			if (((piece & board[1] & board[2] & board[3]) != 0) || ((piece & board[4] & board[8] & board[12]) != 0)
					|| ((piece & board[5] & board[10] & board[15]) != 0)) {
				position = 0;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate1(int[] board, int piece) {
		if (board[1] == 0) {
			if (((piece & board[0] & board[2] & board[3]) != 0) || ((piece & board[5] & board[9] & board[13]) != 0)) {
				position = 1;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate2(int[] board, int piece) {
		if (board[2] == 0) {
			if (((piece & board[0] & board[1] & board[3]) != 0) || ((piece & board[6] & board[10] & board[14]) != 0)) {
				position = 2;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate3(int[] board, int piece) {
		if (board[3] == 0) {
			if (((piece & board[0] & board[1] & board[2]) != 0) || ((piece & board[7] & board[11] & board[15]) != 0)
					|| ((piece & board[6] & board[9] & board[12]) != 0)) {
				position = 3;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate4(int[] board, int piece) {
		if (board[4] == 0) {
			if (((piece & board[5] & board[6] & board[7]) != 0) || ((piece & board[0] & board[8] & board[12]) != 0)) {
				position = 4;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate5(int[] board, int piece) {
		if (board[5] == 0) {
			if (((piece & board[4] & board[6] & board[7]) != 0) || ((piece & board[1] & board[9] & board[13]) != 0)
					|| ((piece & board[0] & board[10] & board[15]) != 0)) {
				position = 5;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate6(int[] board, int piece) {
		if (board[6] == 0) {
			if (((piece & board[4] & board[5] & board[7]) != 0) || ((piece & board[2] & board[10] & board[14]) != 0)
					|| ((piece & board[3] & board[9] & board[12]) != 0)) {
				position = 6;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate7(int[] board, int piece) {
		if (board[7] == 0) {
			if (((piece & board[4] & board[5] & board[6]) != 0) || ((piece & board[3] & board[11] & board[15]) != 0)) {
				position = 7;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate8(int[] board, int piece) {
		if (board[8] == 0) {
			if (((piece & board[9] & board[10] & board[11]) != 0) || ((piece & board[0] & board[4] & board[12]) != 0)) {
				position = 8;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate9(int[] board, int piece) {
		if (board[9] == 0) {
			if (((piece & board[8] & board[10] & board[11]) != 0) || ((piece & board[1] & board[5] & board[13]) != 0)
					|| ((piece & board[3] & board[6] & board[12]) != 0)) {
				position = 9;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate10(int[] board, int piece) {
		if (board[10] == 0) {
			if (((piece & board[8] & board[9] & board[11]) != 0) || ((piece & board[2] & board[6] & board[14]) != 0)
					|| ((piece & board[0] & board[5] & board[15]) != 0)) {
				position = 10;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate11(int[] board, int piece) {
		if (board[11] == 0) {
			if (((piece & board[8] & board[9] & board[10]) != 0) || ((piece & board[3] & board[7] & board[15]) != 0)) {
				position = 11;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate12(int[] board, int piece) {
		if (board[12] == 0) {
			if (((piece & board[13] & board[14] & board[15]) != 0) || ((piece & board[0] & board[4] & board[8]) != 0)
					|| ((piece & board[3] & board[6] & board[9]) != 0)) {
				position = 12;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate13(int[] board, int piece) {
		if (board[13] == 0) {

			if (((piece & board[12] & board[14] & board[15]) != 0) || ((piece & board[1] & board[5] & board[9]) != 0)) {
				position = 13;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate14(int[] board, int piece) {
		if (board[14] == 0) {
			if (((piece & board[12] & board[13] & board[15]) != 0) || ((piece & board[2] & board[6] & board[10]) != 0)) {
				position = 14;
				return true;
			}
		}
		return false;
	}

	private static boolean evaluate15(int[] board, int piece) {
		if (board[15] == 0) {
			if (((piece & board[12] & board[13] & board[14]) != 0) || ((piece & board[3] & board[7] & board[11]) != 0)
					|| ((piece & board[0] & board[5] & board[10]) != 0)) {
				position = 15;
				return true;
			}
		}
		return false;
	}

}
