package classifier.decisiontree.AI2;


import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Nicklas
 */
public class Graphviz {

    private boolean graphCreated = false;
    private static Map<String, String> colorMap = new HashMap<String, String>();
    private static String[] attrColors = new String[]{"aquamarine2", "cadetblue3", "deepskyblue3", "darkgreen", "cornflowerblue", "yellowgreen", "yellow4"};
    private static String[] clsColors = new String[]{"Olivedrab2", "LightPink"};
    
    static {
        attrColors = new String[]{"lemonchiffon3"};
    }
    private static int colorsUsed;

    public Graphviz() {
    }

    public void createGraph(Node root, double accuracy) throws Exception {
        FileWriter fw = new FileWriter(new File("graph.dot"));
        fw.append("digraph G{\n");
        StringBuilder attributeNodes = new StringBuilder();
        StringBuilder leafNodes = new StringBuilder();
        StringBuilder edges = new StringBuilder();

        Stack<Node> stack = new Stack<Node>();
        stack.push(root);
        Node current;
        HashMap<Node, String> vizIdentifiers = new HashMap<Node, String>();
        int nodeCount = 0;
        while (!stack.isEmpty()) {
            current = stack.pop();
            String vizIdString;
            if (vizIdentifiers.containsKey(current)) {
                vizIdString = vizIdentifiers.get(current);
            } else {
                vizIdString = "Node" + (nodeCount++);
                vizIdentifiers.put(current, vizIdString);
            }

            if (current instanceof AttributeNode) {
                attributeNodes.append(vizIdString).append(attrString(current.getName())).append("\n");

                for (Edge e : ((AttributeNode) current).children) {
                    String childId = "Node" + (nodeCount++);
                    vizIdentifiers.put(e.child, childId);

                    edges.append("	").append(vizIdString).append(" -> ").append(childId).append(" [label=\"").append(e.choice).append("\"]\n");
                    stack.push(e.child);
                }
            } else if (current instanceof LeafNode) {
                leafNodes.append(vizIdString).append(clsString(current.getName())).append("\n");
            }
        }
        fw.append(attributeNodes).append("\n");
        fw.append(leafNodes).append("\n");
        fw.append(edges);
        fw.append(" labelloc=\"t\"\n");
        fw.append(" label=\"" + String.format("Classification accuracy: %1$.2f%2$s", accuracy * 100, "%") + "\"\n");
        fw.append("}");
        fw.close();
        this.graphCreated = true;
    }

    public void show() throws Exception {
        Process p = Runtime.getRuntime().exec("cmd.exe /c makeGraph.bat");
        p.waitFor();
    }

    private static String attrString(String l) {
        return "[shape=\"box\", style=\"filled\" color=\"black\", fillcolor=\"" + attrColor(l) + "\", label=\"" + l + "\"]";
    }

    private static String clsString(String l) {
        return "[shape=\"box\", style=\"filled\" color=\"black\", fillcolor=\"" + clsColor(l) + "\", label=\"" + l + "\"]";
    }

    private static String attrColor(String nodeName) {
        if (colorMap.containsKey(nodeName)) {
            return colorMap.get(nodeName);
        } else {
            int c = (colorsUsed++ % attrColors.length);
            colorMap.put(nodeName, attrColors[c]);
            return attrColors[c];
        }
    }
    private static String clsColor(String nodeString) {
        return clsColors[nodeString.length()-4];
    }
}
