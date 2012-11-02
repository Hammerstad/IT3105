/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.matcher.matcher;

import classifier.dataset.Instance;
import classifier.dataset.matcher.Matcher;

/**
 *
 * @author Nicklas
 */
public class CategoryMatcher implements Matcher{
    private int category;
    
    public CategoryMatcher(int category){
        this.category = category;
    }

    @Override
    public boolean match(Instance i) {
        return this.category == i.getCategory();
    }
    
}
