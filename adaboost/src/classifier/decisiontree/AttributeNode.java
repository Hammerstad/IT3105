package classifier.decisiontree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nicklas
 */
public class AttributeNode extends Node {

    public static Map<Integer, String> attributeName;
    List<Edge> children;
    int attr;

    public AttributeNode(int attr) {
        this(attr, null);
    }

    public AttributeNode(int attr, NodeData data) {
        super(data);
        this.children = new LinkedList<Edge>();
        this.attr = attr;
    }

    @Override
    public void addChild(Node n, double choice) {
        this.children.add(new Edge(this, n, choice));
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public List<Node> getChildren() {
        LinkedList<Node> l = new LinkedList<Node>();
        for (Edge e : children) {
            l.add(e.child);
        }
        return l;
    }

    @Override
    public String toString() {
        String o;
        if (attributeName != null && attributeName.containsKey(attr)) {
            o = attributeName.get(attr);
        } else {
            o = "Attribute: " + (attr);
        }
        if (data != null){
            o+="\\n"+data.toString();
        }else {
            o+="\\nNo data";
        }
        return o;
    }

    public String getName() {
        return toString();
    }
}
