/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.matcher;

import classifier.dataset.DataSet;
import classifier.dataset.Instance;
import java.util.ArrayList;

/**
 *
 * @author Nicklas
 */
public class DataSetMatcher {

    public static DataSet filter(DataSet ds, Matcher m) {
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
