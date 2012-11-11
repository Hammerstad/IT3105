package classifier.bayesian;

import java.util.ArrayList;
import java.util.List;

import util.Pair;
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
	public static double probabilityOfAttributeGivenClass(DataSet set, double category, double attributeSize, int attributePosition) {
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
	public static double probabilityOfClass(DataSet set, int category) {
		int amountOfClass = 0;
		for(Instance instance : set.getInstances()){
			if(instance.getCategory() == category){
				amountOfClass++;
			}
		}
		return amountOfClass/(set.length()*1.0);
	}

	/**
	 * Calculates the probability of an attribute given a class in a DataSet. Returns two lists of instances. In P(A|B) it returns a pair of P(B|A) and P(A).
	 * @param set - the DataSet
	 * @param category - the class
	 * @param attributeSize - the value of the attribute
	 * @param attributePosition - which attribute
	 * @return A pair: { P(B|A) , P(A) }
	 */
	public static Pair<List<Instance>, List<Instance>> probabilityOfAttributeGivenClassAsInstanceLists(DataSet set, double category, double attributeSize, int attributePosition) {
		List<Instance> ofClass = new ArrayList<>();
		List<Instance> attributeGivenClass = new ArrayList<>();
		for(Instance instance : set.getInstances()){
			if(instance.getCategory() == category){
				ofClass.add(new Instance(instance));
				if(instance.getAttributes()[attributePosition] == attributeSize){
					attributeGivenClass.add(new Instance(instance));
				}
			}
		}
		return new Pair<>(ofClass, attributeGivenClass);
	}

	/**
	 * Calculates the probability of a class in a DataSet.
	 * @param set - the DataSet
	 * @param category - the class
	 * @return probability of class [ 0.0 , 1.0 ]
	 */
	public static List<Instance> probabilityOfClassAsInstanceList(DataSet set, double category) {
		List<Instance> instances = new ArrayList<>();
		for(Instance instance : set.getInstances()){
			if(instance.getCategory() == category){
				instances.add(new Instance(instance));
			}
		}
		return instances;
	}
}
