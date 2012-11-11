package classifier.bayesian;

import static classifier.bayesian.CalculationUtility.probabilityOfAttributeGivenClass;
import static classifier.bayesian.CalculationUtility.probabilityOfClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classifier.IClassifier;
import classifier.dataset.DataSet;
import classifier.dataset.Instance;

public class BayesianClassifier implements IClassifier {

	private List<List<double[]>> probabilityOfAttributeGivenClass; //List.get(class).get(attribute) = probabilities for attribute values
	private Map<Integer, Double> probabilityOfClass;
	
	
	public BayesianClassifier(DataSet ds) {
		int[] classes = ds.getClasses();
		int amountOfAttributes = ds.get(0).getAttributes().length;
				
		probabilityOfClass = new HashMap<>();
		for(int i = 0; i < classes.length; i++){
			probabilityOfClass.put(classes[i], probabilityOfClass(ds, classes[i]));
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
		int classOfInstance = instance.getCategory();
		double probabilityOfClass = this.probabilityOfClass.get(classOfInstance);
		int multipliedProbabilityOfAttributeGivenClass = 0;
		for(int i = 0; i < instance.getAttributes().length; i++){
			//multipliedProbabilityOfAttributeGivenClass *= probabilityOfAttributeGivenClass.get(classOfInstance).get(i);
		}
		return 0;
	}

}
