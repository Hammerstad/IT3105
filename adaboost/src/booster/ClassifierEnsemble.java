/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package booster;

import classifier.DataSet;
import classifier.IClassifier;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nicklas
 */
public class ClassifierEnsemble {
    private List<IClassifier> classifiers;
    
    public ClassifierEnsemble() {
        this.classifiers = new LinkedList<>();   
    }
    public void addClassifier(IClassifier classifier){
        if (!classifiers.contains(classifier)){
            this.classifiers.add(classifier);
        }
    }
    public void test(DataSet testData) {
        
    }
}
