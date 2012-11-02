/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.discretization;

import classifier.dataset.DataSet;
import classifier.dataset.Instance;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DataSetReader;

/**
 *
 * @author Nicklas
 */
public class Discretization {

    public static DataSet discretization(double[][] data) {
        double[][] transposed = transpose(data);

        for (int attr = 0, s = transposed.length-1; attr < s; attr++) {
            transposed[attr] = discretization(transposed[attr]);
        }
        double[][] untransposed = transpose(transposed);
        Instance[] instances = new Instance[untransposed.length];
        for (int i = 0, l = instances.length; i < l; i++) {
            instances[i] = new Instance(untransposed[i], 1.0 / data.length);
        }
        return new DataSet(instances);
    }

    private static double[][] transpose(double[][] m) {       
        
        double[][] n = new double[m[0].length][m.length];
        for (int i = 0, s = m.length; i < s; i++) {
            for (int j = 0, t = m[0].length; j < t; j++) {
                n[j][i] = m[i][j];
            }
        }
        return n;
    }

    private static double[] discretization(double[] arr) {
        //Count the number of elements
        Map<Double, Double> map = new LinkedHashMap<>();
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
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
        System.out.println("Min: "+min+" Max: "+max);
//        //Normalize
//        for (Entry<Double, Double> e : map.entrySet()){
//            e.setValue(e.getValue()/arr.length);
//        }
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

    public static void main(String[] args) {
        FileOutputStream fos = null;
        try {
            DataSetReader r = new DataSetReader();
            ArrayList<double[]> d = r.read("glass");
            double[][] draw = new double[d.size()][d.get(0).length];
            for (int i = 0; i < d.size(); i++){
                draw[i] = d.get(i);
            }
            DataSet ds = discretization(draw);
            fos = new FileOutputStream(new File("glassDisc.txt"));
            StringBuilder sb = new StringBuilder();
            for (Instance i : ds.getInstances()) {
                for (double dd : i.getAttributes()) {
                    sb.append(dd).append(", ");
                }
                sb.append(i.getCategory());
//                sb.delete(sb.length() - 2, sb.length());
                sb.append("\n");
            }
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Discretization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Discretization.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }
}
