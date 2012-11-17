package classifier.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */
public class DataSet {

	/**
	 * The different classes contained in this dataset
	 */
	private int[] classes;

	/**
	 * The different attribute values contained in this dataset
	 */
	private Map<Integer, double[]> attributeValues;
	/**
	 * All of the instances in this DataSet
	 */
	private Instance[] instances;

	/**
	 * Default constructor
	 */
	private DataSet() {

	}

	/**
	 * Creates a DataSet from an ArrayList containing double lists. The last element in the double lists is the class/category of the instance.
	 * 
	 * @param list
	 *            - ArrayList with double lists, the instances - unweighed.
	 */
	public DataSet(ArrayList<double[]> list) {
		this();
		instances = new Instance[list.size()];
		for (int i = 0; i < list.size(); i++) {
			instances[i] = new Instance(list.get(i), 1.0 / list.size());
		}
		this.attributeValues = new HashMap<>();
	}

	/**
	 * Creates a DataSet from a list of instances.
	 * 
	 * @param instances
	 */
	public DataSet(Instance[] instances) {
		this();
		this.instances = instances;
		this.attributeValues = new HashMap<>();
	}

	/**
	 * Returns a list of the weights of all the instances.
	 * 
	 * @return weight of all the instances
	 */
	public double[] getInstanceWeights() {
		double[] list = new double[instances.length];
		for (int i = 0; i < instances.length; i++) {
			list[i] = instances[i].getWeight();
		}
		return list;
	}

	/**
	 * How many Instances there are in our DataSet.
	 * 
	 * @return length
	 */
	public int length() {
		return instances.length;
	}

	/**
	 * Returns an array of the Instances in this DataSet.
	 * 
	 * @return Instances
	 */
	public Instance[] getInstances() {
		return instances;
	}

	/**
	 * Returns a specific Instance
	 * 
	 * @param position
	 *            - 0-indexed position
	 * @return Instance
	 */
	public Instance get(int position) {
		return instances[position];
	}

	/**
	 * Returns a DataSet containing a subset of instances.</br> IMPORTANT: EXCLUDING LAST ELEMENT!
	 * 
	 * @param from
	 *            - position of first element, 0-indexed
	 * @param to
	 *            - position of last element, 0-indexed, excluding
	 * @return DataSet with a subset of the instances
	 */
	public DataSet subset(int from, int to) {
		Instance[] instances = new Instance[to - from];
		System.arraycopy(this.instances, from, instances, 0, to - from);
		return new DataSet(instances);
	}

	/**
	 * Overridden toString method. Prints all the instances.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Instance element : instances) {
			sb.append(element).append('\n');
		}
		return sb.toString();
	}

	/**
	 * Return the different classes contained in this dataset
	 * 
	 * @return int
	 */
	public int[] getClasses() {
		if (this.classes == null) {
			Set<Integer> found = new HashSet<>();
			for (Instance i : instances) {
				found.add(i.getCategory());
			}
			this.classes = new int[found.size()];
			int index = 0;
			for (Integer i : found) {
				classes[index++] = i;
			}
		}
		return this.classes;
	}

	/**
	 * Returns an array with all the different values an attribute may have.
	 * 
	 * @param attribute
	 *            - double[] with unique double values.
	 * @return
	 */
	public double[] getAttributeValues(int attribute) {
		if (!this.attributeValues.containsKey(attribute)) {
			Set<Double> found = new HashSet<>();
			for (Instance i : instances) {
				found.add(i.getAttributes()[attribute]);
			}
			double[] attributes = new double[found.size()];
			int index = 0;
			for (Double d : found) {
				attributes[index++] = d;
			}
			this.attributeValues.put(attribute, attributes);
		}
		return this.attributeValues.get(attribute);
	}

	/**
	 * Returns the total weights of this dataSet.
	 * 
	 * @return double, should be close to 1.0.
	 */
	public double totalWeights() {
		double weight = 0;
		for (Instance element : this.instances) {
			weight += element.getWeight();
		}
		return weight;
	}
}
