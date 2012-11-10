package classifier.bayesian;

import static classifier.bayesian.CalculationUtility.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import classifier.IClassifier;
import classifier.dataset.DataSet;
import classifier.dataset.Instance;

public class BayesianClassifier implements IClassifier {

	public double[] probabilityOfClass;
	public List<List<double[]>> probabilityOfAttributeGivenClass; //List.get(class).get(attribute) = probabilities for attribute values
	public BayesianClassifier(DataSet ds) {
		int[] classes = ds.getClasses();
		int amountOfAttributes = ds.get(0).getAttributes().length;
				
		probabilityOfClass = new double[classes.length];
		for(int i = 0; i < probabilityOfClass.length; i++){
			probabilityOfClass[i] = probabilityOfClass(ds, classes[i]);
		}
		
		probabilityOfAttributeGivenClass = new ArrayList<>();
		for(int k = 0; k < classes.length; k++){
			List<double[]> givenClass = new ArrayList<>();
			for(int i = 0; i < amountOfAttributes; i++){
				double[] attributeValues = ds.getAttributeValues(i);
				for(int j = 0 ; j < attributeValues.length; j++){
					attributeValues[j] = probabilityOfAttributeGivenClass(ds, classes[k], attributeValues[j], i);
				}
				givenClass.add(attributeValues);
			}
			probabilityOfAttributeGivenClass.add(givenClass);
		}
	}

	@Override
	public int guessClass(Instance instance) {
		// TODO Auto-generated method stub
		return 0;
	}

}
