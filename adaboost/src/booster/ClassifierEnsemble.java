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

    private static int diff = 0;
    private List<IClassifier> classifiers;
    private int[] classesFoundInDataset;
    private DataSet trainingSet, testSet;

    public ClassifierEnsemble(DataSet trainingSet, DataSet testSet) {
        this.classifiers = new LinkedList<>();
        this.trainingSet = trainingSet;
        this.testSet = testSet;
        classesFoundInDataset = trainingSet.getClasses();
    }

    public void addClassifier(IClassifier classifier) {
        if (!classifiers.contains(classifier)) {
            this.classifiers.add(classifier);
            double training = 0, test = 0;
            for (Instance i : trainingSet.getInstances()) {
                if (classifier.guessClass(i) == i.getCategory()) {
                    training++;
                }
            }
            for (Instance i : testSet.getInstances()) {
                if (classifier.guessClass(i) == i.getCategory()) {
                    test++;
                }
            }
            
            String w = String.format(classifiers.size()+". Weight: %.2f", classifier.getWeight());
            String r = String.format(" HitRatio: %.2f / %.2f", (double)(training/trainingSet.length()), (double)(test/testSet.length()));
            System.out.println(w+r);
            diffLast(trainingSet);
            diffLast(testSet);
        }
    }

    public void test() {
        int correctlyClassified = 0;
        for (Instance i : testSet.getInstances()) {
            if (i.getCategory() == getConsensus(i)) {
                correctlyClassified++;
            }
        }
        System.out.println("Correct: " + correctlyClassified + " of a total of " + testSet.length()+String.format(" %.2f", correctlyClassified*1.0/testSet.length() )+"%");
        System.out.println("InterChange: "+diff);
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
        int tdiff = 0;
        IClassifier last = classifiers.get(classifiers.size() - 1);
        IClassifier secondlast = classifiers.get(classifiers.size() - 2);
        for (Instance i : ds.getInstances()) {
            if (last.guessClass(i) != secondlast.guessClass(i)) {
                diff++;
            } else if (last.guessClass(i) == i.getCategory()) {
                tdiff++;
            }
        }
    }
}
