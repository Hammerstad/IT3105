/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.dataset.matcher.attribute;

import classifier.dataset.Instance;
import classifier.dataset.matcher.Matcher;

/**
 *
 * @author Nicklas
 */
public class AttributeEqualsMatcher implements Matcher {
    
    private int attributeNumber;
    private double attributeValue;

    public AttributeEqualsMatcher(int attributeNumber, double attributeValue) {
        this.attributeNumber = attributeNumber;
        this.attributeValue = attributeValue;
    }
    
    

    @Override
    public boolean match(Instance i) {
        return i.getAttributes()[attributeNumber]==attributeValue;
    }
    
}
