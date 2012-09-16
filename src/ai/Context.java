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

        private Action(int i) {
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

    public static Context createContext(int playerId, GameState gameState, int raises, int players, double potOdds, Action action, double handStrength) {
        Context c = new Context();
        c.playerId = playerId;
        c.gameState = gameState;
        c.raises = raises;
        c.players = players;
        c.potOdds = potOdds;
        c.action = action;
        c.handstrengthAvg = handStrength;
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

    public GameState getGameState() {
        return gameState;
    }

    public int getRaises() {
        return raises;
    }

    public int getPlayers() {
        return players;
    }

    public double getPotOdds() {
        return potOdds;
    }

    public Action getAction() {
        return action;
    }

    public double getHandstrengthAvg() {
        return handstrengthAvg;
    }
}
