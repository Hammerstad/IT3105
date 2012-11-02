/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import game.Board;
import game.Piece;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import player.AbstractPlayer;

/**
 *
 * @author Nicklas
 */
public class TCPServerPlayer extends AbstractPlayer {

    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public TCPServerPlayer(Socket connection) {
        try {
            this.connection = connection;
            this.out = new ObjectOutputStream(connection.getOutputStream());
            this.in = new ObjectInputStream(connection.getInputStream());
            out.flush();
        } catch (Exception ex) {
            Logger.getLogger(TCPServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Board yourMove(Piece givenPiece, Board board, Piece[] piecesAvailable) {
        try {
            out.writeObject("Your move");
            out.writeByte(netByte(givenPiece));
            for (Piece p : board.getBoard()) {
                out.writeByte(netByte(p));
            }
            out.writeInt(piecesAvailable.length);
            for (Piece p : piecesAvailable) {
                out.writeByte(netByte(p));
            }
            out.flush();

            byte row = in.readByte();
            byte col = in.readByte();
            System.out.println("Move received");
            board.setPiece(row, col, givenPiece);
            return board;
        } catch (IOException ex) {
            Logger.getLogger(TCPServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Piece givePiece(Board board, Piece[] pieces) {
        try {
            out.writeObject("Give piece");
            for (Piece p : board.getBoard()) {
                out.writeByte(netByte(p));
            }
            out.writeInt(pieces.length);
            for (Piece p : pieces) {
                out.writeByte(netByte(p));
            }
            out.flush();
            while (true) {
                byte piece = in.readByte();
                System.out.println("Position received");
                for (Piece p : pieces) {
                    if (p.byteEquals(piece)) {
                        out.writeObject("OK");
                        out.flush();
                        return p;
                    }
                }
                out.writeObject("Invalid");
            }

        } catch (IOException ex) {
            Logger.getLogger(TCPServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    public void gameEnd() {
        try {
            out.writeObject("GameEnd");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(TCPServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
                out.close();
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(TCPServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void roundEnd() {
        try {
            out.writeObject("RoundEnd");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(TCPServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void win() {
    }

    @Override
    public void loss() {
    }

    @Override
    public void draw() {
    }

    @Override
    public String toString() {
        try {
            out.writeObject("Name");
            out.flush();
            String name = (String) in.readObject();
            return name;
        } catch (Exception ex) {
            Logger.getLogger(TCPServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.toString();
    }

    private byte netByte(Piece p) {
        return (p != null) ? p.getByte() : 0;
    }
}
