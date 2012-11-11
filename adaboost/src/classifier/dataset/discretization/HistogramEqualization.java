/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.discretization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nicklas
 */
public class HistogramEqualization extends IDataSetPreProcess {

    @Override
    public double[][] process(double[][] data) {
        double[][] transposed = transpose(data);

        for (int attr = 0, s = transposed.length-1; attr < s; attr++) {
            transposed[attr] = discretization(transposed[attr]);
        }
        double[][] untransposed = transpose(transposed);
        return untransposed;
    }

    private static double[] discretization(double[] arr) {
        //Count the number of elements
        Map<Double, Double> map = new LinkedHashMap<>();
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        for (double d : arr) {
            if (map.containsKey(d)) {
                map.put(d, map.get(d) + 1);
            } else {
                map.put(d, 1.0);
            }
            if (d < min) {
                min = d;
            }
            if (d > max) {
                max = d;
            }
        }
        List<Double> keys = new ArrayList<Double>(map.keySet());
        Collections.sort(keys);
        double cummulative = 0;
        for (double d : keys) {
            cummulative += (map.get(d) / arr.length);
            map.put(d, cummulative);
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (map.get(arr[i]) * (max - min) + min);
        }
        return arr;
    }
    
}
