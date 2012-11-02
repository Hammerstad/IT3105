/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.matcher.logical;

import classifier.dataset.Instance;
import classifier.dataset.matcher.Matcher;

/**
 *
 * @author Nicklas
 */
public class AndMatcher implements Matcher {
    private Matcher first, second;

    public AndMatcher(Matcher first, Matcher second) {
        this.first = first;
        this.second = second;
    }
    
    
    @Override
    public boolean match(Instance i) {
        return first.match(i)&&second.match(i);
    }
    
}
