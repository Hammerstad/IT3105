package classifier.bayesian;

import classifier.dataset.DataSet;
import classifier.dataset.Instance;

public class CalculationUtility {

	/**
	 * Calculates the probability of an attribute given a class in a DataSet.
	 * @param set - the DataSet
	 * @param category - the class
	 * @param attributeSize - the value of the attribute
	 * @param attributePosition - which attribute
	 * @return probability of class [ 0.0 , 1.0 ] 
	 */
	public static double ProbabilityOfAttributeGivenClass(DataSet set, double category, double attributeSize, int attributePosition) {
		int amountOfClass = 0;
		double amountOfAttributeGivenClass = 0;
		for(Instance instance : set.getInstances()){
			if(instance.getCategory() == category){
				amountOfClass++;
				if(instance.getAttributes()[attributePosition] == attributeSize){
					amountOfAttributeGivenClass+=instance.getWeight();
				}
			}
		}
		return amountOfAttributeGivenClass/amountOfClass;
	}

	/**
	 * Calculates the probability of a class in a DataSet.
	 * @param set - the DataSet
	 * @param category - the class
	 * @return probability of class [ 0.0 , 1.0 ]
	 */
	public static double ProbabilityOfClass(DataSet set, double category) {
		int amountOfClass = 0;
		for(Instance instance : set.getInstances()){
			if(instance.getCategory() == category){
				amountOfClass++;
			}
		}
		return amountOfClass/set.length();
	}

}
