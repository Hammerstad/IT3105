/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.matcher;

import classifier.DataSet;
import classifier.Instance;
import java.util.ArrayList;

/**
 *
 * @author Nicklas
 */
public class DataSetMatcher {

    public static DataSet match(DataSet ds, Matcher m) {
        ArrayList<Instance> l = new ArrayList<>();
        for (Instance i : ds.getInstances()) {
            if (m.match(i)) {
                l.add(i);
            }
        }
        Instance[] lo = new Instance[l.size()];
        return new DataSet(l.toArray(lo));
    }
}
