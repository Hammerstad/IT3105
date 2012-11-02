/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import game.GameDirector;

import player.HumanGUI;
import player.NewNovicePlayer;
import ui.StatisticsUI;
import ui.TCPPlayerUI;
import ui.UserInterface;

/**
 *
 * @author Nicklas
 */
public class TCPClientRun {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                GameDirector gd = new GameDirector(new UserInterface[]{new TCPPlayerUI(), new StatisticsUI()});
                gd.setState(GameDirector.GameState.CONFIG);
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                new TCPClientPlayer(new HumanGUI(), "Human1").run();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                new TCPClientPlayer(new HumanGUI(), "Human2").run();
            }
        }.start();
    }
}
