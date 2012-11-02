/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.matcher.matcher.logical;

import classifier.dataset.Instance;
import classifier.dataset.matcher.Matcher;

/**
 *
 * @author Nicklas
 */
public class OrMatcher implements Matcher {
    private Matcher first, second;

    public OrMatcher(Matcher first, Matcher second) {
        this.first = first;
        this.second = second;
    }
    
    
    @Override
    public boolean match(Instance i) {
        return first.match(i)||second.match(i);
    }
    
}
