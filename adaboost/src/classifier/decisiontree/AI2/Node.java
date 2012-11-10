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
public abstract class Node {    
    public Node() {
    }
    public abstract boolean isLeaf();
    public abstract void addChild(Node n, int choice);
    public abstract List<Node> getChildren();
    public abstract String getName();
}
