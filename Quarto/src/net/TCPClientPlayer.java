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
import player.AbstractPlayer;

/**
 *
 * @author Nicklas
 */
public class TCPClientPlayer {

    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private AbstractPlayer player;
    private String name;

    public TCPClientPlayer(AbstractPlayer player, String name) {
        this.player = player;
        this.name = name;
    }

    public void run() {
        try {
            this.connection = new Socket(getHost(), getPort());
            this.in = new ObjectInputStream(connection.getInputStream());
            this.out = new ObjectOutputStream(connection.getOutputStream());

            boolean keepalive = true;
            String line;
            while (keepalive && (line = (String) in.readObject()) != null) {
                switch (line) {
                    case "Your move":
                        yourMove();
                        break;
                    case "Give piece":
                        givePiece();
                        break;
                    case "GameEnd":
                        gameEnd();
                        break;
                    case "RoundEnd":
                        roundEnd();
                        break;
                    case "Name":
                        name();
                        break;
                }
            }
        } catch (Exception e) {
        }
    }

    private void yourMove() throws IOException {
        Piece piece = new Piece(in.readByte());

        Piece[] boardArr = new Piece[16];
        for (int i = 0; i < 16; i++) {
            byte bPiece = in.readByte();
            if (bPiece != 0) {
                boardArr[i] = new Piece(bPiece);
            }
        }
        Board board = new Board(boardArr);

        int nofAvailable = in.readInt();
        Piece[] availablePieces = new Piece[nofAvailable];
        for (int i = 0; i < nofAvailable; i++) {
            availablePieces[i] = new Piece(in.readByte());
        }

        System.out.println("Requesting move");
        Board temp = player.SyourMove(piece, board, availablePieces);
        System.out.println("Got move");
        System.out.println("Loop Starting");
        int x = -1, y = -1;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (temp.getPiece(row, col)==null)continue;
                if (piece.byteEquals(temp.getPiece(row, col).getByte())) {
                    x = row;
                    y = col;
                    System.out.println("FOUND");
                    break;
                }
            }
            if (x != -1)break;
        }
        System.out.println("Loop complete");
        if (x < 0 || y < 0) {
            throw new RuntimeException("Could not find newly places piece");
        }
        System.out.println("Writing move");
        out.writeByte(x);
        out.writeByte(y);
        out.flush();
        System.out.println("Move complete");
    }

    private void givePiece() throws Exception {
        Piece[] boardArr = new Piece[16];
        for (int i = 0; i < 16; i++) {
            byte bPiece = in.readByte();
            if (bPiece != 0) {
                boardArr[i] = new Piece(bPiece);
            }
        }
        Board board = new Board(boardArr);
        int nofAvailable = in.readInt();
        Piece[] availablePieces = new Piece[nofAvailable];
        for (int i = 0; i < nofAvailable; i++) {
            availablePieces[i] = new Piece(in.readByte());
        }
        String response;
        do {
            Piece p = player.SgivePiece(board, availablePieces);
            out.writeByte(p.getByte());
            out.flush();
            response = (String) in.readObject();
        } while (response == null || response.equals("Invalid"));
    }

    private void gameEnd() throws IOException {
        player.SgameEnd();
        connection.close();
        in.close();
        out.close(); 
    }

    private void roundEnd() {
    }

    private void name() throws IOException {
        out.writeObject(this.name);
        out.flush();
    }

    public String getHost() {
        return "127.0.0.1";
    }

    public int getPort() {
        return 31337;
    }
}
