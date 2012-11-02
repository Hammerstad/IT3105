package game;

import java.util.Arrays;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class Streak {

    public enum Orientation {

        VERTICAL, HORIZONTAL, DIAGONAL;
    }
    private Orientation orientation;
    private int no;     //The row/column/diagonal number
    private int start;  //Allways the smallest distance from the top-left corner. 0, 1, 2
    private int length; //The length of the streak, 2 of more.
    private byte type;

    public Streak(Orientation orientation, int start, int length, int no, byte type) {
        this.orientation = orientation;
        this.start = start;
        this.length = length;
        this.no = no;
        this.type = type;
    }

    public static Streak gotStreak(Piece[] seq, Orientation orientation, int number) {
//        for (Piece p : seq){
//            if (p == null)continue;
//            System.out.print(Integer.toBinaryString(p.getByte())+" ");
//        }
//        System.out.println("");
        int nullCounter = 0;
        byte pre = (byte)0b11111111;
        for (Piece p : seq) {
            if (p == null) {
                nullCounter++;
            }else {
                pre &= p.getByte();
            }
        }
        if (pre == 0){
            return null;
        }
        for (int l = 4; l >= 2; l--) {
            
            byte[] b = new byte[5 - l];//New 5-L checks
            Arrays.fill(b, (byte)0b11111111);
            for (int i = 0; i < 5-l; i++) {//How many checks 4->1, 3->2, 2->3
                for (int bi = 0; bi < l; bi++) {//One check
                    if (seq[i+bi] != null){
                        b[i] &= seq[i+bi].getByte();
                    }
                }
            }
            for (int i = 0; i < 5 - l; i++) {
                if ((b[i]&0b11111111) != 0) {
                    if (l-nullCounter == 4){
                        String win = Integer.toBinaryString(b[i]);
                    }
                    return new Streak(orientation, i, l-nullCounter, number, b[i]);
                }
            }
            if (l > 2 && nullCounter == 4 - l) {
                return null;
            }
        }
        return null;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getNo() {
        return no;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public byte getType() {
        return type;
    }
}
