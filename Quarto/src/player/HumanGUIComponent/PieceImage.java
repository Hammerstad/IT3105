/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player.HumanGUIComponent;

import game.Piece;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Nicklas
 */
public class PieceImage {

    private final static Map<Byte, BufferedImage> map = new HashMap<>();

    ;

    static {
        try {
            List<Piece> pieces = Piece.generateAll();
            for (Piece p : pieces){
                map.put(p.getByte(), ImageIO.read(new File(p.prettyname()+".png")));
            }           
        } catch (IOException ex) {
            Logger.getLogger(PieceImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static BufferedImage getImage(Piece piece) {
        if (piece == null) {
            return null;
        }
        return map.get(piece.getByte());
    }

    public static BufferedImage getImage(byte b) {
        return map.get(b);
    }
}
