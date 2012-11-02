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
public interface IClassifier {
    public int guessClass(Instance instance);
}
