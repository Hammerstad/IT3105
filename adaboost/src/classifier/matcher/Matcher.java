/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.matcher;

import classifier.Instance;

/**
 *
 * @author Nicklas
 */
public interface Matcher {
    public boolean match(Instance i);
}
