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
public class AttributeNode extends Node{
    List<Edge> children;
    int attr;
    
    public AttributeNode(int attr) {
        this.children = new LinkedList<Edge>();
        this.attr = attr;
    }
    
    @Override
    public void addChild(Node n, int choice) {
        this.children.add(new Edge(this, n, choice));
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public List<Node> getChildren() {
        LinkedList<Node> l = new LinkedList<Node>();
        for (Edge e : children)l.add(e.child);
        return l;
    }
    @Override
    public String toString() {
        return "Attribute: "+(attr+1);
    }
    public String getName() {
        return "Attribute "+(attr+1);
    }
    
    
    
}
