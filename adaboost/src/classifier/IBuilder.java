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

    public Pair<IClassifier, DataSet> build(DataSet ds) {
        IClassifier classifier = generateHypothesis(ds);
        DataSet modifiedDataSet = update(classifier, ds);
        return new Pair<>(classifier, modifiedDataSet);
    }

    protected abstract IClassifier generateHypothesis(DataSet ds);

    protected DataSet update(IClassifier classifier, DataSet dataSet) {
        System.err.println("UPDATING!!!");
        double error = 0.0;
        Instance currentInstance;
        int corr = 0;
        for (int i = 0; i < dataSet.length(); i++) {
            currentInstance = dataSet.get(i);
            if (classifier.guessClass(currentInstance) != currentInstance.getCategory()) {
                System.out.println("I was wrong adding weight: "+currentInstance.getWeight());
                error += currentInstance.getWeight();
            }
        }
        for (int i = 0; i < dataSet.length(); i++) {
            currentInstance = dataSet.get(i);
            if (classifier.guessClass(currentInstance) == currentInstance.getCategory()) {
                currentInstance.setWeight(currentInstance.getWeight() * (error / (1 - error)));
                corr++;
            }
        }

        double[] allWeights = dataSet.getInstanceWeights();
        double allWeightsSummed = MathHelper.sum(allWeights);
        for (Instance instance : dataSet.getInstances()) {
            instance.setWeight(instance.getWeight() / allWeightsSummed);
        }
        System.out.println("Error: "+error+" "+((1-error)/error));
        classifier.setWeight(Math.log((1-error)/error));
        System.out.println("Classifier had "+corr+" out of "+dataSet.length()+" correct. Weight: "+classifier.getWeight());
        return dataSet;
    }
}
