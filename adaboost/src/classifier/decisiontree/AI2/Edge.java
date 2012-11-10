package classifier.decisiontree.AI2;

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
    int choice;
    
    public Edge(Node parent, Node child, int choice){
        this.parent = parent;
        this.child = child;
        this.choice = choice;
    }
}
