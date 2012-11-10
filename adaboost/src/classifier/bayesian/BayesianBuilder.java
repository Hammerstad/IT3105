package classifier.bayesian;

import classifier.IBuilder;
import classifier.IClassifier;
import classifier.dataset.DataSet;

/**
 *
 * @author Nicklas Utgaard & Eirik Mildestveit Hammerstad
 */
public class BayesianBuilder extends IBuilder {

    @Override
    protected IClassifier generateHypothesis(DataSet ds) {
        return new BayesianClassifier(ds);
    }

}
