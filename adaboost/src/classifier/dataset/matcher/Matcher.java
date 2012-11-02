/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.matcher;

import classifier.dataset.Instance;

/**
 *
 * @author Nicklas
 */
public interface Matcher {
    public boolean match(Instance i);
}
