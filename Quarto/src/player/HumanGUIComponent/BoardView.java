/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player.HumanGUIComponent;

import game.Board;
import game.Piece;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.text.Position;

/**
 *
 * @author Nicklas
 */
public class BoardView extends JPanel implements MouseListener {

    private boolean enabled;
    private Board board;
    private Point callBack;

    public BoardView() {
        this.addMouseListener(this);
    }

    public void update(Board board) {
        this.board = board;
        this.repaint();
    }

    @Override
    public void setEnabled(boolean b) {
        this.enabled = b;
        super.setEnabled(b);
    }

    public Board requestMove(Piece givenPiece, Board board) {
        while (this.callBack == null) {
            Thread.yield();
        }
        board.setPiece(callBack.x, callBack.y, givenPiece);
        this.callBack = null;
        return board;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.clearRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board != null && board.getPiece(i, j) != null) {
                    g.drawImage(PieceImage.getImage(board.getPiece(i, j)), i * 150, j * 150, 150, 150, null);
                }
            }
        }//draw grid
        for (int i = 0; i < 4; i++) {
            //Horizontal
            g.drawLine(150 * i - 1, 0, 150 * i - 1, 600);
            //Vertical
            g.drawLine(0, 150 * i - 1, 600, 150 * i - 1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!enabled) {
            return;
        }
        int x = (e.getX()/150);
        int y = (e.getY()/150);
        System.out.println("Position: "+x+" "+y);
        callBack = new Point(x, y);
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
