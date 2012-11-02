/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player.HumanGUIComponent;

import game.Board;
import game.Piece;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 *
 * @author Nicklas
 */
public class ContentPanel extends JPanel {
    private BoardView board;
    private PiecesView pieces;
    
    public ContentPanel() {
        super();
        setup();
        createLayout();
    }
    private void setup() {
        this.board = new BoardView();
        this.pieces = new PiecesView();
        
        this.pieces.setEnabled(false);
        this.board.setEnabled(false);
    }
    private void createLayout() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        layout.columnWidths = new int[]{600, 200};
        layout.columnWeights = new double[]{0, 0};
        layout.rowHeights = new int[]{600};
        layout.rowWeights = new double[]{0};
        this.setLayout(layout);
        
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        this.add(board, c);
        
        c.gridx = 1;
        this.add(pieces, c);
    }
    public BoardView getBoard() {
        return board;
    }
    public PiecesView getPieces() {
        return pieces;
    }
    public Board requestMove(Piece givenPiece, Board board) {
        System.out.println("Move request accepted");
        this.board.update(board);
        this.pieces.update(new Piece[]{givenPiece});
        
        this.board.setEnabled(true);
        Board b = this.board.requestMove(givenPiece, board);
        this.board.setEnabled(false);
        System.out.println("Move request finished");
        return b;
    }
    public Piece requestPiece(Board board, Piece[] pieces) {
        System.out.println("Piece request accepted");
        this.board.update(board);
        this.pieces.update(pieces);
        
        this.pieces.setEnabled(true);
        Piece p = this.pieces.requestPiece(board, pieces);
        this.pieces.setEnabled(false);
        System.out.println("Piece request finished");
        return p;
    }
}
