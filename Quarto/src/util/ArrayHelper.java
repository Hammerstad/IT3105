package util;

import game.Piece;

public class ArrayHelper {

    public static Piece[] removeElement(Piece[] original, Piece element) {
        Piece[] n = new Piece[original.length - 1];
        for (int i = 0; i < n.length; i++) {
            if (original[i].byteEquals(element.getByte())) {
                System.arraycopy(original, 0, n, 0, i);
                System.arraycopy(original, i + 1, n, i, n.length - i);
                return n;
            }
        }
        return original;
    }
}
