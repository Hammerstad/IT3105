package classifier.dataset;

import java.util.Arrays;

public class Instance {

    private final int category;
    private final double[] attributes;
    private double weight;

    public Instance(double[] instance) {
        this(instance, 1.0);
    }

    public Instance(double[] instance, double weight) {
        attributes = new double[instance.length - 1];
        System.arraycopy(instance, 0, attributes, 0, instance.length - 1);
        category = (int) instance[instance.length - 1];
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public double[] getAttributes() {
        return attributes;
    }

    public int getCategory() {
        return category;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Attr: " + Arrays.toString(attributes) + " \t Class: " + category + " \t Weight: " + weight;
    }
}
