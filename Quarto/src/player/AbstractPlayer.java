package player;

import game.Board;
import game.Piece;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public abstract class AbstractPlayer {

    public static boolean LOG = false;

    public Board SyourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
        out(this.toString()+"::YourMove");
        out("   Board");
        out(board.toString());
        out("   PieceGiven: " + givenPiece.pieceString());
        Board after = yourMove(givenPiece, board, piecesAvailable);
        out("   BoardAfter");
        out(after.toString());
        return after;
    }

    public Piece SgivePiece(Board board, Piece[] pieces) {
        out(this.toString()+"::GivePiece");
        out("   Board");
        out(board.toString());
        StringBuilder sb = new StringBuilder();
        sb.append(" Available: ");
        for (Piece pp : pieces) {
            sb.append(pp.pieceString()).append(" ");
        }
        out(sb.toString());
        Piece p = givePiece(board, pieces);
        out("   Piece selected: "+p.pieceString());
        return p;
    }

    public void SgameEnd() {
        out(this.toString()+"::Game End");
        gameEnd();
    }

    public void SroundEnd() {
        out(this.toString()+"::Round end");
        roundEnd();
    }

    public void Swin() {
        out(this.toString()+"::You won");
        win();
    }

    public void Sloss() {
        out(this.toString()+"::You lost");
        loss();
    }

    public void Sdraw() {
        out(this.toString()+"::Draw");
        draw();
    }

    private void out(String s) {
        if (LOG) {
            System.out.println(s);
        }
    }

    public abstract Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable);

    public abstract Piece givePiece(Board board, Piece[] pieces);

    public abstract void gameEnd();

    public abstract void roundEnd();

    public abstract void win();

    public abstract void loss();

    public abstract void draw();
}
