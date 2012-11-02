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
