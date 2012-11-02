/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.opponentmodeling;

import utilities.DataOutput;

/**
 *
 * @author Nicklas
 */
public class OpponentModeling {

    public static OpponentModeling instance;
    //contextTree[PlayerId][GAMESTATE][NOF_PLAYERS][potOdd][action][0:avg/1:count]
    private ContextHolder[][][][][] contextTree;

    private OpponentModeling() {
        contextTree = new ContextHolder[10][4][3][3][3];
    }

    public static OpponentModeling getInstance() {
        if (instance == null) {
            instance = new OpponentModeling();
        }
        return instance;
    }

    public void addContext(Context c) {
        ContextHolder soFar = contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()];
        if (soFar == null) {
            soFar = new ContextHolder();
        }
        contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()] = soFar;
        soFar.addHistoryEntry(c);
        double hsCount = soFar.size();
        double hsAvg = 0;
        for (Context cc : soFar.getContexts()) {
            hsAvg += cc.getHandstrength()[0];
        }
        hsAvg /= hsCount;

//        double[] newArr = new double[]{(soFar[0] * soFar[1] + c.getHandstrengthAvg()) / (soFar[1] + 1), soFar[1] + 1};

        int written = 0;
        DataOutput output = DataOutput.getInstance(this.getClass());
        written += output.writeLine("Added new context");
        written += output.writeLine("	PlayerId:		" + c.getPlayerId());
        written += output.writeLine("	Gamestate:	" + c.getGameState());
        written += output.writeLine("	Players:		" + c.getPlayers());
        written += output.writeLine("	PotOdds:		" + c.getPotOdds());
        written += output.writeLine("	Action:		" + c.getAction());
        written += output.writeLine("	HS_new:		" + c.getHandstrength());
        written += output.writeLine("	HS_avg:		" + hsAvg);
        written += output.writeLine("	HS_count:	" + hsCount);
        written += output.writeLine("");
//        contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getRaises(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()] = newArr;
    }

    public ContextHolder getData(Context c) {
        return contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()];
    }

    public double[] getAvgData(Context c) {
        ContextHolder ch = contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()];
        if (ch == null) {
            return new double[]{-1, 0};
        }
        double hsAvg = 0, hsCount = 0;
        hsCount = ch.size();
        for (Context cc : ch.getContexts()) {
            hsAvg += cc.getHandstrength()[0];
        }
        hsAvg /= hsCount;
        return new double[]{hsAvg, hsCount};
    }

    public double[] getMaxData(Context c) {
        ContextHolder ch = contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()];
        if (ch == null) {
            return new double[]{-1, 0};
        }
        double hsMax = -1;
        for (Context cc : ch.getContexts()) {
            if (cc.getHandstrength()[0] > hsMax) {
                hsMax = cc.getHandstrength()[0];
            }
        }
        return new double[]{hsMax, 1};
    }

    public double[] getMinData(Context c) {
        ContextHolder ch = contextTree[c.getPlayerId()][c.getGameState().getBucket()][Discretising.getBucket(c.getPlayers(), 10).bucketNo][Discretising.getBucket(c.getPotOdds()).bucketNo][c.getAction().getBucket()];
        if (ch == null) {
            return new double[]{-1, 0};
        }
        double hsMax = Integer.MAX_VALUE;
        for (Context cc : ch.getContexts()) {
            if (cc.getHandstrength()[0] < hsMax && cc.getHandstrength()[0] > 0) {
                hsMax = cc.getHandstrength()[0];
            }
        }
        return new double[]{hsMax, 1};
    }
}

enum Discretising {

//    NONE(0), FEW(1), SOME(2), MANY(3);
    NONE(0), FEW(1), SOME(2);
    int bucketNo;

    private Discretising(int i) {
        this.bucketNo = i;
    }

    public static Discretising getBucket(double val) {
        return getBucket(val, 1.0);
    }

    public static Discretising getBucket(double value, double max) {
        double val = value / max;
//        if (val < 0.25) {
//            return NONE;
//        } else if (val < 0.5) {
//            return FEW;
//        } else if (val < 0.75) {
//            return SOME;
//        } else {
//            return MANY;
//        }
        if (val < 0.33) {
            return NONE;
        } else if (val < 0.66) {
            return FEW;
        }else {
            return SOME;
        }
    }
}