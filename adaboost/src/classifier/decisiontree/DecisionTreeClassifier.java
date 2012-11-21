/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.decisiontree;

import classifier.IClassifier;
import classifier.dataset.Instance;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DecisionTreeClassifier extends IClassifier {

    private Node root;

    public DecisionTreeClassifier(Node root) {
        try {
            this.root = root;

//            Graphviz gv = new Graphviz();
//            gv.createGraph(root, -1);
        } catch (Exception ex) {
            Logger.getLogger(DecisionTreeClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public int guessClass(Instance instance) {
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        Node current;
        while (!stack.isEmpty()) {
            current = stack.pop();
            if (current instanceof AttributeNode) {
                AttributeNode an = (AttributeNode) current;
                int attrNo = an.attr;
                double attrVal = instance.getAttributes()[attrNo];

                for (Edge e : an.children) {
                    if (e.choice == attrVal) {
                        stack.push(e.child);
                    }
                }
            } else if (current instanceof LeafNode) {
                LeafNode ln = (LeafNode) current;
                return ln.cls;
            }
        }
        return -1;
    }
}
