/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.discretization;

/**
 *
 * @author Nicklas
 */
public class TenFoldSplitting extends IDataSetPreProcess {

    @Override
    public double[][] process(double[][] data) {
        double[][] transposed = transpose(data);
        
        for (int attr = 0, s = transposed.length-1; attr < s; attr++) {
            transposed[attr] = discreization(transposed[attr]);
        }
        
        double[][] untransposed = transpose(transposed);
        return untransposed;
    }
    private static double[] discreization(double[] arr) {
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        for (double d : arr) {
            if (d < min) {
                min = d;
            }
            if (d > max) {
                max = d;
            }
        }
        for (int i = 0, s = arr.length;i < s; i++) {
            arr[i] = singleDiscreization(arr[i], min, max);
        }
        return arr;
    }
    private static int singleDiscreization(double i, double min, double max) {
        double foldSize = (max - min)/10.0;
        return (int) ((i-min)/foldSize);
    }
}
