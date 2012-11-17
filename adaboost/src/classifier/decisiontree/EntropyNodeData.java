/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.decisiontree;

/**
 *
 * @author Nicklas
 */
public class EntropyNodeData extends NodeData {
    private double entropy;
    
    public EntropyNodeData(double e) {
        this.entropy = e;
    }
    @Override
    public String toString() {
        return "Entropy: "+String.format("%.3f", entropy);
    }
    
}
