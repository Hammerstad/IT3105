package classifier.bayesian;

import static classifier.bayesian.CalculationUtility.probabilityOfAttributeGivenClass;
import static classifier.bayesian.CalculationUtility.probabilityOfClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.MathHelper;

import classifier.IClassifier;
import classifier.dataset.DataSet;
import classifier.dataset.Instance;

public class BayesianClassifier implements IClassifier {

	private Map<Integer, Category> probabilityOfAttributeGivenClass; //List.get(class).get(attribute) = probabilities for attribute values
	private Map<Integer, Double> probabilityOfClass;
	
	
	public BayesianClassifier(DataSet ds) {
		int[] classes = ds.getClasses();
		Arrays.sort(classes);
		int amountOfAttributes = ds.get(0).getAttributes().length;
				
		probabilityOfClass = new HashMap<>();
		for(int i = 0; i < classes.length; i++){
			probabilityOfClass.put(classes[i], probabilityOfClass(ds, classes[i]));
		}
		probabilityOfAttributeGivenClass = new HashMap<>();
		for(int k = 0; k < classes.length; k++){
			List<Attribute> givenClass = new ArrayList<>();
			for(int i = 0; i < amountOfAttributes; i++){
				double[] attributeValues = ds.getAttributeValues(i);
				Map<Double, Double> output = new HashMap<>();
				for(int j = 0 ; j < attributeValues.length; j++){
					output.put(attributeValues[j], probabilityOfAttributeGivenClass(ds, classes[k], attributeValues[j], i));
				}
				givenClass.add(new Attribute(output));
			}
			AttributeList probabilityOfAttributeGivenClass = new AttributeList(givenClass);
			this.probabilityOfAttributeGivenClass.put(classes[k], new Category(classes[k],probabilityOfAttributeGivenClass));
		}
		
	}

	@Override
	public int guessClass(Instance instance) {
		double bestProbability = -1;
		int bestClass = -1;
		
		for(int element : probabilityOfClass.keySet()){
			double probabilityOfThisClass = probabilityOfClass.get(element);
			
			for(int i = 0; i < probabilityOfAttributeGivenClass.get(element).size(); i++){
				double attributeOfCurrentInstance = instance.getAttributes()[i];
				probabilityOfThisClass *= probabilityOfAttributeGivenClass.get(element).getAttribute(i).getValue(attributeOfCurrentInstance);
			}

			if(probabilityOfThisClass > bestProbability){
				bestProbability = probabilityOfThisClass;
				bestClass = element;
			}
		}
		
		if(bestClass == -1){
			bestClass = 0;
		}
		return bestClass;
	}

}
