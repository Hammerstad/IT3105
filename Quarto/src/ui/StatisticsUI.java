/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;


import game.Board;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import player.AbstractPlayer;
import player.MinimaxDPlayer;

import player.NovicePlayer;

/**
 *
 * @author Nicklas
 */
public class StatisticsUI extends UserInterface {
    private int i;

    @Override
    public int numberOfGames() {
        return 1;
    }
    
    public class Statistics {
        private int win, loss, draw;
        
        public void win() {
            win++;
        }
        public void loss() {
            loss++;
        }
        public void draw() {
            draw++;
        }

        @Override
        public String toString() {
            return "Statistics{" + "win=" + win + ", loss=" + loss + ", draw=" + draw + '}';
        }
        
    }
    private Map<AbstractPlayer, Statistics> stats;
    
        public StatisticsUI() {
        this.stats = new HashMap<>();
    }

    @Override
    public AbstractPlayer getPlayer() {
        if (i == 0) {
            i++;
            return new NovicePlayer();
        }else {
            return new MinimaxDPlayer();
        }
    }

    @Override
    public void updateView(Board b) {
    }

    @Override
    public void highlightStreak() {

    }

    @Override
    public void announceWinner(AbstractPlayer winner, AbstractPlayer loser) {
        System.out.println("Winner: "+winner);
        getStats(winner).win();
        getStats(loser).loss();
    }

    @Override
    public void draw(AbstractPlayer[] players) {
        System.out.println("Draws");
        for (AbstractPlayer player : players){
            getStats(player).draw();
        }
    }

    @Override
    public void gameEnd() {
        System.out.println(toString());
    }

    @Override
    public void roundEnd() {
        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<AbstractPlayer, Statistics> entry : stats.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
        }
        return sb.toString();
    }
    private Statistics getStats(AbstractPlayer player) {
        if (stats.containsKey(player)){
            return stats.get(player);
        }else {
            Statistics s = new Statistics();
            stats.put(player, s);
            return s;
        }
    }
    
}
