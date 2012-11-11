/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.decisiontree;

import classifier.IClassifier;
import classifier.dataset.Instance;
import java.util.Stack;


public class DecisionTreeClassifier implements IClassifier {
    private Node root;
    
    public DecisionTreeClassifier(Node root) {
        this.root = root;
    }

    @Override
    public int guessClass(Instance instance) {
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        Node current;
        while (!stack.isEmpty()) {
            current = stack.pop();
            if (current instanceof AttributeNode) {
                AttributeNode an = (AttributeNode)current;
                int attrNo = an.attr;
                double attrVal = instance.getAttributes()[attrNo];
                
                for (Edge e : an.children) {
                    if (e.choice == attrVal){
                        stack.push(e.child);
                    }
                }
            }else if (current instanceof LeafNode) {
                LeafNode ln = (LeafNode)current;
                return ln.cls;
            }
        }
        return -1;
    }
}
