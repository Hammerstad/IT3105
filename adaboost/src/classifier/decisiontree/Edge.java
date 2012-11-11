package classifier.decisiontree;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nicklas
 */
public class Edge {
    Node parent, child;
    double choice;
    
    public Edge(Node parent, Node child, double choice){
        this.parent = parent;
        this.child = child;
        this.choice = choice;
    }
}
