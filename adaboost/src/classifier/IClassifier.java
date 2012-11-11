/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import classifier.dataset.Instance;

/**
 *
 * @author Nicklas
 */
public abstract class IClassifier {
    private double weight;
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return this.weight;
    }
    public abstract int guessClass(Instance instance);
}
