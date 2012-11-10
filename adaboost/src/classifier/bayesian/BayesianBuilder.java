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
public class BayesianBuilder extends IBuilder {

    @Override
    protected IClassifier generateHypothesis(DataSet ds) {
        return new BayesianClassifier(ds);
    }

}
