/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import poker.GameState;

/**
 *
 * @author Nicklas
 */
public class Context {

    public enum Action {

        CALL(0), RAISE(1), FOLD(2);
        
        int i;
        private Action(int i){
            this.i = i;
        }
        public int getBucket() {
            return i;
        }
    }

    private int playerId;
    private GameState gameState;
    private int raises;
    private int players;
    private double potOdds;
    private Action action;
    private double handstrengthAvg = 0;
    private int handstrengthCount = 0;
    
    public static Context createContext(int playerId, GameState gameState, int raises, int players, double potOdds, Action action, double handStrength) {
        Context c = new Context();
        c.playerId = playerId;
        c.gameState = gameState;
        c.raises = raises;
        c.players = players;
        c.potOdds = potOdds;
        c.action = action;
        c.handstrengthAvg = handStrength;
        c.handstrengthCount++;
        return c;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.gameState != null ? this.gameState.hashCode() : 0);
        hash = 37 * hash + this.raises;
        hash = 37 * hash + this.players;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.potOdds) ^ (Double.doubleToLongBits(this.potOdds) >>> 32));
        hash = 37 * hash + (this.action != null ? this.action.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Context other = (Context) obj;
        if (this.gameState != other.gameState) {
            return false;
        }
        if (this.raises != other.raises) {
            return false;
        }
        if (this.players != other.players) {
            return false;
        }
        if (Double.doubleToLongBits(this.potOdds) != Double.doubleToLongBits(other.potOdds)) {
            return false;
        }
        if (this.action != other.action) {
            return false;
        }
        return true;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getRaises() {
        return raises;
    }

    public void setRaises(int raises) {
        this.raises = raises;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public double getPotOdds() {
        return potOdds;
    }

    public void setPotOdds(double potOdds) {
        this.potOdds = potOdds;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public double getHandstrengthAvg() {
        return handstrengthAvg;
    }

    public void setHandstrengthAvg(double handstrengthAvg) {
        this.handstrengthAvg = handstrengthAvg;
    }

    public int getHandstrengthCount() {
        return handstrengthCount;
    }

    public void setHandstrengthCount(int handstrengthCount) {
        this.handstrengthCount = handstrengthCount;
    }
    

}
