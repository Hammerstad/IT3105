package game;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class Piece {
    private byte piece;

    public Piece(boolean square, boolean small, boolean red, boolean whole) {
        this.piece |= (square)  ? 0b00000001 : 0b00000010;
        this.piece |= (small)   ? 0b00000100 : 0b00001000;
        this.piece |= (red)     ? 0b00010000 : 0b00100000;
        this.piece |= (whole)   ? 0b01000000 : 0b10000000;
    }

    public Piece(byte piece) {
        this.piece = piece;
    }

    public Piece(int piece) {
        this.piece = (byte) piece;
    }

    public Piece(Piece piece) {
        this.piece = piece.piece;
    }

    public byte getByte() {
        return this.piece;
    }

    public boolean isSquare() {
        return ((this.piece & 0b00000001) > 0);
    }

    public boolean isSmall() {
        return ((this.piece & 0b00000100) > 0);
    }

    public boolean isRed() {
        return ((this.piece & 0b00010000) > 0);
    }

    public boolean isWhole() {
        return ((this.piece & 0b01000000) > 0);
    }

    public Piece intersection(Piece piece) {
        return new Piece(this.piece & piece.piece);
    }

    @Override
    public boolean equals(Object obj) {
        return piece==((Piece)obj).piece;
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Piece other = (Piece) obj;
//        if (this.piece != other.piece) {
//            return false;
//        }
//        return true;
    }
    public boolean byteEquals(byte b){
        return piece == b;
    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.piece;
        return hash;
    }
    public static byte getSimilarities(Piece p, Piece o) {
        return (byte) (p.getByte()&o.getByte());
    }
    public String prettyname() {
        StringBuilder sb = new StringBuilder();
        sb.append((isSquare())?"square":"round");
        sb.append((isSmall())?"small":"big");
        sb.append((isRed())?"red":"blue");
        sb.append((isWhole())?"whole":"hole");
        return sb.toString();
    }
    @Override
    public String toString() {
        return "Piece{"
                + "square=" + isSquare()
                + " small=" + isSmall()
                + " red=" + isRed()
                + " whole=" + isWhole()
                + '}';
    }

    public String pieceString() {
        StringBuilder sb = new StringBuilder();
        boolean square = isSquare();
        if (square) {
            sb.append('(');
        }
        char c = (isRed()) ? 'r' : 'b';
        if (!isSmall()) {
            c = Character.toUpperCase(c);
        }
        sb.append(c);
        if (!isWhole()) {
            sb.append('*');
        }
        if (square) {
            sb.append(')');
        }
        return sb.toString();
    }
    public static List<Piece> generateAll() {
        List<Piece> l = new ArrayList<>(16);
        l.add(new Piece(true, true, true, true));
        l.add(new Piece(true, true, true, false));
        l.add(new Piece(true, true, false, true));
        l.add(new Piece(true, true, false, false));
        l.add(new Piece(true, false, true, true));
        l.add(new Piece(true, false, true, false));
        l.add(new Piece(true, false, false, true));
        l.add(new Piece(true, false, false, false));
        l.add(new Piece(false, true, true, true));
        l.add(new Piece(false, true, true, false));
        l.add(new Piece(false, true, false, true));
        l.add(new Piece(false, true, false, false));
        l.add(new Piece(false, false, true, true));
        l.add(new Piece(false, false, true, false));
        l.add(new Piece(false, false, false, true));
        l.add(new Piece(false, false, false, false));
        return l;
    }
}
