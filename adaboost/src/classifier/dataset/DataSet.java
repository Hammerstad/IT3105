package classifier.dataset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataSet {
    
    /**
     * The number of different classes contained in this dataset
     */
    private int[] numberOfClasses;

    /**
     * All of the instances in this DataSet
     */
    private Instance[] instances;
    /**
     * The weight of this DataSet
     */
    private double weight;

    /**
     * Default constructor
     */
    private DataSet() {
        weight = 1.0;
    }

    /**
     * Creates a DataSet from an ArrayList containing double lists. The last
     * element in the double lists is the class/category of the instance.
     *
     * @param list - ArrayList with double lists, the instances - unweighed.
     */
    public DataSet(ArrayList<double[]> list) {
        this();
        instances = new Instance[list.size()];
        for (int i = 0; i < list.size(); i++) {
            instances[i] = new Instance(list.get(i), 1.0 / list.size());
        }
    }

    /**
     * Creates a DataSet from a list of instances.
     *
     * @param instances
     */
    public DataSet(Instance[] instances) {
        this();
        this.instances = instances;
    }

    /**
     * Returns a list of the weights of all the instances.
     * @return weight of all the instances
     */
    public double[] getInstanceWeights(){
    	double[] list = new double[instances.length];
    	for(int i = 0; i < instances.length; i++){
    		list[i] = instances[i].getWeight();
    	}
    	return list;
    }
    /**
     * The weight of the entire DataSet
     *
     * @return the weight, [ 0.0 , 1.0 ]
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the entire DataSet
     *
     * @param weight - double [ 0.0 , 1.0 ] are you kind.
     */
    public void setWeight(double weight) {
        this.weight = weight;
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
     * @param position - 0-indexed position
     * @return Instance
     */
    public Instance get(int position) {
        return instances[position];
    }

    /**
     * Returns a DataSet containing a subset of instances.</br> IMPORTANT:
     * EXCLUDING LAST ELEMENT!
     *
     * @param from - position of first element, 0-indexed
     * @param to - position of last element, 0-indexed, excluding
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
     * @return int 
     */
    public int[] getNumberOfClasses() {
        if (this.numberOfClasses == null) {
            Set<Integer> found = new HashSet<>();
            for (Instance i : instances) {
                found.add(i.getCategory());
            }
            this.numberOfClasses = new int[found.size()];
            int index = 0;
            for (Integer i : found) {
                numberOfClasses[index++] = i;
            }
        }
        return this.numberOfClasses;
    }
}
