package classifier.dataset;

import java.util.Arrays;

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */
public class Instance {

	/**
	 * The class of this instance
	 */
	private final int category;

	/**
	 * An array with the attributes of this instance.
	 */
	private final double[] attributes;

	/**
	 * The current weight of this instance. Initiated to 1.0 if not specified.
	 */
	private double weight;

	/**
	 * Creates an instance from a double array. The last element in the array becomes the category(class). <br/>
	 * Sets the weight of this instance to 1.0.
	 * 
	 * @param instance
	 *            - double array, last element will become category(class).
	 */
	public Instance(double[] instance) {
		this(instance, 1.0);
	}

	/**
	 * Creates an instance from a double array. The last element in the array becomes the category(class).
	 * 
	 * @param instance
	 *            - double array, last element will become category(class).
	 * @param weight
	 *            - the weight of this instance.
	 */
	public Instance(double[] instance, double weight) {
		attributes = new double[instance.length - 1];
		System.arraycopy(instance, 0, attributes, 0, instance.length - 1);
		category = (int) instance[instance.length - 1];
		this.weight = weight;
	}

	/**
	 * Creates an instance from another instance.
	 * 
	 * @param instance
	 *            - instance to be copied.
	 */
	public Instance(Instance instance) {
		// No need to shallow-copy the attributes, we never change the attributes =)
		this.attributes = instance.attributes;
		this.category = instance.category;
		this.weight = instance.weight;
	}

	/**
	 * Returns the weight of this instance.
	 * 
	 * @return
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Returns the attributes of this instance.
	 * 
	 * @return double[] instance
	 */
	public double[] getAttributes() {
		return attributes;
	}

	/**
	 * Returns the category(class) of this instance.
	 * 
	 * @return int category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * Sets the weight of this instance.
	 * 
	 * @param weight
	 *            - double weight
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	/**
	 * Prints out the attributes, class and weight of this Instance.
	 */
	public String toString() {
		return "Attr: " + Arrays.toString(attributes) + " \t Class: " + category + " \t Weight: " + weight;
	}
}
