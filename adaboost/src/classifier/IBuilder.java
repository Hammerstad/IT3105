/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import classifier.dataset.DataSet;
import classifier.dataset.Instance;
import util.MathHelper;
import util.Pair;

/**
 *
 * @author Nicklas
 */
public abstract class IBuilder {

    private boolean redo = true;

    public Pair<IClassifier, DataSet> build(DataSet ds) {
        redo = true;
        IClassifier classifier = null;
        DataSet modifiedDataSet = null;
        while (redo) {
            classifier = generateHypothesis(ds);
            modifiedDataSet = update(classifier, ds);
        }
        return new Pair<>(classifier, modifiedDataSet);
    }

    protected abstract IClassifier generateHypothesis(DataSet ds);

    protected DataSet update(IClassifier classifier, DataSet dataSet) {
        int misses = 0;
        int hits = 0;
        double error = 0;
        Instance currentInstance;
        int corr = 0;
        double beta = 1.5;
        int numberOfClasses = dataSet.getClasses().length;
        for (int i = 0; i < dataSet.length(); i++) {
            currentInstance = dataSet.get(i);
            if (classifier.guessClass(currentInstance) != currentInstance.getCategory()) {
                error += currentInstance.getWeight();
            }
        }
        if (error >= (numberOfClasses - 1) / (numberOfClasses * 1.0)) {
            return jiggleWeight(dataSet, beta);
        } else if (error > 0 && error < (numberOfClasses - 1) / (numberOfClasses * 1.0)) {
            for (int i = 0; i < dataSet.length(); i++) {
                currentInstance = dataSet.get(i);
                if (classifier.guessClass(currentInstance) == currentInstance.getCategory()) {
                    hits++;
                    currentInstance.setWeight(currentInstance.getWeight() * ((1 - error) / error) * (numberOfClasses - 1));
                    corr++;
                }
            }

            double[] allWeights = dataSet.getInstanceWeights();
            double allWeightsSummed = MathHelper.sum(allWeights);
            for (Instance instance : dataSet.getInstances()) {
                instance.setWeight(instance.getWeight() / allWeightsSummed);
            }
            classifier.setWeight(Math.log(((1 - error) / error)) * (numberOfClasses - 1));
        }else if (error == 0) {
//            System.out.println("Null error");
            jiggleWeight(dataSet, beta);
            classifier.setWeight(10+Math.log(numberOfClasses-1));
        }
        redo = false;
        return dataSet;
    }

    private DataSet jiggleWeight(DataSet ds, double beta) {
//        System.out.println("Wiggle wiggle wiggle");
        for (Instance i : ds.getInstances()) {
            i.setWeight(Math.max(0, i.getWeight() + random(-1 / (Math.pow(ds.length(), beta)), 1 / (Math.pow(ds.length(), beta)))));
        }
        double sum = MathHelper.sum(ds.getInstanceWeights());
        for (Instance i : ds.getInstances()) {
            i.setWeight(i.getWeight() / sum);
        }
        return ds;
    }

    private double random(double start, double end) {
        return Math.random() * (end - start) - start;
    }
}
