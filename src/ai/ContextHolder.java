/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.ArrayList;
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
    public void pushToOpponentModeler() {
        OpponentModeling om = OpponentModeling.getInstance();
        for (Context c : history){
            om.addContext(c);
        }
    }
    public int size() {
        return this.history.size();
    }
    public List<Context> getContexts() {
        return this.history;
    }
}
