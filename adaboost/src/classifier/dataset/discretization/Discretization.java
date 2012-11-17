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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DataSetReader;

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */
public class Discretization {
    private IDataSetPreProcess[] processes;

    public Discretization(IDataSetPreProcess[] preprocessing) {
        this.processes = preprocessing;
    }
    public DataSet process(double[][] data) {
        for (IDataSetPreProcess dspp : processes) {
            data = dspp.process(data);
        }
        Instance[] instances = new Instance[data.length];
        int i = 0;
        double weight = 1.0/instances.length;
        for (double[] row : data) {
            instances[i++] = new Instance(row, weight);
        }
        return new DataSet(instances);
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
            Discretization disc = new Discretization(new IDataSetPreProcess[]{new TenFoldSplitting()});
            DataSet ds = disc.process(draw);
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
            System.out.println("Ending");
        }
    }
}
