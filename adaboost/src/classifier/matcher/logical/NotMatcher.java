/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.matcher.logical;

import classifier.Instance;
import classifier.matcher.Matcher;

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
