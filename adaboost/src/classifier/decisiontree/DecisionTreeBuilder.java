/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.decisiontree;

import classifier.dataset.DataSet;
import classifier.IBuilder;
import classifier.IClassifier;
import util.Pair;

/**
 *
 * @author Nicklas
 */
public class DecisionTreeBuilder implements IBuilder {

    @Override
    public Pair<IClassifier,DataSet> build(DataSet data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
