/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.opponentmodeling;

import ai.player.AbstractPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Nicklas
 */
public class ContextHolder {

    private List<Context> history;

    public ContextHolder() {
        this.history = new ArrayList<Context>();
    }

    public void addHistoryEntry(Context he) {
        this.history.add(he);
    }

    public void clearHistory() {
        this.history.clear();
    }

    public void pushToOpponentModeler(List<AbstractPlayer> activePlayers) {
        OpponentModeling om = OpponentModeling.getInstance();
        List<Integer> activePlayersId = new ArrayList<Integer>();
        for (AbstractPlayer ap : activePlayers) {
            activePlayersId.add(ap.getPlayerId());
        }
        Collections.sort(activePlayersId);
        for (Context c : history) {
            if (Collections.binarySearch(activePlayersId, c.getPlayerId()) < 0) {
                continue;
            }
            om.addContext(c);
        }
    }

    public int size() {
        return this.history.size();
    }

    public List<Context> getContexts() {
        return this.history;
    }

    public double getAverageHandstrength() {
        double sum = 0;
        for (Context c : history) {
            sum += c.getHandstrength()[0];
        }
        return sum / history.size();
    }

    public double getMaxHandstrength() {
        double max = 2;
        for (Context c : history) {
            if (c.getHandstrength()[0] > max) {
                max = c.getHandstrength()[0];
            }
        }
        return max;
    }

    public double getMinHandstrength() {
        double min = 0;
        for (Context c : history) {
            if (c.getHandstrength()[0] < min && c.getHandstrength()[0] > 0) {
                min = c.getHandstrength()[0];
            }
        }
        return min;
    }
}
