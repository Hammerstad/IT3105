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
public class NotMatcher implements Matcher {
    private Matcher first;

    public NotMatcher(Matcher first) {
        this.first = first;
    }
    
    
    @Override
    public boolean match(Instance i) {
        return !first.match(i);
    }
    
}
