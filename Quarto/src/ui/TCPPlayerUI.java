/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import game.Board;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.TCPServerPlayer;
import player.AbstractPlayer;
import player.RandomPlayer;

/**
 *
 * @author Nicklas
 */
public class TCPPlayerUI extends UserInterface {
    private ServerSocket socket;
    
    public TCPPlayerUI() {
        try {
            this.socket = new ServerSocket(31337, 10);
        } catch (IOException ex) {
            Logger.getLogger(TCPPlayerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public AbstractPlayer getPlayer() {
        try {
//            System.out.println("Waiting for player...");
            Socket connection = socket.accept();
//            System.out.println("Found player: "+connection.getInetAddress()+":"+connection.getPort());
            return new TCPServerPlayer(connection);
        } catch (IOException ex) {
            Logger.getLogger(TCPPlayerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new RandomPlayer();
    }

    @Override
    public void updateView(Board b) {
    }

    @Override
    public void highlightStreak() {
    }

    @Override
    public void announceWinner(AbstractPlayer winner, AbstractPlayer loser) {
    }

    @Override
    public void draw(AbstractPlayer[] players) {
    }

    @Override
    public void gameEnd() {
    }

    @Override
    public void roundEnd() {
    }

    @Override
    public int numberOfGames() {
        return 1;
    }
    
}
