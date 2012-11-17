/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package booster;

import classifier.dataset.DataSet;
import classifier.IClassifier;
import classifier.dataset.Instance;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Nicklas
 */
public class ClassifierEnsemble {

    private List<IClassifier> classifiers;
    private int[] classesFoundInDataset;

    public ClassifierEnsemble(DataSet dataset) {
        this.classifiers = new LinkedList<>();
        classesFoundInDataset = dataset.getClasses();
    }

    public void addClassifier(IClassifier classifier) {
        if (!classifiers.contains(classifier)) {
            this.classifiers.add(classifier);
        }
    }

    public void test(DataSet testData) {
        int correctlyClassified = 0;
        for (Instance i : testData.getInstances()) {
            if (i.getCategory() == getConsensus(i)) {
                correctlyClassified++;
            }
        }
        System.out.println("Correct: " + correctlyClassified + " of a total of " + testData.length());
    }

    private int getConsensus(Instance i) {
        Map<Integer, Double> votes = new HashMap<>();
        for (int cls : classesFoundInDataset) {
            votes.put(cls, new Double(0));
        }
        for (IClassifier c : classifiers) {
            int guess = c.guessClass(i);
            if (guess == -1) {
                continue;
            }
            votes.put(guess, votes.get(guess) + c.getWeight());
        }
        double min = -Double.MAX_VALUE;
        int minCls = -1;
        for (Entry<Integer, Double> entry : votes.entrySet()) {
            if (entry.getValue() > min) {
                min = entry.getValue();
                minCls = entry.getKey();
            }
        }
//        System.out.println("Entries: "+Arrays.toString(votes.entrySet().toArray()));
        return minCls;
    }

    public void diffLast(DataSet ds) {
        if (classifiers.size() < 2) {
            return;
        }
        IClassifier last = classifiers.get(classifiers.size() - 1);
        IClassifier secondlast = classifiers.get(classifiers.size() - 1);
        int diff = 0;
        for (Instance i : ds.getInstances()) {
            if (last.guessClass(i) != secondlast.guessClass(i)) {
                diff++;
            }
        }
        System.out.println("Diff between last to classifiers: "+diff);
    }
}
