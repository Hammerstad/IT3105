package classifier.bayesian;

import util.MathHelper;
import util.Pair;
import classifier.IBuilder;
import classifier.IClassifier;
import classifier.dataset.DataSet;
import classifier.dataset.Instance;


/**
 * 
 * @author Nicklas Utgaard & Eirik Mildestveit Hammerstad
 */
public class BayesianBuilder implements IBuilder {

	@Override
	public Pair<IClassifier, DataSet> build(DataSet ds) {
		double[] weights = ds.getInstanceWeights();
		IClassifier classifier = generateHypothesis(ds);
		DataSet modifiedDataSet = update(classifier, ds);
		return new Pair<IClassifier, DataSet>(classifier, modifiedDataSet);
	}

	private IClassifier generateHypothesis(DataSet ds) {
		return new BayesianClassifier(ds);
	}

	private DataSet update(IClassifier classifier, DataSet dataSet) {
		double error = 0.0;
		Instance currentInstance;
		for (int i = 0; i < dataSet.length(); i++) {
			currentInstance = dataSet.get(i);
			if (classifier.guessClass(currentInstance) == currentInstance.getCategory()) {
				currentInstance.setWeight(currentInstance.getWeight()*(error/(1-error)));
			} else {
				error += currentInstance.getWeight();
			}
		}
		double[] allWeights = dataSet.getInstanceWeights();
		double allWeightsSummed = MathHelper.sum(allWeights);
		for (Instance instance : dataSet.getInstances()) {
			instance.setWeight(instance.getWeight() / allWeightsSummed);
		}
		dataSet.setWeight(Math.log((1 - error) / error));
		return dataSet;
	}

}
