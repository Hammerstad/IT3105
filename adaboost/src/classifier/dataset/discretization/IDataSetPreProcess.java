/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.discretization;

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */
public abstract class IDataSetPreProcess {
    public abstract double[][] process(double[][] data);
    
    protected static double[][] transpose(double[][] m) {       
        
        double[][] n = new double[m[0].length][m.length];
        for (int i = 0, s = m.length; i < s; i++) {
            for (int j = 0, t = m[0].length; j < t; j++) {
                n[j][i] = m[i][j];
            }
        }
        return n;
    }
}
