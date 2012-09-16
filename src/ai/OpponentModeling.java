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
public class OpponentModeling {

    public static OpponentModeling instance;
    //contextTree[PlayerId][GAMESTATE][NOF_PLAYERS][RAISES][potOdd][action][0:avg/1:count]
    private double[][][][][][][] contextTree;

    private OpponentModeling() {
        contextTree = new double[10][9][4][4][4][3][2];
    }
    public static OpponentModeling getInstance() {
        if (instance == null)instance = new OpponentModeling();
        return instance;
    }
    public void addContext(Context c) {
        double[] soFar = contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getRaises(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()];
        double[] newArr = new double[]{(soFar[0] * soFar[1] + c.getHandstrengthAvg()) / (soFar[1] + 1), soFar[1] + 1};
        contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getRaises(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()] = newArr;
    }
    public double[] getData(Context c){
        return contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getRaises(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()];
    }
}

enum Discretising {

    NONE(0), FEW(1), SOME(2), MANY(3);
    int bucketNo;

    private Discretising(int i) {
        this.bucketNo = i;
    }

    public static Discretising getBucket(double val) {
        return getBucket(val, 1.0);
    }

    public static Discretising getBucket(double value, double max) {
        double val = value / max;
        if (val < 0.25) {
            return NONE;
        } else if (val < 0.5) {
            return FEW;
        } else if (val < 0.75) {
            return SOME;
        } else {
            return MANY;
        }
    }
}