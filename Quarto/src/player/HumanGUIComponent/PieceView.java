/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player.HumanGUIComponent;

import game.Piece;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Nicklas
 */
public class PieceView extends JButton {

    private Piece piece;

    public PieceView() {
        createLayout(false);
    }

    private void createLayout(boolean allowEmpty) {
        this.setMaximumSize(this.getSize());
        if (this.piece == null || this.piece.getByte() == 0) {
            this.setEnabled(allowEmpty);
        } else {
            this.setEnabled(!allowEmpty);
        }
        loadImage();
    }

    public void update(Piece p, boolean allowEmpty) {
        this.piece = p;
        createLayout(allowEmpty);
    }

    private void loadImage() {
        this.setMaximumSize(this.getSize());
        BufferedImage bi = PieceImage.getImage(piece);
        if (bi == null) {
            this.setIcon(null);
        } else {
            int width = this.getWidth()-20;
            int height = this.getHeight()-20;
            System.out.println("Dim: "+width+" "+height);
            BufferedImage sbi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = sbi.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            g.drawImage(bi, 0, 0, width, height, null);
            g.dispose();
            this.setIcon(new ImageIcon(sbi));
            this.setSize(width+20, height+20);
            this.setPreferredSize(new Dimension(width+20, height+20));
        }
    }

    public Piece getPiece() {
        return this.piece;
    }
}
