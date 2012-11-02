/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player.HumanGUIComponent;

import game.Board;
import game.Piece;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author Nicklas
 */
public class PiecesView extends JPanel implements MouseListener {

    private boolean enabled;
    private Piece[] pieces;
    private Piece callBack;

    public PiecesView() {
        this.addMouseListener(this);
    }

    public void update(Piece[] pieces) {
        this.pieces = pieces;
        this.repaint();
    }

    @Override
    public void setEnabled(boolean b) {
        this.enabled = b;
        super.setEnabled(b);
    }

    public Piece requestPiece(Board board, Piece[] pieces) {
        while (this.callBack == null) {
            Thread.yield();
        }
        Piece p = callBack;
        this.callBack = null;
        return p;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
        if (pieces == null || pieces.length == 0)return;
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] != null && pieces[i].getByte() != 0) {
                int x = 50 * (i / 4);
                int y = 50 * (i % 4);
                g.drawImage(PieceImage.getImage(pieces[i]), x, y, 50, 50, null);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!enabled) {
            return;
        }
        int x = (e.getX()/50);
        int y = (e.getY()/50);
        this.callBack = pieces[4*x+y];
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
