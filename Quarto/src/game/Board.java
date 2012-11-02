package game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class Board {

    /**
     * Represents the board of Pieces. 4x4.
     */
    private Piece[] board;

    /**
     * Default constructor for board. Initiates an empty board of Pieces.
     */
    public Board() {
        this.board = new Piece[16];
    }

    /**
     * Constructor for Board. Creates a shallow-copy of the input board.
     *
     * @param board - input board, shallow-copied
     */
    public Board(Piece[] board) {
        this.board = copyOf(board);
    }

    /**
     * Copies all the pieces of a board.
     *
     * @param b - board to copy
     * @return a spelunking new board, exactly like the old one.
     */
    public final Piece[] copyOf(Piece[] b) {
        Piece[] c = new Piece[16];
//        for (int i = 0; i < c.length; i++) {
//            System.arraycopy(b[i], 0, c[i], 0, b[i].length);
//        }
        System.arraycopy(b, 0, c, 0, b.length);
        return c;
    }

    /**
     * Checks if a given state is a winning state.\
     *
     * @return true or false
     */
//    public boolean isWinningState() {
//        Streak[] streaks = findStreaks();
//        for (Streak streak : streaks) {
//            if (streak.getLength() == 4) {
//                return true;
//            }
//        }
//        return false;
//    }
    public boolean isWinningState() {
        byte[] winning = new byte[10];
        Arrays.fill(winning, (byte) 0b11111111);
        for (int i = 0; i < 4; i++) {
            winning[0] &= ((board[i] != null) ? board[i].getByte() : 0);
            winning[1] &= ((board[4 + i] != null) ? board[4 + i].getByte() : 0);
            winning[2] &= ((board[8 + i] != null) ? board[8 + i].getByte() : 0);
            winning[3] &= ((board[12 + i] != null) ? board[12 + i].getByte() : 0);

            winning[4] &= ((board[4 * i] != null) ? board[4 * i].getByte() : 0);
            winning[5] &= ((board[4 * i + 1] != null) ? board[4 * i + 1].getByte() : 0);
            winning[6] &= ((board[4 * i + 2] != null) ? board[4 * i + 2].getByte() : 0);
            winning[7] &= ((board[4 * i + 3] != null) ? board[4 * i + 3].getByte() : 0);

            winning[8] &= ((board[4 * i + i] != null) ? board[4 * i + i].getByte() : 0);
            winning[9] &= ((board[4 * (3 - i) + i] != null) ? board[4 * (3 - i) + i].getByte() : 0);
        }
        byte a = 0;
        for (byte b : winning) {
            a |= b;
        }
        return a != 0;
    }

    public Piece getPiece(int row, int column) {
        return this.board[4 * row + column];
    }

    public void setPiece(int row, int column, Piece p) {
        this.board[4 * row + column] = p;
    }

    public void setPiece(int position, Piece p) {
        this.board[position] = p;
    }

    /**
     * Checks if you have a streak in some way. A streak is two or three in a
     * row.
     *
     * @return a list of streaks, potentially null.
     */
    public Streak[] findStreaks() {
        List<Streak> streaks = new LinkedList<>();
        Streak s;
        Piece[][] arrs = new Piece[4][4];
        Piece[][] diagArr = new Piece[2][4];
        int f;
        for (int row = 0; row < 4; row++) {
            f = 4 * row;
            s = Streak.gotStreak(Arrays.copyOfRange(board, f, f + 4), Streak.Orientation.HORIZONTAL, row);
            if (s != null) {
                streaks.add(s);
            }
            arrs[0][row] = board[f];
            arrs[1][row] = board[f + 1];
            arrs[2][row] = board[f + 2];
            arrs[3][row] = board[f + 3];

            diagArr[0][row] = board[f + row];
            diagArr[1][row] = board[4 * (3 - row) + row];
        }
        for (int col = 0; col < 4; col++) {
            s = Streak.gotStreak(arrs[col], Streak.Orientation.VERTICAL, col);
            if (s != null) {
                streaks.add(s);
            }
        }
        for (int diag = 0; diag < 2; diag++) {
            s = Streak.gotStreak(diagArr[diag], Streak.Orientation.DIAGONAL, diag);
            if (s != null) {
                streaks.add(s);
            }
        }
        Streak[] st = new Streak[streaks.size()];
        return streaks.toArray(st);
    }

    /**
     * Overridden clone method for Board.
     *
     * @return a shallow-copy of the board
     */
    @Override
    public Board clone() {
        return new Board(copyOf(this.board));
    }

    /**
     * ToString method for Board.
     *
     * @return an empty string at the moment.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //Print board
        int c = 0;
        for (Piece piece : board) {
            if (piece == null) {
                sb.append("\t");
            } else {
                sb.append(piece.pieceString()).append("\t");
            }
            if (++c % 4 == 0) {
                sb.append("\n");
            }
        }
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Represents the board of Pieces. 4x4.
     */
    public Piece[] getBoard() {
        return board;
    }
}
