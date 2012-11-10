package classifier.decisiontree.AI2;


import java.util.LinkedList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nicklas
 */
public class LeafNode extends Node{
    int cls;
    
    public LeafNode(int cls) {
        this.cls = cls;
    }

    public void addChild(Node n, int choice) {
        throw new NoSuchMethodError("addChild not permitted on LeafNode");
    }

    public boolean isLeaf() {
        return true;
    }
    @Override
    public List<Node> getChildren() {
        return new LinkedList<Node>();
    }
    @Override
    public String toString() {
        return "Class: "+cls;
    }
    public String getName() {
        //return "C"+cls;
        return (cls == 1) ? "True" : "False";
    }
}
