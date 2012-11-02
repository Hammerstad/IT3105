/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.opponentmodeling;

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
    private int players;
    private double potOdds;
    private Action action;
    private int[] powerrating = null;

    public static Context createContext(int playerId, GameState gameState, int players, double potOdds, Action action, int[] powerrating) {
        Context c = new Context();
        c.playerId = playerId;
        c.gameState = gameState;
        c.players = players;
        c.potOdds = potOdds;
        c.action = action;
        c.powerrating = powerrating;
        return c;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.gameState != null ? this.gameState.hashCode() : 0);
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
    public int getPlayers() {
        return players;
    }

    public double getPotOdds() {
        return potOdds;
    }

    public Action getAction() {
        return action;
    }

    public int[] getHandstrength() {
        return powerrating;
    }
}
